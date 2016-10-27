/**
 * 
 */
package org.goko.tools.macro.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.runtime.Platform;
import org.eclipse.osgi.service.datalocation.Location;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkTechnicalException;
import org.goko.core.common.io.xml.IXmlPersistenceService;
import org.goko.core.common.service.IGokoService;
import org.goko.core.common.utils.CacheByKey;
import org.goko.core.log.GkLog;
import org.goko.core.workspace.service.IMapperService;
import org.goko.tools.macro.bean.GCodeMacro;
import org.goko.tools.macro.io.XmlGCodeMacro;
import org.goko.tools.macro.io.exporter.XmlGCodeMacroExporter;
import org.goko.tools.macro.io.loader.XmlGCodeMacroLoader;

/**
 * Services to initialize the Macro service 
 * 
 * @author Psyko
 * @date 16 oct. 2016
 */
public class DefaultGCodeMacroPersistenceService implements IGokoService, IGCodeMacroServiceListener {
	private static final GkLog LOG = GkLog.getLogger(DefaultGCodeMacroPersistenceService.class);
	private static final String SERVICE_ID = "org.goko.tools.macro.service.DefaultGCodeMacroPersistenceService";
	/** The XML persistence service to read macros files */
	private IXmlPersistenceService xmlPersistenceService;
	/** The macro service to initialize */
	private IGCodeMacroService gcodeMacroService;
	/** Cache of file for macros id */
	private CacheByKey<Integer, File> cacheFileByMacro;
	/** The Mapper service */
	private IMapperService mapperService;

	/** (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#getServiceId()
	 */
	@Override
	public String getServiceId() throws GkException {
		return SERVICE_ID;
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#start()
	 */
	@Override
	public void start() throws GkException {
		xmlPersistenceService.register(XmlGCodeMacro.class);
		mapperService.addLoader(new XmlGCodeMacroLoader());
		mapperService.addExporter(new XmlGCodeMacroExporter());
		this.cacheFileByMacro = new CacheByKey<Integer, File>(); 
		initialize();
		gcodeMacroService.addListener(this);
		
	}

	private void initialize() throws GkException{		
		try {
			Location workspace = Platform.getInstanceLocation();
			URL data = workspace.getURL();
			Path uri = Paths.get(data.toURI());			
			Path macroFolderPath = uri.resolve("macros");
			File macroFolder = macroFolderPath.toFile();
			if(!macroFolder.exists()){
				macroFolder.mkdirs();
			}
			LOG.info("Loading macros from ["+macroFolder.getAbsolutePath()+"]");
			File[] lstFiles = macroFolder.listFiles();
			if(lstFiles != null && lstFiles.length > 0){
				for (File file : lstFiles) {
					if(file.isFile()){
						loadMacroFromFile(file);
					}
				}
			}
			List<GCodeMacro> lstMacros = gcodeMacroService.getGCodeMacro();
			LOG.info(CollectionUtils.size(lstMacros)+" macros loaded");
		} catch (URISyntaxException e) {
			LOG.error(e);
		}
	}
	
	protected void loadMacroFromFile(File file) throws GkException{
		FileInputStream input = null;
		try {
			input = new FileInputStream(file);
			XmlGCodeMacro xmlMacro = xmlPersistenceService.read(XmlGCodeMacro.class, input);
			GCodeMacro macro = mapperService.load(xmlMacro, GCodeMacro.class);
			gcodeMacroService.addGCodeMacro(macro);
			cacheFileByMacro.add(macro.getId(), file);
		} catch (FileNotFoundException e) {
			throw new GkTechnicalException(e);
		}finally{
			if(input != null){
				IOUtils.closeQuietly(input);
			}
		}
	}
	/** (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#stop()
	 */
	@Override
	public void stop() throws GkException {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @return the gcodeMacroService
	 */
	public IGCodeMacroService getGCodeMacroService() {
		return gcodeMacroService;
	}

	/**
	 * @param gcodeMacroService the gcodeMacroService to set
	 */
	public void setGCodeMacroService(IGCodeMacroService gcodeMacroService) {
		this.gcodeMacroService = gcodeMacroService;
	}

	/**
	 * @return the xmlPersistenceService
	 */
	public IXmlPersistenceService getXmlPersistenceService() {
		return xmlPersistenceService;
	}

	/**
	 * @param xmlPersistenceService the xmlPersistenceService to set
	 */
	public void setXmlPersistenceService(IXmlPersistenceService xmlPersistenceService) {
		this.xmlPersistenceService = xmlPersistenceService;
	}

	/**
	 * @return the mapperService
	 */
	public IMapperService getMapperService() {
		return mapperService;
	}

	/**
	 * @param mapperService the mapperService to set
	 */
	public void setMapperService(IMapperService mapperService) {
		this.mapperService = mapperService;
	}

	/** (inheritDoc)
	 * @see org.goko.tools.macro.service.IGCodeMacroServiceListener#onGCodeMacroCreate(org.goko.tools.macro.bean.GCodeMacro)
	 */
	@Override
	public void onGCodeMacroCreate(GCodeMacro macro) {
		try {
			String filename = generateFileNameByMacro(macro);
			Location workspace = Platform.getInstanceLocation();
			URL data = workspace.getURL();
			Path uri = Paths.get(data.toURI());			
			Path macroFolderPath = uri.resolve("macros");
			File macroFile = new File(macroFolderPath.toFile(), filename);
			macroFile.createNewFile();
			OutputStream output = new FileOutputStream(macroFile);
			XmlGCodeMacro xmlMacro = mapperService.export(macro, XmlGCodeMacro.class);
			xmlPersistenceService.write(xmlMacro, output); 
			cacheFileByMacro.add(macro.getId(), macroFile);
			output.close();
			
		} catch (URISyntaxException | GkException | IOException e) {
			LOG.error(e);
		} 
		
	}

	protected void saveMacroToFile(GCodeMacro macro) throws GkException{
		OutputStream output = null;
		try {
			String filename = generateFileNameByMacro(macro);
			Location workspace = Platform.getInstanceLocation();
			URL data = workspace.getURL();
			Path uri = Paths.get(data.toURI());			
			Path macroFolderPath = uri.resolve("macros");
			File macroFile = new File(macroFolderPath.toFile(), filename);
			macroFile.createNewFile();
			output = new FileOutputStream(macroFile);
			XmlGCodeMacro xmlMacro = mapperService.export(macro, XmlGCodeMacro.class);
			xmlPersistenceService.write(xmlMacro, output); 
			output.flush();
			output.close();
		} catch (URISyntaxException | IOException e) {
			LOG.error(e);
		}finally{
			if(output != null){				
				IOUtils.closeQuietly(output);
			}
		}
	}
	/** (inheritDoc)
	 * @see org.goko.tools.macro.service.IGCodeMacroServiceListener#onGCodeMacroUpdate(org.goko.tools.macro.bean.GCodeMacro)
	 */
	@Override
	public void onGCodeMacroUpdate(GCodeMacro macro) {
		try{
			File previousFile = getFileByMacro(macro);
			String fileName = generateFileNameByMacro(macro);			
			if(!StringUtils.equals(previousFile.getName(), fileName)){
				previousFile.delete();
			}
			saveMacroToFile(macro);
		}catch(GkException e){
			LOG.error(e);
		}
	}

	/** (inheritDoc)
	 * @see org.goko.tools.macro.service.IGCodeMacroServiceListener#beforeGCodeMacroDelete(org.goko.tools.macro.bean.GCodeMacro)
	 */
	@Override
	public void beforeGCodeMacroDelete(GCodeMacro macro) {
		
	}

	/** (inheritDoc)
	 * @see org.goko.tools.macro.service.IGCodeMacroServiceListener#afterGCodeMacroDelete(org.goko.tools.macro.bean.GCodeMacro)
	 */
	@Override
	public void afterGCodeMacroDelete(GCodeMacro macro) {
		try {
			File file = getFileByMacro(macro);
			if(file.exists()){
				file.delete();
			}
		} catch (GkException e) {
			LOG.error(e);
		}
	}
	
	protected File getFileByMacro(GCodeMacro macro) throws GkException{
		return cacheFileByMacro.get(macro.getId());
	}
	
	protected String generateFileNameByMacro(GCodeMacro macro)throws GkException{
		String filename = StringUtils.deleteWhitespace(macro.getCode());
		filename = StringUtils.stripAccents(filename);
		return filename+".gkm";
	}
}

/**
 * 
 */
package org.goko.core.workspace.service;

import java.util.HashMap;
import java.util.Map;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkTechnicalException;
import org.goko.core.common.service.AbstractGokoService;
import org.goko.core.log.GkLog;

/**
 * @author PsyKo
 * @date 10 févr. 2016
 */
public class MapperServiceImpl extends AbstractGokoService implements IMapperService {
	/** Log */
	private static final GkLog LOG = GkLog.getLogger(MapperServiceImpl.class);
	/** Service ID */
	private static final String SERVICE_ID = "org.goko.core.workspace.service.MapperServiceImpl";
	private Map<Class<?>, ILoader<?, ?>> loaders;
	private Map<Class<?>, IExporter<?, ?>> exporters;
	
	/**
	 * Constructor  
	 */
	public MapperServiceImpl() {
		loaders = new HashMap<Class<?>, ILoader<?, ?>>();
		exporters = new HashMap<Class<?>, IExporter<?, ?>>();
	}
	
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
	public void startService() throws GkException {
		
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#stop()
	 */
	@Override
	public void stopService() throws GkException {
		
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IMapperService#addLoader(org.goko.core.workspace.service.ILoader)
	 */
	@Override
	public void addLoader(ILoader<?, ?> loader) throws GkException {
		if(loaders.containsKey(loader.getInputClass())){
			throw new GkTechnicalException("A loder for input class ["+loader.getInputClass()+"] already exist (registered is ["+loaders.get(loader.getInputClass()).getClass()+"]).");
		}
		loaders.put(loader.getInputClass(), loader);
		LOG.info("Added loader "+loader.getClass());
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IMapperService#addExporter(org.goko.core.workspace.service.IExporter)
	 */
	@Override
	public void addExporter(IExporter<?, ?> exporter) throws GkException {
		if(exporters.containsKey(exporter.getInputClass())){
			throw new GkTechnicalException("An exporter for input class ["+exporter.getInputClass()+"] already exist (registered is ["+exporters.get(exporter.getInputClass()).getClass()+"]).");
		}
		exporters.put(exporter.getInputClass(), exporter);
		LOG.info("Added exporter "+exporter.getClass());
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IMapperService#load(java.lang.Object, java.lang.Class)
	 */
	@Override
	public <O> O load(Object object, Class<O> outputClass) throws GkException {		
		return getLoader(object.getClass(), outputClass).load(object, this);
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IMapperService#export(java.lang.Object, java.lang.Class)
	 */
	@Override
	public <O> O export(Object object, Class<O> outputClass) throws GkException {
		return getExporter(object.getClass(), outputClass).export(object, this);
	}

	/**
	 * Returns the loader that transforms the inputClass to the outputClass
	 * @param inputClass the input class
	 * @param outputClass the output class
	 * @return ILoader
	 * @throws GkException GkException
	 */
	@SuppressWarnings("unchecked")
	private <O> ILoader<Object, O> getLoader(Class<?> inputClass, Class<O> outputClass) throws GkException{
		if(loaders.containsKey(inputClass)){
			ILoader<?, ?> loader = loaders.get(inputClass);
			if(outputClass.isAssignableFrom(loader.getOutputClass())){
				return (ILoader<Object, O>) loader;
			}
			throw new GkTechnicalException("No loader found for input class ["+inputClass+"] with matching output class ["+outputClass+"]");
		}
		throw new GkTechnicalException("No loader found for input class ["+inputClass+"]");
	}
	
	/**
	 * Returns the exporter that transforms the inputClass to the outputClass
	 * @param inputClass the input class
	 * @param outputClass the output class
	 * @return IExporter
	 * @throws GkException GkException
	 */
	@SuppressWarnings("unchecked")
	private <O> IExporter<Object, O> getExporter(Class<?> inputClass, Class<O> outputClass) throws GkException{
		if(exporters.containsKey(inputClass)){
			IExporter<?, ?> exporter = exporters.get(inputClass);
			if(outputClass.isAssignableFrom(exporter.getOutputClass())){
				return (IExporter<Object, O>) exporter;
			}
			throw new GkTechnicalException("No exporter found for input class ["+inputClass+"] with matching output class ["+outputClass+"]");
		}
		throw new GkTechnicalException("No exporter found for input class ["+inputClass+"]");
	}		
}

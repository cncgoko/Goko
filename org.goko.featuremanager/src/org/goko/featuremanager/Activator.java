package org.goko.featuremanager;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import org.apache.commons.io.filefilter.RegexFileFilter;
import org.apache.commons.lang3.StringUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkTechnicalException;
import org.goko.core.log.GkLog;
import org.goko.featuremanager.service.FeatureManagerImpl;
import org.goko.featuremanager.service.IFeatureManager;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {
	private static final GkLog LOG = GkLog.getLogger(Activator.class);	
	
	private static BundleContext context;

	public static BundleContext getContext() {
		return context;
	}

	/** (inheritDoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		IFeatureManager featureManager = new FeatureManagerImpl();
		bundleContext.registerService(IFeatureManager.class, featureManager, null);
		featureManager.start();
		
		//LOG.info("Starting PluginManager");
//		Activator.context = bundleContext;
//		
//		File pluginConfigFile = new File(PLUGIN_CONFIG_FILE);
//			
//		Map<String, File> mapBundleBySymbolicName= buildBundleCache(PLUGIN_FOLDER);
//		LOG.info("Found "+mapBundleBySymbolicName.size()+" bundles");
//		
//		if(pluginConfigFile.exists()){			
//			
//			for (String key : mapBundleBySymbolicName.keySet()) {
//				LOG.info("Key "+key+" value : "+mapBundleBySymbolicName.get(key));
//			}
//			LOG.info("Parsing '"+PLUGIN_CONFIG_FILE+"' for plugins configuration...");
//			XmlGokoConfig config = XmlUtils.load(pluginConfigFile, XmlGokoConfig.class);			
//			
//			LOG.info("Found "+config.getXmlFeatureConfig().size()+" configured features");
//			
//			if(CollectionUtils.isNotEmpty(config.getXmlFeatureConfig())){
//				for (XmlFeatureConfig feature : config.getXmlFeatureConfig()) {
//					
//					if(mapBundleBySymbolicName.containsKey(feature.getBundleSymbolicName())){
//						Bundle bundle = bundleContext.getBundle(feature.getBundleSymbolicName()); 
//						if(bundle == null){
//							File bundleFile = mapBundleBySymbolicName.get(feature.getBundleSymbolicName()); 
//							bundle = bundleContext.installBundle(feature.getBundleSymbolicName(), new FileInputStream(bundleFile));
//							LOG.info("Starting "+bundleFile.getAbsolutePath()+" for feature "+feature.getBundleSymbolicName());
//						}else{
//							LOG.info("Bundle already installed "+bundle.getLocation());
//						}
//						
//						bundle.start();
//					}else{
//						LOG.warn("Could not find JAR for symbolic name "+feature.getBundleSymbolicName());
//					}
//				}
//			}
//		}
	}

	protected Map<String, File> buildBundleCache(String pluginFolder) throws GkException{
		Map<String, File> mapBundleBySymbolicName = new HashMap<String, File>();
		File folder = new File(pluginFolder);
		try {
			if(folder.isDirectory()){														 
				FileFilter filter = new RegexFileFilter(".*\\.jar");
				
				for (File file: folder.listFiles(filter)) {					
					JarFile jarFile = new JarFile(file);					
					Manifest manifest = jarFile.getManifest();
					if(manifest.getMainAttributes() != null){
						String fullSymbolicName = manifest.getMainAttributes().getValue("Bundle-SymbolicName");
						String symbolicName = StringUtils.substring(fullSymbolicName,0,StringUtils.indexOf(fullSymbolicName, ";"));					
						mapBundleBySymbolicName.put(symbolicName, file);
					}else{
						LOG.warn("No bundle manifest main attributes found for "+jarFile.getName());
					}
					jarFile.close();
				}
			}
		} catch (IOException e) {
			throw new GkTechnicalException(e);
		}
		return mapBundleBySymbolicName;
	}
	
	/** (inheritDoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

}

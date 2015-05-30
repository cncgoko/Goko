package org.goko.featuremanager.service;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.apache.commons.lang3.StringUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkFunctionalException;
import org.goko.core.common.exception.GkTechnicalException;
import org.goko.core.common.io.xml.XmlUtils;
import org.goko.core.log.GkLog;
import org.goko.featuremanager.bean.Feature;
import org.goko.featuremanager.bean.FeatureBinding;
import org.goko.featuremanager.bean.FeatureBundle;
import org.goko.featuremanager.io.XmlFeatureConfig;
import org.goko.featuremanager.io.XmlGokoConfig;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.FrameworkUtil;

/**
 * Implementation of the default plugin manager 
 * 
 * @author PsyKo
 *
 */
/**
 * @author PsyKo
 *
 */
public class FeatureManagerImpl implements IFeatureManager {
	private static final GkLog LOG = GkLog.getLogger(FeatureManagerImpl.class);
	private static final String SERVICE_ID = "org.goko.feature.manager";
	private static final String PLUGIN_FOLDER = "plugins";
	private static final String PLUGIN_CONFIG_FILE = "goko.features.xml";
	/** The map of requested features*/
	private Map<String, Feature> mapFeatureById;
	/** The map of feature provider*/
	private Map<String, List<FeatureBundle>> mapFeatureBundleByFeatureId;
	/** The map of feature provider by id */
	private Map<String, FeatureBundle> mapFeatureBundleBySymbolicName;
	/** The map of feature provider by id */
	private Map<String, FeatureBinding> mapFeatureBindingByFeatureId;
	
	public FeatureManagerImpl() {
		mapFeatureById 					= new HashMap<String, Feature>();
		mapFeatureBundleByFeatureId 	= new HashMap<String, List<FeatureBundle>>();
		mapFeatureBundleBySymbolicName 	= new HashMap<String, FeatureBundle>();
		mapFeatureBindingByFeatureId 	= new HashMap<String, FeatureBinding>();
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
	public void start() throws GkException {
		// TODO Auto-generated method stub
		File pluginConfigFile = new File(PLUGIN_CONFIG_FILE);
		
		if(pluginConfigFile.exists()){
			Bundle parentBundle = FrameworkUtil.getBundle(getClass());
		
			
			buildBundleCache(PLUGIN_FOLDER);
			
			LOG.info("Parsing '"+PLUGIN_CONFIG_FILE+"' for plugins configuration...");
			XmlGokoConfig config = XmlUtils.load(pluginConfigFile, XmlGokoConfig.class);		
			
			if(CollectionUtils.isNotEmpty(config.getXmlFeatureConfig())){
				for (XmlFeatureConfig feature : config.getXmlFeatureConfig()) {
					//FeatureBundle bundle = getFeatureBundle(feature.getBundleSymbolicName());
					startFeature(parentBundle.getBundleContext(), feature.getBundleSymbolicName());
				}
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
	/** (inheritDoc)
	 * @see org.goko.featuremanager.service.IFeatureManager#getFeature(java.lang.String)
	 */
	@Override
	public Feature getFeature(String id) throws GkException {	
		if(!mapFeatureById.containsKey(id)){
			throw new GkFunctionalException("Feature '"+id+"' does not exist");
		}
		return mapFeatureById.get(id);
	}
	
	/** (inheritDoc)
	 * @see org.goko.featuremanager.service.IFeatureManager#getFeatureProvider(java.lang.String)
	 */
	@Override
	public FeatureBundle getFeatureBundle(String id) throws GkException {
		if(!mapFeatureBundleBySymbolicName.containsKey(id)){
			throw new GkFunctionalException("FeatureProvider '"+id+"' does not exist");
		}
		return mapFeatureBundleBySymbolicName.get(id);
	}
	
	/** (inheritDoc)
	 * @see org.goko.featuremanager.service.IFeatureManager#getFeatureBinding(java.lang.String)
	 */
	@Override
	public FeatureBinding getFeatureBinding(String featureId) throws GkException {
		if(!mapFeatureBindingByFeatureId.containsKey(featureId)){
			throw new GkFunctionalException("FeatureBinding for the feature '"+featureId+"' does not exist");
		}
		return mapFeatureBindingByFeatureId.get(featureId);
	}
	
	/** (inheritDoc)
	 * @see org.goko.featuremanager.service.IFeatureManager#getProvider(org.goko.featuremanager.bean.Feature)
	 */
	@Override
	public List<FeatureBundle> getFeatureBundle(Feature feature) throws GkException {
		List<FeatureBundle> providers = new ArrayList<FeatureBundle>();
		if(mapFeatureBundleByFeatureId.containsKey(feature.getId())){
			providers = mapFeatureBundleByFeatureId.get(feature.getId());
		}
		return providers;
	}

	/** (inheritDoc)
	 * @see org.goko.featuremanager.service.IFeatureManager#addProvider(org.goko.featuremanager.bean.FeatureBundle)
	 */
	@Override
	public void addFeatureProvider(FeatureBundle bundle) throws GkException {
		if(CollectionUtils.isNotEmpty(bundle.getProvidedFeatureIds())){
			for (String featureId : bundle.getProvidedFeatureIds()) {
				registerFeatureBundle(featureId, bundle);
			}
		}
					
		mapFeatureBundleBySymbolicName.put(bundle.getSymbolicName(), bundle);
	}
	/**
	 * Unitary registration of a bundle for a given feature
	 * @param featureId the feature id
	 * @param bundle the bundle
	 */
	private void registerFeatureBundle(String featureId, FeatureBundle bundle){
		if(!mapFeatureBundleByFeatureId.containsKey(featureId)){
			mapFeatureBundleByFeatureId.put(featureId, new ArrayList<FeatureBundle>());
		}
		
		List<FeatureBundle> providers = mapFeatureBundleByFeatureId.get(featureId);
		if(!providers.contains(bundle)){
			providers.add(bundle);
		}
	}
	
	/** (inheritDoc)
	 * @see org.goko.featuremanager.service.IFeatureManager#addFeature(org.goko.featuremanager.bean.Feature)
	 */
	@Override
	public void addFeature(Feature feature) throws GkException {
		mapFeatureById.put(feature.getId(), feature);
	}
	
	/** (inheritDoc)
	 * @see org.goko.featuremanager.service.IFeatureManager#getInstalledBundleByFeature(java.lang.String)
	 */
	@Override
	public FeatureBundle getInstalledBundleByFeature(String featureId) throws GkException {
		if(mapFeatureBindingByFeatureId.containsKey(featureId)){
			String bundleName = mapFeatureBindingByFeatureId.get(featureId).getFeatureBundleSymbolicName();
			return getFeatureBundle(bundleName);
		}
		throw new GkFunctionalException("No bundle currently installed for feature '"+featureId+"'");
	}
	
	/** (inheritDoc)
	 * @see org.goko.featuremanager.service.IFeatureManager#startFeature(org.osgi.framework.BundleContext, java.lang.String)
	 */
	@Override
	public void startFeature(BundleContext context, String bundleSymbolicName) throws GkException {		
		FeatureBundle featureProvider = getFeatureBundle(bundleSymbolicName);		
		try {
			Bundle bundle = context.getBundle(featureProvider.getSymbolicName());
			if(bundle == null){			
				bundle = context.installBundle(featureProvider.getSymbolicName(), new FileInputStream(featureProvider.getBundleFile()));
				LOG.info("Installing "+featureProvider.getBundleFile().getAbsolutePath());			
			}
			bundle.start();
			LOG.info("Starting "+featureProvider.getBundleFile().getAbsolutePath());	
			for (String featureId : featureProvider.getProvidedFeatureIds()) {
				bindFeatureProvider(featureId, featureProvider.getSymbolicName());
			}
		} catch (FileNotFoundException e) {
			throw new GkTechnicalException(e);
		} catch (BundleException e) {
			throw new GkTechnicalException(e);
		}
	}
	

	/** (inheritDoc)
	 * @see org.goko.featuremanager.service.IFeatureManager#bindFeatureProvider(java.lang.String, java.lang.String)
	 */
	@Override
	public void bindFeatureProvider(String featureId, String bundleSymbolicName) throws GkException{
		if(mapFeatureBindingByFeatureId.containsKey(featureId)){
			throw new GkFunctionalException("Feature '"+featureId+"' is already provided by the bundle '"+mapFeatureBindingByFeatureId.get(featureId).getFeatureBundleSymbolicName()+"'");
		}
		mapFeatureBindingByFeatureId.put(featureId, new FeatureBinding(featureId, bundleSymbolicName));
	}
	
	/** (inheritDoc)
	 * @see org.goko.featuremanager.service.IFeatureManager#unbindFeatureProvider(java.lang.String)
	 */
	@Override
	public void unbindFeatureProvider(String featureId) throws GkException{
		if(mapFeatureBindingByFeatureId.containsKey(featureId)){
			mapFeatureBindingByFeatureId.remove(featureId);
		}		
	}
	/**
	 * Build the Bundle cache from the plugin folder
	 * @param pluginFolder the path to the plugin folder 
	 * @throws GkException GkException
	 */
	protected void buildBundleCache(String pluginFolder) throws GkException{		
		File folder = new File(pluginFolder);
		try {
			if(!folder.isDirectory()){
				LOG.warn("Location '"+folder.getAbsolutePath()+"' is not a folder.");
			}
				
			FileFilter filter = new RegexFileFilter(".*\\.jar");
			LOG.info("Found "+folder.listFiles(filter).length+"' to read in '"+folder.getAbsolutePath()+"'");
			for (File file: folder.listFiles(filter)) {					
				JarFile jarFile = new JarFile(file);				
				
				if(isFeatureBundle(jarFile)){
					FeatureBundle bundle = parseFeatureBundle(file);			
					mapFeatureBundleBySymbolicName.put(bundle.getSymbolicName(), bundle);
					String features = StringUtils.EMPTY;
					for (String featureId : bundle.getProvidedFeatureIds()) {
						if(StringUtils.isNotBlank(features)){
							featureId +=", ";
						}
						features += featureId;
						// TEMPORARY CODE TO REPLACE WITH A PROPER FEATURE MANAGEMENT
						Feature feature = new Feature();
						feature.setId(featureId);
						addFeature(feature);
						registerFeatureBundle(featureId, bundle);
					}
					LOG.info("Registering '"+bundle.getSymbolicName()+"' as provider for '"+features+"'");
				}
				jarFile.close();
			}
			
		} catch (IOException e) {
			throw new GkTechnicalException(e);
		}	
		LOG.info("Found "+mapFeatureBundleBySymbolicName.size()+" bundles");
	}
	
	/**
	 * Returns the symbolic name of the given jar file
	 * @param jarFile the JAR file
	 * @return String
	 * @throws GkException GkException
	 */
	protected String getSymbolicName(JarFile jarFile) throws GkException{
		Manifest manifest;
		try {
			manifest = jarFile.getManifest();
			String symbolicName = manifest.getMainAttributes().getValue("Bundle-SymbolicName");
			if(StringUtils.contains(symbolicName, ";")){
				symbolicName = StringUtils.substring(symbolicName,0,StringUtils.indexOf(symbolicName, ";"));	
			}
			return symbolicName;
		} catch (IOException e) {
			throw new GkTechnicalException(e);
		}		
	}
	
	/**
	 * Create a FeatureBundle from the given JAR file
	 * @param jarFile the JAR file
	 * @return {@link FeatureBundle}
	 * @throws GkException GkException
	 */
	protected FeatureBundle parseFeatureBundle(File file) throws GkException{		
		FeatureBundle bundle = new FeatureBundle();
		try {
			JarFile jarFile = new JarFile(file);
			Manifest manifest = jarFile.getManifest();
			
			String gokoFeature= manifest.getMainAttributes().getValue("Goko-Feature");
			String bundleName = manifest.getMainAttributes().getValue("Bundle-Name");
			String[] gokoFeatureList = StringUtils.split(gokoFeature, ",");
			bundle.setBundleFile(file);
			bundle.setName(bundleName);
			bundle.setProvidedFeatureIds(Arrays.asList(gokoFeatureList));	
			bundle.setSymbolicName(getSymbolicName(jarFile));
			return bundle;			
		} catch (IOException e) {
			throw new GkTechnicalException(e);
		}	
	}
	
	/**
	 * Returns <code>true</code> if the given JAR file is a Goko feature bundle, <code>false</code> otherwise
	 * @param jarFile the JAR file
	 * @return boolean
	 * @throws GkException GkException
	 */
	protected boolean isFeatureBundle(JarFile jarFile) throws GkException{		
		try {
			Manifest manifest = jarFile.getManifest();
			if(manifest.getMainAttributes() != null){
				String gokoFeature= manifest.getMainAttributes().getValue("Goko-Feature");
				if(StringUtils.isNotBlank(gokoFeature)){
					return true;
				}
			}
		} catch (IOException e) {
			throw new GkTechnicalException(e);
		}	
		return false;
	}

	/** (inheritDoc)
	 * @see org.goko.featuremanager.service.IFeatureManager#save()
	 */
	@Override
	public void save() throws GkException {
		XmlGokoConfig config = new XmlGokoConfig();
		if(mapFeatureBundleByFeatureId != null && !mapFeatureBundleByFeatureId.isEmpty()){
			List<XmlFeatureConfig> lstXmlFeatureConfig = new ArrayList<XmlFeatureConfig>();
			List<String> lstBundleSymbolicName = new ArrayList<String>();
			
			for (String featureId : mapFeatureById.keySet()) {
				FeatureBundle bundle = getInstalledBundleByFeature(featureId);
				
				if(!lstBundleSymbolicName.contains(bundle.getSymbolicName())){
					lstBundleSymbolicName.add(bundle.getSymbolicName());
					XmlFeatureConfig xmlFeatureConfig = new XmlFeatureConfig();
					xmlFeatureConfig.setBundleSymbolicName(bundle.getSymbolicName());
					lstXmlFeatureConfig.add(xmlFeatureConfig);
				}
			}
			config.setXmlFeatureConfig(lstXmlFeatureConfig);
		}
		File pluginConfigFile = new File(PLUGIN_CONFIG_FILE);
		XmlUtils.write(pluginConfigFile, config);	
		LOG.info("Writing plugin file configuration to '"+pluginConfigFile.getAbsolutePath()+"'");
	}
}

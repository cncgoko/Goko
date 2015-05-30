package org.goko.featuremanager.service;

import java.util.List;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.service.IGokoService;
import org.goko.featuremanager.bean.Feature;
import org.goko.featuremanager.bean.FeatureBinding;
import org.goko.featuremanager.bean.FeatureBundle;
import org.osgi.framework.BundleContext;

public interface IFeatureManager extends IGokoService {

	/**
	 * Returns the feature for the given id
	 * @param id id of the feature
	 * @return a {@link Feature}
	 * @throws GkException GkException
	 */
	Feature getFeature(String id) throws GkException;
	
	/**
	 * Returns the feature bundle for the given id
	 * @param id id of the feature provider
	 * @return a {@link FeatureBundle}
	 * @throws GkException GkException
	 */
	FeatureBundle getFeatureBundle(String id) throws GkException;
	
	/**
	 * Returns the provider bound to the given feature id
	 * @param id id of the feature
	 * @return a {@link FeatureBinding}
	 * @throws GkException GkException
	 */
	FeatureBinding getFeatureBinding(String featureId) throws GkException;
	
	/**
	 * Returns the list of feature provider for the given feature
	 * @param feature the feature
	 * @return a List of {@link FeatureProvider}
	 * @throws GkException GkException
	 */
	List<FeatureBundle> getFeatureBundle(Feature feature) throws GkException;
	
	/**
	 * Add the given bundle 
	 * @param provider the {@link FeatureBundle}
	 * @throws GkException GkException
	 */
	void addFeatureProvider(FeatureBundle provider) throws GkException;
	
	/**
	 * Add the given feature  
	 * @param feature the {@link Feature}
	 * @throws GkException GkException
	 */
	void addFeature(Feature feature) throws GkException;
	
	/**
	 * Starts the given feature 
	 * @param context the bundle context to start the feature in
	 * @param bundleSymbolicName the symbolicName of the bundle feature to start
	 * @throws GkException GkException
	 */
	void startFeature(BundleContext context, String bundleSymbolicName) throws GkException;
	
	/**
	 * Returns the current {@link FeatureBundle} for the given feature  
	 * @param featureId the feature id
	 * @return FeatureBundle
	 * @throws GkException GkException
	 */
	FeatureBundle getInstalledBundleByFeature(String featureId) throws GkException;
	
	/**
	 * Unbinds the bundle for the given feature 
	 * @param featureId the id of the feature
	 * @throws GkException GkException
	 */
	void unbindFeatureProvider(String featureId) throws GkException;
	
	/**
	 * Binds the given feature with the given bundle name
	 * @param featureId the id of the feature
	 * @param bundleSymbolicName the id of the providing bundle
	 * @throws GkException GkException
	 */
	void bindFeatureProvider(String featureId, String bundleSymbolicName) throws GkException;
	
	/**
	 * Saves the stored features configurations
	 * @throws GkException GkException
	 */
	void save() throws GkException;
}

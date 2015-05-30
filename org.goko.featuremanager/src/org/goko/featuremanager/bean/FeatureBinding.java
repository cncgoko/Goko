package org.goko.featuremanager.bean;

public class FeatureBinding {
	/** Id of the provided feature */
	private String featureId;
	/** Id of the provider */
	private String featureBundleSymbolicName;
	
	public FeatureBinding(String featureId, String featureBundleSymbolicName) {
		super();
		this.featureId = featureId;
		this.featureBundleSymbolicName = featureBundleSymbolicName;
	}
	/**
	 * @return the featureId
	 */
	public String getFeatureId() {
		return featureId;
	}
	/**
	 * @param featureId the featureId to set
	 */
	public void setFeatureId(String featureId) {
		this.featureId = featureId;
	}
	/**
	 * @return the featureBundleSymbolicName
	 */
	public String getFeatureBundleSymbolicName() {
		return featureBundleSymbolicName;
	}
	/**
	 * @param featureBundleSymbolicName the featureBundleSymbolicName to set
	 */
	public void setFeatureBundleSymbolicName(String featureBundleSymbolicName) {
		this.featureBundleSymbolicName = featureBundleSymbolicName;
	}

	
}

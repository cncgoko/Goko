package org.goko.featuremanager.bean;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FeatureBundle {
	/** The name of the bundle (user friendly)*/
	private String name;
	/** The symbolic name of the bundle */
	private String symbolicName;
	/** The list of provided features Id */
	private List<String> providedFeatureIds;
	/** The file of the providing bundle */
	private File bundleFile;
		
	/** Default constructor */
	public FeatureBundle() {
		providedFeatureIds = new ArrayList<String>();
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the symbolicName
	 */
	public String getSymbolicName() {
		return symbolicName;
	}
	/**
	 * @param symbolicName the symbolicName to set
	 */
	public void setSymbolicName(String symbolicName) {
		this.symbolicName = symbolicName;
	}
	/**
	 * @return the providedFeatureIds
	 */
	public List<String> getProvidedFeatureIds() {
		return providedFeatureIds;
	}
	/**
	 * @param providedFeatureIds the providedFeatureIds to set
	 */
	public void setProvidedFeatureIds(List<String> providedFeatureIds) {
		this.providedFeatureIds = providedFeatureIds;
	}
	/**
	 * Add the given feature as a provided feature
	 * @param featureId the id of the feature to add
	 */
	public void addProvidedFeature(String featureId){
		this.providedFeatureIds.add(featureId);
	}
	/**
	 * @return the bundleFile
	 */
	public File getBundleFile() {
		return bundleFile;
	}
	/**
	 * @param bundleFile the bundleFile to set
	 */
	public void setBundleFile(File bundleFile) {
		this.bundleFile = bundleFile;
	}
	/** (inheritDoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((symbolicName == null) ? 0 : symbolicName.hashCode());
		return result;
	}
	/** (inheritDoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FeatureBundle other = (FeatureBundle) obj;
		if (symbolicName == null) {
			if (other.symbolicName != null)
				return false;
		} else if (!symbolicName.equals(other.symbolicName))
			return false;
		return true;
	}
}

package org.goko.featuremanager.io;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name="feature")
public class XmlFeatureConfig {
	
	@Attribute
	private String bundleSymbolicName;

	/**
	 * @return the bundleSymbolicName
	 */
	public String getBundleSymbolicName() {
		return bundleSymbolicName;
	}

	/**
	 * @param bundleSymbolicName the bundleSymbolicName to set
	 */
	public void setBundleSymbolicName(String bundleSymbolicName) {
		this.bundleSymbolicName = bundleSymbolicName;
	}
}

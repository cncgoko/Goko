package org.goko.featuremanager.io;

import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name="gokoConfiguration")
public class XmlGokoConfig {
	
	@ElementList
	private List<XmlFeatureConfig> xmlFeatureConfig;

	/**
	 * @return the xmlFeatureConfig
	 */
	public List<XmlFeatureConfig> getXmlFeatureConfig() {
		return xmlFeatureConfig;
	}

	/**
	 * @param xmlFeatureConfig the xmlFeatureConfig to set
	 */
	public void setXmlFeatureConfig(List<XmlFeatureConfig> xmlFeatureConfig) {
		this.xmlFeatureConfig = xmlFeatureConfig;
	}
	
	
}

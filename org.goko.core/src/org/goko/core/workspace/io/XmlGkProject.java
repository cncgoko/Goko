/**
 *
 */
package org.goko.core.workspace.io;

import java.util.ArrayList;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

/**
 * @author PsyKo
 * @date 9 déc. 2015
 */
@Root
public class XmlGkProject {
	/** The name of the resources folder */
	@Attribute
	private String resourcesFolderName;

	/** The list of saved resources */
	@ElementList
	private ArrayList<XmlProjectContainer> lstProjectContainer;

//	@ElementList
	private ArrayList<Object> testList;

	public XmlGkProject() {
		super();
		this.lstProjectContainer = new ArrayList<>();
		this.testList =  new ArrayList<>();
	}

	/**
	 * @return the lstProjectContainer
	 */
	public ArrayList<XmlProjectContainer> getLstProjectContainer() {
		return lstProjectContainer;
	}

	/**
	 * @param lstProjectContainer the lstProjectContainer to set
	 */
	public void setLstProjectContainer(ArrayList<XmlProjectContainer> lstProjectContainer) {
		this.lstProjectContainer = lstProjectContainer;
	}

	/**
	 * @return the testList
	 */
	public ArrayList<Object> getTestList() {
		return testList;
	}

	/**
	 * @param testList the testList to set
	 */
	public void setTestList(ArrayList<Object> testList) {
		this.testList = testList;
	}

	/**
	 * @return the resourcesFolderName
	 */
	public String getResourcesFolderName() {
		return resourcesFolderName;
	}

	/**
	 * @param resourcesFolderName the resourcesFolderName to set
	 */
	public void setResourcesFolderName(String resourcesFolderName) {
		this.resourcesFolderName = resourcesFolderName;
	}

}

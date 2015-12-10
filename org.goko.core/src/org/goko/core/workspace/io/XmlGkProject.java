/**
 *
 */
package org.goko.core.workspace.io;

import java.util.ArrayList;

import org.simpleframework.xml.ElementList;

/**
 * @author PsyKo
 * @date 9 déc. 2015
 */
public class XmlGkProject {

	@ElementList
	private ArrayList<XmlProjectContainer<?>> lstProjectContainer;

	/**
	 * @return the lstProjectContainer
	 */
	public ArrayList<XmlProjectContainer<?>> getLstProjectContainer() {
		return lstProjectContainer;
	}

	/**
	 * @param lstProjectContainer the lstProjectContainer to set
	 */
	public void setLstProjectContainer(ArrayList<XmlProjectContainer<?>> lstProjectContainer) {
		this.lstProjectContainer = lstProjectContainer;
	}

}

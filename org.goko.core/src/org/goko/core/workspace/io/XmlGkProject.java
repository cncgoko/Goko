/**
 *
 */
package org.goko.core.workspace.io;

import java.util.ArrayList;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

/**
 * @author PsyKo
 * @date 9 déc. 2015
 */
@Root(name="gkProject")
public class XmlGkProject {
	/** The list of saved resources */
	@ElementList(name="containers")
	private ArrayList<XmlProjectContainer> projectContainer;

	public XmlGkProject() {
		super();
		this.projectContainer = new ArrayList<>();	
	}

	/**
	 * @return the lstProjectContainer
	 */
	public ArrayList<XmlProjectContainer> getProjectContainer() {
		return projectContainer;
	}

	/**
	 * @param lstProjectContainer the lstProjectContainer to set
	 */
	public void setProjectContainer(ArrayList<XmlProjectContainer> lstProjectContainer) {
		this.projectContainer = lstProjectContainer;
	}

}

/**
 * 
 */
package org.goko.core.execution.monitor.io.xml;

import java.util.ArrayList;

import org.goko.core.workspace.io.XmlProjectContainer;
import org.simpleframework.xml.DerivedType;
import org.simpleframework.xml.ElementList;

/**
 * @author PsyKo
 * @date 1 janv. 2016
 */

@DerivedType(parent=XmlProjectContainer.class, name="content:execution")
public class XmlExecutionService extends XmlProjectContainer{
	/** The list of execution token */
	@ElementList(name = "executionTokens")
	private ArrayList<XmlExecutionToken> lstExecutionToken;

	/**
	 * @return the lstExecutionToken
	 */
	public ArrayList<XmlExecutionToken> getLstExecutionToken() {
		return lstExecutionToken;
	}

	/**
	 * @param lstExecutionToken the lstExecutionToken to set
	 */
	public void setLstExecutionToken(ArrayList<XmlExecutionToken> lstExecutionToken) {
		this.lstExecutionToken = lstExecutionToken;
	}
	
	
}

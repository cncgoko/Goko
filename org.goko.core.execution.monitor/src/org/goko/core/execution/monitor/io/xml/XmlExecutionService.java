/**
 * 
 */
package org.goko.core.execution.monitor.io.xml;

import java.util.ArrayList;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

/**
 * @author PsyKo
 * @date 1 janv. 2016
 */

@Root(name="executionService")
public class XmlExecutionService {
	/** The list of execution token */
	@ElementList(name = "lstExecutionToken")
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

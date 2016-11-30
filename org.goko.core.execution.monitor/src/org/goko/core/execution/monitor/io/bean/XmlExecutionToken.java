/**
 * 
 */
package org.goko.core.execution.monitor.io.bean;

import org.goko.core.gcode.io.XmlGCodeProviderReference;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * @author PsyKo
 * @date 1 janv. 2016
 */
@Root(name="executionToken")
public class XmlExecutionToken {
	/** Execution order of this token in the queue */
	@Attribute(name="executionOrder")
	private Integer executionOrder;
	/** Reference to a GCode provider */
	@Element(required=false)
	private XmlGCodeProviderReference providerReference;
	/** Attribute used for backward compatibility with 0.3.1 */
	@Attribute(required=false)
	private String gcodeProvider;
	/**
	 * @return the providerReference
	 */
	public XmlGCodeProviderReference getProviderReference() {
		return providerReference;
	}
	/**
	 * @param providerReference the providerReference to set
	 */
	public void setProviderReference(XmlGCodeProviderReference providerReference) {
		this.providerReference = providerReference;
	}
	/**
	 * @return the executionOrder
	 */
	public Integer getExecutionOrder() {
		return executionOrder;
	}
	/**
	 * @param executionOrder the executionOrder to set
	 */
	public void setExecutionOrder(Integer executionOrder) {
		this.executionOrder = executionOrder;
	}
	/**
	 * @return the gcodeProvider
	 */
	public String getGcodeProvider() {
		return gcodeProvider;
	}
	/**
	 * @param gcodeProvider the gcodeProvider to set
	 */
	public void setGcodeProvider(String gcodeProvider) {
		this.gcodeProvider = gcodeProvider;
	}
}

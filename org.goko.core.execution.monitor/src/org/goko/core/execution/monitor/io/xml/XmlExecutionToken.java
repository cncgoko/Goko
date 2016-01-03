/**
 * 
 */
package org.goko.core.execution.monitor.io.xml;

import org.simpleframework.xml.Attribute;

/**
 * @author PsyKo
 * @date 1 janv. 2016
 */
public class XmlExecutionToken {
	/** Execution order of this token in the queue */
	@Attribute(name="executionOrder")
	private Integer executionOrder;
	/** GCode provider used by this token */
	@Attribute(name="gcodeProvider")
	private String codeGCodeProvider;
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
	 * @return the codeGCodeProvider
	 */
	public String getCodeGCodeProvider() {
		return codeGCodeProvider;
	}
	/**
	 * @param codeGCodeProvider the codeGCodeProvider to set
	 */
	public void setCodeGCodeProvider(String codeGCodeProvider) {
		this.codeGCodeProvider = codeGCodeProvider;
	}
	
	
}

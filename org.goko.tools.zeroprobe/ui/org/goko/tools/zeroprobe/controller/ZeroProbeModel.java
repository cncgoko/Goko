/**
 * 
 */
package org.goko.tools.zeroprobe.controller;

import org.goko.common.bindings.AbstractModelObject;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.quantity.Speed;
import org.goko.core.controller.bean.EnumControllerAxis;

/**
 * @author Psyko
 * @date 30 juil. 2017
 */
public class ZeroProbeModel extends AbstractModelObject{
	public static final String AXIS 	  					= "axis"; 	
	public static final String FEEDRATE 					= "feedrate"; 
	public static final String EXPECTED 					= "expected"; 
	public static final String MAX_DISTANCE 				= "maxDistance"; 
	public static final String TOOL_DIAMETER_COMPENSATION = "toolDiameterCompensation"; 
	public static final String TOOL_DIAMETER 				= "toolDiameter";
	
	private EnumControllerAxis axis;
	private Speed feedrate;
	private Length expected;
	private Length maxDistance;
	private boolean toolDiameterCompensation;
	private Length toolDiameter;
	/**
	 * @return the axis
	 */
	public EnumControllerAxis getAxis() {
		return axis;
	}
	/**
	 * @param axis the axis to set
	 */
	public void setAxis(EnumControllerAxis axis) {
		this.axis = axis;
	}
	/**
	 * @return the feedrate
	 */
	public Speed getFeedrate() {
		return feedrate;
	}
	/**
	 * @param feedrate the feedrate to set
	 */
	public void setFeedrate(Speed feedrate) {
		this.feedrate = feedrate;
	}
	/**
	 * @return the expected
	 */
	public Length getExpected() {
		return expected;
	}
	/**
	 * @param expected the expected to set
	 */
	public void setExpected(Length expected) {
		this.expected = expected;
	}
	/**
	 * @return the maxDistance
	 */
	public Length getMaxDistance() {
		return maxDistance;
	}
	/**
	 * @param maxDistance the maxDistance to set
	 */
	public void setMaxDistance(Length maxDistance) {
		this.maxDistance = maxDistance;
	}
	/**
	 * @return the toolDiameterCompensation
	 */
	public boolean isToolDiameterCompensation() {
		return toolDiameterCompensation;
	}
	/**
	 * @param toolDiameterCompensation the toolDiameterCompensation to set
	 */
	public void setToolDiameterCompensation(boolean toolDiameterCompensation) {
		this.toolDiameterCompensation = toolDiameterCompensation;
	}
	/**
	 * @return the toolDiameter
	 */
	public Length getToolDiameter() {
		return toolDiameter;
	}
	/**
	 * @param toolDiameter the toolDiameter to set
	 */
	public void setToolDiameter(Length toolDiameter) {
		this.toolDiameter = toolDiameter;
	}
	
	
}

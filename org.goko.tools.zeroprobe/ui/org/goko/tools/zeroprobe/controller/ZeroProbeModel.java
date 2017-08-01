/**
 * 
 */
package org.goko.tools.zeroprobe.controller;

import java.util.ArrayList;
import java.util.List;

import org.goko.common.bindings.AbstractModelObject;
import org.goko.common.elements.combo.LabeledValue;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.quantity.LengthUnit;
import org.goko.core.common.measure.quantity.Speed;
import org.goko.core.common.measure.quantity.SpeedUnit;
import org.goko.core.controller.bean.EnumControllerAxis;
import org.goko.core.gcode.element.ICoordinateSystem;

/**
 * @author Psyko
 * @date 30 juil. 2017
 */
public class ZeroProbeModel extends AbstractModelObject{
	public static final String AXIS 	  					= "axis"; 	
	public static final String FEEDRATE 					= "feedrate"; 
	public static final String EXPECTED 					= "expected"; 
	public static final String MAX_DISTANCE 				= "maxDistance"; 
	public static final String TOOL_DIAMETER_COMPENSATION 	= "toolDiameterCompensation"; 
	public static final String TOOL_DIAMETER 				= "toolDiameter";
	public static final String COORDINATE_SYSTEM			= "coordinateSystem";
	public static final String COORDINATE_SYSTEM_LIST		= "coordinateSystemList";
	public static final String IS_ERROR						= "error";
	public static final String ERROR_MESSAGE				= "errorMessage";
	public static final String INFO_MESSAGE					= "infoMessage";
	
	private EnumControllerAxis axis;
	private Speed feedrate;
	private Length expected;
	private Length maxDistance;
	private boolean toolDiameterCompensation;
	private Length toolDiameter;
	private LabeledValue<ICoordinateSystem> coordinateSystem;
	private List<LabeledValue<ICoordinateSystem>> coordinateSystemList;
	private boolean error;
	private String errorMessage;
	private String infoMessage;
	/**
	 * 
	 */
	public ZeroProbeModel() {
		axis = EnumControllerAxis.X_POSITIVE;		
		toolDiameter = Length.ZERO;
		feedrate = Speed.valueOf("100", SpeedUnit.MILLIMETRE_PER_MINUTE);
		expected = Length.ZERO;
		maxDistance = Length.valueOf(10, LengthUnit.MILLIMETRE);
		coordinateSystemList = new ArrayList<>();
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
		firePropertyChange(FEEDRATE, this.feedrate, this.feedrate = feedrate);
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
		firePropertyChange(EXPECTED, this.expected, this.expected = expected);
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
		firePropertyChange(MAX_DISTANCE, this.maxDistance, this.maxDistance = maxDistance);
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
		firePropertyChange(TOOL_DIAMETER_COMPENSATION, this.toolDiameterCompensation, this.toolDiameterCompensation = toolDiameterCompensation);
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
		firePropertyChange(TOOL_DIAMETER, this.toolDiameter, this.toolDiameter = toolDiameter);
	}

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
		firePropertyChange(AXIS, this.axis, this.axis = axis);
	}


	/**
	 * @return the coordinateSystem
	 */
	public LabeledValue<ICoordinateSystem> getCoordinateSystem() {
		return coordinateSystem;
	}


	/**
	 * @param coordinateSystem the coordinateSystem to set
	 */
	public void setCoordinateSystem(LabeledValue<ICoordinateSystem> coordinateSystem) {
		firePropertyChange(COORDINATE_SYSTEM, this.coordinateSystem, this.coordinateSystem = coordinateSystem);
	}


	/**
	 * @return the coordinateSystemList
	 */
	public List<LabeledValue<ICoordinateSystem>> getCoordinateSystemList() {
		return coordinateSystemList;
	}


	/**
	 * @param coordinateSystemList the coordinateSystemList to set
	 */
	public void setCoordinateSystemList(List<LabeledValue<ICoordinateSystem>> coordinateSystemList) {
		firePropertyChange(COORDINATE_SYSTEM_LIST, this.coordinateSystemList, this.coordinateSystemList = coordinateSystemList);
	}


	/**
	 * @return the error
	 */
	public boolean isError() {
		return error;
	}


	/**
	 * @param error the error to set
	 */
	public void setError(boolean error) {
		firePropertyChange(IS_ERROR, this.error, this.error = error);
	}


	/**
	 * @return the errorMessage
	 */
	public String getErrorMessage() {
		return errorMessage;
	}


	/**
	 * @param errorMessage the errorMessage to set
	 */
	public void setErrorMessage(String errorMessage) {
		firePropertyChange(ERROR_MESSAGE, this.errorMessage, this.errorMessage = errorMessage);
	}


	/**
	 * @return the infoMessage
	 */
	public String getInfoMessage() {
		return infoMessage;
	}


	/**
	 * @param infoMessage the infoMessage to set
	 */
	public void setInfoMessage(String infoMessage) {
		firePropertyChange(INFO_MESSAGE, this.infoMessage, this.infoMessage = infoMessage);
	}
}

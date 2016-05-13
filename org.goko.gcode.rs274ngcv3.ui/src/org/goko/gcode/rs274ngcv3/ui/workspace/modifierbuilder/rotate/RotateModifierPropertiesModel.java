/**
 * 
 */
package org.goko.gcode.rs274ngcv3.ui.workspace.modifierbuilder.rotate;

import org.goko.core.common.measure.quantity.Angle;
import org.goko.core.controller.bean.EnumControllerAxis;
import org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.panel.AbstractModifierModelObject;

/**
 * @author PsyKo
 * @date 17 janv. 2016
 */
public class RotateModifierPropertiesModel extends AbstractModifierModelObject {
	public static final String ROTATION_ANGLE = "rotationAngle";
	public static final String ROTATION_AXIS = "rotationAxis";
	/** The rotation angle */
	private Angle rotationAngle;
	/** The rotation axis */
	private EnumControllerAxis rotationAxis;

	/**
	 * @return the rotationAngle
	 */
	public Angle getRotationAngle() {
		return rotationAngle;
	}
	/**
	 * @param rotationAngle the rotationAngle to set
	 */
	public void setRotationAngle(Angle rotationAngle) {
		firePropertyChange(ROTATION_ANGLE, this.rotationAngle, this.rotationAngle = rotationAngle);
	}
	/**
	 * @return the rotationAxis
	 */
	public EnumControllerAxis getRotationAxis() {
		return rotationAxis;
	}
	/**
	 * @param rotationAxis the rotationAxis to set
	 */
	public void setRotationAxis(EnumControllerAxis rotationAxis) {
		firePropertyChange(ROTATION_AXIS, this.rotationAxis, this.rotationAxis = rotationAxis);
	}
	
	
}

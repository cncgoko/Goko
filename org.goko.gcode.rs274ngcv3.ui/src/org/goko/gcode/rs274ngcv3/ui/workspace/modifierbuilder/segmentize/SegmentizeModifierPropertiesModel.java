/**
 * 
 */
package org.goko.gcode.rs274ngcv3.ui.workspace.modifierbuilder.segmentize;

import org.goko.core.common.measure.quantity.Length;
import org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.panel.AbstractModifierModelObject;

/**
 * @author PsyKo
 * @date 17 janv. 2016
 */
public class SegmentizeModifierPropertiesModel extends AbstractModifierModelObject {
	public static final String CHORDAL_TOLERANCE = "chordalTolerance";
	/** The chordal tolerance of the modifier */
	private Length chordalTolerance;
	/**
	 * @return the chordalTolerance
	 */
	public Length getChordalTolerance() {
		return chordalTolerance;
	}
	/**
	 * @param chordalTolerance the chordalTolerance to set
	 */
	public void setChordalTolerance(Length chordalTolerance) {
		firePropertyChange(CHORDAL_TOLERANCE, this.chordalTolerance, this.chordalTolerance = chordalTolerance);
	}
	
	
}

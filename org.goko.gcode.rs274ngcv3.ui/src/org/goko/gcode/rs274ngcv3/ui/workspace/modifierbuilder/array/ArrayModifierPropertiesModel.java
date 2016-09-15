package org.goko.gcode.rs274ngcv3.ui.workspace.modifierbuilder.array;

import org.goko.core.common.measure.quantity.Angle;
import org.goko.core.common.measure.quantity.Length;
import org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.panel.AbstractModifierModelObject;

/**
 * Model for this configuration panel
 */
public class ArrayModifierPropertiesModel extends AbstractModifierModelObject{
	public static final String TRANSLATION_X = "translationX";
	public static final String TRANSLATION_Y = "translationY";
	public static final String TRANSLATION_Z = "translationZ";
	public static final String TRANSLATION_A = "translationA";
	public static final String TRANSLATION_B = "translationB";
	public static final String TRANSLATION_C = "translationC";
	public static final String COUNT = "count";
	private Length translationX;
	private Length translationY;
	private Length translationZ;
	private Angle translationA;
	private Angle translationB;
	private Angle translationC;
	private int count;
	/**
	 * @return the translationX
	 */
	public Length getTranslationX() {
		return translationX;
	}

	/**
	 * @param translationX the translationX to set
	 */
	public void setTranslationX(Length translationX) {		
		firePropertyChange("translationX", this.translationX, this.translationX = translationX);
		
	}

	/**
	 * @return the translationY
	 */
	public Length getTranslationY() {
		return translationY;
	}

	/**
	 * @param translationY the translationY to set
	 */
	public void setTranslationY(Length translationY) {
		firePropertyChange("translationY", this.translationY, this.translationY = translationY);
	}

	/**
	 * @return the translationZ
	 */
	public Length getTranslationZ() {
		return translationZ;
	}

	/**
	 * @param translationZ the translationZ to set
	 */
	public void setTranslationZ(Length translationZ) {
		firePropertyChange("translationZ", this.translationZ, this.translationZ = translationZ);
	}

	/**
	 * @return the translationA
	 */
	public Angle getTranslationA() {
		return translationA;
	}

	/**
	 * @param translationA the translationA to set
	 */
	public void setTranslationA(Angle translationA) {
		firePropertyChange("translationA", this.translationA, this.translationA = translationA);
	}

	/**
	 * @return the translationB
	 */
	public Angle getTranslationB() {
		return translationB;
	}

	/**
	 * @param translationB the translationB to set
	 */
	public void setTranslationB(Angle translationB) {
		firePropertyChange("translationB", this.translationB, this.translationB = translationB);
	}

	/**
	 * @return the translationC
	 */
	public Angle getTranslationC() {
		return translationC;
	}

	/**
	 * @param translationC the translationC to set
	 */
	public void setTranslationC(Angle translationC) {
		firePropertyChange("translationC", this.translationC, this.translationC = translationC);
	}

	/**
	 * @return the count
	 */
	public int getCount() {
		return count;
	}

	/**
	 * @param count the count to set
	 */
	public void setCount(int count) {
		firePropertyChange("count", this.count, this.count = count);
	}
	
	
}
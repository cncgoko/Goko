/*
 *
 *   Goko
 *   Copyright (C) 2013  PsyKo
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.goko.tools.viewer.jogl.preferences;

import java.math.BigDecimal;

import javax.vecmath.Color3f;
import javax.vecmath.Vector3f;

import org.eclipse.jface.resource.StringConverter;
import org.eclipse.swt.graphics.RGB;
import org.goko.core.common.GkUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.SI;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.quantity.type.NumberQuantity;
import org.goko.core.common.measure.units.Unit;
import org.goko.core.config.GkPreference;
import org.goko.core.config.GokoPreference;
import org.goko.core.math.Tuple6b;

/**
 * Jogl Viewer settings
 *
 * @author PsyKo
 *
 */
public class JoglViewerPreference extends GkPreference{	
	public static final String NODE = "org.goko.tools.viewer.jogl";

	public static final String MULTISAMPLING 	= "performances.multisampling";
	public static final String GROUP_GRID 			= "grid.";
	public static final String MAJOR_GRID_SPACING 	= "grid.majorSpacing";
	public static final String MINOR_GRID_SPACING 	= "grid.minorSpacing";
	public static final String MAJOR_GRID_COLOR 	= "grid.majorColor";
	public static final String MINOR_GRID_COLOR 	= "grid.minorColor";
	public static final String GRID_OPACITY 		= "grid.opacity";
	public static final String GRID_START_X 		= "grid.start.x";
	public static final String GRID_START_Y 		= "grid.start.y";
	public static final String GRID_START_Z 		= "grid.start.z";
	public static final String GRID_END_X 			= "grid.end.x";
	public static final String GRID_END_Y 			= "grid.end.y";
	public static final String GRID_END_Z 			= "grid.end.z";	
	
	public static final String ROTARY_AXIS_ENABLED 	= "rotaryAxisEnabled";
	public static final String ROTARY_AXIS_DIRECTION 	= "rotaryAxisDirection";
	public static final String ROTARY_AXIS_POSITION_X 	= "rotaryAxisPositionX";
	public static final String ROTARY_AXIS_POSITION_Y 	= "rotaryAxisPositionY";
	public static final String ROTARY_AXIS_POSITION_Z 	= "rotaryAxisPositionZ";

	private static JoglViewerPreference instance;
	private Tuple6b rotaryAxisPosition;


	public enum EnumRotaryAxisDirection{
		X(1,0,0),
		Y(0,1,0),
		Z(0,0,1);

		Vector3f direction;

		private EnumRotaryAxisDirection(float x, float y, float z) {
			this.direction = new Vector3f(x,y,z);
		}

		/**
		 * @return the direction
		 */
		public Vector3f getVector3f() {
			return direction;
		}
	}
	
	public JoglViewerPreference(){
		super(NODE);	
	}

	public static JoglViewerPreference getInstance() {
		if(instance == null){
			instance = new JoglViewerPreference();
		}
		return instance;
	}

	/**
	 * @return the rotaryAxisPosition
	 */
	public Tuple6b getRotaryAxisPosition() {
		return rotaryAxisPosition;
	}
	/**
	 * @param rotaryAxisPosition the rotaryAxisPosition to set
	 */
	public void setRotaryAxisPosition(Tuple6b rotaryAxisPosition) {
		this.rotaryAxisPosition = rotaryAxisPosition;
		setValue(ROTARY_AXIS_POSITION_X, rotaryAxisPosition.getX().getValue().toPlainString());
		setValue(ROTARY_AXIS_POSITION_Y, rotaryAxisPosition.getY().getValue().toPlainString());
		setValue(ROTARY_AXIS_POSITION_Z, rotaryAxisPosition.getZ().getValue().toPlainString());
	}
	/**
	 * @return the rotaryAxisDirection
	 */
	public EnumRotaryAxisDirection getRotaryAxisDirection() {
		return EnumRotaryAxisDirection.valueOf(getString(ROTARY_AXIS_DIRECTION));
	}
	/**
	 * Return the vector defining the direction of the rotary axis
	 * @return Vector3f
	 */
	public Vector3f getRotaryAxisDirectionVector() {
		switch(getRotaryAxisDirection()){
		case X: return new Vector3f(1,0,0);
		case Y: return new Vector3f(0,1,0);
		case Z: return new Vector3f(0,0,1);
		}
		return new Vector3f(1,0,0);
	}
	/**
	 * @param rotaryAxisDirection the rotaryAxisDirection to set
	 */
	public void setRotaryAxisDirection(EnumRotaryAxisDirection rotaryAxisDirection) {		
		setValue(ROTARY_AXIS_DIRECTION, rotaryAxisDirection.name());
	}

	/**
	 * @return the rotaryAxisEnabled
	 */
	public boolean isRotaryAxisEnabled() {
		return getBoolean(ROTARY_AXIS_ENABLED);
	}

	/**
	 * @param rotaryAxisEnabled the rotaryAxisEnabled to set
	 */
	public void setRotaryAxisEnabled(boolean rotaryAxisEnabled) {
		setValue(ROTARY_AXIS_ENABLED, rotaryAxisEnabled);
	}

	/**
	 * @return the multisampling
	 */
	public Integer getMultisampling() {
		return Integer.valueOf(getString(MULTISAMPLING));
	}

	/**
	 * @param multisampling the multisampling to set
	 */
	public void setMultisampling(Integer multisampling) {
		setValue(MULTISAMPLING, String.valueOf(multisampling));
	}

	/**
	 * @return the majorGridSpacing
	 * @throws GkException 
	 */
	public BigDecimal getMajorGridSpacing() throws GkException {
		return GkUtils.toBigDecimal(getString(MAJOR_GRID_SPACING));			
	}

	/**
	 * @param majorGridSpacing the majorGridSpacing to set
	 */
	public void setMajorGridSpacing(BigDecimal majorGridSpacing) {
		setValue(MAJOR_GRID_SPACING, String.valueOf(majorGridSpacing));
	}

	/**
	 * @return the minorGridSpacing
	 * @throws GkException 
	 */
	public BigDecimal getMinorGridSpacing() throws GkException {		
		return GkUtils.toBigDecimal(getString(MINOR_GRID_SPACING));		
	}

	/**
	 * @param minorGridSpacing the minorGridSpacing to set
	 */
	public void setMinorGridSpacing(BigDecimal minorGridSpacing) {
		setValue(MINOR_GRID_SPACING, String.valueOf(minorGridSpacing));
	}
	
	public Color3f getMajorColor(){
		String value = getString(MAJOR_GRID_COLOR);
		RGB color = StringConverter.asRGB(value);
		return new Color3f((float)(color.red/255.0), (float)(color.green/255.0), (float)(color.blue/255.0));
	}
	
	public Color3f getMinorColor(){
		String value = getString(MINOR_GRID_COLOR);
		RGB color = StringConverter.asRGB(value);
		return new Color3f((float)(color.red/255.0), (float)(color.green/255.0), (float)(color.blue/255.0));
	}
	
	public float getGridOpacity(){
		return (float) (getInt(GRID_OPACITY) / 100.0);
	}
	
	public Tuple6b getGridStart() throws GkException{
		Unit<Length> unit = GokoPreference.getInstance().getLengthUnit();
		Tuple6b start = new Tuple6b(unit, SI.DEGREE_ANGLE);
		start.setX(NumberQuantity.of(new BigDecimal(getString(GRID_START_X)), unit));
		start.setY(NumberQuantity.of(new BigDecimal(getString(GRID_START_Y)), unit));
		start.setZ(NumberQuantity.of(new BigDecimal(getString(GRID_START_Z)), unit));
		return start;
	}
	
	public Tuple6b getGridEnd() throws GkException{
		Unit<Length> unit = GokoPreference.getInstance().getLengthUnit();
		Tuple6b end = new Tuple6b(unit, SI.DEGREE_ANGLE);
		end.setX(NumberQuantity.of(new BigDecimal(getString(GRID_END_X)), unit));
		end.setY(NumberQuantity.of(new BigDecimal(getString(GRID_END_Y)), unit));
		end.setZ(NumberQuantity.of(new BigDecimal(getString(GRID_END_Z)), unit));
		return end;
	}	
}

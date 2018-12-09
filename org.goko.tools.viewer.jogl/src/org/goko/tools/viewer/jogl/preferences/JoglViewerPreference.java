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
import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.AngleUnit;
import org.goko.core.common.measure.quantity.Length;
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

	public static final String MULTISAMPLING 		= "performances.multisampling";
	public static final String BACKGROUND_COLOR 	= "viewer.backgroundColor";
	public static final String SHOW_FPS			 	= "viewer.showFps";
	public static final String DEFAULT_CAMERA		= "viewer.defaultCamera";
	public static final String GROUP_GRID 			= "grid.";
	public static final String MAJOR_GRID_SPACING 	= "grid.majorSpacing";
	public static final String MINOR_GRID_SPACING 	= "grid.minorSpacing";
	public static final String MAJOR_GRID_COLOR 	= "grid.majorColor";
	public static final String MINOR_GRID_COLOR 	= "grid.minorColor";
	public static final String MAJOR_GRID_OPACITY 	= "grid.major.opacity";
	public static final String MINOR_GRID_OPACITY 	= "grid.minor.opacity";
	public static final String GRID_AXIS_OPACITY 	= "grid.axis.opacity";
	public static final String GRID_GRADUATION_SIZE	= "grid.graduation.size";
	public static final String GRID_USE_VOLUME_PROVIDER	= "grid.use.volume.provider";
	public static final String GRID_START_X 		= "grid.start.x";
	public static final String GRID_START_Y 		= "grid.start.y";
	public static final String GRID_START_Z 		= "grid.start.z";
	public static final String GRID_END_X 			= "grid.end.x";
	public static final String GRID_END_Y 			= "grid.end.y";
	public static final String GRID_END_Z 			= "grid.end.z";	
	
	public static final String ROTARY_AXIS_ENABLED 		= "rotaryAxisEnabled";
	public static final String ROTARY_AXIS_DIRECTION 	= "rotaryAxisDirection";
	public static final String ROTARY_AXIS_POSITION_X 	= "rotaryAxisPositionX";
	public static final String ROTARY_AXIS_POSITION_Y 	= "rotaryAxisPositionY";
	public static final String ROTARY_AXIS_POSITION_Z 	= "rotaryAxisPositionZ";

	public static final String ORBIT_INVERT_X_AXIS 	= "cameraOrbitInvertXAxis";
	public static final String ORBIT_INVERT_Y_AXIS	= "cameraOrbitInvertYAxis";
	public static final String ORBIT_SENSITIVITY 	= "cameraOrbitSensitivity";
	
	public static final String PAN_INVERT_X_AXIS 	= "cameraPanInvertXAxis";
	public static final String PAN_INVERT_Y_AXIS	= "cameraPanInvertYAxis";
	public static final String PAN_SENSITIVITY 		= "cameraPanSensitivity";
	
	public static final String ZOOM_INVERT_AXIS		= "cameraZoomInvertAxis";
	public static final String ZOOM_SENSITIVITY 	= "cameraZoomSensitivity";
	
	public static final String DISPLAY_POSITION_OVERLAY	= "displayPositionOverlay";
	
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
	 * @throws GkException 
	 */
	public Tuple6b getRotaryAxisPosition() throws GkException {
		if (rotaryAxisPosition == null) {
			rotaryAxisPosition = new Tuple6b(
					Length.parse(getString(ROTARY_AXIS_POSITION_X)),
					Length.parse(getString(ROTARY_AXIS_POSITION_Y)),
					Length.parse(getString(ROTARY_AXIS_POSITION_Z))
			);
		}
		return rotaryAxisPosition;		
		//return rotaryAxisPosition;
	}
	/**
	 * @param rotaryAxisPosition the rotaryAxisPosition to set
	 */
	public void setRotaryAxisPosition(Tuple6b rotaryAxisPosition) {
		this.rotaryAxisPosition = rotaryAxisPosition;
//		setValue(ROTARY_AXIS_POSITION_X, rotaryAxisPosition.getX().getValue().toPlainString());
//		setValue(ROTARY_AXIS_POSITION_Y, rotaryAxisPosition.getY().getValue().toPlainString());
//		setValue(ROTARY_AXIS_POSITION_Z, rotaryAxisPosition.getZ().getValue().toPlainString());
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
	public Length getMajorGridSpacing() throws GkException {
		return Length.parse(getString(MAJOR_GRID_SPACING));			
	}

	/**
	 * @param majorGridSpacing the majorGridSpacing to set
	 * @throws GkException GkException 
	 */
	public void setMajorGridSpacing(Length majorGridSpacing) throws GkException {
		setValue(MAJOR_GRID_SPACING, GokoPreference.getInstance().format(majorGridSpacing));
	}

	/**
	 * @return the minorGridSpacing
	 * @throws GkException 
	 */
	public Length getMinorGridSpacing() throws GkException {		
		return Length.parse(getString(MINOR_GRID_SPACING));				
	}

	/**
	 * @param minorGridSpacing the minorGridSpacing to set
	 * @throws GkException GkException 
	 */
	public void setMinorGridSpacing(Length minorGridSpacing) throws GkException {
		setValue(MINOR_GRID_SPACING, GokoPreference.getInstance().format(minorGridSpacing));
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
	
	public float getMajorGridOpacity(){
		return (float) (getInt(MAJOR_GRID_OPACITY) / 100.0);
	}
	
	public float getMinorGridOpacity(){
		return (float) (getInt(MINOR_GRID_OPACITY) / 100.0);
	}
	
	public float getAxisGridOpacity(){
		return (float) (getInt(GRID_AXIS_OPACITY) / 100.0);
	}
	
	public Tuple6b getGridStart() throws GkException{
		Unit<Length> unit = GokoPreference.getInstance().getLengthUnit();
		Tuple6b start = new Tuple6b(unit, AngleUnit.DEGREE_ANGLE);
		start.setX(Length.parse(getString(GRID_START_X)));
		start.setY(Length.parse(getString(GRID_START_Y)));
		start.setZ(Length.parse(getString(GRID_START_Z)));
		return start;
	}
	
	public Tuple6b getGridEnd() throws GkException{
		Unit<Length> unit = GokoPreference.getInstance().getLengthUnit();
		Tuple6b end = new Tuple6b(unit, AngleUnit.DEGREE_ANGLE);
		end.setX(Length.parse(getString(GRID_END_X)));
		end.setY(Length.parse(getString(GRID_END_Y)));
		end.setZ(Length.parse(getString(GRID_END_Z)));
		return end;
	}	
	
	public Color3f getBackgroundColor(){
		String value = getString(BACKGROUND_COLOR);
		RGB color = StringConverter.asRGB(value);
		return new Color3f((float)(color.red/255.0), (float)(color.green/255.0), (float)(color.blue/255.0));	
	}
	
	public boolean isShowFps(){
		return getBoolean(SHOW_FPS);
	}

	/**
	 * @return the cameraOrbitInvertXAxis
	 */
	public boolean isCameraOrbitInvertXAxis() {
		return getBoolean(ORBIT_INVERT_X_AXIS);
	}

	/**
	 * @param cameraOrbitInvertXAxis the cameraOrbitInvertXAxis to set
	 */
	public void setCameraOrbitInvertXAxis(boolean cameraOrbitInvertXAxis) {
		setValue(ORBIT_INVERT_X_AXIS, cameraOrbitInvertXAxis);
	}

	/**
	 * @return the cameraOrbitInvertYAxis
	 */
	public boolean isCameraOrbitInvertYAxis() {
		return getBoolean(ORBIT_INVERT_Y_AXIS);
	}

	/**
	 * @param cameraOrbitInvertYAxis the cameraOrbitInvertYAxis to set
	 */
	public void setCameraOrbitInvertYAxis(boolean cameraOrbitInvertYAxis) {
		setValue(ORBIT_INVERT_Y_AXIS, cameraOrbitInvertYAxis);
	}

	/**
	 * @return the cameraOrbitSensitivity
	 */
	public BigDecimal getCameraOrbitSensitivity() {
		return new BigDecimal(getString(ORBIT_SENSITIVITY));
	}

	/**
	 * @param cameraOrbitSensitivity the cameraOrbitSensitivity to set
	 */
	public void setCameraOrbitSensitivity(BigDecimal cameraOrbitSensitivity) {
		setValue(ORBIT_SENSITIVITY, cameraOrbitSensitivity.toPlainString());
	}

	/**
	 * @return the cameraPanInvertXAxis
	 */
	public boolean isCameraPanInvertXAxis() {
		return getBoolean(PAN_INVERT_X_AXIS);
	}

	/**
	 * @param cameraPanInvertXAxis the cameraPanInvertXAxis to set
	 */
	public void setCameraPanInvertXAxis(boolean cameraPanInvertXAxis) {
		setValue(PAN_INVERT_X_AXIS, cameraPanInvertXAxis);
	}

	/**
	 * @return the cameraPanInvertYAxis
	 */
	public boolean isCameraPanInvertYAxis() {
		return getBoolean(PAN_INVERT_Y_AXIS);
	}

	/**
	 * @param cameraPanInvertYAxis the cameraPanInvertYAxis to set
	 */
	public void setCameraPanInvertYAxis(boolean cameraPanInvertYAxis) {
		setValue(PAN_INVERT_Y_AXIS, cameraPanInvertYAxis);
	}

	/**
	 * @return the cameraPanSensitivity
	 */
	public BigDecimal getCameraPanSensitivity() {
		return new BigDecimal(getString(PAN_SENSITIVITY));
	}

	/**
	 * @param cameraPanSensitivity the cameraPanSensitivity to set
	 */
	public void setCameraPanSensitivity(BigDecimal cameraPanSensitivity) {
		setValue(PAN_SENSITIVITY, cameraPanSensitivity.toPlainString());
	}

	/**
	 * @return the cameraZoomInvertAxis
	 */
	public boolean isCameraZoomInvertAxis() {
		return getBoolean(ZOOM_INVERT_AXIS);
	}

	/**
	 * @param cameraZoomInvertAxis the cameraZoomInvertAxis to set
	 */
	public void setCameraZoomInvertAxis(boolean cameraZoomInvertAxis) {
		setValue(ZOOM_INVERT_AXIS, cameraZoomInvertAxis);
	}

	/**
	 * @return the cameraZoomSensitivity
	 */
	public BigDecimal getCameraZoomSensitivity() {
		return new BigDecimal(getString(ZOOM_SENSITIVITY));
	}

	/**
	 * @param cameraZoomSensitivity the cameraZoomSensitivity to set
	 */
	public void setCameraZoomSensitivity(BigDecimal cameraZoomSensitivity) {
		setValue(ZOOM_SENSITIVITY, cameraZoomSensitivity.toPlainString());
	}

	/**
	 * @return the graduationSize
	 * @throws GkException GkException 
	 */
	public Length getGraduationSize() throws GkException {
		return Length.parse(getString(GRID_GRADUATION_SIZE));
	}

	/**
	 * @param graduationSize the graduationSize to set
	 * @throws GkException GkException 
	 */
	public void setGraduationSize(Length graduationSize) throws GkException {
		setValue(GRID_GRADUATION_SIZE, GokoPreference.getInstance().format(graduationSize));
	}
	
	/**
	 * @return the defaultView 
	 */
	public String getDefaultCamera() {
		return getString(DEFAULT_CAMERA);
	}

	/**
	 * @param cameraPanSensitivity the cameraPanSensitivity to set
	 */
	public void setDefaultCamera(String defaultView) {
		setValue(DEFAULT_CAMERA, defaultView);
	}
	
	/**
	 * @return the GRID_USE_VOLUME_PROVIDER 
	 */
	public boolean isUseWorkVolumeProvider() {
		return getBoolean(GRID_USE_VOLUME_PROVIDER);
	}

	/**
	 * @param use the GRID_USE_VOLUME_PROVIDER to set
	 */
	public void setUseWorkVolumeProvider(boolean use) {
		setValue(GRID_USE_VOLUME_PROVIDER, use);
	}
	
	/**
	 * @param displayPositionOverlay the displayPositionOverlay to set
	 */
	public void setDisplayPositionOverlay(boolean displayPositionOverlay) {
		setValue(DISPLAY_POSITION_OVERLAY, displayPositionOverlay);
	}

	/**
	 * @return the displayPositionOverlay
	 */
	public boolean isDisplayPositionOverlay() {
		return getBoolean(MULTISAMPLING);
	}
}

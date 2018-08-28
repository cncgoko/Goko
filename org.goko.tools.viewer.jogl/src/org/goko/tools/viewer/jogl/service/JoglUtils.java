package org.goko.tools.viewer.jogl.service;

import java.nio.FloatBuffer;
import java.util.List;

import javax.vecmath.Color4f;
import javax.vecmath.Point3f;
import javax.vecmath.Tuple3d;
import javax.vecmath.Tuple3f;
import javax.vecmath.Tuple4d;
import javax.vecmath.Tuple4f;
import javax.vecmath.Vector3f;

import org.apache.commons.collections.CollectionUtils;
import org.goko.core.common.measure.Units;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.units.Unit;
import org.goko.core.controller.IGCodeContextProvider;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.tools.viewer.jogl.utils.render.grid.GraduatedGridRenderer;
import org.goko.tools.viewer.jogl.utils.render.grid.IGridRenderer;

import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLAutoDrawable;

/**
 * Utility class for Jogl rendering
 *
 * @author Psyko
 *
 */
public class JoglUtils {
	/** Default Jogl unit (by convention)*/
	public static final Unit<Length> JOGL_UNIT = Units.MILLIMETRE;
	public static final String XY_GRID_ID = "GRID.XY";
	public static final String XZ_GRID_ID = "GRID.XZ";
	public static final String YZ_GRID_ID = "GRID.YZ";
	public static final Vector3f X_AXIS = new Vector3f(1f,0f,0f);
	public static final Vector3f Y_AXIS = new Vector3f(0f,1f,0f);
	public static final Vector3f Z_AXIS = new Vector3f(0f,0f,1f);
	public static final Vector3f X_AXIS_NEGATIVE = new Vector3f(-1f,0f,0f);
	public static final Vector3f Y_AXIS_NEGATIVE = new Vector3f(0f,-1f,0f);
	public static final Vector3f Z_AXIS_NEGATIVE = new Vector3f(0f,0f,-1f);
	public static final Color4f X_COLOR = new Color4f(1f,0f,0f,1f);
	public static final Color4f Y_COLOR = new Color4f(0f,1f,0f,1f);
	public static final Color4f Z_COLOR = new Color4f(0f,0f,1f,1f);
	
	

	private static final Point3f DEFAULT_COLOR = new Point3f(0.8f,0.8f,0.8f);

	public static IGridRenderer drawXYGrid(IGCodeContextProvider<GCodeContext> gcodeContextProvider){
		IGridRenderer xyGridRenderer = new GraduatedGridRenderer(JoglUtils.XY_GRID_ID, gcodeContextProvider);
		xyGridRenderer.setNormal(JoglUtils.Z_AXIS);		
		xyGridRenderer.setHorizontalVector(JoglUtils.X_AXIS);
		xyGridRenderer.setVerticalVector(JoglUtils.Y_AXIS);
		xyGridRenderer.setHorizontalColor(JoglUtils.X_COLOR);
		xyGridRenderer.setVerticalColor(JoglUtils.Y_COLOR);
		return xyGridRenderer;
	}
	
	public static IGridRenderer drawXZGrid(IGCodeContextProvider<GCodeContext> gcodeContextProvider){
		IGridRenderer xzGridRenderer = new GraduatedGridRenderer(JoglUtils.XZ_GRID_ID, gcodeContextProvider);
		xzGridRenderer.setNormal(JoglUtils.Y_AXIS);
		xzGridRenderer.setHorizontalVector(JoglUtils.X_AXIS);
		xzGridRenderer.setVerticalVector(JoglUtils.Z_AXIS);
		xzGridRenderer.setHorizontalColor(JoglUtils.X_COLOR);
		xzGridRenderer.setVerticalColor(JoglUtils.Z_COLOR);
		return xzGridRenderer;
	}
	
	public static IGridRenderer drawYZGrid(IGCodeContextProvider<GCodeContext> gcodeContextProvider){
		IGridRenderer yzGridRenderer = new GraduatedGridRenderer(JoglUtils.YZ_GRID_ID, gcodeContextProvider);
		yzGridRenderer.setNormal(JoglUtils.X_AXIS);		
		yzGridRenderer.setHorizontalVector(JoglUtils.Y_AXIS_NEGATIVE);
		yzGridRenderer.setVerticalVector(JoglUtils.Z_AXIS);		
		yzGridRenderer.setHorizontalColor(JoglUtils.Y_COLOR);
		yzGridRenderer.setVerticalColor(JoglUtils.Z_COLOR);
		return yzGridRenderer;
	}
	/**
	 * Generate a FloatBuffer with the given list of Tuple4d.
	 * For each tuple4d, 4 float values will be added the the created buffer.
	 * @param lstTuple4d the list of Tuple4d
	 * @return a FloatBuffer
	 */
	public static FloatBuffer buildFloatBuffer4d(List<? extends Tuple4d> lstTuple4d){
		FloatBuffer buffer = FloatBuffer.allocate( CollectionUtils.size(lstTuple4d) * 4);
		for (Tuple4d tuple4d : lstTuple4d) {
			buffer.put((float) tuple4d.x);
			buffer.put((float) tuple4d.y);
			buffer.put((float) tuple4d.z);
			buffer.put((float) tuple4d.w);
		}
		return buffer;
	}
	/**
	 * Generate a FloatBuffer with the given list of Tuple4f.
	 * For each tuple4f, 4 float values will be added the the created buffer.
	 * @param lstTuple4d the list of Tuple4d
	 * @return a FloatBuffer
	 */
	public static FloatBuffer buildFloatBuffer4f(List<? extends Tuple4f> lstTuple4f){
		FloatBuffer buffer = FloatBuffer.allocate( CollectionUtils.size(lstTuple4f) * 4);
		for (Tuple4f tuple4d : lstTuple4f) {
			buffer.put(tuple4d.x);
			buffer.put(tuple4d.y);
			buffer.put(tuple4d.z);
			buffer.put(tuple4d.w);
		}
		return buffer;
	}

	/**
	 * Generate a FloatBuffer with the given list of Tuple3d.
	 * For each tuple3d, 3 float values will be added the the created buffer.
	 * @param lstTuple4d the list of Tuple4d
	 * @return a FloatBuffer
	 */
	public static FloatBuffer buildFloatBuffer3d(List<? extends Tuple3d> lstTuple){
		FloatBuffer buffer = FloatBuffer.allocate( CollectionUtils.size(lstTuple) * 4);
		for (Tuple3d tuple3d : lstTuple) {
			buffer.put((float) tuple3d.x);
			buffer.put((float) tuple3d.y);
			buffer.put((float) tuple3d.z);
			buffer.put(1);
		}
		return buffer;
	}

	/**
	 * Generate a FloatBuffer with the given list of Tuple3f.
	 * For each tuple3f, 3 float values will be added the the created buffer.
	 * @param lstTuple4d the list of Tuple4d
	 * @return a FloatBuffer
	 */
	public static FloatBuffer buildFloatBuffer3f(List<? extends Tuple3f> lstTuple){
		FloatBuffer buffer = FloatBuffer.allocate( CollectionUtils.size(lstTuple) * 4);
		for (Tuple3f tuple3f : lstTuple) {
			buffer.put(tuple3f.x);
			buffer.put(tuple3f.y);
			buffer.put(tuple3f.z);
			buffer.put(1);
		}
		return buffer;
	}
	
	public static GL3 getSupportedGL(GLAutoDrawable gLAutoDrawable){		
		if(gLAutoDrawable.getGL().isGL4()){			
			return gLAutoDrawable.getGL().getGL4();
		}
		if(gLAutoDrawable.getGL().isGL3()){			
			return gLAutoDrawable.getGL().getGL3();
		}
		throw new RuntimeException("Could not identify a compatible GL version");
	}
}

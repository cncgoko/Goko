/**
 * 
 */
package org.goko.tools.viewer.jogl.utils.render.basic;

import java.nio.FloatBuffer;

import javax.vecmath.Color4f;
import javax.vecmath.Vector3d;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Angle;
import org.goko.core.common.measure.quantity.AngleUnit;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.math.Tuple6b;
import org.goko.tools.viewer.jogl.service.JoglUtils;
import org.goko.tools.viewer.jogl.shaders.EnumGokoShaderProgram;
import org.goko.tools.viewer.jogl.shaders.ShaderLoader;
import org.goko.tools.viewer.jogl.utils.render.internal.AbstractVboJoglRenderer;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;

/**
 * @author Psyko
 * @date 7 déc. 2018
 */
public class DiscRenderer extends AbstractVboJoglRenderer{
	private Tuple6b position;
	private Vector3d normal;
	private Vector3d side;
	private Color4f color;	
	private Length scale;
	private Angle startAngle;
	private Angle endAngle;

	public DiscRenderer(Tuple6b position, Color4f color, Vector3d normal, Length scale, Angle startAngle, Angle endAngle, Vector3d side) {
		super(GL.GL_TRIANGLE_FAN, VERTICES | COLORS);
		this.position  = position;				
		this.color = color;
		this.normal = normal;
		this.scale = scale;
		this.startAngle = startAngle;
		this.endAngle = endAngle;
		this.side = side;
	}
	
	/**
	 * @throws GkException
	 */
	@Override
	protected void buildGeometry() throws GkException {
		Angle deltaAngle = endAngle.subtract(startAngle);
		int fullCirclePoints = 48;
		int nbPoints = (int)Math.ceil(deltaAngle.multiply(fullCirclePoints).divide(Angle.TWO_PI).doubleValue()); 
		double dStartAngle = startAngle.doubleValue(AngleUnit.RADIAN);
		double dAngleStep = deltaAngle.divide(nbPoints).doubleValue(AngleUnit.RADIAN);
		double dScale = scale.doubleValue(JoglUtils.JOGL_UNIT);
		
		setVerticesCount(nbPoints + 2);
		FloatBuffer vertices = FloatBuffer.allocate(getVerticesCount() * 4);
		FloatBuffer colors 	 = FloatBuffer.allocate(getVerticesCount() * 4);
		
		Vector3d vPosition = position.toVector3d(JoglUtils.JOGL_UNIT);		
		// Central point
		vertices.put(new float[]{(float) vPosition.x,(float) vPosition.y,(float) vPosition.z, 1});
		colors.put(new float[]{color.x,color.y,color.z,color.w});
		
		Vector3d a = new Vector3d(side);
		Vector3d b = new Vector3d();
		b.cross(normal, side);
		
		for (int i = 0; i <= nbPoints; i++) {
			vertices.put(new float[]{
					(float) (vPosition.x + dScale * Math.cos(dStartAngle + i*dAngleStep)*a.x + dScale * Math.sin(dStartAngle + i * dAngleStep) * b.x),
					(float) (vPosition.y + dScale * Math.cos(dStartAngle + i*dAngleStep)*a.y + dScale * Math.sin(dStartAngle + i * dAngleStep) * b.y),
					(float) (vPosition.z + dScale * Math.cos(dStartAngle + i*dAngleStep)*a.z + dScale * Math.sin(dStartAngle + i * dAngleStep) * b.z),
					 1});
			colors.put(new float[]{color.x,color.y,color.z,color.w});			
		}		

		vertices.rewind();
		colors.rewind();
		setVerticesBuffer(vertices);
		setColorsBuffer(colors);
	}

	/**
	 * @param gl
	 * @return
	 * @throws GkException
	 */
	@Override
	protected int loadShaderProgram(GL3 gl) throws GkException {
		return ShaderLoader.loadShader(gl, EnumGokoShaderProgram.SHADED_FLAT_SHADER);
	}

}

/**
 * 
 */
package org.goko.tools.viewer.jogl.utils.render.coordinate;

import javax.vecmath.Color4f;
import javax.vecmath.Vector3d;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Angle;
import org.goko.core.common.measure.quantity.AngleUnit;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.math.Tuple6b;
import org.goko.tools.viewer.jogl.service.AbstractCoreJoglMultipleRenderer;
import org.goko.tools.viewer.jogl.service.JoglUtils;
import org.goko.tools.viewer.jogl.utils.render.basic.CircleRenderer;
import org.goko.tools.viewer.jogl.utils.render.basic.DiscRenderer;

import com.jogamp.opengl.GL3;

/**
 * 
 * @author Psyko
 * @date 7 déc. 2018
 */
public class OriginRenderer extends AbstractCoreJoglMultipleRenderer {
	private Length scale;
	private Color4f color;
	private Tuple6b center;
	private Vector3d normal;
	
	public OriginRenderer(Length scale, Color4f color, Tuple6b center) {
		this(scale, color, center, new Vector3d(0, 0, 1));
	}
	/**
	 * @param scale
	 * @param color
	 * @param center
	 */
	public OriginRenderer(Length scale, Color4f color, Tuple6b center, Vector3d normal) {
		super();
		this.scale = scale;
		this.color = color;
		this.center = center;
		this.normal = normal;
		CircleRenderer circleRender = new CircleRenderer(center, scale, color, normal);
		addRenderer(circleRender);
		Vector3d horizontal = normal.dot(JoglUtils.X_AXIS_D) == 0 ? JoglUtils.X_AXIS_D: JoglUtils.Y_AXIS_D;
		DiscRenderer discRenderer1 = new DiscRenderer(center, color, normal, scale, Angle.ZERO, Angle.valueOf(90, AngleUnit.DEGREE_ANGLE), horizontal);
		addRenderer(discRenderer1);
		DiscRenderer discRenderer2 = new DiscRenderer(center, color, normal, scale, Angle.valueOf(180, AngleUnit.DEGREE_ANGLE), Angle.valueOf(270, AngleUnit.DEGREE_ANGLE), horizontal);
		addRenderer(discRenderer2);
	}
	
	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.service.AbstractCoreJoglRenderer#performInitialize(com.jogamp.opengl.GL3)
	 */
	@Override
	protected void performInitialize(GL3 gl) throws GkException {		
				
		
	}

}

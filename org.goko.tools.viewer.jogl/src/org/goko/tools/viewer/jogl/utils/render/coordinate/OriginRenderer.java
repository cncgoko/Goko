/**
 * 
 */
package org.goko.tools.viewer.jogl.utils.render.coordinate;

import javax.vecmath.Color4f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Angle;
import org.goko.core.common.measure.quantity.AngleUnit;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.quantity.LengthUnit;
import org.goko.core.math.Tuple6b;
import org.goko.tools.viewer.jogl.service.AbstractCoreJoglMultipleRenderer;
import org.goko.tools.viewer.jogl.utils.render.basic.CircleRenderer;
import org.goko.tools.viewer.jogl.utils.render.basic.DiscRenderer;

import com.jogamp.opengl.GL3;

/**
 * 
 * @author Psyko
 * @date 7 déc. 2018
 */
public class OriginRenderer extends AbstractCoreJoglMultipleRenderer {
	private float scale;
	private Color4f color;
	private Tuple6b center;
	
	/**
	 * @param scale
	 * @param color
	 * @param center
	 */
	public OriginRenderer(float scale, Color4f color, Tuple6b center) {
		super();
		this.scale = scale;
		this.color = color;
		this.center = center;
		CircleRenderer circleRender = new CircleRenderer(new Tuple6b(), Length.valueOf(1, LengthUnit.MILLIMETRE), new Color4f(1,1,1,1), new Vector3d(0,0,1));
		addRenderer(circleRender);
		DiscRenderer discRenderer1 = new DiscRenderer(new Point3d(), new Color4f(1,1,1,1), new Vector3d(0,0,1), Length.valueOf(1, LengthUnit.MILLIMETRE), Angle.ZERO, Angle.valueOf(90, AngleUnit.DEGREE_ANGLE), new Vector3d(1,0,0));
		addRenderer(discRenderer1);
		DiscRenderer discRenderer2 = new DiscRenderer(new Point3d(), new Color4f(1,1,1,1), new Vector3d(0,0,1), Length.valueOf(1, LengthUnit.MILLIMETRE), Angle.valueOf(180, AngleUnit.DEGREE_ANGLE), Angle.valueOf(270, AngleUnit.DEGREE_ANGLE), new Vector3d(1,0,0));
		addRenderer(discRenderer2);
	}
	
	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.service.AbstractCoreJoglRenderer#performInitialize(com.jogamp.opengl.GL3)
	 */
	@Override
	protected void performInitialize(GL3 gl) throws GkException {		
				
		
	}

}

/**
 * 
 */
package org.goko.tools.viewer.jogl.utils.render.coordinate;

import java.math.BigDecimal;

import javax.vecmath.Color4f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.math.Tuple6b;
import org.goko.tools.viewer.jogl.service.AbstractCoreJoglMultipleRenderer;
import org.goko.tools.viewer.jogl.service.JoglUtils;
import org.goko.tools.viewer.jogl.utils.render.basic.PolylineRenderer;
import org.goko.tools.viewer.jogl.utils.render.coordinate.measurement.ArrowRenderer;

import com.jogamp.opengl.GL3;

/**
 * @author Psyko
 * @date 15 déc. 2018
 */
public class RotaryAxisRenderer extends AbstractCoreJoglMultipleRenderer {
	private Tuple6b position;
	private Vector3d axis;
	
	public RotaryAxisRenderer(Vector3d axis, Tuple6b position, Length scale, Color4f color){
		this.axis = axis;
		this.position = position;
		addRenderer(new OriginRenderer(scale, color, position, axis));		
		
		Tuple6b arrowPosition = new Tuple6b(position);
		Tuple6b delta = new Tuple6b(axis, JoglUtils.JOGL_UNIT);
		delta.scale(new BigDecimal(2 * scale.doubleValue(JoglUtils.JOGL_UNIT)));
		arrowPosition.add(delta);
		
		Vector3d horizontal = axis.dot(JoglUtils.X_AXIS_D) == 0 ? JoglUtils.X_AXIS_D: JoglUtils.Y_AXIS_D;
		addRenderer(new ArrowRenderer(arrowPosition, axis, horizontal, color));
		addRenderer(new PolylineRenderer(false, color, new Point3d[]{
				position.toPoint3d(JoglUtils.JOGL_UNIT),
				arrowPosition.toPoint3d(JoglUtils.JOGL_UNIT),
		}));
	}
	
	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.service.AbstractCoreJoglRenderer#performInitialize(com.jogamp.opengl.GL3)
	 */
	@Override
	protected void performInitialize(GL3 gl) throws GkException {
		
		
	}

}

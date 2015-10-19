/*
 *	This file is part of Goko.
 *
 *  Goko is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Goko is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Goko.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.goko.tools.viewer.jogl.utils.render.coordinate.measurement;

import javax.media.opengl.GL3;
import javax.vecmath.Color4f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.quantity.Quantity;
import org.goko.core.common.measure.quantity.type.NumberQuantity;
import org.goko.core.config.GokoPreference;
import org.goko.core.math.Tuple6b;
import org.goko.tools.viewer.jogl.service.AbstractCoreJoglMultipleRenderer;
import org.goko.tools.viewer.jogl.service.JoglUtils;
import org.goko.tools.viewer.jogl.utils.render.basic.CircleRenderer;
import org.goko.tools.viewer.jogl.utils.render.basic.PointRenderer;
import org.goko.tools.viewer.jogl.utils.render.basic.PolylineRenderer;
import org.goko.tools.viewer.jogl.utils.render.text.TextRenderer;

public class DiameterRenderer extends AbstractCoreJoglMultipleRenderer{
	/** The center of the diameter*/
	private Tuple6b center;
	/** The value of the diameter */
	private Quantity<Length> diameter;
	private Quantity<Length> radius;
	/** The color */
	private Color4f color;
	/** The normal */
	private Vector3d normal;
	/** Center color */
	private Color4f centerColor;
	
	/** Constructor */
	public DiameterRenderer(Tuple6b center, Quantity<Length> diameter, Color4f color, Vector3d normal) {
		this(center, diameter, color, normal, color);
	}
	
	/** Constructor */
	public DiameterRenderer(Tuple6b center, Quantity<Length> diameter, Color4f color, Vector3d normal, Color4f centerColor) {
		super();
		this.center = center;
		this.diameter = diameter;
		this.radius = NumberQuantity.of( diameter.doubleValue() / 2, diameter.getUnit());
		this.color = color;
		this.normal = normal;
		this.centerColor = centerColor;
	}


	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.service.AbstractCoreJoglRenderer#performInitialize(javax.media.opengl.GL3)
	 */
	@Override
	protected void performInitialize(GL3 gl) throws GkException {
		addRenderer(new CircleRenderer(center, radius, color, normal));
		addRenderer(new PointRenderer(center, 1, centerColor));
		Vector3d a = new Vector3d(1,0,0);
		Vector3d b = new Vector3d(1,0,0);
		if(a.dot(normal) <= 0.01){
			a = new Vector3d(0,1,0);
		}
		b.cross(normal, a);
		a.cross(b, normal);
		Vector3d direction = new Vector3d();
		Point3d pos = center.to(JoglUtils.JOGL_UNIT).toPoint3d();
		direction.add(a, b);
		direction.normalize();
		direction.scale((float) radius.doubleValue());
		pos.add(direction);
		Vector3d base = new Vector3d();
		base.sub(a,b);

		// Points on the circle
		Point3d p1 = center.to(JoglUtils.JOGL_UNIT).toPoint3d();
		Point3d p2 = new Point3d(p1);
		direction.normalize();
		direction.scale((float) radius.doubleValue());
		p1.add(direction);
		p2.sub(direction);

		direction.normalize();
		direction.scale(5);
		Point3d p3 = new Point3d(p1);
		p3.add(direction);
		// Draw the diameter line
		direction.normalize();
		addRenderer(new PolylineRenderer(false, color, p2, p3));

		// Draw arrows
		addRenderer(new ArrowRenderer(p1, direction, base, color));
		direction.negate();
		addRenderer(new ArrowRenderer(p2, direction, base, color));

		Vector3d txtHeight = new Vector3d();
		direction.normalize();
		txtHeight.cross(normal,direction);

		Point3d pos3d = new Point3d(p3.x, p3.y, p3.z);		
		addRenderer(new TextRenderer( GokoPreference.getInstance().format(diameter), 3.0, pos3d, direction, txtHeight, TextRenderer.RIGHT | TextRenderer.MIDDLE));

	}


}

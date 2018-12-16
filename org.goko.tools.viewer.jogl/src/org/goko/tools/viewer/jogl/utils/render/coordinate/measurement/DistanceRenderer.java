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

import javax.vecmath.Color4f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.config.GokoPreference;
import org.goko.core.log.GkLog;
import org.goko.core.math.Tuple6b;
import org.goko.tools.viewer.jogl.service.AbstractCoreJoglMultipleRenderer;
import org.goko.tools.viewer.jogl.service.JoglUtils;
import org.goko.tools.viewer.jogl.utils.render.basic.PolylineRenderer;
import org.goko.tools.viewer.jogl.utils.render.text.v2.TextRenderer;

import com.jogamp.opengl.GL3;

/**
 * Simple rule display
 *
 * @author PsyKo
 *
 */
public class DistanceRenderer extends AbstractCoreJoglMultipleRenderer{
	static GkLog LOG = GkLog.getLogger(DistanceRenderer.class);
	private Tuple6b startPoint;
	private Tuple6b endPoint;
	private Vector3d normal;
	private Vector3d direction;
	private Length length;

	private PolylineRenderer lineRenderer;
	private ArrowRenderer startArrowRenderer;
	private ArrowRenderer endArrowRenderer;
	private TextRenderer textRenderer;

	public DistanceRenderer(Tuple6b pStartPoint, Tuple6b pEndPoint, Vector3d pNormal , int verticalAlignement) throws GkException {
		super();
		this.startPoint = new Tuple6b(pStartPoint);
		this.endPoint = new Tuple6b(pEndPoint);
		this.length = startPoint.distance(endPoint);
		
		this.direction = new Vector3d();
		this.direction.sub(endPoint.toPoint3d(JoglUtils.JOGL_UNIT), startPoint.toPoint3d(JoglUtils.JOGL_UNIT));		
		this.direction.normalize();
		this.normal = new Vector3d(pNormal);
		normal.normalize();
		Vector3d baseDirection = new Vector3d();
		baseDirection.cross(direction, normal);

		lineRenderer = new PolylineRenderer(false, new Color4f(1,1,1,1), startPoint.toPoint3d(JoglUtils.JOGL_UNIT), endPoint.toPoint3d(JoglUtils.JOGL_UNIT));
		endArrowRenderer = new ArrowRenderer(endPoint, new Vector3d(direction.x,direction.y,direction.z), baseDirection, new Color4f(1,1,1,1));
		startArrowRenderer = new ArrowRenderer(startPoint, new Vector3d(-direction.x,-direction.y,-direction.z), baseDirection, new Color4f(1,1,1,1));

		String sLength = GokoPreference.getInstance().format(length);
		double dLength = length.doubleValue(JoglUtils.JOGL_UNIT);
		Point3d sPoint = startPoint.toPoint3d(JoglUtils.JOGL_UNIT);
				
		textRenderer = new TextRenderer(sLength, 3, new Point3d(sPoint.x + direction.x * dLength/2, sPoint.y + direction.y * dLength/2, sPoint.z + direction.z * dLength/2), this.direction, new Vector3d(-baseDirection.x,-baseDirection.y,-baseDirection.z), TextRenderer.CENTER | verticalAlignement);
		textRenderer.setPadding(Length.valueOf("0.2", JoglUtils.JOGL_UNIT));
		addRenderer(lineRenderer);
		addRenderer(endArrowRenderer);
		addRenderer(startArrowRenderer);
		addRenderer(textRenderer);
	}

	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.service.AbstractCoreJoglRenderer#performInitialize(javax.media.opengl.GL3)
	 */
	@Override
	protected void performInitialize(GL3 gl) throws GkException {

	}


}

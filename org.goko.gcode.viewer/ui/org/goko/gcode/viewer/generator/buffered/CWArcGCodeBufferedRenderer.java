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
package org.goko.gcode.viewer.generator.buffered;

import java.util.Map;

/**
 * Clockwise arc rendering
 *
 * @author PsyKo
 *
 */
public class CWArcGCodeBufferedRenderer extends ArcGCodeBufferedRenderer {

	public CWArcGCodeBufferedRenderer(Map<Integer, BufferedRenderingData> buffer) {
		super(buffer, true);
	}


	/** (inheritDoc)
	 * @see org.goko.gcode.viewer.generator.AbstractGCodeGlRenderer#getSupportedMotionType()
	 */
	@Override
	public String getSupportedMotionType() {
		return "g2";
	}
//	/** (inheritDoc)
//	 * @see org.goko.gcode.viewer.generator.AbstractGCodeGlRenderer#render(org.goko.core.gcode.bean.GCodeCommand, javax.media.opengl.GL2)
//	 */
//	@Override
//	public void render(GCodeContext context, GCodeArcCommand command, GL2 gl) {
//		List<Point3d> lstPoint = null;
//		if(!buffer.containsKey(command.getId())){
//			lstPoint = addGeometryToBuffer(context, command);
//			buffer.put(command.getId(), lstPoint);
//		}else{
//			lstPoint = buffer.get(command.getId());
//		}
//		if(CollectionUtils.isNotEmpty(lstPoint)){
//			gl.glBegin(GL2.GL_LINE_STRIP);
//			for (Point3d p : lstPoint) {
//				gl.glColor3d(G03_COLOR.x, G03_COLOR.y, G03_COLOR.z);
//				gl.glVertex3d(p.x, p.y, p.z);
//			}
//
//			gl.glEnd();
//		}
//	}
//
//	protected List<Point3d> addGeometryToBuffer(GCodeContext context, GCodeArcCommand command){
//		List<Point3d> lst = new ArrayList<Point3d>();
//
//		lst.add(new Point3d(context.getPosition().getX().doubleValue(), context.getPosition().getY().doubleValue(), context.getPosition().getZ().doubleValue()));
//
//		Double x = context.getPosition().getX().doubleValue();
//		Double y = context.getPosition().getY().doubleValue();
//		Double z = context.getPosition().getZ().doubleValue();
//
//		Point3d center = new Point3d(x + command.getOffsetIDouble(), y + command.getOffsetJDouble(), z + command.getOffsetKDouble());
//
//		Vector3d v1 = new Vector3d(x - center.x, y - center.y, z - center.z);
//
//		GCodeContext endContext = new GCodeContext(context);
//		command.updateContext(endContext);
//		Tuple6b tuple = endContext.getPosition();
//
//		x = tuple.getX().doubleValue();
//		y = tuple.getY().doubleValue();
//		z = tuple.getZ().doubleValue();
//		Vector3d v2 = new Vector3d(x - center.x, y - center.y, z - center.z);
//
//		double smallestAngle = StrictMath.atan2(v1.y,v1.x) - StrictMath.atan2(v2.y,v2.x);
//		double angle = smallestAngle ;
//		// If smallestAngle < 0 then it is a counterclockwise angle.
//		if(smallestAngle < 0){
//			if(command.isClockwise()){ // The angle is CCW but the command is CCW
//				angle = - ( 2*Math.PI - Math.abs(smallestAngle) );  // In OpenGl when rotating, CW rotation = negative angle
//			}else{
//				angle = Math.abs(smallestAngle); // In OpenGl when rotating, CCW rotation = positive angle
//			}
//		}else{
//			if(command.isClockwise()){ // The angle is CW and we have a CW command
//				angle = - Math.abs(smallestAngle); // In OpenGl when rotating, CW rotation = negative angle
//			}else{ // The angle is CW but we want the CCW command
//				angle =  2*Math.PI - smallestAngle;
//			}
//		}
//
//		int nbPoints = 8;
//		// Adaptive points count
//		double arcLength = Math.abs(angle * v1.length());
//		nbPoints = (int) (arcLength * 8 );
//		Matrix3d rot = new Matrix3d();
//		rot.rotZ(angle / (nbPoints + 1));
//		for(int i = 0; i <= nbPoints; i++){
//			rot.transform(v1);
//			lst.add(new Point3d(center.x + v1.x, center.y + v1.y, z));
//		}
//
//		return lst;
//	}



}

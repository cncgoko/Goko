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
package org.goko.gcode.viewer.generator.renderer;

import java.math.BigDecimal;

import javax.media.opengl.GL2;
import javax.vecmath.Matrix3d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import org.goko.core.gcode.bean.GCodeCommandState;
import org.goko.core.gcode.bean.GCodeContext;
import org.goko.core.gcode.bean.Tuple6b;
import org.goko.gcode.rs274ngcv3.command.GCodeArcCommand;
import org.goko.gcode.viewer.generator.AbstractGCodeGlRenderer;

public class CWArcGCodeRenderer extends AbstractGCodeGlRenderer<GCodeArcCommand> {

	private static Point3d G03_COLOR = new Point3d(0,0.86,0);

	/** (inheritDoc)
	 * @see org.goko.gcode.viewer.generator.AbstractGCodeGlRenderer#getRenderedCommandClass()
	 */
	@Override
	public Class<GCodeArcCommand> getRenderedCommandClass() {
		return GCodeArcCommand.class;
	}

	/** (inheritDoc)
	 * @see org.goko.gcode.viewer.generator.AbstractGCodeGlRenderer#render(org.goko.core.gcode.bean.GCodeCommand, javax.media.opengl.GL2)
	 */
	@Override
	public void render(GCodeContext context, GCodeArcCommand command, GL2 gl) {
		gl.glBegin(GL2.GL_LINE_STRIP);
		// Let's redraw the current position with the accurate color
		if(command.getState().isState(GCodeCommandState.EXECUTED) || command.getState().isState(GCodeCommandState.SENT)){
			gl.glColor3d(0.4,0.4,0.4);
		}else{
			gl.glColor3d(G03_COLOR.x, G03_COLOR.y, G03_COLOR.z);
		}

		gl.glVertex3d(context.getPosition().getX().doubleValue(), context.getPosition().getY().doubleValue(), context.getPosition().getZ().doubleValue());

		Tuple6b tuple = new Tuple6b(context.getPosition());
		Double x = context.getPosition().getX().doubleValue();
		Double y = context.getPosition().getY().doubleValue();
		Double z = context.getPosition().getZ().doubleValue();

		Point3d center = new Point3d(x + command.getOffsetIDouble(), y + command.getOffsetJDouble(), z + command.getOffsetKDouble());

		Vector3d v1 = new Vector3d(x - center.x, y - center.y, z - center.z);

		/* ** */
		if(context.isAbsolute()){
			tuple.updateAbsolute(command.getEndpoint());
		}else{
			tuple.updateRelative(command.getEndpoint());
		}
		x = tuple.getX().doubleValue();
		y = tuple.getY().doubleValue();
		z = tuple.getZ().doubleValue();
		Vector3d v2 = new Vector3d(x - center.x, y - center.y, z - center.z);

		double smallestAngle = StrictMath.atan2(v1.y,v1.x) - StrictMath.atan2(v2.y,v2.x);
		double angle = smallestAngle ;
		// If smallestAngle < 0 then it is a counterclockwise angle.
		if(smallestAngle < 0){
			if(command.isClockwise()){ // The angle is CCW but the command is CCW
				angle = - ( 2*Math.PI - Math.abs(smallestAngle) );  // In OpenGl when rotating, CW rotation = negative angle
			}else{
				angle = Math.abs(smallestAngle); // In OpenGl when rotating, CCW rotation = positive angle
			}
		}else{
			if(command.isClockwise()){ // The angle is CW and we have a CW command
				angle = - Math.abs(smallestAngle); // In OpenGl when rotating, CW rotation = negative angle
			}else{ // The angle is CW but we want the CCW command
				angle =  2*Math.PI - smallestAngle;
			}
		}

		int nbPoints = 8;
		// Adaptive points count
		double arcLength = Math.abs(angle * v1.length());
		nbPoints = (int) (arcLength * 8 );
		Matrix3d rot = new Matrix3d();
		rot.rotZ(angle / (nbPoints + 1));
		for(int i = 0; i < nbPoints; i++){

			rot.transform(v1);
			gl.glVertex3d(center.x + v1.x, center.y + v1.y, z);
		}


		if(command.getState().isState(GCodeCommandState.EXECUTED) || command.getState().isState(GCodeCommandState.SENT)){
			gl.glColor3d(0.4,0.4,0.4);
		}else{
			gl.glColor3d(G03_COLOR.x, G03_COLOR.y, G03_COLOR.z);
		}
		gl.glVertex3d(x, y, z);
		gl.glEnd();
	}

	protected double add(double position, BigDecimal delta){
		if( delta == null){
			return position;
		}
		return position + delta.doubleValue();
	}

	public static void main(String[] args) {
		Vector3d v1 = new Vector3d(1,0,0);
		Vector3d v2 = new Vector3d(0,1,0);
		Vector3d v3 = new Vector3d(0,-1,0);
		System.out.println(StrictMath.atan2(v1.y,v1.x) - StrictMath.atan2(v2.y,v2.x));
		System.out.println(StrictMath.atan2(v1.y,v1.x) - StrictMath.atan2(v3.y,v3.x));

		/*
		G0 X-0.8028 Y0.7736
		G1 F300.0 Z0.0
		G3 X0.5031 Y-0.1044 I27.3961 J-9.2013
		*/

		v1 = new Vector3d(-0.8028 + 27.3961, 0.7736 - 9.2013, 0);
		System.err.println(v1.length());
		v1 = new Vector3d(0.5031 - (-0.8028 + 27.3961), -0.1044 - (0.7736 - 9.2013), 0);
		System.err.println(v1.length());
	}
}

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

import javax.media.opengl.GL2;
import javax.vecmath.Matrix3d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import org.goko.core.gcode.bean.GCodeCommand;
import org.goko.core.gcode.bean.GCodeCommandState;
import org.goko.core.gcode.bean.GCodeContext;
import org.goko.core.gcode.bean.Tuple6b;
import org.goko.gcode.viewer.generator.AbstractGCodeGlRendererOld;

public abstract class ArcGCodeRenderer extends AbstractGCodeGlRendererOld {
	private static Point3d G03_COLOR = new Point3d(0,0.86,0);
	private boolean clockwise;

	public ArcGCodeRenderer(boolean clockwise) {
		this.clockwise = clockwise;
	}
	/** (inheritDoc)
	 * @see org.goko.gcode.viewer.generator.AbstractGCodeGlRendererOld#render(org.goko.core.gcode.bean.GCodeCommand, javax.media.opengl.GL2)
	 */
	@Override
	public void render(GCodeContext preContext,GCodeContext postContext, GCodeCommand command, GL2 gl) {
		gl.glBegin(GL2.GL_LINE_STRIP);
		// Let's redraw the current position with the accurate color
		if(command.getState().isState(GCodeCommandState.EXECUTED) || command.getState().isState(GCodeCommandState.SENT)){
			gl.glColor3d(0.4,0.4,0.4);
		}else{
			gl.glColor3d(G03_COLOR.x, G03_COLOR.y, G03_COLOR.z);
		}

		gl.glVertex3d(preContext.getPosition().getX().doubleValue(), preContext.getPosition().getY().doubleValue(), preContext.getPosition().getZ().doubleValue());

		Tuple6b tuple = new Tuple6b(preContext.getPosition());
		Double x = preContext.getPosition().getX().doubleValue();
		Double y = preContext.getPosition().getY().doubleValue();
		Double z = preContext.getPosition().getZ().doubleValue();

		Point3d center = new Point3d(x + postContext.getOffset().getX().doubleValue(), y + postContext.getOffset().getY().doubleValue(), z + postContext.getOffset().getZ().doubleValue());

		Vector3d v1 = new Vector3d(x - center.x, y - center.y, z - center.z);

		/* ** */
		tuple = new Tuple6b(postContext.getPosition());

		x = tuple.getX().doubleValue();
		y = tuple.getY().doubleValue();
		z = tuple.getZ().doubleValue();
		Vector3d v2 = new Vector3d(x - center.x, y - center.y, z - center.z);

		double smallestAngle = StrictMath.atan2(v1.y,v1.x) - StrictMath.atan2(v2.y,v2.x);
		double angle = smallestAngle ;
		// If smallestAngle < 0 then it is a counterclockwise angle.
		if(smallestAngle < 0){
			if(isClockwise()){ // The angle is CCW but the command is CCW
				angle = - ( 2*Math.PI - Math.abs(smallestAngle) );  // In OpenGl when rotating, CW rotation = negative angle
			}else{
				angle = Math.abs(smallestAngle); // In OpenGl when rotating, CCW rotation = positive angle
			}
		}else{
			if(isClockwise()){ // The angle is CW and we have a CW command
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

	/**
	 * @return the clockwise
	 */
	public boolean isClockwise() {
		return clockwise;
	}


	/**
	 * @param clockwise the clockwise to set
	 */
	public void setClockwise(boolean clockwise) {
		this.clockwise = clockwise;
	}
}

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

package org.goko.viewer.jogl.utils.render.gcode.geometry;

import java.util.List;

import javax.vecmath.Matrix4d;
import javax.vecmath.Point3d;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.bean.GCodeCommand;
import org.goko.core.gcode.bean.IGCodeCommandVisitor;
import org.goko.core.gcode.bean.commands.ArcMotionCommand;
import org.goko.core.gcode.bean.commands.CommentCommand;
import org.goko.core.gcode.bean.commands.FunctionCommand;
import org.goko.core.gcode.bean.commands.LinearMotionCommand;
import org.goko.core.gcode.bean.commands.MotionCommand;
import org.goko.core.gcode.bean.commands.RawCommand;
import org.goko.core.gcode.bean.commands.SettingCommand;
import org.goko.viewer.jogl.preferences.JoglViewerPreference;

public class GCodeGeometryGenerator implements IGCodeCommandVisitor{
	private List<Point3d> vertices;

	public List<Point3d> generateVertices(GCodeCommand command) throws GkException{
		command.accept(this);
		return vertices;
	}
	@Override
	public void visit(RawCommand command) throws GkException {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(CommentCommand command) throws GkException {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(SettingCommand command) throws GkException {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(MotionCommand command) throws GkException {
		// TODO Auto-generated method stub

	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.bean.IGCodeCommandVisitor#visit(org.goko.core.gcode.bean.commands.LinearMotionCommand)
	 */
	@Override
	public void visit(LinearMotionCommand command) throws GkException {
		vertices = new LinearMotionRenderer().render(command);
	}

	@Override
	public void visit(ArcMotionCommand command) throws GkException {
		vertices = new ArcMotionRenderer().render(command);

	}

	@Override
	public void visit(FunctionCommand command) throws GkException {
		// TODO Auto-generated method stub

	}

	private void rotateMatrix(Matrix4d matrix, double angleRadians){
		JoglViewerPreference settings = JoglViewerPreference.getInstance();
		switch(settings.getRotaryAxisDirection()){
			case X:matrix.rotX( angleRadians );
			break;
			case Y:matrix.rotY( angleRadians );
			break;
			case Z:matrix.rotZ( angleRadians );
			break;
			default:matrix.rotY( angleRadians );
		}
	}
}

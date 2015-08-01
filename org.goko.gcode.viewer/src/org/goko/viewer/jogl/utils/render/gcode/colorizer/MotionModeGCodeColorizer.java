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

package org.goko.viewer.jogl.utils.render.gcode.colorizer;

import javax.vecmath.Color4f;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.bean.GCodeCommand;
import org.goko.core.gcode.bean.GCodeCommandVisitorAdapter;
import org.goko.core.gcode.bean.commands.ArcMotionCommand;
import org.goko.core.gcode.bean.commands.EnumGCodeCommandMotionMode;
import org.goko.core.gcode.bean.commands.LinearMotionCommand;
import org.goko.core.gcode.bean.commands.MotionCommand;
import org.goko.core.gcode.bean.commands.SettingCommand;
import org.goko.viewer.jogl.utils.render.gcode.IGCodeColorizer;

public class MotionModeGCodeColorizer extends GCodeCommandVisitorAdapter implements IGCodeColorizer {
	private static final Color4f DEFAULT_COLOR = new Color4f(0.58f,0.58f,0.58f,0.9f);
	private static final Color4f RAPID_COLOR = new Color4f(0.854f,0.0f,0.0f,0.9f);
	private static final Color4f FEEDRATE_COLOR = new Color4f(0.14f,0.33f,0.80f,0.9f);	
	private static final Color4f ARC_COLOR = new Color4f(0,0.86f,0,0.9f);
	private Color4f color;

	/** (inheritDoc)
	 * @see org.goko.viewer.jogl.utils.render.gcode.IGCodeColorizer#getColor(org.goko.core.gcode.bean.GCodeCommand)
	 */
	@Override
	public Color4f getColor(GCodeCommand command) throws GkException {
		command.accept(this);
		return color;
	}

	private Color4f getColor(EnumGCodeCommandMotionMode motionMode){
		Color4f color = DEFAULT_COLOR;
		switch (motionMode) {
		case ARC_CLOCKWISE:
		case ARC_COUNTERCLOCKWISE:	color = ARC_COLOR;
			break;
		case FEEDRATE: color = FEEDRATE_COLOR;
			break;
		case RAPID: color = RAPID_COLOR;
			break;
		default: color = DEFAULT_COLOR;
			break;
		}
		return color;
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.bean.GCodeCommandVisitorAdapter#visit(org.goko.core.gcode.bean.commands.SettingCommand)
	 */
	@Override
	public void visit(SettingCommand command) throws GkException {
		this.color = getColor(command.getMotionMode());
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.bean.GCodeCommandVisitorAdapter#visit(org.goko.core.gcode.bean.commands.MotionCommand)
	 */
	@Override
	public void visit(MotionCommand command) throws GkException {
		visit((SettingCommand)command);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.bean.GCodeCommandVisitorAdapter#visit(org.goko.core.gcode.bean.commands.LinearMotionCommand)
	 */
	@Override
	public void visit(LinearMotionCommand command) throws GkException {
		visit((MotionCommand)command);

	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.bean.GCodeCommandVisitorAdapter#visit(org.goko.core.gcode.bean.commands.ArcMotionCommand)
	 */
	@Override
	public void visit(ArcMotionCommand command) throws GkException {
		visit((MotionCommand)command);
	}


}

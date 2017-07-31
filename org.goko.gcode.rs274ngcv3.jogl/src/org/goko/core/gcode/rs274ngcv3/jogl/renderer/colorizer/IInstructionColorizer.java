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

package org.goko.core.gcode.rs274ngcv3.jogl.renderer.colorizer;

import javax.vecmath.Color4f;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.element.IGCodeContext;
import org.goko.core.gcode.element.IInstruction;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.InstructionProvider;
import org.goko.tools.viewer.jogl.utils.overlay.IOverlayRenderer;

public interface IInstructionColorizer<G extends IGCodeContext, T extends IInstruction> {

	public void initialize(GCodeContext context, InstructionProvider instructionSet) throws GkException;
	
	public Color4f getColor(G context, T instruction) throws GkException;
	
	public void conclude() throws GkException;
	
	public IOverlayRenderer getOverlay();
}

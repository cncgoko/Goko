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

package org.goko.tools.viewer.jogl.utils.render.gcode;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.element.IGCodeProvider;
import org.goko.core.gcode.execution.IExecutionState;
import org.goko.core.gcode.execution.IExecutionToken;
import org.goko.core.gcode.service.IGCodeExecutionListener;
import org.goko.tools.viewer.jogl.service.ICoreJoglRenderer;

public interface IGCodeProviderRenderer<S extends IExecutionState, T extends IExecutionToken<S>> extends ICoreJoglRenderer, IGCodeExecutionListener<S, T>{

	void setGCodeProvider(IGCodeProvider provider) throws GkException;

	IGCodeProvider getGCodeProvider() throws GkException;

	void setColorizer(IGCodeColorizer colorizer) throws GkException;

	IGCodeColorizer getColorizer() throws GkException;
	
	void update();
 }

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

package org.goko.viewer.jogl.service;

import javax.media.opengl.GL3;

import org.goko.core.common.exception.GkException;

import com.jogamp.opengl.util.PMVMatrix;

public interface ICoreJoglRenderer {

	public String getId();

	public int getLayerId();
	
	public void render(GL3 gl, PMVMatrix modelViewMatrix) throws GkException;

	public void setEnabled(boolean enabled);

	public void destroy() throws GkException;

	public boolean shouldDestroy() throws GkException;

	public void performDestroy(GL3 gl) throws GkException;
}

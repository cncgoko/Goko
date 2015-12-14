/*******************************************************************************
 * 	This file is part of Goko.
 *
 *   Goko is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   Goko is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with Goko.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package org.goko.tools.viewer.jogl;

import javax.media.opengl.GLCapabilities;

import org.eclipse.swt.widgets.Composite;

import com.jogamp.opengl.swt.GLCanvas;

/**
 * Goko implementation of the GLCanvas
 *
 * @author PsyKo
 *
 */
public class GokoJoglCanvas extends GLCanvas{
	protected boolean keyboardJogEnabled;

	/**
	 * Constructor
	 * @param parent
	 * @param style
	 * @param data
	 */
	public GokoJoglCanvas(Composite parent, int style,GLCapabilities caps) {
		super(parent, style, caps, null, null);
	}
	/**
	 * @return the keyboardJogEnabled
	 */
	public boolean isKeyboardJogEnabled() {
		return keyboardJogEnabled;
	}
	/**
	 * @param keyboardJogEnabled the keyboardJogEnabled to set
	 */
	public void setKeyboardJogEnabled(boolean keyboardJogEnabled) {
		this.keyboardJogEnabled = keyboardJogEnabled;
	}
}

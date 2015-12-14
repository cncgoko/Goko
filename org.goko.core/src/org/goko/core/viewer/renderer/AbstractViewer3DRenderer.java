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
package org.goko.core.viewer.renderer;


public abstract class AbstractViewer3DRenderer implements IViewer3DRenderer {
	/** Enabled state of the renderer */
	private boolean disabled;

	public AbstractViewer3DRenderer() {
		disabled = false;
	}

	/** (inheritDoc)
	 * @see org.goko.core.viewer.renderer.IViewer3DRenderer#isEnabled()
	 */
	@Override
	public boolean isEnabled() {
		return !disabled;
	}

	/** (inheritDoc)
	 * @see org.goko.core.viewer.renderer.IViewer3DRenderer#setEnabled(boolean)
	 */
	@Override
	public void setEnabled(boolean enabled) {
		this.disabled = !enabled;
	}
}

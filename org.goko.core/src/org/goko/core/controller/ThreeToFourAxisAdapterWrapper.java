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
package org.goko.core.controller;

import org.goko.core.common.exception.GkException;

public class ThreeToFourAxisAdapterWrapper implements IFourAxisControllerAdapter {
	/** Nase 3 axis adapter */
	private IThreeAxisControllerAdapter baseAdapter;

	public ThreeToFourAxisAdapterWrapper(IThreeAxisControllerAdapter threeAxisControllerAdapter) {
		this.baseAdapter = threeAxisControllerAdapter;
	}
	/** (inheritDoc)
	 * @see org.goko.core.controller.IThreeAxisControllerAdapter#getX()
	 */
	@Override
	public Double getX() throws GkException {
		return baseAdapter.getX();
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IThreeAxisControllerAdapter#getY()
	 */
	@Override
	public Double getY() throws GkException {
		return baseAdapter.getY();
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IThreeAxisControllerAdapter#getZ()
	 */
	@Override
	public Double getZ() throws GkException {
		return baseAdapter.getZ();
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.IFourAxisControllerAdapter#getA()
	 */
	@Override
	public Double getA() {
		return 0d;
	}

}

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
package org.goko.common;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.goko.common.bindings.AbstractController;
import org.goko.common.bindings.AbstractModelObject;

/**
 * A package proxy for GkUiComponent
 * @author PsyKo
 *
 * @param <C>
 * @param <D>
 */
public class GkUiComponentProxy<C extends AbstractController<D>, D extends AbstractModelObject> extends GkUiComponent<C , D> {

	protected GkUiComponentProxy(IEclipseContext context, C abstractController, D abstractModelObject) {
		super(context, abstractController, abstractModelObject);
	}
	protected GkUiComponentProxy(IEclipseContext context, C abstractController) {
		super(context, abstractController);
	}

}

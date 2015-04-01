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

package org.goko.common.bindings.toolbar;

import org.eclipse.core.databinding.property.value.DelegatingValueProperty;
import org.eclipse.jface.databinding.swt.ISWTObservableValue;
import org.eclipse.jface.databinding.swt.IWidgetValueProperty;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.widgets.Widget;


public abstract class ToolItemDelegatingProperty extends DelegatingValueProperty implements IWidgetValueProperty{


	/** (inheritDoc)
	 * @see org.eclipse.jface.databinding.swt.IWidgetValueProperty#observe(org.eclipse.swt.widgets.Widget)
	 */
	@Override
	public ISWTObservableValue observe(Widget widget) {
		return (ISWTObservableValue) observe(SWTObservables.getRealm(widget.getDisplay()), widget);
	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.databinding.swt.IWidgetValueProperty#observeDelayed(int, org.eclipse.swt.widgets.Widget)
	 */
	@Override
	public ISWTObservableValue observeDelayed(int delay, Widget widget) {
		return SWTObservables.observeDelayedValue(delay, observe(widget));
	}

}

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

import org.eclipse.core.databinding.property.INativePropertyListener;
import org.eclipse.core.databinding.property.ISimplePropertyListener;
import org.eclipse.core.databinding.property.value.SimpleValueProperty;
import org.eclipse.swt.widgets.ToolItem;

public class ToolItemSelectionProperty extends SimpleValueProperty{

	/**
	 * @return
	 */
	@Override
	public Object getValueType() {
		return Boolean.TYPE;
	}

	/** (inheritDoc)
	 * @see org.eclipse.core.databinding.property.value.SimpleValueProperty#doGetValue(java.lang.Object)
	 */
	@Override
	protected Object doGetValue(Object source) {
		return ((ToolItem)source).getSelection();
	}

	/** (inheritDoc)
	 * @see org.eclipse.core.databinding.property.value.SimpleValueProperty#doSetValue(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected void doSetValue(Object source, Object value) {
		((ToolItem)source).setSelection((boolean) value);
	}

	/** (inheritDoc)
	 * @see org.eclipse.core.databinding.property.value.SimpleValueProperty#adaptListener(org.eclipse.core.databinding.property.ISimplePropertyListener)
	 */
	@Override
	public INativePropertyListener adaptListener(ISimplePropertyListener listener) {
		return null;
	}


}

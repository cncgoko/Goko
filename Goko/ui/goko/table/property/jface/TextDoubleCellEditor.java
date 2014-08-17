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
package goko.table.property.jface;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.widgets.Composite;

public class TextDoubleCellEditor extends TextCellEditor {

	public TextDoubleCellEditor(Composite parent) {
		super(parent);
	}

	@Override
	protected Object doGetValue() {
		String text = (String) super.doGetValue();
		if(NumberUtils.isNumber(text)){
			return Double.valueOf(text);
		}
		return null;
	}

	@Override
	protected void doSetValue(Object value) {
		String val = StringUtils.EMPTY;
		if(value != null){
			val = String.valueOf(value);
		}
		super.doSetValue(val);//StringUtils.defaultIfBlank(String.valueOf(value), StringUtils.EMPTY));
	}
}

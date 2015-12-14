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
package org.goko.test.recorder.ui.test;

import org.eclipse.jface.viewers.TextCellEditor;

public class DoubleCellEditor extends TextCellEditor{

	public void setDoubleValue(Double d){
		String str = String.valueOf(d);
		setValue(str);
	}

	public Double getDoubleValue(){
		String str = (String) getValue();
		return Double.valueOf(str);
	}
}

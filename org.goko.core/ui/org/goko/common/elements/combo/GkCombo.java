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
package org.goko.common.elements.combo;

import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;

public class GkCombo< T extends LabeledValue> extends ComboViewer {

	public GkCombo(Combo list) {
		super(list);		
		setContentProvider(new ArrayContentProvider());		
		setLabelProvider(new LabelledValueContentProvider());
		
	}
		
	public GkCombo(Composite composite, int style) {
		super(composite, style);
		setContentProvider(new ArrayContentProvider());		
		setLabelProvider(new LabelledValueContentProvider());
	}

	public void setInput(List<T> input){		
		setInput((Object) input);
	}
		
}

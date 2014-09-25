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

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboBoxViewerCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public class ComboCellEditor<T> extends ComboBoxViewerCellEditor{

	public ComboCellEditor(Composite parent, TupleLabeledValue<T> tuple) {
		super(parent, SWT.READ_ONLY);
		setContentProvider(new ArrayContentProvider());
		setInput(tuple.getAvailableValues());
		setActivationStyle(DROP_DOWN_ON_KEY_ACTIVATION | DROP_DOWN_ON_MOUSE_ACTIVATION);
		setStyle(SWT.READ_ONLY);
	}

}

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
package org.goko.common.elements.combo.v2;

import java.util.List;

import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.goko.common.GkUiUtils;
import org.goko.common.elements.combo.LabeledValue;
import org.goko.core.common.exception.GkException;
import org.goko.core.log.GkLog;

public class GkCombo2<T>  extends ComboViewer {
	private static final GkLog LOG = GkLog.getLogger(GkCombo2.class);

	public GkCombo2(Composite parent, int style) {
		super(parent, style);

		setContentProvider(new GkComboContentProvider());
		setLabelProvider(new GkComboLabelProvider<T>(this));
	}

	/**
	 * @return the choices
	 */
	public List<LabeledValue<T>> getChoices() {
		return (List<LabeledValue<T>>) getInput();
	}

	@Override
	public void setSelection(ISelection selection) {
		if(!selection.isEmpty()){
			T val = (T) ((IStructuredSelection)selection).getFirstElement();
			try {
				LabeledValue<T> lVal = GkUiUtils.getLabelledValueByKey(val, getChoices());
				super.setSelection(new StructuredSelection(lVal));
			} catch (GkException e) {
				LOG.error(e);
			}

		}
	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.viewers.StructuredViewer#setSelection(org.eclipse.jface.viewers.ISelection, boolean)
	 */
	@Override
	public void setSelection(ISelection selection, boolean reveal) {
		if(!selection.isEmpty()){
			T val = (T) ((IStructuredSelection)selection).getFirstElement();
			try {
				LabeledValue<T> lVal = GkUiUtils.getLabelledValueByKey(val, getChoices());
				super.setSelection(new StructuredSelection(lVal), reveal);
			} catch (GkException e) {
				LOG.error(e);
			}
		}
		super.setSelection(selection, reveal);
	}

	@Override
	public ISelection getSelection() {
		IStructuredSelection selection = (IStructuredSelection) super.getSelection();
		LabeledValue<?> firstElement = (LabeledValue<?>) selection.getFirstElement();
		if(firstElement != null){
			return new StructuredSelection(firstElement.getValue());
		}else{
			return new StructuredSelection();
		}
	}


}


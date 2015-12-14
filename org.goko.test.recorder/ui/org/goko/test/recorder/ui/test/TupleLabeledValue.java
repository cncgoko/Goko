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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Composite;
import org.goko.common.GkUiUtils;
import org.goko.common.elements.combo.LabeledValue;
import org.goko.core.common.exception.GkException;

public class TupleLabeledValue<T> extends Tuple<LabeledValue<T>>{

	private List<LabeledValue<T>> lstAvailableValues;

	public TupleLabeledValue(TableViewer viewer, String name, LabeledValue<T>... lstAvailableValue) {
		super(name, null,null,null,null);
		this.lstAvailableValues = new ArrayList<LabeledValue<T>>(Arrays.asList(lstAvailableValue));
		this.editor = new ComboCellEditor<T>((Composite) viewer.getControl(), this);
	}

	public String getLabel(int index){
		return get(index).getLabel();
	}

	public LabeledValue getValue(int index){
		if(get(index) != null){
			return get(index);
		}
		return null;
	}

	public void setValue(int index, T value) throws GkException{
		put(index, GkUiUtils.getLabelledValueByKey(value, lstAvailableValues));
	}

	@Override
	public void putObject(int columnIndex, Object userInputValue) {
		try {
			LabeledValue<T> labelledValueByKey = GkUiUtils.getLabelledValueByKey((T)userInputValue, lstAvailableValues);
			put(columnIndex, labelledValueByKey);
		} catch (GkException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return the lstAvailableValue
	 */
	public List<LabeledValue<T>> getAvailableValues() {
		return lstAvailableValues;
	}
}

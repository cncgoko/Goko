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
package goko.table.property.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ComboBoxViewerCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.goko.common.GkUiUtils;
import org.goko.common.elements.combo.LabeledValue;
import org.goko.core.common.exception.GkException;

/**
 *
 * @author PsyKo
 *
 * @param <T>
 */
public class ComboboxPropertyDescriptor<T> extends AbstractPropertyDescriptor<T>{
	private List<LabeledValue<T>> choices;

	public ComboboxPropertyDescriptor(String id, String label) {
		super(id, label);
		choices = new ArrayList<LabeledValue<T>>();
	}

	public void addChoice(String label, T value){
		choices.add( new LabeledValue<T>(value, label));
	}

	@Override
	public CellEditor createPropertyEditor(Composite parent) {
		ComboBoxViewerCellEditor combo = null;
		combo = new ComboBoxViewerCellEditor(parent, SWT.READ_ONLY){
			@Override
			protected Object doGetValue() {
				Object value = super.doGetValue();
				if(value != null){
					if(value instanceof LabeledValue){
						return ((LabeledValue) value).getValue();
					}else{
						return value;
					}
				}
				return super.doGetValue();
			}

			/** (inheritDoc)
			 * @see org.eclipse.jface.viewers.ComboBoxViewerCellEditor#doSetValue(java.lang.Object)
			 */
			@Override
			protected void doSetValue(Object value) {
				// TODO Auto-generated method stub
				try {
					super.doSetValue(GkUiUtils.getLabelledValueByKey((T)value, choices));
				} catch (GkException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}


		};
		combo.setContentProvider(new ArrayContentProvider());

		combo.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				if(element instanceof LabeledValue){
					LabeledValue<T> value;
					try {
						value = GkUiUtils.getLabelledValueByKey((T)element, choices);
						if(value != null){
							return value.getLabel();
						}
					} catch (GkException e) {
						e.printStackTrace();
					}
				}
				return super.getText(element);
			}
		});
		combo.setInput(choices);
		combo.setActivationStyle(ComboBoxViewerCellEditor.DROP_DOWN_ON_KEY_ACTIVATION | ComboBoxViewerCellEditor.DROP_DOWN_ON_MOUSE_ACTIVATION);
		return combo;
	}

	@Override
	public String getPropertyValueLabel(T value) {
		if(value != null){
			LabeledValue<T> LabeledValue;
			try {
				LabeledValue = GkUiUtils.getLabelledValueByKey(value, choices);
				if(LabeledValue != null){
					return LabeledValue.getLabel();
				}
			} catch (GkException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public void setPropertyValue(PropertyArray<T> pArray, int index, Object value) {
		if(value != null){
			if(value instanceof LabeledValue){
				pArray.addValue(index,((LabeledValue<T>) value).getValue());
			}else{
				pArray.addValue(index,(T) value);
			}
		}
	}

	/** (inheritDoc)
	 * @see goko.table.property.model.AbstractPropertyDescriptor#getPropertyValue(goko.table.property.model.PropertyArray, int)
	 */
	@Override
	public T getPropertyValue(PropertyArray<T> pArray, int index) {
		return pArray.getValue(index);
	}

}

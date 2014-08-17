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

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.wb.swt.SWTResourceManager;
import org.goko.common.bindings.validator.StringBigDecimalValidator;

public class TextBigDecimalCellEditor extends TextCellEditor {

	private IValidator validator;

	public TextBigDecimalCellEditor(Composite parent) {
		super(parent);
		this.validator = new StringBigDecimalValidator();
		setValidator(new ICellEditorValidator() {

			@Override
			public String isValid(Object value) {
				IStatus status = validator.validate(StringUtils.defaultIfBlank(text.getText(), StringUtils.EMPTY));
				boolean valid = status.getSeverity() == IStatus.OK;
				if(valid){
					return null;
				}
				return status.getMessage();
			}
		});
	}

	@Override
	protected Object doGetValue() {
		String text = (String) super.doGetValue();
		if(NumberUtils.isNumber(text)){
			return new BigDecimal(text);
		}
		return StringUtils.EMPTY;
	}

	@Override
	protected void doSetValue(Object value) {
		if(value != null){
			super.doSetValue( String.valueOf(value));
		}
	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.viewers.CellEditor#getValidator()
	 */
	@Override
	public ICellEditorValidator getValidator() {
		return super.getValidator();
	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.viewers.CellEditor#isValueValid()
	 */
	@Override
	public boolean isValueValid() {
		IStatus status = validator.validate(StringUtils.defaultIfBlank(text.getText(), StringUtils.EMPTY));
		boolean valid = status.getSeverity() == IStatus.OK;
		if(valid){
			getControl().setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		}else{
			getControl().setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		}
		return valid;
	}

}

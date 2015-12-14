/*******************************************************************************
 * 	This file is part of Goko.
 *
 *   Goko is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   Goko is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with Goko.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package org.goko.common.elements.combo.v2;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jface.viewers.LabelProvider;
import org.goko.common.GkUiUtils;
import org.goko.common.elements.combo.LabeledValue;
import org.goko.core.common.exception.GkException;
import org.goko.core.log.GkLog;


public class GkComboLabelProvider<T> extends LabelProvider {
	private static final GkLog LOG = GkLog.getLogger(GkComboLabelProvider.class);
	private GkCombo2<T> combo;


	/**
	 * @param lstValues
	 */
	GkComboLabelProvider(GkCombo2<T> combo) {
		super();
		this.combo = combo;
	}


	/** (inheritDoc)
	 * @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
	 */
	@Override
	public String getText(Object element) {
		String text = StringUtils.EMPTY;
		LabeledValue<T> value;
		try {
			value = GkUiUtils.getLabelledValueByKey((T)element, combo.getChoices());
			if(value != null){
				text = value.getLabel();
			}
		} catch (GkException e) {
			LOG.error(e);
		}
		return text;
	}

}

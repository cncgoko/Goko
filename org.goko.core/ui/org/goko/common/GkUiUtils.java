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
package org.goko.common;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.goko.common.elements.combo.LabeledValue;
import org.goko.core.common.exception.GkException;

public final class GkUiUtils {

	private GkUiUtils() { }

	public static <T> LabeledValue<T> getLabelledValueByKey(T key, List<LabeledValue<T>> lstValues) throws GkException{
		for(LabeledValue<T> value : lstValues){
			if(ObjectUtils.equals(key, value.getValue())){
				return value;
			}
		}
		return null;
	}
	/**
	 * Refresh the selected item in a list of labeled value (typically for a combo box
	 * - If lstValue is empty, <code>null</code> is returned
	 * - If previous value is null, and listValue is empty, <code>null</code> is returned
	 * - If previous value is not null, and lstValue does not contains previous value, first item in list is returned
	 * - If previous value is not null, and lstValue contains the previous value, previous value is returned
	 *
	 * @param lstValue the list of available choices
	 * @param previousSelection the previous selection
	 * @return LabeledValue
	 * @throws GkException  GkException
	 */
	public static <T> LabeledValue<T> refreshSelectedItem(List<LabeledValue<T>> lstValue, LabeledValue<T> previousSelection) throws GkException{
		if(CollectionUtils.isEmpty(lstValue)){
			return null;
		}
		// Handle auto selection
		if(previousSelection == null){
			return lstValue.get(0) ;
		}else{
			LabeledValue<T> selection = GkUiUtils.getLabelledValueByKey(previousSelection.getValue() , lstValue);
			if(selection != null){
				return selection;
			}else{
				return lstValue.get(0) ;
			}
		}

	}
}

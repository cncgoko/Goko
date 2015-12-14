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
package org.goko.controller.grbl.v08.element.model;

import java.util.ArrayList;
import java.util.List;


public class MultichoiceProperty extends Property<String> {
	private List<String> choices;
	private Integer selectedIndex;

	public MultichoiceProperty(String id,String label, String value, String... choicesArr) {
		super(id, label, value, false);
		selectedIndex = 0;
		choices = new ArrayList<String>();
		if(choicesArr != null && choicesArr.length > 0){
			for (String choice : choicesArr) {
				choices.add(choice);
			}
		}
	}

	@Override
	public String getValue() {
		if(selectedIndex != null){
			return choices.get(selectedIndex);
		}
		return null;
	}

	@Override
	public void setValue(String value) {
		selectedIndex = choices.indexOf(value);
	}
	/**
	 * @return the selectedIndex
	 */
	public Integer getSelectedIndex() {
		return selectedIndex;
	}

	/**
	 * @param selectedIndex the selectedIndex to set
	 */
	public void setSelectedIndex(Integer selectedIndex) {
		this.selectedIndex = selectedIndex;
	}

	/**
	 * @return the choices
	 */
	public List<String> getChoices() {
		return choices;
	}

	public String[] getChoicesArray() {
		return choices.toArray(new String[]{});
	}



}

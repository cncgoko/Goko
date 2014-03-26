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
package org.goko.base.dro.controller;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.goko.common.bindings.AbstractModelObject;
import org.goko.core.controller.bean.MachineValueDefinition;

public class DROPreferencesPageModel extends AbstractModelObject{

	private IObservableList displayedValuesList;


	/**
	 *
	 */
	public DROPreferencesPageModel() {
		super();

		displayedValuesList = new WritableList(new ArrayList<MachineValueDefinition>(), MachineValueDefinition.class);
	}

	/**
	 * @return the displayedValuesList
	 */
	public List<MachineValueDefinition> getDisplayedValuesTypedList() {
		return displayedValuesList;
	}


	public IObservableList getDisplayedValuesList() {
		return displayedValuesList;
	}

	/**
	 * @param displayedValuesList the displayedValuesList to set
	 */
	public void setDisplayedValuesList(IObservableList displayedValuesList) {
		this.displayedValuesList = displayedValuesList;
	}

}

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

import java.util.List;

import javax.inject.Inject;

import org.goko.common.bindings.AbstractController;
import org.goko.core.common.exception.GkException;
import org.goko.core.controller.IControllerService;
import org.goko.core.controller.bean.MachineValueDefinition;

public class DROValueProposalController extends AbstractController<DROValueProposalModel> {
	@Inject
	IControllerService controllerService;

	public DROValueProposalController(DROValueProposalModel dataModel) {
		super(dataModel);
	}

	public void refreshDisplayedValuesList() throws GkException {
		List<MachineValueDefinition> lst = controllerService.getMachineValueDefinition();

		getDataModel().getAvailableValuesTypedList().clear();
		getDataModel().getAvailableValuesTypedList().addAll(lst);

	}

	public void saveSelectedElements(List<MachineValueDefinition> lstElements){
		getDataModel().getSelectedValuesTypedList().addAll(lstElements);
	}
	/** (inheritDoc)
	 * @see org.goko.common.bindings.AbstractController#initialize()
	 */
	@Override
	public void initialize() throws GkException {
		refreshDisplayedValuesList();
	}

}

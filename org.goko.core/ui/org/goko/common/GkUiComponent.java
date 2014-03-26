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

import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.goko.common.bindings.AbstractController;
import org.goko.common.bindings.AbstractModelObject;
import org.goko.common.bindings.ErrorEvent;
import org.goko.common.bindings.WarningEvent;
import org.goko.core.common.event.EventListener;

public abstract class GkUiComponent<C extends AbstractController<D>, D extends AbstractModelObject> {
	private C controller;
	private D dataModel;


	public GkUiComponent(C abstractController, D abstractModelObject) {
		this.controller = abstractController;
		this.dataModel = abstractModelObject;
		this.controller.addListener(this);
	}

	public GkUiComponent(C abstractController) {
		this.controller = abstractController;
		this.dataModel = this.controller.getDataModel();
	}

	@EventListener(ErrorEvent.class)
	public void displayError(ErrorEvent e){
		ErrorDialog.openError(null, "Goko", e.getMessage(), e.getStatus());
	}

	@EventListener(WarningEvent.class)
	public void displayWarning(WarningEvent e){
		MessageDialog.openWarning(null, "Goko", e.getMessage());
	}

	/**
	 * @return the controller
	 */
	public C getController() {
		return controller;
	}

	/**
	 * @param controller the controller to set
	 */
	public void setController(C controller) {
		this.controller = controller;
	}

	/**
	 * @return the dataModel
	 */
	public D getDataModel() {
		return dataModel;
	}

	/**
	 * @param dataModel the dataModel to set
	 */
	public void setDataModel(D dataModel) {
		this.dataModel = dataModel;
	}

}

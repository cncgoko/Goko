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

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.widgets.Shell;
import org.goko.common.bindings.AbstractController;
import org.goko.common.bindings.AbstractModelObject;
import org.goko.common.bindings.ErrorEvent;
import org.goko.common.bindings.WarningEvent;
import org.goko.core.common.event.EventListener;
import org.goko.core.common.exception.GkException;

public abstract class GkUiDialogComponent<C extends AbstractController<D>, D extends AbstractModelObject> extends Dialog{
	GkUiComponentProxy<C, D> uiComponent;

	public GkUiDialogComponent(Shell shell, C abstractController) {
		super(shell);
		uiComponent = new GkUiComponentProxy<C, D>(abstractController);
	}

	@EventListener(ErrorEvent.class)
	public void displayError(ErrorEvent e){
		uiComponent.displayError(e);
	}


	@EventListener(WarningEvent.class)
	public void displayWarning(WarningEvent e){
		uiComponent.displayWarning(e);
	}

	public void displayMessage(GkException e){
		uiComponent.displayMessage(e);
	}
	/**
	 * @return the controller
	 */
	public C getController() {
		return uiComponent.getController();
	}

	/**
	 * @param controller the controller to set
	 */
	public void setController(C controller) {
		uiComponent.setController(controller);
	}

	/**
	 * @return the dataModel
	 */
	public D getDataModel() {
		return uiComponent.getDataModel();
	}

	/**
	 * @param dataModel the dataModel to set
	 */
	public void setDataModel(D dataModel) {
		uiComponent.setDataModel(dataModel);
	}

}

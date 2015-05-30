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
package org.goko.common;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.goko.common.bindings.AbstractController;
import org.goko.common.bindings.AbstractModelObject;
import org.goko.common.bindings.ErrorEvent;
import org.goko.common.bindings.WarningEvent;
import org.goko.core.common.event.EventListener;
import org.goko.core.common.exception.GkException;
import org.goko.core.log.GkLog;

public abstract class GkUiPreferencePageComponent<C extends AbstractController<D>, D extends AbstractModelObject> extends PreferencePage implements IWorkbenchPreferencePage {
	private static final GkLog LOG = GkLog.getLogger(GkUiPreferencePageComponent.class);
	GkUiComponentProxy<C, D> uiComponent;

	public GkUiPreferencePageComponent(C abstractController) {
		super();
		uiComponent = new GkUiComponentProxy<C, D>(abstractController);
	}
	public GkUiPreferencePageComponent(String title, C abstractController) {
		super(title);
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
		
	@Override
	public final boolean performOk() {		
		try {
			if(getController().validate()){
				return internPerformOk() && super.performOk();
			}
		} catch (GkException e) {
			LOG.error(e);
		}
		return false;
	}
	
	protected abstract boolean internPerformOk() throws GkException;

}

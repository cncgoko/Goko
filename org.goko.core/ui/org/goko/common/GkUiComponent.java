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

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.goko.common.bindings.AbstractController;
import org.goko.common.bindings.AbstractModelObject;
import org.goko.common.bindings.ErrorEvent;
import org.goko.common.bindings.WarningEvent;
import org.goko.core.common.event.EventListener;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkFunctionalException;
import org.goko.core.log.GkLog;

public abstract class GkUiComponent<C extends AbstractController<D>, D extends AbstractModelObject> {
	private static final GkLog LOG = GkLog.getLogger(GkUiComponent.class);
	private C controller;
	private D dataModel;


	public GkUiComponent(IEclipseContext context, C abstractController, D abstractModelObject) {
		this.controller = abstractController;
		this.dataModel = abstractModelObject;
		this.controller.addListener(this);		
		initialize(context);
	}

	public GkUiComponent(IEclipseContext context, C abstractController) {
		this.controller = abstractController;
		this.dataModel = this.controller.getDataModel();
		initialize(context);
	}

	private void initialize(IEclipseContext context){
		//IEclipseContext ct = EclipseContextFactory.getServiceContext(FrameworkUtil.getBundle(getClass()).getBundleContext());		
		ContextInjectionFactory.inject(getController(), context);
		try {
			getController().initialize();
		} catch (GkException e) {
			LOG.error(e);
		}		
	}
	public void displayError(GkException e){
		getController().log(e);
		displayError(new ErrorEvent(e, "Goko"));
	}

	@EventListener(ErrorEvent.class)
	public void displayError(final ErrorEvent e){
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				ErrorDialog.openError(Display.getDefault().getActiveShell(), "Goko", e.getMessage(), e.getStatus());
			}
		});
	}

	@EventListener(WarningEvent.class)
	public void displayWarning(final WarningEvent e){
		Display.getDefault().asyncExec(new Runnable() {

			@Override
			public void run() {
				MessageDialog.openWarning(Display.getDefault().getActiveShell(), "Goko", e.getMessage());
			}
		});
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

	protected void displayMessage(GkException e){
		LOG.error(e);
		if(e instanceof GkFunctionalException){
			displayWarning(new WarningEvent(e.getMessage()));
		}else{
			displayError(new ErrorEvent(e, "Goko"));
		}

	}
}

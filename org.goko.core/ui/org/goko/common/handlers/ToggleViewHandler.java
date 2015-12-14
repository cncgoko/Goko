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
package org.goko.common.handlers;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.menu.ItemType;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledMenuItem;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkTechnicalException;

public class ToggleViewHandler {
	@Inject
	EPartService partService;

	@Execute
	public void execute(MHandledMenuItem item, @Named("org.goko.commands.toggleView.viewName") String partId) throws GkException {
		MPart part = partService.findPart(partId);
		if(part == null){
			throw new GkTechnicalException("Unable to find part '"+partId+"'...");
		}
		if(item.getType() == ItemType.CHECK){
			part.setVisible( item.isSelected() );
			if(item.isSelected()){
				partService.showPart(part, PartState.ACTIVATE);
			}
		}else if(item.getType() == ItemType.PUSH){
			part.setVisible( true );
			partService.showPart(part, PartState.ACTIVATE);
		}
	}

	@CanExecute
	public boolean canExecute(MHandledMenuItem item, @Named("org.goko.commands.toggleView.viewName") String partId){
		return true;
	}
}

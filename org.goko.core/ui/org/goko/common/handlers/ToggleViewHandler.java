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

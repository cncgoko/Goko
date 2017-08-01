 
package goko.contribution;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.AboutToHide;
import org.eclipse.e4.ui.di.AboutToShow;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.commands.MCommand;
import org.eclipse.e4.ui.model.application.commands.MCommandsFactory;
import org.eclipse.e4.ui.model.application.commands.MParameter;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledMenuItem;
import org.eclipse.e4.ui.model.application.ui.menu.MMenu;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuElement;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuFactory;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.goko.core.log.GkLog;

public class ViewMenuDynamicContribution {
	private static GkLog LOG = GkLog.getLogger(ViewMenuDynamicContribution.class);
	public static final String VIEW_MENU_ENTRY_TAG = "view";
	public static final String VIEW_NAME_PARAMETER = "org.goko.commands.toggleView.viewName";
	
	@Inject
	private EPartService partService;
	@Inject
	private EModelService modelService;
	@Inject
	@Optional
	private MApplication application;
	@Inject
	private ECommandService commandService;
	@Inject
	@Optional
	@Named("goko.menu.window")
	private MMenu menu;
	
	@AboutToHide
	public void aboutToHide(List<MMenuElement> items) {
	}
	
	@AboutToShow
	public void aboutToShow(List<MMenuElement> items) {		
		Collection<MPart> parts = partService.getParts(); 

		Iterator<MPart> iterator = parts.iterator();
				
		List<MMenuElement> children = new ArrayList<MMenuElement>();		
		List<String> existingChidlrenIds = new ArrayList<String>();
		
		MMenu viewMenu = getViewMenu(); // Get existing items. Dirty hack against eclipse bug ?
		for (MMenuElement windowsElement : viewMenu.getChildren()) {
			existingChidlrenIds.add(windowsElement.getElementId());
		}
		List<MCommand> commands = modelService.findElements(application, "goko.command.toggleView", MCommand.class, null);
		
		if(CollectionUtils.isNotEmpty(commands)){
			MCommand modeledCommand = commands.get(0);
			
			while (iterator.hasNext()) {
				MPart mPart = iterator.next();
				if(mPart.getTags().contains(VIEW_MENU_ENTRY_TAG)){
					String menuItemId = "goko.menu.window.view.menuitem."+mPart.getElementId();
					// Only creates the button if it doesn't exist yet
					
					if(!existingChidlrenIds.contains(menuItemId)){ 
						MHandledMenuItem item = MMenuFactory.INSTANCE.createHandledMenuItem();						
						item.setElementId(menuItemId);
						item.setLabel(mPart.getLabel());
						item.setTooltip(mPart.getLabel());					
						item.setIconURI(mPart.getIconURI());
						item.setContributorURI("platform:/plugin/Goko");
						Map<String, Object> parameters = new HashMap<String, Object>();
						parameters.put(VIEW_NAME_PARAMETER, mPart.getElementId());
						ParameterizedCommand command = commandService.createCommand("goko.command.toggleView", parameters);
						MParameter parameter = MCommandsFactory.INSTANCE.createParameter();
						parameter.setName(VIEW_NAME_PARAMETER);
						parameter.setValue(mPart.getElementId());
						item.getParameters().add(parameter);
						item.setCommand(modeledCommand);
						children.add(item);
					}
				}
			}
			
			Collections.sort(children, new MenuLabelComparator());
			items.addAll(children);
			//viewSubmenu.getChildren().addAll(children);
		}
	}		
	
	private MMenu getViewMenu(){
		List<MMenuElement> mainMenus = application.getChildren().get(0).getMainMenu().getChildren();
		for (MMenuElement mMenuElement : mainMenus) {
			if(StringUtils.equals(mMenuElement.getElementId(), "goko.menu.window")){
				MMenu windowsMenu = (MMenu) mMenuElement;
				
				for (MMenuElement windowsElement : windowsMenu.getChildren()) {
					if(StringUtils.equals(windowsElement.getElementId(), "goko.menu.window.view")){
						return (MMenu) windowsElement;
					}
				}
			}
		}
		return null;
	}
}


class MenuLabelComparator implements Comparator<MMenuElement>{

	/** (inheritDoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(MMenuElement arg0, MMenuElement arg1) {		
		return StringUtils.defaultString(arg0.getLabel()).compareTo(arg1.getLabel());
	}
	
}
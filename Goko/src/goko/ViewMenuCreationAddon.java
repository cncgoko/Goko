package goko;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledMenuItem;
import org.eclipse.e4.ui.model.application.ui.menu.MMenu;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuFactory;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

@Creatable
public class ViewMenuCreationAddon implements EventHandler{
	public static final String VIEW_MENU_ENTRY_TAG = "view";
	@Inject
	private EPartService partService;
	@Inject
	private EModelService modelService;
	@Inject
	@Optional
	private MApplication application;
	@Inject
	private ECommandService commandService;

	@Override
	public void handleEvent(Event event) {
		List<MMenu> lstViewSubmenu = modelService.findElements(application, "goko.menu.window.view", MMenu.class, new ArrayList<String>(), EModelService.IN_MAIN_MENU);
		MMenu viewSubmenu = lstViewSubmenu.get(0);

		Collection<MPart> parts = partService.getParts();
		Iterator<MPart> iterator = parts.iterator();

		while (iterator.hasNext()) {
			MPart mPart = iterator.next();
			if(mPart.getTags().contains(VIEW_MENU_ENTRY_TAG)){

				MHandledMenuItem item = MMenuFactory.INSTANCE.createHandledMenuItem();//modelService.createModelElement(HandledMenuItemImpl.class);
				item.setLabel(mPart.getLabel());
				item.setTooltip(mPart.getLabel());
				item.setIconURI(mPart.getIconURI());
				Map<String, Object> parameters = new HashMap<String, Object>();
				parameters.put("org.goko.commands.toggleView.viewName", mPart.getElementId());
				ParameterizedCommand command = commandService.createCommand("goko.command.toggleView", parameters);
				item.setWbCommand(command);
				viewSubmenu.getChildren().add(item);
			}
		}
	}
}

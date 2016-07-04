/**
 * 
 */
package org.goko.tools.editor.handlers;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IContributionItem;
import org.goko.core.gcode.element.IGCodeProvider;
import org.goko.core.gcode.service.IGCodeProviderRepository;
import org.goko.core.log.GkLog;
import org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.menu.gcodeprovider.IGCodeProviderContributionItem;
import org.goko.tools.editor.GCodeEditorTopic;
import org.goko.tools.editor.component.provider.GCodeProviderSourceDocument;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;

/**
 * @author Psyko
 * @date 28 mai 2016
 */
public class GCodeEditContributionItem implements IGCodeProviderContributionItem{
	private static final GkLog LOG = GkLog.getLogger(GCodeEditContributionItem.class);
	/** Event admin */
	private EventAdmin eventAdmin;
	/** GCode service */
	private IGCodeProviderRepository gcodeProviderRepository;
	
	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.menu.gcodeprovider.IGCodeProviderContributionItem#getItem(org.goko.core.gcode.element.IGCodeProvider)
	 */
	@Override
	public IContributionItem getItem(final IGCodeProvider provider) {
		return new ActionContributionItem(new Action(){
			/** (inheritDoc)
			 * @see org.eclipse.jface.action.Action#getText()
			 */
			@Override
			public String getText() {				
				return "Edit";
			}
			
			/** (inheritDoc)
			 * @see org.eclipse.jface.action.Action#run()
			 */
			@Override
			public void run() {
				Map<String, Object> map = new HashMap<String, Object>();
				GCodeProviderSourceDocument documentProvider = new GCodeProviderSourceDocument(gcodeProviderRepository, provider);				
				map.put(IEventBroker.DATA, documentProvider);
				eventAdmin.sendEvent(new Event(GCodeEditorTopic.TOPIC_OPEN_EDITOR, map));	
			}
		});
	}
	
	/**
	 * @return the eventAdmin
	 */
	public EventAdmin getEventAdmin() {
		return eventAdmin;
	}

	/**
	 * @param eventAdmin the eventAdmin to set
	 */
	public void setEventAdmin(EventAdmin eventAdmin) {
		this.eventAdmin = eventAdmin;
	}

	/**
	 * @return the gcodeProviderRepository
	 */
	public IGCodeProviderRepository getGcodeProviderRepository() {
		return gcodeProviderRepository;
	}

	/**
	 * @param gcodeProviderRepository the gcodeProviderRepository to set
	 */
	public void setGcodeProviderRepository(IGCodeProviderRepository gcodeProviderRepository) {
		this.gcodeProviderRepository = gcodeProviderRepository;
	}

}
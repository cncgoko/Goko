/**
 * 
 */
package org.goko.tools.editor;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.goko.core.common.exception.GkException;
import org.goko.tools.editor.component.provider.IDocumentProvider;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;

/**
 * Addon used for opening a document in the GCode editor
 * @author Psyko
 * @date 25 mai 2016
 */
public class GCodeEditorPartOpenerAddon {
	@Inject EPartService partService;
	@Inject EventAdmin eventAdmin;
	
	
	/**
	 * Received an open request. We first have to make sure that the part is activated and then dispatch the event for opening the document in the part 
	 * @param provider the documentProvider to open
	 * @throws GkException GkException
	 */
	@Inject
	@Optional
	public void onOpenRequest(@UIEventTopic(GCodeEditorTopic.TOPIC_OPEN_EDITOR) IDocumentProvider provider) throws GkException{
		partService.showPart("org.goko.tools.editor.part.editor", PartState.ACTIVATE);		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(IEventBroker.DATA, provider);
		eventAdmin.sendEvent(new Event(GCodeEditorTopic.TOPIC_OPEN_PART_EDITOR, map));	
	}
}

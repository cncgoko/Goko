package org.goko.core.gcode.rs274ngcv3.event;

import org.goko.core.gcode.element.IGCodeProvider;
import org.goko.core.gcode.rs274ngcv3.element.GCodeProvider;
import org.goko.core.gcode.rs274ngcv3.element.IModifier;
import org.goko.core.workspace.service.IWorkspaceEvent;
import org.goko.core.workspace.service.WorkspaceEvent;

public class RS274WorkspaceEvent extends WorkspaceEvent {
	/** Type of event for RS274 GCode */
	public static final String TYPE = "workspace.event.RS274WorkspaceEvent";
	/** Content is a {@link GCodeProvider} */
	public static final Integer GCODE_PROVIDER_EVENT = 1;
	/** Content is a {@link IModifier} */
	public static final Integer GCODE_MODIFIER_EVENT = 2;
	
	/** The content type */
	private Integer contentType;
	
	/**
	 * Constructor 
	 * @param action the type of action
	 * @param idElement the id of the element
	 */
	public RS274WorkspaceEvent(String action, Integer idElement, Integer contentType) {
		super(TYPE, action, idElement);	
		this.contentType = contentType;
	}

	/**
	 * @return the contentType
	 */
	public Integer getContentType() {
		return contentType;
	}
	
	public static IWorkspaceEvent getCreateEvent(IGCodeProvider gcodeProvider){
		return new RS274WorkspaceEvent(IWorkspaceEvent.ACTION_CREATE, gcodeProvider.getId(), GCODE_PROVIDER_EVENT);
	}
	
	public static IWorkspaceEvent getUpdateEvent(IGCodeProvider gcodeProvider){
		return new RS274WorkspaceEvent(IWorkspaceEvent.ACTION_UPDATE, gcodeProvider.getId(), GCODE_PROVIDER_EVENT);
	}
	
	public static IWorkspaceEvent getDeleteEvent(IGCodeProvider gcodeProvider){
		return new RS274WorkspaceDeleteEvent(IWorkspaceEvent.ACTION_DELETE, gcodeProvider.getId(), GCODE_PROVIDER_EVENT, gcodeProvider);
	}
	
	public static IWorkspaceEvent getCreateEvent(IModifier<?> modifier){
		return new RS274WorkspaceEvent(IWorkspaceEvent.ACTION_CREATE, modifier.getId(), GCODE_MODIFIER_EVENT);
	}
	
	public static IWorkspaceEvent getUpdateEvent(IModifier<?> modifier){
		return new RS274WorkspaceEvent(IWorkspaceEvent.ACTION_UPDATE, modifier.getId(), GCODE_MODIFIER_EVENT);
	}
	
	public static IWorkspaceEvent getDeleteEvent(IModifier<?> modifier){
		return new RS274WorkspaceDeleteEvent(IWorkspaceEvent.ACTION_DELETE, modifier.getId(), GCODE_MODIFIER_EVENT, modifier);
	}
}

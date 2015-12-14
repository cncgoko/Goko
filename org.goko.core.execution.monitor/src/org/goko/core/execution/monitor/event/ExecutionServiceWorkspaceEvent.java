package org.goko.core.execution.monitor.event;

import org.goko.core.gcode.execution.ExecutionTokenState;
import org.goko.core.gcode.execution.IExecutionToken;
import org.goko.core.workspace.service.IWorkspaceEvent;
import org.goko.core.workspace.service.WorkspaceEvent;

public class ExecutionServiceWorkspaceEvent extends WorkspaceEvent {
	/** Type of event for Execution service */
	public static final String TYPE = "workspace.event.ExecutionServiceWorkspaceEvent";
	
	/**
	 * Constructor 
	 * @param action the type of action
	 * @param idElement the id of the element
	 */
	public ExecutionServiceWorkspaceEvent(String action, Integer idElement) {
		super(TYPE, action, idElement);	
	}
	
	public static IWorkspaceEvent getCreateEvent(IExecutionToken<ExecutionTokenState> executionToken){
		return new ExecutionServiceWorkspaceEvent(IWorkspaceEvent.ACTION_CREATE, executionToken.getId());
	}
	
	public static IWorkspaceEvent getUpdateEvent(IExecutionToken<ExecutionTokenState> executionToken){
		return new ExecutionServiceWorkspaceEvent(IWorkspaceEvent.ACTION_UPDATE, executionToken.getId());
	}
	
	public static IWorkspaceEvent getDeleteEvent(IExecutionToken<ExecutionTokenState> executionToken){
		return new ExecutionServiceWorkspaceEvent(IWorkspaceEvent.ACTION_DELETE, executionToken.getId());
	}
}

package org.goko.core.workspace.service;

public interface IWorkspaceEvent {
	/** Create action */
	public static final String ACTION_CREATE = "workspace.action.create";
	/** Delete action */
	public static final String ACTION_DELETE = "workspace.action.delete";
	/** Update action */
	public static final String ACTION_UPDATE = "workspace.action.update";
	
	/**
	 * The type of the event 
	 * @return the type of the event
	 */
	public String getType();
	
	/**
	 * Test the event type
	 * @param type the target type
	 * @return <code>true</code> if the type of the event equals the given parameter, <code>false</code> otherwise
	 */
	public boolean isType(String type);
	
	/**
	 * The action of the event 
	 * @return the action of the event 
	 */
	public String getAction();
	
	/**
	 * Test the event action
	 * @param action the target action
	 * @return <code>true</code> if the action of the event equals the given parameter, <code>false</code> otherwise
	 */
	public boolean isAction(String action);
	
	/**
	 * The id of the target element
	 * @return an id
	 */
	public Integer getIdElement();
}

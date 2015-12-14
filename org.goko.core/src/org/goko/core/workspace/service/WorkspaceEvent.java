package org.goko.core.workspace.service;

import org.apache.commons.lang3.StringUtils;

public class WorkspaceEvent implements IWorkspaceEvent {
	/** The type of event */
	private String type;
	/** The action of the event */
	private String action;
	/** The id of the target element */
	private Integer idElement;
	
	/**
	 * Constructor 
	 * @param type type of event
	 * @param action action of the event 
	 * @param idElement id of the target element
	 */
	public WorkspaceEvent(String type, String action, Integer idElement) {
		super();
		this.type = type;
		this.action = action;
		this.idElement = idElement;
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IWorkspaceEvent#getType()
	 */
	@Override
	public String getType() {
		return type;
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IWorkspaceEvent#isType(java.lang.String)
	 */
	@Override
	public boolean isType(String type) {
		return StringUtils.equals(type, this.type);
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IWorkspaceEvent#getAction()
	 */
	@Override
	public String getAction() {
		return action;
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IWorkspaceEvent#isAction(java.lang.String)
	 */
	@Override
	public boolean isAction(String action) {
		return StringUtils.equals(action, this.action);
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IWorkspaceEvent#getIdElement()
	 */
	@Override
	public Integer getIdElement() {
		return idElement;
	}
	
	
}

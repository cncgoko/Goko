package org.goko.core.gcode.rs274ngcv3.event;

import org.goko.core.workspace.service.IWorkspaceEvent;

/**
 * A Workspace event for deletion holding the deleted object. Since notification is called after the object
 * is removed from storage, it is required to give the full deleted object so deletion listener can still 
 * find information about it. 
 * 
 * @author Psyko
 *
 */
public class RS274WorkspaceDeleteEvent extends RS274WorkspaceEvent {
	/** The delete object */
	private Object deletedObject;
	
	/**
	 * Constructor allowing to pass the deleted object
	 * @param action the action type
	 * @param idElement the id of the deleted element 
	 * @param contentType the type of content of this object 
	 * @param deletedObject the deleted object
	 */
	public RS274WorkspaceDeleteEvent(String action, Integer idElement, Integer contentType, Object deletedObject) {
		super(IWorkspaceEvent.ACTION_DELETE, idElement, contentType);		
		this.deletedObject = deletedObject;
	}

	/**
	 * @return the deletedObject
	 */
	public Object getDeletedObject() {
		return deletedObject;
	}

	/**
	 * @param deletedObject the deletedObject to set
	 */
	public void setDeletedObject(Object deletedObject) {
		this.deletedObject = deletedObject;
	}

}

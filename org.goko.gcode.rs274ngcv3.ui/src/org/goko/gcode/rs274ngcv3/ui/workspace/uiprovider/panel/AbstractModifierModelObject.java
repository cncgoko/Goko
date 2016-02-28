package org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.panel;

import org.apache.commons.lang3.ObjectUtils;
import org.goko.common.bindings.AbstractModelObject;

public class AbstractModifierModelObject extends AbstractModelObject {
	protected static final String DIRTY = "dirty";
	
	private boolean dirty;

	/**
	 * @return the dirty
	 */
	public boolean isDirty() {
		return dirty;
	}

	/**
	 * @param dirty the dirty to set
	 */
	public void setDirty(boolean dirty) {
		this.dirty = dirty;		
	}
	
	/** (inheritDoc)
	 * @see org.goko.common.bindings.AbstractModelObject#firePropertyChange(java.lang.String, java.lang.Object, java.lang.Object)
	 */
	@Override
	protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
		if(!ObjectUtils.equals(oldValue, newValue)){
			setDirty(true); 			
		}
		super.firePropertyChange(propertyName, oldValue, newValue);
	}
	
	
}

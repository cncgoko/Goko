package org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.panel;

import org.goko.common.bindings.AbstractController;


public abstract class AbstractModifierPanelController<T extends AbstractModifierModelObject> extends AbstractController<T> {

	/**
	 * Constructor
	 * @param binding
	 */
	public AbstractModifierPanelController(T binding) {
		super(binding);
	}


}

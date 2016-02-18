package org.goko.gcode.rs274ngcv3.ui.workspace.io;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name="modifier")
public abstract class XmlGCodeModifier {
	/** State of the modifier */
	@Attribute
	private boolean enabled;
	/** Order */
	@Attribute
	private int order;

	/**
	 * @return the enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}
	/**
	 * @param enabled the enabled to set
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	/**
	 * @return the order
	 */
	public int getOrder() {
		return order;
	}
	/**
	 * @param order the order to set
	 */
	public void setOrder(int order) {
		this.order = order;
	}


}

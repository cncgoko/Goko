package org.goko.core.gcode.rs274ngcv3.element;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.utils.IIdBean;

public interface IModifier<T extends GCodeProvider> extends IIdBean {
	
	Integer getIdGCodeProvider();
	
	String getModifierName();
	
	boolean isEnabled();
	
	void setEnabled(boolean enabled);
	
	public void apply(T source, T target) throws GkException;

	
}


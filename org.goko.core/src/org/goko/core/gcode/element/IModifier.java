package org.goko.core.gcode.element;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.utils.IIdBean;

public interface IModifier<T extends IGCodeProvider, C extends IGCodeContext> extends IIdBean {
	
	public T apply(C initialContext) throws GkException;

	
}

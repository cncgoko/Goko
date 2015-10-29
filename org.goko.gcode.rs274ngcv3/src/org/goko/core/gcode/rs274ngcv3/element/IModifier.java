package org.goko.core.gcode.rs274ngcv3.element;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.utils.IIdBean;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;

public interface IModifier extends IIdBean {
	
	public void apply(GCodeContext initialContext, GCodeProvider source, GCodeProvider target) throws GkException;

}

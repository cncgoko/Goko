package org.goko.core.gcode.bean.modifier;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.bean.IGCodeProvider;

public interface IGCodeProviderModifier {
	
	void setEnabled(boolean enabled);
	
	boolean isEnabled();
	
	IGCodeProvider getResult() throws GkException;
	
	void setTarget(Integer idProvider) throws GkException;
}

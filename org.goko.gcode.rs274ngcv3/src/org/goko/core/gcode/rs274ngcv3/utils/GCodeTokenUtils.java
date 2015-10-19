package org.goko.core.gcode.rs274ngcv3.utils;

import org.apache.commons.lang3.StringUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkTechnicalException;
import org.goko.core.gcode.rs274ngcv3.parser.GCodeToken;
import org.goko.core.gcode.rs274ngcv3.parser.GCodeTokenType;

public class GCodeTokenUtils {

	public static int getLineNumber(GCodeToken token) throws GkException{
		if(token.getType() != GCodeTokenType.LINE_NUMBER){
			throw new GkTechnicalException("Given token is not a line number token");
		}
		return Integer.valueOf( StringUtils.substring(token.getValue(), 1));
	}
}

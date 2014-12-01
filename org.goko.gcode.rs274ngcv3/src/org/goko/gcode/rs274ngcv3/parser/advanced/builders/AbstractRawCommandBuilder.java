package org.goko.gcode.rs274ngcv3.parser.advanced.builders;

import java.util.List;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.bean.GCodeCommand;
import org.goko.core.gcode.bean.GCodeContext;
import org.goko.gcode.rs274ngcv3.RS274;
import org.goko.gcode.rs274ngcv3.parser.GCodeToken;
import org.goko.gcode.rs274ngcv3.parser.advanced.AbstractRS274CommandBuilder;

public abstract class AbstractRawCommandBuilder<T extends GCodeCommand> extends AbstractRS274CommandBuilder<T> {

	/** {@inheritDoc}
	 * @see org.goko.gcode.rs274ngcv3.parser.advanced.IRS274CommandBuilder#match(java.util.List, org.goko.core.gcode.bean.GCodeContext)
	 */
	@Override
	public boolean match(List<GCodeToken> lstTokens, GCodeContext context) throws GkException {
		return true;
	}



	/** {@inheritDoc}
	 * @see org.goko.gcode.rs274ngcv3.parser.advanced.IRS274CommandBuilder#buildCommand(java.util.List, org.goko.core.gcode.bean.GCodeContext, java.lang.Object)
	 */
	@Override
	public void buildCommand(List<GCodeToken> lstTokens, GCodeContext context, T targetCommand) throws GkException {
		targetCommand.setStringCommand( RS274.toString(lstTokens) );

		GCodeToken lineToken = RS274.findUniqueTokenByLetter("n", lstTokens);
		if(lineToken != null){
			String strNumber =  RS274.getTokenValue(lineToken);
			targetCommand.setLineNumber( Integer.valueOf(strNumber) );
			lstTokens.remove(lineToken);
		}
	}


}

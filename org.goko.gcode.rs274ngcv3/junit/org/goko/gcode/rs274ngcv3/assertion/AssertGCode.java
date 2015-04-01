package org.goko.gcode.rs274ngcv3.assertion;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.bean.GCodeCommand;
import org.goko.core.gcode.bean.GCodeContext;
import org.goko.core.gcode.service.IGCodeService;
import org.goko.gcode.rs274ngcv3.parser.GCodeLexer;
import org.goko.gcode.rs274ngcv3.parser.GCodeToken;
import org.junit.Assert;

public class AssertGCode {

	public static void assertParseEquals(IGCodeService service , String command) throws GkException{
		GCodeCommand comm = service.parseCommand(command, new GCodeContext());
		String converted = new String(service.convert(comm));
		AssertGCode.assertEquals(command, converted);
	}

	public static void assertEquals(String expectedGCodeCommand, String actualGCodeCommand) throws GkException{
		GCodeLexer 			gcodeLexer = new GCodeLexer();
		List<GCodeToken> 	lstExpectedTokens = gcodeLexer.createTokens(expectedGCodeCommand);
		List<GCodeToken> 	lstActualTokens   = gcodeLexer.createTokens(actualGCodeCommand);
		List<GCodeToken> 	lstActualTmp      = gcodeLexer.createTokens(actualGCodeCommand);

		if(CollectionUtils.size(lstExpectedTokens) != CollectionUtils.size(lstActualTokens)){
			Assert.fail("The 2 commands are not equal : Exepcted '"+expectedGCodeCommand+"', got '"+actualGCodeCommand+"'");
		}

		for (GCodeToken gCodeToken : lstExpectedTokens) {
			if(lstActualTmp.contains(gCodeToken)){
				lstActualTmp.remove(gCodeToken);
			}else{
				Assert.fail("The 2 commands are not equal : Exepcted '"+expectedGCodeCommand+"', got '"+actualGCodeCommand+"'");
			}
		}

	}
}

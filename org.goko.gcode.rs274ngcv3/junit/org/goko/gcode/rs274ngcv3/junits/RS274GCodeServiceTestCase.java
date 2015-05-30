package org.goko.gcode.rs274ngcv3.junits;

import junit.framework.TestCase;

import org.goko.core.gcode.bean.GCodeCommand;
import org.goko.core.gcode.bean.GCodeContext;
import org.goko.core.gcode.service.IGCodeService;
import org.goko.gcode.rs274ngcv3.RS274GCodeService;
import org.goko.gcode.rs274ngcv3.assertion.AssertGCode;
import org.goko.gcode.rs274ngcv3.config.RS274Config;

public class RS274GCodeServiceTestCase extends TestCase{
	private IGCodeService gcodeService;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.gcodeService = new RS274GCodeService();
		this.gcodeService.start();
		RS274Config.getInstance().setDecimalTruncateEnabled(false);
	}

	public void testCommands() throws Exception{
		AssertGCode.assertEquals("G90X12.5Y36.8Z45.3F100", "G90F100Y36.8Z45.3X12.5");
	}

	public void testTruncateHalfDownCommands() throws Exception{
		RS274Config.getInstance().setDecimalTruncateEnabled(true);
		RS274Config.getInstance().setDecimalCount(3);

		GCodeCommand comm = gcodeService.parseCommand("G90 X12.53642", new GCodeContext());
		String converted = new String(gcodeService.convert(comm));
		AssertGCode.assertEquals("G90 X12.536", converted);

		RS274Config.getInstance().setDecimalTruncateEnabled(false);
	}

	public void testTruncateHalfUpCommands() throws Exception{
		RS274Config.getInstance().setDecimalTruncateEnabled(true);
		RS274Config.getInstance().setDecimalCount(3);

		GCodeCommand comm = gcodeService.parseCommand("G90 X12.53672", new GCodeContext());
		String converted = new String(gcodeService.convert(comm));
		AssertGCode.assertEquals("G90 X12.536", converted);

		RS274Config.getInstance().setDecimalTruncateEnabled(false);
	}

	public void testCommandPartialNumber() throws Exception{
		GCodeCommand comm = gcodeService.parseCommand("N12 G90 X.5 Y.8 Z45.3 F100", new GCodeContext());
		String converted = new String(gcodeService.convert(comm));
		AssertGCode.assertEquals("N12 G90 X0.5 Y0.8 Z45.3 F100", converted);
	}

	public void testCommandParserOk() throws Exception{
		AssertGCode.assertParseEquals(gcodeService, "( TEST )");
		AssertGCode.assertParseEquals(gcodeService, "( T0 M6 )");
		AssertGCode.assertParseEquals(gcodeService, "N12G90X12.5Y36.8Z45.3F100");
		AssertGCode.assertParseEquals(gcodeService, "N12 G90 X12.5 Y36.8 Z45.3 F100");
		AssertGCode.assertParseEquals(gcodeService, "G1 X12.5 Y36.8 Z45.3 F100");
		AssertGCode.assertParseEquals(gcodeService, "G3 X0.5 Y36.8 Z45.3 F100 I14.56 J32.23 K12.5");
		AssertGCode.assertParseEquals(gcodeService, "G21 G17 G55");
		AssertGCode.assertParseEquals(gcodeService, "G54");
		AssertGCode.assertParseEquals(gcodeService, "G55");
		AssertGCode.assertParseEquals(gcodeService, "G58 S1200");
		AssertGCode.assertParseEquals(gcodeService, "G57 M3");
		AssertGCode.assertParseEquals(gcodeService, "N12G90X12.5Y36.8Z45.3F100;M3");
		AssertGCode.assertParseEquals(gcodeService, "N12G90X12.5Y36.8Z45.3F100(M3)");
		AssertGCode.assertParseEquals(gcodeService, "N12G90X12.5Y36.8Z45.3F100.00(M3)");
		AssertGCode.assertParseEquals(gcodeService, "N12G90X12.5Y36.8Z45.3F100.00;M3");
		AssertGCode.assertParseEquals(gcodeService, "N12G90X12.5Y36.8Z45.3F100.00;Test");
		AssertGCode.assertParseEquals(gcodeService, "N12G90X12.5Y36.8Z-100.00;Test");
		AssertGCode.assertParseEquals(gcodeService, "N12G90X12.5Y36.8Z0.05;Test");
		AssertGCode.assertParseEquals(gcodeService, "N12G90X12.5Y36.8Z-0.05;Test");
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		this.gcodeService.stop();
	}
}

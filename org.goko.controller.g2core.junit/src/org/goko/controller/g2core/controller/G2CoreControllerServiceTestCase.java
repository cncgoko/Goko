package org.goko.controller.g2core.controller;

import java.math.BigDecimal;

import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.quantity.LengthUnit;
import org.goko.core.common.measure.quantity.Speed;
import org.goko.core.common.measure.quantity.SpeedUnit;
import org.goko.core.controller.bean.EnumControllerAxis;
import org.goko.core.controller.bean.MachineState;
import org.goko.core.gcode.rs274ngcv3.IRS274NGCService;
import org.goko.core.gcode.rs274ngcv3.RS274NGCServiceImpl;
import org.goko.core.gcode.rs274ngcv3.context.CoordinateSystem;
import org.goko.core.gcode.rs274ngcv3.context.EnumDistanceMode;
import org.goko.core.gcode.rs274ngcv3.context.EnumMotionMode;
import org.goko.core.gcode.rs274ngcv3.context.EnumPlane;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.math.Tuple6b;
import org.goko.junit.tools.connection.AssertSerialEmulator;
import org.goko.junit.tools.connection.SerialConnectionEmulator;

import junit.framework.TestCase;

/*
 
{"r":{"fv":0.99,"fb":100.17,"fbs":"100.17-dirty","fbc":"settings_othermill.h","hp":3,"hv":0,"id":"0084-d639-18c6-7bd","msg":"SYSTEM READY"},"f":[1,0,1]}
{"r":{"ej":1},"f":[1,0,10]}
{"r":{"sr":{"stat":1,"vel":0,"feed":0,"unit":1,"coor":2,"momo":4,"plan":0,"dist":0,"mpox":0,"mpoy":0,"mpoz":0,"posx":0,"posy":0,"posz":0}},"f":[1,0,177]}
{"r":{"sr":{"stat":1,"vel":0,"feed":0,"unit":1,"coor":2,"momo":4,"plan":0,"dist":0,"mpox":0,"mpoy":0,"mpoz":0,"posx":0,"posy":0,"posz":0}},"f":[1,0,11]}
{"r":{"qr":48},"f":[1,0,11]}
{"r":{"sys":{"fb":100.17,"fbs":"100.17-dirty","fbc":"settings_othermill.h","fv":0.99,"hp":3,"hv":0,"id":"0084-d639-18c6-7bd","jt":0.75,"ct":0.01,"sl":0,"lim":1,"saf":1,"m48e":1,"mfoe":0,"mfo":1,"mtoe":0,"mto":1,"mt":2,"spep":1,"spdp":0,"spph":1,"spdw":1.5,"ssoe":0,"sso":1,"cofp":1,"comp":1,"coph":1,"tv":1,"ej":1,"jv":2,"qv":1,"sv":1,"si":250,"gpl":0,"gun":1,"gco":2,"gpa":2,"gdi":0}},"f":[1,0,12]}
{"r":{"x":{"am":1,"vm":1500,"fr":1500,"tn":0,"tm":145.6,"jm":500,"jh":1000,"hi":1,"hd":0,"sv":500,"lv":25,"lb":1,"zb":0.4}},"f":[1,0,10]}
{"r":{"y":{"am":1,"vm":1500,"fr":1500,"tn":0,"tm":119.1,"jm":500,"jh":1000,"hi":3,"hd":0,"sv":500,"lv":25,"lb":1,"zb":0.4}},"f":[1,0,10]}
{"r":{"z":{"am":1,"vm":1500,"fr":1500,"tn":-60.1,"tm":0,"jm":500,"jh":1000,"hi":6,"hd":1,"sv":500,"lv":25,"lb":1,"zb":0.4}},"f":[1,0,10]}
{"r":{"a":{"am":0,"vm":110728,"fr":110728,"tn":-1,"tm":-1,"jm":36909,"jh":36909,"ra":0.776,"hi":0,"hd":0,"sv":55364,"lv":11072.83,"lb":5,"zb":2}},"f":[1,0,10]}
{"r":{"b":{"am":0,"vm":110728,"fr":110728,"tn":-1,"tm":-1,"jm":36909,"jh":36909,"ra":0.776,"hi":0,"hd":0,"sv":55364,"lv":11072.83,"lb":5,"zb":2}},"f":[1,0,10]}
{"r":{"c":{"am":0,"vm":110728,"fr":110728,"tn":-1,"tm":-1,"jm":36909,"jh":36909,"ra":0.776,"hi":0,"hd":0,"sv":55364,"lv":11072.83,"lb":5,"zb":2}},"f":[1,0,10]}
{"r":{"1":{"ma":0,"sa":1.8,"tr":4.8768,"mi":8,"su":328.08398,"po":1,"pm":2,"pl":0.375}},"f":[1,0,10]}
{"r":{"2":{"ma":1,"sa":1.8,"tr":4.8768,"mi":8,"su":328.08398,"po":1,"pm":2,"pl":0.375}},"f":[1,0,10]}
{"r":{"3":{"ma":2,"sa":1.8,"tr":4.8768,"mi":8,"su":328.08398,"po":0,"pm":2,"pl":0.375}},"f":[1,0,10]}
{"r":{"4":{"ma":3,"sa":1.8,"tr":360,"mi":8,"su":4.44444,"po":0,"pm":0,"pl":0}},"f":[1,0,10]}
{"r":{"p1":{"frq":100,"csl":10500,"csh":16400,"cpl":0.13,"cph":0.17,"wsl":0,"wsh":0,"wpl":0.1,"wph":0.1,"pof":0.1}},"f":[1,0,11]}
{"r":{"g55":{"x":0,"y":0,"z":0,"a":0,"b":0,"c":0}},"f":[1,0,12]}
{"r":{"g56":{"x":0,"y":0,"z":0,"a":0,"b":0,"c":0}},"f":[1,0,12]}
{"r":{"g57":{"x":0,"y":0,"z":0,"a":0,"b":0,"c":0}},"f":[1,0,12]}
{"r":{"g58":{"x":0,"y":0,"z":0,"a":0,"b":0,"c":0}},"f":[1,0,12]}
{"r":{"g59":{"x":0,"y":0,"z":0,"a":0,"b":0,"c":0}},"f":[1,0,12]}
{"sr":{"stat":1,"vel":0,"feed":0,"unit":1,"coor":2,"momo":4,"plan":0,"dist":0,"mpox":0,"mpoy":0,"mpoz":0,"posx":0,"posy":0,"posz":0}}
{"sys":{"jv":5,"qv":2}}
{"r":{"sys":{"jv":5,"qv":2}},"f":[1,0,25]}
 */
public class G2CoreControllerServiceTestCase extends TestCase {
	private G2CoreControllerService g2core;
	private SerialConnectionEmulator serialEmulator;
	private IRS274NGCService gcodeService;
	
	/** {@inheritDoc}
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {		
		super.setUp();
		serialEmulator = new SerialConnectionEmulator();
		serialEmulator.setDebugOutputConsole(true);
		G2CoreCommunicator communicator = new G2CoreCommunicator();
		communicator.setConnectionService(serialEmulator);
		g2core = new G2CoreControllerService(communicator);				
		gcodeService = new RS274NGCServiceImpl();
		g2core.setGCodeService(gcodeService);
		g2core.start(); 				  // Start the G2Core service
		serialEmulator.connect(null, null, null, null, null, null); // Make sure the service is connected
		g2core.setPlannerBufferCheck(false);		
	}
	
	/**
	 * Simple test for configuration update
	 * @throws Exception Exception
	 */
	public void testConfigurationUpdate() throws Exception{	
		
		BigDecimal queueReportVerbosity = g2core.getConfiguration().getSetting(G2Core.Configuration.Groups.SYSTEM, G2Core.Configuration.System.QUEUE_REPORT_VERBOSITY, BigDecimal.class);
		assertEquals(BigDecimal.ZERO, queueReportVerbosity);
		
		serialEmulator.receiveData("{\"r\":{\"qv\":2},\"f\":[1,0,10,4252]}"+'\n');
		BigDecimal queueReportVerbosityUpdated = g2core.getConfiguration().getSetting(G2Core.Configuration.Groups.SYSTEM, G2Core.Configuration.System.QUEUE_REPORT_VERBOSITY, BigDecimal.class);
		assertEquals(new BigDecimal("2"), queueReportVerbosityUpdated);
	}
	
	/**
	 * Context : We receive notification about the modification of the units in the GCode context
	 * Result  : TinyG controller updates it's internal units
	 * @throws Exception
	 */
	public void testGCodeContextUnitChange() throws Exception{			
		serialEmulator.clearOutputBuffer();
		serialEmulator.clearSentBuffer();
		
		serialEmulator.receiveDataWithEndChar("{\"r\":{\"sr\":{\"unit\":0}},\"f\":[1,0,0,0]}");		
		assertEquals(LengthUnit.INCH, g2core.getCurrentUnit());
		
		serialEmulator.receiveDataWithEndChar("{\"r\":{\"sr\":{\"unit\":1}},\"f\":[1,0,0,0]}");		
		assertEquals(LengthUnit.MILLIMETRE, g2core.getCurrentUnit());
	}
	
	/**
	 * Context : We receive notification about the coordinate system update offset
	 * Result  : TinyG controller updates it's internal units
	 * @throws Exception
	 */
	public void testGCodeContextCoordinateSystemOffsetChange() throws Exception{			
		serialEmulator.clearOutputBuffer();
		serialEmulator.clearSentBuffer();
		
		serialEmulator.receiveDataWithEndChar("{\"r\":{\"g55\":{\"x\":-10,\"y\":20,\"z\":35.53,\"a\":0,\"b\":0,\"c\":0}},\"f\":[1,0,12]}");		
		Tuple6b expectedG55 = new Tuple6b(Length.valueOf("-10", g2core.getCurrentUnit()),
											Length.valueOf("20", g2core.getCurrentUnit()),
											Length.valueOf("35.53", g2core.getCurrentUnit()));
		Tuple6b actualG55 = g2core.getGCodeContext().getCoordinateSystemData(CoordinateSystem.G55);
		assertEquals(expectedG55, actualG55);
		
	}
	
	/**
	 * Tests the various stat values in status report 
	 * @throws Exception Exception
	 */
	public void testStatusReportState() throws Exception{
		serialEmulator.clearOutputBuffer();
		serialEmulator.clearSentBuffer();
		{	// Stat 0 
			serialEmulator.receiveDataWithEndChar("{\"sr\":{\"stat\":0}}");
			assertEquals(g2core.getState(), MachineState.INITIALIZING);
		}
		{	// Stat 1 
			serialEmulator.receiveDataWithEndChar("{\"sr\":{\"stat\":1}}");
			assertEquals(g2core.getState(), MachineState.READY);
		}
		{	// Stat 2
			serialEmulator.receiveDataWithEndChar("{\"sr\":{\"stat\":2}}");
			assertEquals(g2core.getState(), MachineState.ALARM);
		}
		{	// Stat 3 
			serialEmulator.receiveDataWithEndChar("{\"sr\":{\"stat\":3}}");
			assertEquals(g2core.getState(), MachineState.PROGRAM_STOP);
		}
		{	// Stat 4 
			serialEmulator.receiveDataWithEndChar("{\"sr\":{\"stat\":4}}");
			assertEquals(g2core.getState(), MachineState.PROGRAM_END);
		}
		{	// Stat 5 
			serialEmulator.receiveDataWithEndChar("{\"sr\":{\"stat\":5}}");
			assertEquals(g2core.getState(), MachineState.MOTION_RUNNING);
		}
		{	// Stat 6 
			serialEmulator.receiveDataWithEndChar("{\"sr\":{\"stat\":6}}");
			assertEquals(g2core.getState(), MachineState.MOTION_HOLDING);
		}
		{	// Stat 7 
			serialEmulator.receiveDataWithEndChar("{\"sr\":{\"stat\":7}}");
			assertEquals(g2core.getState(), MachineState.PROBE_CYCLE);
		}		
		{	// Stat 9 
			serialEmulator.receiveDataWithEndChar("{\"sr\":{\"stat\":9}}");
			assertEquals(g2core.getState(), MachineState.HOMING);
		}	
		
	}
		
	/**
	 * Tests the various stat values in status report 
	 * @throws Exception Exception
	 */
	public void testStatusReportVelocity() throws Exception{
		serialEmulator.clearOutputBuffer();
		serialEmulator.clearSentBuffer();
		serialEmulator.receiveDataWithEndChar("{\"sr\":{\"vel\":109.35}}");
		assertEquals(g2core.getVelocity(), Speed.valueOf("109.35", SpeedUnit.MILLIMETRE_PER_MINUTE));
	}
	
	/**
	 * Tests the various feed values in status report 
	 * @throws Exception Exception
	 */
	public void testStatusReportDistanceMode() throws Exception{
		serialEmulator.clearOutputBuffer();
		serialEmulator.clearSentBuffer();
		{
			serialEmulator.receiveDataWithEndChar("{\"sr\":{\"dist\":0}}");
			assertEquals(EnumDistanceMode.ABSOLUTE, g2core.getGCodeContext().getDistanceMode());
		}
		{
			serialEmulator.receiveDataWithEndChar("{\"sr\":{\"dist\":1}}");
			assertEquals(EnumDistanceMode.RELATIVE, g2core.getGCodeContext().getDistanceMode());
		}
		
	}
	
	/**
	 * Tests the various feed values in status report 
	 * @throws Exception Exception
	 */
	public void testStatusReportCoordinateSystem() throws Exception{
		serialEmulator.clearOutputBuffer();
		serialEmulator.clearSentBuffer();
		{
			serialEmulator.receiveDataWithEndChar("{\"sr\":{\"coor\":0}}");
			assertEquals(CoordinateSystem.G53, g2core.getCurrentCoordinateSystem());
		}
		{
			serialEmulator.receiveDataWithEndChar("{\"sr\":{\"coor\":1}}");
			assertEquals(CoordinateSystem.G54, g2core.getCurrentCoordinateSystem());
		}
		{
			serialEmulator.receiveDataWithEndChar("{\"sr\":{\"coor\":2}}");
			assertEquals(CoordinateSystem.G55, g2core.getCurrentCoordinateSystem());
		}
		{
			serialEmulator.receiveDataWithEndChar("{\"sr\":{\"coor\":3}}");
			assertEquals(CoordinateSystem.G56, g2core.getCurrentCoordinateSystem());
		}
		{
			serialEmulator.receiveDataWithEndChar("{\"sr\":{\"coor\":4}}");
			assertEquals(CoordinateSystem.G57, g2core.getCurrentCoordinateSystem());
		}
		{
			serialEmulator.receiveDataWithEndChar("{\"sr\":{\"coor\":5}}");
			assertEquals(CoordinateSystem.G58, g2core.getCurrentCoordinateSystem());
		}
		{
			serialEmulator.receiveDataWithEndChar("{\"sr\":{\"coor\":6}}");
			assertEquals(CoordinateSystem.G59, g2core.getCurrentCoordinateSystem());
		}
	}
	
	/**
	 * Tests the various plane values in status report 
	 * @throws Exception Exception
	 */
	public void testStatusReportPlane() throws Exception{
		serialEmulator.clearOutputBuffer();
		serialEmulator.clearSentBuffer();
		{
			serialEmulator.receiveDataWithEndChar("{\"sr\":{\"plan\":0}}");
			assertEquals(EnumPlane.XY_PLANE, g2core.getGCodeContext().getPlane());
		}
		{
			serialEmulator.receiveDataWithEndChar("{\"sr\":{\"plan\":1}}");
			assertEquals(EnumPlane.XZ_PLANE, g2core.getGCodeContext().getPlane());
		}
		{
			serialEmulator.receiveDataWithEndChar("{\"sr\":{\"plan\":2}}");
			assertEquals(EnumPlane.YZ_PLANE, g2core.getGCodeContext().getPlane());
		}
	}
	
	/**
	 * Tests the various motion mode values in status report 
	 * @throws Exception Exception
	 */
	public void testStatusReportMotionMode() throws Exception{
		serialEmulator.clearOutputBuffer();
		serialEmulator.clearSentBuffer();
		{
			serialEmulator.receiveDataWithEndChar("{\"sr\":{\"momo\":0}}");
			assertEquals(EnumMotionMode.RAPID, g2core.getGCodeContext().getMotionMode());			
		}
		{
			serialEmulator.receiveDataWithEndChar("{\"sr\":{\"momo\":1}}");
			assertEquals(EnumMotionMode.FEEDRATE, g2core.getGCodeContext().getMotionMode());			
		}
		{
			serialEmulator.receiveDataWithEndChar("{\"sr\":{\"momo\":2}}");
			assertEquals(EnumMotionMode.ARC_CLOCKWISE, g2core.getGCodeContext().getMotionMode());			
		}
		{
			serialEmulator.receiveDataWithEndChar("{\"sr\":{\"momo\":3}}");
			assertEquals(EnumMotionMode.ARC_COUNTERCLOCKWISE, g2core.getGCodeContext().getMotionMode());			
		}
	}
	
	/**
	 * Tests the machine position values in status report 
	 * @throws Exception Exception
	 */
	public void testStatusReportMachinePosition() throws Exception{
		serialEmulator.clearOutputBuffer();
		serialEmulator.clearSentBuffer();
		{
			serialEmulator.receiveDataWithEndChar("{\"sr\":{\"mpox\":-10,\"mpoy\":20,\"mpoz\":35.53}}");
			Tuple6b expectedPosition = new Tuple6b(Length.valueOf("-10", g2core.getCurrentUnit()),
													Length.valueOf("20", g2core.getCurrentUnit()),
													Length.valueOf("35.53", g2core.getCurrentUnit()));
			assertEquals(expectedPosition, g2core.getGCodeContext().getMachinePosition());		
		}
	}
	
	/**
	 * Tests the work position values in status report 
	 * @throws Exception Exception
	 */
	public void testStatusReportWorkPosition() throws Exception{
		serialEmulator.clearOutputBuffer();
		serialEmulator.clearSentBuffer();
		{
			serialEmulator.receiveDataWithEndChar("{\"sr\":{\"posx\":-10,\"posy\":20,\"posz\":35.53}}");
			Tuple6b expectedPosition = new Tuple6b(Length.valueOf("-10", g2core.getCurrentUnit()),
													Length.valueOf("20", g2core.getCurrentUnit()),
													Length.valueOf("35.53", g2core.getCurrentUnit()));
			assertEquals(expectedPosition, g2core.getGCodeContext().getPosition());		
		}
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	public void testResetCurrentCoordinateSystem() throws Exception{		
		serialEmulator.clearOutputBuffer();
		serialEmulator.clearSentBuffer();			

		serialEmulator.receiveDataWithEndChar("{\"r\":{\"sr\":{\"posx\":15.031,\"posy\":35.000,\"posz\":-16.031}},\"f\":[1,0,0,0]}");
		// Enable G55 
		serialEmulator.receiveDataWithEndChar("{\"r\":{\"sr\":{\"coor\":2}},\"f\":[1,0,0,0]}"); 
			
		assertEquals(CoordinateSystem.G55, g2core.getCurrentCoordinateSystem());
		g2core.resetCurrentCoordinateSystem();		
		
		AssertSerialEmulator.assertOutputMessagePresent(serialEmulator, "{\"G55\":{\"X\":15.031,\"Y\":35,\"Z\":-16.031}}"+'\n', 1000);
	}
	
	/* ************************************************
	 *  Jogging tests
	 * ************************************************/
	
	
	public void testJogStartRelative() throws Exception{
		serialEmulator.clearOutputBuffer();
		serialEmulator.clearSentBuffer();			

		serialEmulator.receiveDataWithEndChar("{\"r\":{\"sr\":{\"posx\":15.031,\"posy\":35.000,\"posz\":-16.031, \"dist\":1, \"stat\":1}},\"f\":[1,0,0,0]}");
		serialEmulator.receiveDataWithEndChar("{\"qr\":48,\"qi\":1,\"qo\":1}");
				
		GCodeContext context = g2core.getGCodeContext();
		assertEquals(EnumDistanceMode.RELATIVE, context.getDistanceMode());

		
		g2core.jog(EnumControllerAxis.X_POSITIVE, Length.valueOf(BigDecimal.ONE, LengthUnit.MILLIMETRE), Speed.valueOf("1000", SpeedUnit.MILLIMETRE_PER_MINUTE));		
		AssertSerialEmulator.assertOutputMessagePresent(serialEmulator, "G91G1F1000X1.000\n",1000);
		
		
		g2core.jog(EnumControllerAxis.X_NEGATIVE, Length.valueOf(BigDecimal.ONE, LengthUnit.MILLIMETRE), Speed.valueOf("1000", SpeedUnit.MILLIMETRE_PER_MINUTE));		
		AssertSerialEmulator.assertOutputMessagePresent(serialEmulator, "G91G1F1000X-1.000\n",1000);
		
		
		g2core.jog(EnumControllerAxis.Y_POSITIVE, Length.valueOf("0.01", LengthUnit.MILLIMETRE), Speed.valueOf("1000", SpeedUnit.MILLIMETRE_PER_MINUTE));		
		AssertSerialEmulator.assertOutputMessagePresent(serialEmulator, "G91G1F1000Y0.010\n",1000);
		
		
		g2core.jog(EnumControllerAxis.Y_NEGATIVE, Length.valueOf("0.01", LengthUnit.MILLIMETRE), Speed.valueOf("1000", SpeedUnit.MILLIMETRE_PER_MINUTE));		
		AssertSerialEmulator.assertOutputMessagePresent(serialEmulator, "G91G1F1000Y-0.010\n",1000);
		
		
		g2core.jog(EnumControllerAxis.Z_POSITIVE, Length.valueOf("2.035", LengthUnit.MILLIMETRE), Speed.valueOf("1000", SpeedUnit.MILLIMETRE_PER_MINUTE));		
		AssertSerialEmulator.assertOutputMessagePresent(serialEmulator, "G91G1F1000Z2.035\n",1000);
		
		
		g2core.jog(EnumControllerAxis.Z_NEGATIVE, Length.valueOf(BigDecimal.ONE, LengthUnit.MILLIMETRE), Speed.valueOf("1000", SpeedUnit.MILLIMETRE_PER_MINUTE));		
		AssertSerialEmulator.assertOutputMessagePresent(serialEmulator, "G91G1F1000Z-1.000\n",1000);
		
	}
	/** (inheritDoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		g2core.stop();
	};
}

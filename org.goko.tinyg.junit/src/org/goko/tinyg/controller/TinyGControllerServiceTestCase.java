/**
 * 
 */
package org.goko.tinyg.controller;

import java.math.BigDecimal;

import junit.framework.TestCase;

import org.goko.core.common.exception.GkFunctionalException;
import org.goko.core.gcode.bean.IGCodeProvider;
import org.goko.gcode.rs274ngcv3.RS274GCodeService;
import org.goko.junit.tools.assertion.AssertGkFunctionalException;
import org.goko.junit.tools.connection.AssertSerialEmulator;
import org.goko.junit.tools.connection.SerialConnectionEmulator;
import org.goko.tinyg.controller.configuration.TinyGConfiguration;

/*
 
  {"r":{"fv":0.970,"fb":435.10,"hp":1,"hv":8,"id":"9H3583-PXN","msg":"SYSTEM READY"},"f":[1,0,0,3412]}
{"qr":28,"qi":1,"qo":1}
{"r":{"sr":{"line":0,"posx":0.000,"posy":0.000,"posz":0.000,"posa":0.000,"feed":0.00,"vel":0.00,"unit":1,"coor":1,"dist":0,"frmo":1,"momo":4,"stat":1}},"f":[1,0,10,8936]}
{"sr":{"line":0,"posx":0.000,"posy":0.000,"posz":0.000,"posa":0.000,"feed":0.00,"vel":0.00,"unit":1,"coor":1,"dist":0,"frmo":1,"momo":4,"stat":1}}
{"r":{"qr":28},"f":[1,0,10,8758]}
{"r":{"1":{"ma":0,"sa":0.900,"tr":32.0000,"mi":8,"po":1,"pm":1}},"f":[1,0,9,8694]}
{"r":{"2":{"ma":1,"sa":0.900,"tr":32.0000,"mi":8,"po":1,"pm":1}},"f":[1,0,9,5984]}
{"r":{"3":{"ma":2,"sa":1.800,"tr":2.1170,"mi":8,"po":0,"pm":1}},"f":[1,0,9,3553]}
{"r":{"4":{"ma":3,"sa":1.800,"tr":75.0000,"mi":8,"po":1,"pm":1}},"f":[1,0,9,8385]}
{"r":{"x":{"am":1,"vm":1500,"fr":1500,"tn":0.000,"tm":300.000,"jm":5000,"jh":30000,"jd":0.0100,"sn":0,"sx":1,"sv":1000,"lv":100,"lb":20.000,"zb":3.000}},"f":[1,0,9,1167]}
{"r":{"y":{"am":1,"vm":1500,"fr":1500,"tn":0.000,"tm":220.000,"jm":5000,"jh":30000,"jd":0.0100,"sn":0,"sx":1,"sv":1000,"lv":100,"lb":20.000,"zb":3.000}},"f":[1,0,9,716]}
{"r":{"z":{"am":1,"vm":1000,"fr":1000,"tn":0.000,"tm":100.000,"jm":50,"jh":1000,"jd":0.0100,"sn":1,"sx":0,"sv":800,"lv":100,"lb":20.000,"zb":10.000}},"f":[1,0,9,6257]}
{"r":{"a":{"am":1,"vm":48000,"fr":48000,"tn":-1.000,"tm":-1.000,"jm":24000,"jh":24000,"jd":0.1000,"ra":0.000,"sn":0,"sx":0,"sv":6000,"lv":1000,"lb":5.000,"zb":2.000}},"f":[1,0,9,7419]}
{"r":{"sys":{"fb":435.10,"fv":0.970,"hp":1,"hv":8,"id":"9H3583-PXN","ja":2000000,"ct":0.0100,"sl":0,"st":1,"mt":180.00,"ej":1,"jv":5,"js":1,"tv":1,"qv":2,"sv":1,"si":100,"ec":0,"ee":0,"ex":2,"baud":5,"net":0,"gpl":0,"gun":1,"gco":1,"gpa":0,"gdi":0}},"f":[1,0,11,346]}
{"r":{"g55":{"x":-164.127,"y":-208.999,"z":53.778,"a":0.000,"b":0.000,"c":0.000}},"f":[1,0,11,1231]}
{"r":{"g56":{"x":-84.739,"y":-103.984,"z":-10.000,"a":0.000,"b":0.000,"c":0.000}},"f":[1,0,11,3338]}
{"r":{"g57":{"x":0.000,"y":0.000,"z":0.000,"a":0.000,"b":0.000,"c":0.000}},"f":[1,0,11,8386]}
{"r":{"g58":{"x":-8.931,"y":9.450,"z":0.000,"a":0.000,"b":0.000,"c":0.000}},"f":[1,0,11,2282]}
{"r":{"g59":{"x":0.434,"y":17.316,"z":0.000,"a":0.000,"b":0.000,"c":0.000}},"f":[1,0,11,4046]}
 
 */
public class TinyGControllerServiceTestCase extends TestCase {
	private TinyGControllerService 	 tinyg;
	private SerialConnectionEmulator serialEmulator;
	private RS274GCodeService gcodeService;
	
	/** {@inheritDoc}
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {		
		super.setUp();
		serialEmulator = new SerialConnectionEmulator();		
		tinyg = new TinyGControllerService();		
		tinyg.setConnectionService(serialEmulator);
		gcodeService = new RS274GCodeService();
		tinyg.setGCodeService(gcodeService);
		tinyg.start(); 				  // Start the TinyG service
		serialEmulator.connect(null); // Make sure the service is connected
		tinyg.setPlannerBufferSpaceCheck(false);
	}
	
	/**
	 * Simple test for configuration update
	 * @throws Exception Exception
	 */
	public void testConfigurationUpdate() throws Exception{	
		BigDecimal queueReportVerbosity = tinyg.getConfiguration().getSetting(TinyGConfiguration.QUEUE_REPORT_VERBOSITY, BigDecimal.class);
		assertEquals(BigDecimal.ZERO, queueReportVerbosity);
		
		serialEmulator.receiveData("{\"r\":{\"qv\":2},\"f\":[1,0,10,4252]}"+'\n');
		BigDecimal queueReportVerbosityUpdated = tinyg.getConfiguration().getSetting(TinyGConfiguration.QUEUE_REPORT_VERBOSITY, BigDecimal.class);
		assertEquals(new BigDecimal("2"), queueReportVerbosityUpdated);
	}
	
	/**
	 * Context : TinyG has no motion control enabled and we try to run a motion
	 * Result  : TinyG throws an exception. Motion control is required 
	 * @throws Exception
	 */
	public void testNoMotionControlEnabled() throws Exception{
		// Emulate the reception of the GCode context
		serialEmulator.receiveDataWithEndChar("{\"r\":{\"sr\":{\"line\":0,\"posx\":0.000,\"posy\":0.000,\"posz\":0.000,\"posa\":0.000,\"feed\":0.00,\"vel\":0.00,\"unit\":1,\"coor\":1,\"dist\":0,\"frmo\":1,\"momo\":4,\"stat\":1}},\"f\":[1,0,0,0]}");
		serialEmulator.receiveDataWithEndChar("{\"r\":{\"ex\":2},\"f\":[1,0,0,0]}");
		
		tinyg.setPlannerBufferSpaceCheck(true);
		// Let's check that flow control is enabled
		assertEquals(new BigDecimal("2"), tinyg.getConfiguration().getSetting(TinyGConfiguration.ENABLE_FLOW_CONTROL, BigDecimal.class));
		
		IGCodeProvider motionCode = gcodeService.parse("G90X10Y10", tinyg.getCurrentGCodeContext());
		try{
			tinyg.executeGCode(motionCode);
			fail();
		}catch(GkFunctionalException e){
			AssertGkFunctionalException.assertException(e, "TNG-002");
		}
	}
	
	/**
	 * Context : TinyG has no flow control enabled and we try to run a motion
	 * Result  : TinyG throws an exception. Flow control is required 
	 * @throws Exception
	 */
	public void testNoFlowControlEnabled() throws Exception{
		// Emulate the reception of the GCode context
		serialEmulator.receiveDataWithEndChar("{\"r\":{\"sr\":{\"line\":0,\"posx\":0.000,\"posy\":0.000,\"posz\":0.000,\"posa\":0.000,\"feed\":0.00,\"vel\":0.00,\"unit\":1,\"coor\":1,\"dist\":0,\"frmo\":1,\"momo\":4,\"stat\":1}},\"f\":[1,0,0,0]}");
		serialEmulator.receiveDataWithEndChar("{\"r\":{\"ex\":0},\"f\":[1,0,0,0]}");
		
		tinyg.setPlannerBufferSpaceCheck(true);
		// Let's check that flow control is enabled
		assertEquals(new BigDecimal("0"), tinyg.getConfiguration().getSetting(TinyGConfiguration.ENABLE_FLOW_CONTROL, BigDecimal.class));
		
		IGCodeProvider motionCode = gcodeService.parse("G90X10Y10", tinyg.getCurrentGCodeContext());
		try{
			tinyg.executeGCode(motionCode);
			fail();
		}catch(GkFunctionalException e){
			AssertGkFunctionalException.assertException(e, "TNG-001");
		}
	}
	
	/**
	 * Context : We try to move to the given absolute position
	 * Result  : TinyG performs the motion 
	 * @throws Exception
	 */
	public void testMoveToAbsolutePosition() throws Exception{	
		
		// Emulate the reception of the GCode context
		serialEmulator.clearOutputBuffer();
		serialEmulator.clearSentBuffer();
		serialEmulator.receiveDataWithEndChar("{\"r\":{\"sr\":{\"line\":0,\"posx\":0.000,\"posy\":0.000,\"posz\":0.000,\"posa\":0.000,\"feed\":0.00,\"vel\":0.00,\"unit\":1,\"coor\":1,\"dist\":0,\"frmo\":1,\"momo\":4,\"stat\":1}},\"f\":[1,0,0,0]}");
		serialEmulator.receiveDataWithEndChar("{\"r\":{\"ex\":2},\"f\":[1,0,0,0]}");		
		serialEmulator.receiveDataWithEndChar("{\"r\":{\"qv\":2},\"f\":[1,0,0,0]}");
		
		IGCodeProvider motionCode = gcodeService.parse("G90X10Y10", tinyg.getCurrentGCodeContext());		
		tinyg.executeGCode(motionCode);	
		AssertSerialEmulator.assertMessagePresent(serialEmulator, "{\"gc\":\"n1 g90 x10 y10\"}"+'\n');				
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	public void testUnsupportedAction() throws Exception{	
		try{
			tinyg.getControllerAction("INCONNU");
		}catch(GkFunctionalException e){
			AssertGkFunctionalException.assertException(e, "TNG-004", "INCONNU", "TinyG Controller");
		}
	}
	
	/** (inheritDoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		tinyg.stop();
	};
}

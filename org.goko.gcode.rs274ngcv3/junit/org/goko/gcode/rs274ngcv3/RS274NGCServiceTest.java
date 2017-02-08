/**
 * 
 */
package org.goko.gcode.rs274ngcv3;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.goko.core.common.measure.quantity.Angle;
import org.goko.core.common.measure.quantity.AngleUnit;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.quantity.LengthUnit;
import org.goko.core.common.measure.quantity.Speed;
import org.goko.core.common.measure.quantity.SpeedUnit;
import org.goko.core.gcode.element.GCodeLine;
import org.goko.core.gcode.element.GCodeWord;
import org.goko.core.gcode.element.IGCodeProvider;
import org.goko.core.gcode.rs274ngcv3.RS274NGCServiceImpl;
import org.goko.core.gcode.rs274ngcv3.context.CoordinateSystem;
import org.goko.core.gcode.rs274ngcv3.context.EnumDistanceMode;
import org.goko.core.gcode.rs274ngcv3.context.EnumMotionControl;
import org.goko.core.gcode.rs274ngcv3.context.EnumMotionMode;
import org.goko.core.gcode.rs274ngcv3.context.EnumPlane;
import org.goko.core.gcode.rs274ngcv3.context.EnumSpindleMode;
import org.goko.core.gcode.rs274ngcv3.context.EnumUnit;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.InstructionProvider;
import org.goko.core.gcode.rs274ngcv3.element.InstructionSet;
import org.goko.core.gcode.rs274ngcv3.instruction.ArcFeedInstruction;
import org.goko.core.gcode.rs274ngcv3.instruction.ChangeToolInstruction;
import org.goko.core.gcode.rs274ngcv3.instruction.DwellInstruction;
import org.goko.core.gcode.rs274ngcv3.instruction.FloodOffInstruction;
import org.goko.core.gcode.rs274ngcv3.instruction.FloodOnInstruction;
import org.goko.core.gcode.rs274ngcv3.instruction.MistOnInstruction;
import org.goko.core.gcode.rs274ngcv3.instruction.OriginOffsetsOffInstruction;
import org.goko.core.gcode.rs274ngcv3.instruction.OriginOffsetsOnInstruction;
import org.goko.core.gcode.rs274ngcv3.instruction.SelectPlaneInstruction;
import org.goko.core.gcode.rs274ngcv3.instruction.SelectToolInstruction;
import org.goko.core.gcode.rs274ngcv3.instruction.SetCoordinateSystemInstruction;
import org.goko.core.gcode.rs274ngcv3.instruction.SetDistanceModeInstruction;
import org.goko.core.gcode.rs274ngcv3.instruction.SetFeedRateInstruction;
import org.goko.core.gcode.rs274ngcv3.instruction.SetMotionControlModeInstruction;
import org.goko.core.gcode.rs274ngcv3.instruction.SetOriginOffsetInstruction;
import org.goko.core.gcode.rs274ngcv3.instruction.SetSpindleSpeedInstruction;
import org.goko.core.gcode.rs274ngcv3.instruction.StartSpindleClockwiseInstruction;
import org.goko.core.gcode.rs274ngcv3.instruction.StartSpindleCounterClockwiseInstruction;
import org.goko.core.gcode.rs274ngcv3.instruction.StopSpindleTurningInstruction;
import org.goko.core.gcode.rs274ngcv3.instruction.StraightFeedInstruction;
import org.goko.core.gcode.rs274ngcv3.instruction.StraightTraverseInstruction;
import org.goko.core.gcode.rs274ngcv3.instruction.UserLengthUnitsInstruction;
import org.goko.gcode.rs274ngcv3.assertion.AssertGCodeLine;
import org.goko.gcode.rs274ngcv3.assertion.AssertGCodeProvider;
import org.goko.gcode.rs274ngcv3.assertion.AssertInstructionProvider;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Psyko
 * @date 30 juin 2016
 */
public class RS274NGCServiceTest {
	/** Tested service */
	private RS274NGCServiceImpl service;
	
	@Before
	public void setUp(){
		service = new RS274NGCServiceImpl();
	}
		
	@Test
	public void testParseString() throws Exception{
		IGCodeProvider provider = service.parse("N12G90X12.5Y36.8Z45.3F100");
		AssertGCodeProvider.assertLineCount(provider, 1);
		
		AssertGCodeLine.assertExactWords(provider.getLineAtIndex(0), 	new GCodeWord("N","12"),
																		new GCodeWord("G","90"),
																		new GCodeWord("X","12.5"),
																		new GCodeWord("Y","36.8"),
																		new GCodeWord("Z","45.3"),
																		new GCodeWord("F","100"));		
	}
	
	@Test
	public void testMixedInstruction() throws Exception{
		IGCodeProvider provider = service.parse("N12G90X12.5Y36.8Z45.3F100");
		AssertGCodeProvider.assertLineCount(provider, 1);
		GCodeLine line = provider.getLineAtIndex(0);
		
		GCodeContext context = new GCodeContext();
		context.setA(Angle.ZERO);
		context.setB(Angle.ZERO);
		context.setC(Angle.ZERO);
		context.setUnit(EnumUnit.MILLIMETERS);
		
		InstructionProvider instructionProvider = service.getInstructions(context, provider);
		InstructionSet set = new InstructionSet();
		
		SetFeedRateInstruction feedrateInstr = new SetFeedRateInstruction(Speed.valueOf(100, SpeedUnit.MILLIMETRE_PER_MINUTE));
		feedrateInstr.setIdGCodeLine(line.getId());
		set.addInstruction(feedrateInstr);
		
		SetDistanceModeInstruction distanceInstr = new SetDistanceModeInstruction(EnumDistanceMode.ABSOLUTE);
		distanceInstr.setIdGCodeLine(line.getId());
		set.addInstruction(distanceInstr);
		
		StraightTraverseInstruction traverserInstr = new StraightTraverseInstruction(Length.valueOf("12.5", LengthUnit.MILLIMETRE), Length.valueOf("36.8", LengthUnit.MILLIMETRE),Length.valueOf("45.3", LengthUnit.MILLIMETRE), null, null, null); 
		traverserInstr.setIdGCodeLine(line.getId());
		set.addInstruction(traverserInstr);
				
		AssertInstructionProvider.assertContainsInstructionSet(instructionProvider, set);
	}
	
	@Test
	public void testArcFeedClockwiseInstruction() throws Exception{
		IGCodeProvider provider = service.parse("G2 X10 Y12 Z-8.32 I5 J6");
		AssertGCodeProvider.assertLineCount(provider, 1);
		GCodeLine line = provider.getLineAtIndex(0);
		
		GCodeContext context = new GCodeContext();
		context.setA(Angle.ZERO);
		context.setB(Angle.ZERO);
		context.setC(Angle.ZERO);
		context.setX(Length.ZERO);
		context.setY(Length.ZERO);
		context.setZ(Length.ZERO);
		context.setUnit(EnumUnit.MILLIMETERS);
		context.setMotionMode(EnumMotionMode.ARC_COUNTERCLOCKWISE);
		InstructionProvider instructionProvider = service.getInstructions(context, provider);
		InstructionSet set = new InstructionSet();
		
		ArcFeedInstruction instr = new ArcFeedInstruction( Length.valueOf("10", LengthUnit.MILLIMETRE),
															Length.valueOf("12", LengthUnit.MILLIMETRE),
															Length.valueOf("-8.32", LengthUnit.MILLIMETRE),
															Length.valueOf("5", LengthUnit.MILLIMETRE),
															Length.valueOf("6", LengthUnit.MILLIMETRE),															
															null,
															null,
															null,
															null,
															1,
															true);
		instr.setIdGCodeLine(line.getId());
		set.addInstruction(instr);
		
		AssertInstructionProvider.assertContainsInstructionSet(instructionProvider, set);
		
		// Context update check 
		GCodeContext postContext = service.update(context, instr);
		assertEquals( Length.valueOf("10", LengthUnit.MILLIMETRE) , postContext.getX() );
		assertEquals( Length.valueOf("12", LengthUnit.MILLIMETRE) , postContext.getY() );
		assertEquals( Length.valueOf("-8.32", LengthUnit.MILLIMETRE)  , postContext.getZ() );
		assertEquals(  EnumMotionMode.ARC_CLOCKWISE, postContext.getMotionMode());
		
		
	}
	
	@Test
	public void testArcFeedCounterClockwiseInstruction() throws Exception{
		IGCodeProvider provider = service.parse("G3 X10 Y12 Z42 I5 J6");
		AssertGCodeProvider.assertLineCount(provider, 1);
		GCodeLine line = provider.getLineAtIndex(0);
		
		GCodeContext context = new GCodeContext();
		context.setA(Angle.ZERO);
		context.setB(Angle.ZERO);
		context.setC(Angle.ZERO);
		context.setX(Length.ZERO);
		context.setY(Length.ZERO);
		context.setZ(Length.ZERO);
		context.setUnit(EnumUnit.MILLIMETERS);
		context.setMotionMode(EnumMotionMode.ARC_CLOCKWISE);
		
		InstructionProvider instructionProvider = service.getInstructions(context, provider);
		InstructionSet set = new InstructionSet();
		
		ArcFeedInstruction instr = new ArcFeedInstruction( Length.valueOf("10", LengthUnit.MILLIMETRE), 
															Length.valueOf("12", LengthUnit.MILLIMETRE),															 
															Length.valueOf("42", LengthUnit.MILLIMETRE),
															Length.valueOf("5", LengthUnit.MILLIMETRE), 
															Length.valueOf("6", LengthUnit.MILLIMETRE),
															null,
															null,
															null,
															null,
															1, 
															false);
		instr.setIdGCodeLine(line.getId());
		set.addInstruction(instr);
					
		AssertInstructionProvider.assertContainsInstructionSet(instructionProvider, set);
		
		// Context update check 
		GCodeContext postContext = service.update(context, instr);
		assertEquals( Length.valueOf("10", LengthUnit.MILLIMETRE) , postContext.getX() );
		assertEquals( Length.valueOf("12", LengthUnit.MILLIMETRE) , postContext.getY() );
		assertEquals( Length.valueOf("42", LengthUnit.MILLIMETRE)  , postContext.getZ() );
		assertEquals(  EnumMotionMode.ARC_COUNTERCLOCKWISE, postContext.getMotionMode());
	}
	
	
	@Test
	public void testStraightFeedInstruction() throws Exception{
		IGCodeProvider provider = service.parse("G1 X10 Y12 Z5 A-23.5 B45.3 C-0.001");
		AssertGCodeProvider.assertLineCount(provider, 1);
		GCodeLine line = provider.getLineAtIndex(0);
		
		GCodeContext context = new GCodeContext();
		context.setA(Angle.ZERO);
		context.setB(Angle.ZERO);
		context.setC(Angle.ZERO);
		context.setX(Length.ZERO);
		context.setY(Length.ZERO);
		context.setZ(Length.ZERO);
		context.setUnit(EnumUnit.MILLIMETERS);
		context.setMotionMode(EnumMotionMode.ARC_CLOCKWISE);
		
		InstructionProvider instructionProvider = service.getInstructions(context, provider);
		InstructionSet set = new InstructionSet();
		
		StraightFeedInstruction instr = new StraightFeedInstruction( Length.valueOf("10", LengthUnit.MILLIMETRE),
																	 Length.valueOf("12", LengthUnit.MILLIMETRE),
																	 Length.valueOf("5", LengthUnit.MILLIMETRE),
																	 Angle.valueOf("-23.5", AngleUnit.DEGREE_ANGLE),
																	 Angle.valueOf("45.3", AngleUnit.DEGREE_ANGLE),
																	 Angle.valueOf("-0.001", AngleUnit.DEGREE_ANGLE));
		instr.setIdGCodeLine(line.getId());
		set.addInstruction(instr);
					
		AssertInstructionProvider.assertContainsInstructionSet(instructionProvider, set);
		
		// Context update check 
		GCodeContext postContext = service.update(context, instr);
		assertEquals( Length.valueOf("10", LengthUnit.MILLIMETRE) , postContext.getX() );
		assertEquals( Length.valueOf("12", LengthUnit.MILLIMETRE) , postContext.getY() );
		assertEquals( Length.valueOf("5", LengthUnit.MILLIMETRE)  , postContext.getZ() );
		assertEquals( Angle.valueOf("-23.5", AngleUnit.DEGREE_ANGLE)  , postContext.getA() );
		assertEquals( Angle.valueOf("45.3", AngleUnit.DEGREE_ANGLE)  , postContext.getB() );
		assertEquals( Angle.valueOf("-0.001", AngleUnit.DEGREE_ANGLE)  , postContext.getC() );
		assertEquals(  EnumMotionMode.FEEDRATE, postContext.getMotionMode());
	}
	
	@Test
	public void testStraightTraverseInstruction() throws Exception{
		IGCodeProvider provider = service.parse("G0 X10 Y12 Z5 A-23.5 B45.3 C-0.001");
		AssertGCodeProvider.assertLineCount(provider, 1);
		GCodeLine line = provider.getLineAtIndex(0);
		
		GCodeContext context = new GCodeContext();
		context.setA(Angle.ZERO);
		context.setB(Angle.ZERO);
		context.setC(Angle.ZERO);
		context.setX(Length.ZERO);
		context.setY(Length.ZERO);
		context.setZ(Length.ZERO);
		context.setUnit(EnumUnit.MILLIMETERS);
		context.setMotionMode(EnumMotionMode.ARC_CLOCKWISE);
		
		InstructionProvider instructionProvider = service.getInstructions(context, provider);
		InstructionSet set = new InstructionSet();
		
		StraightTraverseInstruction instr = new StraightTraverseInstruction( Length.valueOf("10", LengthUnit.MILLIMETRE),
																	 Length.valueOf("12", LengthUnit.MILLIMETRE),
																	 Length.valueOf("5", LengthUnit.MILLIMETRE),
																	 Angle.valueOf("-23.5", AngleUnit.DEGREE_ANGLE),
																	 Angle.valueOf("45.3", AngleUnit.DEGREE_ANGLE),
																	 Angle.valueOf("-0.001", AngleUnit.DEGREE_ANGLE));
		instr.setIdGCodeLine(line.getId());
		set.addInstruction(instr);
					
		AssertInstructionProvider.assertContainsInstructionSet(instructionProvider, set);
		
		// Context update check 
		GCodeContext postContext = service.update(context, instr);
		assertEquals( Length.valueOf("10", LengthUnit.MILLIMETRE) , postContext.getX() );
		assertEquals( Length.valueOf("12", LengthUnit.MILLIMETRE) , postContext.getY() );
		assertEquals( Length.valueOf("5", LengthUnit.MILLIMETRE)  , postContext.getZ() );
		assertEquals( Angle.valueOf("-23.5", AngleUnit.DEGREE_ANGLE)  , postContext.getA() );
		assertEquals( Angle.valueOf("45.3", AngleUnit.DEGREE_ANGLE)  , postContext.getB() );
		assertEquals( Angle.valueOf("-0.001", AngleUnit.DEGREE_ANGLE)  , postContext.getC() );
		assertEquals(  EnumMotionMode.RAPID, postContext.getMotionMode());
	}
	
	@Test
	public void testUserLengthInchesInstruction() throws Exception{
		IGCodeProvider provider = service.parse("G20");
		AssertGCodeProvider.assertLineCount(provider, 1);
		GCodeLine line = provider.getLineAtIndex(0);
		
		GCodeContext context = new GCodeContext();
		context.setUnit(EnumUnit.MILLIMETERS);
				
		InstructionProvider instructionProvider = service.getInstructions(context, provider);
		InstructionSet set = new InstructionSet();
		
		UserLengthUnitsInstruction instr = new UserLengthUnitsInstruction( EnumUnit.INCHES );
		instr.setIdGCodeLine(line.getId());
		set.addInstruction(instr);
					
		AssertInstructionProvider.assertContainsInstructionSet(instructionProvider, set);
		
		// Context update check 
		GCodeContext postContext = service.update(context, instr);
		assertEquals(EnumUnit.INCHES,  postContext.getUnit());		
	}
	
	@Test
	public void testUserLengthMmInstruction() throws Exception{
		IGCodeProvider provider = service.parse("G21");
		AssertGCodeProvider.assertLineCount(provider, 1);
		GCodeLine line = provider.getLineAtIndex(0);
		
		GCodeContext context = new GCodeContext();
		context.setUnit(EnumUnit.INCHES);
		
		InstructionProvider instructionProvider = service.getInstructions(context, provider);
		InstructionSet set = new InstructionSet();
		
		UserLengthUnitsInstruction instr = new UserLengthUnitsInstruction( EnumUnit.MILLIMETERS );
		instr.setIdGCodeLine(line.getId());
		set.addInstruction(instr);
					
		AssertInstructionProvider.assertContainsInstructionSet(instructionProvider, set);
		
		// Context update check 
		GCodeContext postContext = service.update(context, instr);
		assertEquals(EnumUnit.MILLIMETERS,  postContext.getUnit());	
	}
	
	@Test
	public void testSelectPlaneXyInstruction() throws Exception{
		IGCodeProvider provider = service.parse("G17");
		AssertGCodeProvider.assertLineCount(provider, 1);
		GCodeLine line = provider.getLineAtIndex(0);
		
		GCodeContext context = new GCodeContext();
		context.setPlane(EnumPlane.XZ_PLANE);
		
		InstructionProvider instructionProvider = service.getInstructions(context, provider);
		InstructionSet set = new InstructionSet();
		
		SelectPlaneInstruction instr = new SelectPlaneInstruction( EnumPlane.XY_PLANE );
		instr.setIdGCodeLine(line.getId());
		set.addInstruction(instr);
					
		AssertInstructionProvider.assertContainsInstructionSet(instructionProvider, set);
		
		// Context update check 
		GCodeContext postContext = service.update(context, instr);
		assertEquals(EnumPlane.XY_PLANE,  postContext.getPlane());	
	}
	
	@Test
	public void testSelectPlaneXzInstruction() throws Exception{
		IGCodeProvider provider = service.parse("G18");
		AssertGCodeProvider.assertLineCount(provider, 1);
		GCodeLine line = provider.getLineAtIndex(0);
		
		GCodeContext context = new GCodeContext();
		context.setPlane(EnumPlane.XY_PLANE);
		
		InstructionProvider instructionProvider = service.getInstructions(context, provider);
		InstructionSet set = new InstructionSet();
		
		SelectPlaneInstruction instr = new SelectPlaneInstruction( EnumPlane.XZ_PLANE );
		instr.setIdGCodeLine(line.getId());
		set.addInstruction(instr);
					
		AssertInstructionProvider.assertContainsInstructionSet(instructionProvider, set);
		
		// Context update check 
		GCodeContext postContext = service.update(context, instr);
		assertEquals(EnumPlane.XZ_PLANE,  postContext.getPlane());	
	}
	
	@Test
	public void testSelectPlaneYzInstruction() throws Exception{
		IGCodeProvider provider = service.parse("G19");
		AssertGCodeProvider.assertLineCount(provider, 1);
		GCodeLine line = provider.getLineAtIndex(0);
		
		GCodeContext context = new GCodeContext();
		context.setPlane(EnumPlane.XY_PLANE);
		
		InstructionProvider instructionProvider = service.getInstructions(context, provider);
		InstructionSet set = new InstructionSet();
		
		SelectPlaneInstruction instr = new SelectPlaneInstruction( EnumPlane.YZ_PLANE );
		instr.setIdGCodeLine(line.getId());
		set.addInstruction(instr);
					
		AssertInstructionProvider.assertContainsInstructionSet(instructionProvider, set);
		
		// Context update check 
		GCodeContext postContext = service.update(context, instr);
		assertEquals(EnumPlane.YZ_PLANE,  postContext.getPlane());	
	}
	
	@Test
	public void testSelectToolInstruction() throws Exception{
		IGCodeProvider provider = service.parse("T19");
		AssertGCodeProvider.assertLineCount(provider, 1);
		GCodeLine line = provider.getLineAtIndex(0);
		
		GCodeContext context = new GCodeContext();
		context.setSelectedToolNumber(32);
		context.setActiveToolNumber(21);
		
		InstructionProvider instructionProvider = service.getInstructions(context, provider);
		InstructionSet set = new InstructionSet();
		
		SelectToolInstruction instr = new SelectToolInstruction( 19 );
		instr.setIdGCodeLine(line.getId());
		set.addInstruction(instr);
					
		AssertInstructionProvider.assertContainsInstructionSet(instructionProvider, set);
		// Context update check 
		GCodeContext postContext = service.update(context, instr);
		assertEquals(new Integer(19),  postContext.getSelectedToolNumber());
		assertEquals(new Integer(21),  postContext.getActiveToolNumber()); // Make sure it didn't change the active tool
	}
	
	@Test
	public void testChangeToolInstruction() throws Exception{
		IGCodeProvider provider = service.parse("M6");
		AssertGCodeProvider.assertLineCount(provider, 1);
		GCodeLine line = provider.getLineAtIndex(0);
		
		GCodeContext context = new GCodeContext();
		context.setSelectedToolNumber(21);
		context.setActiveToolNumber(5);
		
		InstructionProvider instructionProvider = service.getInstructions(context, provider);
		InstructionSet set = new InstructionSet();
		
		ChangeToolInstruction instr = new ChangeToolInstruction();
		instr.setIdGCodeLine(line.getId());
		set.addInstruction(instr);
					
		AssertInstructionProvider.assertContainsInstructionSet(instructionProvider, set);
		// Context update check 
		GCodeContext postContext = service.update(context, instr);		
		assertEquals(new Integer(21),  postContext.getActiveToolNumber()); 
	}
	
	@Test
	public void testDwellInstruction() throws Exception{
		IGCodeProvider provider = service.parse("G4 P123");
		AssertGCodeProvider.assertLineCount(provider, 1);
		GCodeLine line = provider.getLineAtIndex(0);
		
		GCodeContext context = new GCodeContext();
		
		InstructionProvider instructionProvider = service.getInstructions(context, provider);
		InstructionSet set = new InstructionSet();
		
		DwellInstruction instr = new DwellInstruction(123);
		instr.setIdGCodeLine(line.getId());
		set.addInstruction(instr);
					
		AssertInstructionProvider.assertContainsInstructionSet(instructionProvider, set);
	}
	
	@Test
	public void testSetCoordinateSystemInstruction() throws Exception{
		IGCodeProvider provider = service.parse("G58");
		AssertGCodeProvider.assertLineCount(provider, 1);
		GCodeLine line = provider.getLineAtIndex(0);
		
		GCodeContext context = new GCodeContext();
		context.setCoordinateSystem(CoordinateSystem.G54);
		
		InstructionProvider instructionProvider = service.getInstructions(context, provider);
		InstructionSet set = new InstructionSet();
		
		SetCoordinateSystemInstruction instr = new SetCoordinateSystemInstruction(CoordinateSystem.G58);
		instr.setIdGCodeLine(line.getId());
		set.addInstruction(instr);
					
		AssertInstructionProvider.assertContainsInstructionSet(instructionProvider, set);
		
		// Context update check 
		GCodeContext postContext = service.update(context, instr);		
		assertEquals(CoordinateSystem.G58, postContext.getCoordinateSystem());
	}
	
	@Test
	public void testSetMotionControlModeInstruction() throws Exception{
		IGCodeProvider provider = service.parse("G64");
		AssertGCodeProvider.assertLineCount(provider, 1);
		GCodeLine line = provider.getLineAtIndex(0);
		
		GCodeContext context = new GCodeContext();
		context.setMotionControl(EnumMotionControl.EXACT_PATH);
		InstructionProvider instructionProvider = service.getInstructions(context, provider);
		InstructionSet set = new InstructionSet();
		
		SetMotionControlModeInstruction instr = new SetMotionControlModeInstruction(EnumMotionControl.CONTINUOUS);
		instr.setIdGCodeLine(line.getId());
		set.addInstruction(instr);
					
		AssertInstructionProvider.assertContainsInstructionSet(instructionProvider, set);
		// Context update check 
		GCodeContext postContext = service.update(context, instr);		
		assertEquals(EnumMotionControl.CONTINUOUS, postContext.getMotionControl());
	}

	@Test
	public void testSetOriginOffsetInstruction() throws Exception{
		IGCodeProvider provider = service.parse("G92 X10 Y12 Z5 A-23.5 B45.3 C-0.001");
		AssertGCodeProvider.assertLineCount(provider, 1);
		GCodeLine line = provider.getLineAtIndex(0);
		
		GCodeContext context = new GCodeContext();
		context.setA(Angle.ZERO);
		context.setB(Angle.ZERO);
		context.setC(Angle.ZERO);
		context.setX(Length.ZERO);
		context.setY(Length.ZERO);
		context.setZ(Length.ZERO);
		context.setUnit(EnumUnit.MILLIMETERS);
		
		InstructionProvider instructionProvider = service.getInstructions(context, provider);
		InstructionSet set = new InstructionSet();
		
		SetOriginOffsetInstruction instr = new SetOriginOffsetInstruction( Length.valueOf("10", LengthUnit.MILLIMETRE),
																	 Length.valueOf("12", LengthUnit.MILLIMETRE),
																	 Length.valueOf("5", LengthUnit.MILLIMETRE),
																	 Angle.valueOf("-23.5", AngleUnit.DEGREE_ANGLE),
																	 Angle.valueOf("45.3", AngleUnit.DEGREE_ANGLE),
																	 Angle.valueOf("-0.001", AngleUnit.DEGREE_ANGLE));
		instr.setIdGCodeLine(line.getId());
		set.addInstruction(instr);
					
		AssertInstructionProvider.assertContainsInstructionSet(instructionProvider, set);
	}
	
	@Test
	public void testSetFeedrateMmInstruction() throws Exception{
		IGCodeProvider provider = service.parse("F1000");
		AssertGCodeProvider.assertLineCount(provider, 1);
		GCodeLine line = provider.getLineAtIndex(0);
		
		GCodeContext context = new GCodeContext();		
		context.setUnit(EnumUnit.MILLIMETERS);
		context.setFeedrate(Speed.ZERO);
		
		InstructionProvider instructionProvider = service.getInstructions(context, provider);
		InstructionSet set = new InstructionSet();
		
		SetFeedRateInstruction instr = new SetFeedRateInstruction(Speed.valueOf(1000, SpeedUnit.MILLIMETRE_PER_MINUTE));
		instr.setIdGCodeLine(line.getId());
		set.addInstruction(instr);
					
		AssertInstructionProvider.assertContainsInstructionSet(instructionProvider, set);
		// Context update check 
		GCodeContext postContext = service.update(context, instr);		
		assertEquals(Speed.valueOf(1000, SpeedUnit.MILLIMETRE_PER_MINUTE), postContext.getFeedrate());
	}
	
	@Test
	public void testSetFeedrateInchInstruction() throws Exception{
		IGCodeProvider provider = service.parse("F1000");
		AssertGCodeProvider.assertLineCount(provider, 1);
		GCodeLine line = provider.getLineAtIndex(0);
		
		GCodeContext context = new GCodeContext();		
		context.setUnit(EnumUnit.INCHES);
		
		InstructionProvider instructionProvider = service.getInstructions(context, provider);
		InstructionSet set = new InstructionSet();
		context.setFeedrate(Speed.ZERO);
		
		SetFeedRateInstruction instr = new SetFeedRateInstruction(Speed.valueOf(1000, SpeedUnit.INCH_PER_MINUTE));
		instr.setIdGCodeLine(line.getId());
		set.addInstruction(instr);
					
		AssertInstructionProvider.assertContainsInstructionSet(instructionProvider, set);
		// Context update check 
		GCodeContext postContext = service.update(context, instr);		
		assertEquals(Speed.valueOf(1000, SpeedUnit.INCH_PER_MINUTE), postContext.getFeedrate());
	}
	
	@Test
	public void testSetSpindleSpeedInstruction() throws Exception{
		IGCodeProvider provider = service.parse("S1235");
		AssertGCodeProvider.assertLineCount(provider, 1);
		GCodeLine line = provider.getLineAtIndex(0);
		
		GCodeContext context = new GCodeContext();
		context.setSpindleSpeed(new BigDecimal(0));
		
		InstructionProvider instructionProvider = service.getInstructions(context, provider);
		InstructionSet set = new InstructionSet();
		
		SetSpindleSpeedInstruction instr = new SetSpindleSpeedInstruction(new BigDecimal("1235"));
		instr.setIdGCodeLine(line.getId());
		set.addInstruction(instr);
					
		AssertInstructionProvider.assertContainsInstructionSet(instructionProvider, set);
		// Context update check 
		GCodeContext postContext = service.update(context, instr);		
		assertEquals(new BigDecimal("1235"), postContext.getSpindleSpeed());
	}
	
	@Test
	public void testStopSpindleTurningInstruction() throws Exception{
		IGCodeProvider provider = service.parse("M5");
		AssertGCodeProvider.assertLineCount(provider, 1);
		GCodeLine line = provider.getLineAtIndex(0);
		
		GCodeContext context = new GCodeContext();
		context.setSpindleMode(EnumSpindleMode.ON_CLOCKWISE);
		InstructionProvider instructionProvider = service.getInstructions(context, provider);
		InstructionSet set = new InstructionSet();
		
		StopSpindleTurningInstruction instr = new StopSpindleTurningInstruction();
		instr.setIdGCodeLine(line.getId());
		set.addInstruction(instr);
					
		AssertInstructionProvider.assertContainsInstructionSet(instructionProvider, set);
		// Context update check 
		GCodeContext postContext = service.update(context, instr);
		assertEquals(EnumSpindleMode.OFF, context.getSpindleMode());
	}
	
	@Test
	public void testStartSpindleCounterClockwiseInstruction() throws Exception{
		IGCodeProvider provider = service.parse("M4");
		AssertGCodeProvider.assertLineCount(provider, 1);
		GCodeLine line = provider.getLineAtIndex(0);
		
		GCodeContext context = new GCodeContext();
		
		InstructionProvider instructionProvider = service.getInstructions(context, provider);
		InstructionSet set = new InstructionSet();
		context.setSpindleMode(EnumSpindleMode.OFF);
		
		StartSpindleCounterClockwiseInstruction instr = new StartSpindleCounterClockwiseInstruction();
		instr.setIdGCodeLine(line.getId());
		set.addInstruction(instr);
					
		AssertInstructionProvider.assertContainsInstructionSet(instructionProvider, set);
		// Context update check 
		GCodeContext postContext = service.update(context, instr);
		assertEquals(EnumSpindleMode.ON_COUNTERCLOCKWISE, context.getSpindleMode());
	}
	
	@Test
	public void testStartSpindleClockwiseInstruction() throws Exception{
		IGCodeProvider provider = service.parse("M3");
		AssertGCodeProvider.assertLineCount(provider, 1);
		GCodeLine line = provider.getLineAtIndex(0);
		
		GCodeContext context = new GCodeContext();
		context.setSpindleMode(EnumSpindleMode.OFF);
		
		InstructionProvider instructionProvider = service.getInstructions(context, provider);
		InstructionSet set = new InstructionSet();
		
		StartSpindleClockwiseInstruction instr = new StartSpindleClockwiseInstruction();
		instr.setIdGCodeLine(line.getId());
		set.addInstruction(instr);
					
		AssertInstructionProvider.assertContainsInstructionSet(instructionProvider, set);
		// Context update check 
		GCodeContext postContext = service.update(context, instr);
		assertEquals(EnumSpindleMode.ON_CLOCKWISE, context.getSpindleMode());		
	}
	
	@Test
	public void testFloodOnInstruction() throws Exception{
		IGCodeProvider provider = service.parse("M8");
		AssertGCodeProvider.assertLineCount(provider, 1);
		GCodeLine line = provider.getLineAtIndex(0);
		
		GCodeContext context = new GCodeContext();
		
		InstructionProvider instructionProvider = service.getInstructions(context, provider);
		InstructionSet set = new InstructionSet();
		
		FloodOnInstruction instr = new FloodOnInstruction();
		instr.setIdGCodeLine(line.getId());
		set.addInstruction(instr);
					
		AssertInstructionProvider.assertContainsInstructionSet(instructionProvider, set);
		// Context update check 
		GCodeContext postContext = service.update(context, instr);
		// TODO		
	}
	
	@Test
	public void testFloodOffInstruction() throws Exception{
		IGCodeProvider provider = service.parse("M9");
		AssertGCodeProvider.assertLineCount(provider, 1);
		GCodeLine line = provider.getLineAtIndex(0);
		
		GCodeContext context = new GCodeContext();
		
		InstructionProvider instructionProvider = service.getInstructions(context, provider);
		InstructionSet set = new InstructionSet();
		
		FloodOffInstruction instr = new FloodOffInstruction();
		instr.setIdGCodeLine(line.getId());
		set.addInstruction(instr);
					
		AssertInstructionProvider.assertContainsInstructionSet(instructionProvider, set);
		// Context update check 
		GCodeContext postContext = service.update(context, instr);
		// TODO		
	}
	
	@Test
	public void testMistOnInstruction() throws Exception{
		IGCodeProvider provider = service.parse("M7");
		AssertGCodeProvider.assertLineCount(provider, 1);
		GCodeLine line = provider.getLineAtIndex(0);
		
		GCodeContext context = new GCodeContext();
		
		InstructionProvider instructionProvider = service.getInstructions(context, provider);
		InstructionSet set = new InstructionSet();
		
		MistOnInstruction instr = new MistOnInstruction();
		instr.setIdGCodeLine(line.getId());
		set.addInstruction(instr);
					
		AssertInstructionProvider.assertContainsInstructionSet(instructionProvider, set);
		// Context update check 
		GCodeContext postContext = service.update(context, instr);
		// TODO		
	}
	
	@Test
	public void testOriginOffsetsOnInstruction() throws Exception{
		IGCodeProvider provider = service.parse("G92.3");
		AssertGCodeProvider.assertLineCount(provider, 1);
		GCodeLine line = provider.getLineAtIndex(0);
		
		GCodeContext context = new GCodeContext();
		
		InstructionProvider instructionProvider = service.getInstructions(context, provider);
		InstructionSet set = new InstructionSet();
		
		OriginOffsetsOnInstruction instr = new OriginOffsetsOnInstruction();
		instr.setIdGCodeLine(line.getId());
		set.addInstruction(instr);
					
		AssertInstructionProvider.assertContainsInstructionSet(instructionProvider, set);
	}
	
	@Test
	public void testOriginOffsetsOffInstruction() throws Exception{
		IGCodeProvider provider = service.parse("G92.2");
		AssertGCodeProvider.assertLineCount(provider, 1);
		GCodeLine line = provider.getLineAtIndex(0);
		
		GCodeContext context = new GCodeContext();
		
		InstructionProvider instructionProvider = service.getInstructions(context, provider);
		InstructionSet set = new InstructionSet();
		
		OriginOffsetsOffInstruction instr = new OriginOffsetsOffInstruction();
		instr.setIdGCodeLine(line.getId());
		set.addInstruction(instr);
			
		AssertInstructionProvider.assertContainsInstructionSet(instructionProvider, set);
	}
	
}

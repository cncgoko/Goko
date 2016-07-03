/**
 * 
 */
package org.goko.gcode.rs274ngcv3;

import static org.junit.Assert.assertEquals;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Angle;
import org.goko.core.common.measure.quantity.AngleUnit;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.quantity.LengthUnit;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.instruction.StraightFeedInstruction;
import org.junit.Test;

/**
 * @author Psyko
 * @date 2 juil. 2016
 */
public class StraightInstructionTest {

	@Test
	public void testCompleteContextUpdate() throws GkException{
		GCodeContext context = new GCodeContext();
		assertEquals(Length.ZERO, context.getX());
		assertEquals(Length.ZERO, context.getY());
		assertEquals(Length.ZERO, context.getZ());
		assertEquals(Angle.ZERO, context.getA());
		assertEquals(Angle.ZERO, context.getB());
		assertEquals(Angle.ZERO, context.getC());
		
		StraightFeedInstruction instr = new StraightFeedInstruction(Length.valueOf("17.356", LengthUnit.MILLIMETRE),
																	Length.valueOf("-21.36", LengthUnit.MILLIMETRE),
																	Length.valueOf("100"   , LengthUnit.MILLIMETRE), 
																	Angle.valueOf("0.356", AngleUnit.DEGREE_ANGLE) ,
																	Angle.valueOf("-7.6", AngleUnit.DEGREE_ANGLE)  ,
																	Angle.valueOf("45.00", AngleUnit.DEGREE_ANGLE) );
		instr.apply(context);
		
		// Make sure only the target coordinate updated
		assertEquals(Length.valueOf("17.356", LengthUnit.MILLIMETRE), context.getX());
		assertEquals(Length.valueOf("-21.36", LengthUnit.MILLIMETRE), context.getY());
		assertEquals(Length.valueOf("100"   , LengthUnit.MILLIMETRE), context.getZ());
		assertEquals(Angle.valueOf("0.356", AngleUnit.DEGREE_ANGLE) , context.getA());
		assertEquals(Angle.valueOf("-7.6", AngleUnit.DEGREE_ANGLE)  , context.getB());		
		assertEquals(Angle.valueOf("45.00", AngleUnit.DEGREE_ANGLE) , context.getC());
	}
	
	@Test
	public void testPartialContextUpdateX() throws GkException{
		GCodeContext context = new GCodeContext();
		assertEquals(Length.ZERO, context.getX());
		assertEquals(Length.ZERO, context.getY());
		assertEquals(Length.ZERO, context.getZ());
		assertEquals(Angle.ZERO, context.getA());
		assertEquals(Angle.ZERO, context.getB());
		assertEquals(Angle.ZERO, context.getC());
		
		StraightFeedInstruction instr = new StraightFeedInstruction(Length.valueOf("17.356", LengthUnit.MILLIMETRE), null, null, null, null, null);
		instr.apply(context);
		
		// Make sure only the target coordinate updated
		assertEquals(Length.valueOf("17.356", LengthUnit.MILLIMETRE), context.getX());
		assertEquals(Length.ZERO, context.getY());
		assertEquals(Length.ZERO, context.getZ());
		assertEquals(Angle.ZERO, context.getA());
		assertEquals(Angle.ZERO, context.getB());
		assertEquals(Angle.ZERO, context.getC());		
	}
	
	@Test
	public void testPartialContextUpdateY() throws GkException{
		GCodeContext context = new GCodeContext();
		assertEquals(Length.ZERO, context.getX());
		assertEquals(Length.ZERO, context.getY());
		assertEquals(Length.ZERO, context.getZ());
		assertEquals(Angle.ZERO, context.getA());
		assertEquals(Angle.ZERO, context.getB());
		assertEquals(Angle.ZERO, context.getC());
		
		StraightFeedInstruction instr = new StraightFeedInstruction(null, Length.valueOf("17.356", LengthUnit.MILLIMETRE), null, null, null, null);
		instr.apply(context);
		
		// Make sure only the target coordinate updated
		assertEquals(Length.ZERO, context.getX());
		assertEquals(Length.valueOf("17.356", LengthUnit.MILLIMETRE), context.getY());		
		assertEquals(Length.ZERO, context.getZ());
		assertEquals(Angle.ZERO, context.getA());
		assertEquals(Angle.ZERO, context.getB());
		assertEquals(Angle.ZERO, context.getC());		
	}
	
	@Test
	public void testPartialContextUpdateZ() throws GkException{
		GCodeContext context = new GCodeContext();
		assertEquals(Length.ZERO, context.getX());
		assertEquals(Length.ZERO, context.getY());
		assertEquals(Length.ZERO, context.getZ());
		assertEquals(Angle.ZERO, context.getA());
		assertEquals(Angle.ZERO, context.getB());
		assertEquals(Angle.ZERO, context.getC());
		
		StraightFeedInstruction instr = new StraightFeedInstruction(null, null, Length.valueOf("17.356", LengthUnit.MILLIMETRE),  null, null, null);
		instr.apply(context);
		
		// Make sure only the target coordinate updated		
		assertEquals(Length.ZERO, context.getX());
		assertEquals(Length.ZERO, context.getY());
		assertEquals(Length.valueOf("17.356", LengthUnit.MILLIMETRE), context.getZ());
		assertEquals(Angle.ZERO, context.getA());
		assertEquals(Angle.ZERO, context.getB());
		assertEquals(Angle.ZERO, context.getC());		
	}
	
	@Test
	public void testPartialContextUpdateA() throws GkException{
		GCodeContext context = new GCodeContext();
		assertEquals(Length.ZERO, context.getX());
		assertEquals(Length.ZERO, context.getY());
		assertEquals(Length.ZERO, context.getZ());
		assertEquals(Angle.ZERO, context.getA());
		assertEquals(Angle.ZERO, context.getB());
		assertEquals(Angle.ZERO, context.getC());
		
		StraightFeedInstruction instr = new StraightFeedInstruction(null, null, null, Angle.valueOf("17.356", AngleUnit.DEGREE_ANGLE), null, null);
		instr.apply(context);
		
		// Make sure only the target coordinate updated
		assertEquals(Length.ZERO, context.getX());
		assertEquals(Length.ZERO, context.getY());
		assertEquals(Length.ZERO, context.getZ());
		assertEquals(Angle.valueOf("17.356", AngleUnit.DEGREE_ANGLE), context.getA());
		assertEquals(Angle.ZERO, context.getB());
		assertEquals(Angle.ZERO, context.getC());		
	}
	
	@Test
	public void testPartialContextUpdateB() throws GkException{
		GCodeContext context = new GCodeContext();
		assertEquals(Length.ZERO, context.getX());
		assertEquals(Length.ZERO, context.getY());
		assertEquals(Length.ZERO, context.getZ());
		assertEquals(Angle.ZERO, context.getA());
		assertEquals(Angle.ZERO, context.getB());
		assertEquals(Angle.ZERO, context.getC());
		
		StraightFeedInstruction instr = new StraightFeedInstruction(null, null, null, null, Angle.valueOf("17.356", AngleUnit.DEGREE_ANGLE), null);
		instr.apply(context);
		
		// Make sure only the target coordinate updated
		assertEquals(Length.ZERO, context.getX());
		assertEquals(Length.ZERO, context.getY());
		assertEquals(Length.ZERO, context.getZ());
		assertEquals(Angle.ZERO, context.getA());
		assertEquals(Angle.valueOf("17.356", AngleUnit.DEGREE_ANGLE), context.getB());
		assertEquals(Angle.ZERO, context.getC());		
	}
	
	@Test
	public void testPartialContextUpdateC() throws GkException{
		GCodeContext context = new GCodeContext();
		assertEquals(Length.ZERO, context.getX());
		assertEquals(Length.ZERO, context.getY());
		assertEquals(Length.ZERO, context.getZ());
		assertEquals(Angle.ZERO, context.getA());
		assertEquals(Angle.ZERO, context.getB());
		assertEquals(Angle.ZERO, context.getC());
		
		StraightFeedInstruction instr = new StraightFeedInstruction(null, null, null, null, null, Angle.valueOf("17.356", AngleUnit.DEGREE_ANGLE));
		instr.apply(context);
		
		// Make sure only the target coordinate updated
		assertEquals(Length.ZERO, context.getX());
		assertEquals(Length.ZERO, context.getY());
		assertEquals(Length.ZERO, context.getZ());
		assertEquals(Angle.ZERO, context.getA());
		assertEquals(Angle.ZERO, context.getB());		
		assertEquals(Angle.valueOf("17.356", AngleUnit.DEGREE_ANGLE), context.getC());
	}
	
}

/**
 * 
 */
package org.goko.gcode.rs274ngcv3;

import static org.junit.Assert.assertEquals;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Angle;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.quantity.LengthUnit;
import org.goko.core.gcode.rs274ngcv3.context.EnumPlane;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.instruction.ArcFeedInstruction;
import org.junit.Test;

/**
 * @author Psyko
 * @date 2 juil. 2016
 */
public class ArcFeedInstructionTest {

	@Test
	public void contextXyPlaneUpdate() throws GkException{
		ArcFeedInstruction instr = new ArcFeedInstruction(  Length.valueOf("10", LengthUnit.MILLIMETRE),
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
		
		GCodeContext context = new GCodeContext();
		context.setPlane(EnumPlane.XY_PLANE);
		assertEquals(Angle.ZERO, context.getA());
		assertEquals(Angle.ZERO, context.getB());
		assertEquals(Angle.ZERO, context.getC());
		
		instr.apply(context);
		assertEquals(Length.valueOf("10", LengthUnit.MILLIMETRE), context.getX());
		assertEquals(Length.valueOf("12", LengthUnit.MILLIMETRE), context.getY());
		assertEquals(Length.valueOf("-8.32", LengthUnit.MILLIMETRE), context.getZ());
		assertEquals(Angle.ZERO, context.getA());
		assertEquals(Angle.ZERO, context.getB());
		assertEquals(Angle.ZERO, context.getC());
	}
	
	@Test
	public void contextYzPlaneUpdate() throws GkException{
		ArcFeedInstruction instr = new ArcFeedInstruction(  Length.valueOf("-8.32", LengthUnit.MILLIMETRE),
														 	Length.valueOf("12", LengthUnit.MILLIMETRE),
														 	Length.valueOf("10", LengthUnit.MILLIMETRE),
														 	null,
														 	Length.valueOf("5", LengthUnit.MILLIMETRE),
														 	Length.valueOf("6", LengthUnit.MILLIMETRE),														 	
														 	null,
														 	null,
														 	null,
														 	1,														 	
														 	true);
		
		GCodeContext context = new GCodeContext();
		context.setPlane(EnumPlane.YZ_PLANE);
		assertEquals(Angle.ZERO, context.getA());
		assertEquals(Angle.ZERO, context.getB());
		assertEquals(Angle.ZERO, context.getC());
		
		instr.apply(context);
		
		assertEquals(Length.valueOf("-8.32", LengthUnit.MILLIMETRE), context.getX());
		assertEquals(Length.valueOf("12", LengthUnit.MILLIMETRE), context.getY());
		assertEquals(Length.valueOf("10", LengthUnit.MILLIMETRE), context.getZ());		
		assertEquals(Angle.ZERO, context.getA());
		assertEquals(Angle.ZERO, context.getB());
		assertEquals(Angle.ZERO, context.getC());
	}
	
	@Test
	public void contextXzPlaneUpdate() throws GkException{
		ArcFeedInstruction instr = new ArcFeedInstruction(  Length.valueOf("12", LengthUnit.MILLIMETRE),
															Length.valueOf("-8.32", LengthUnit.MILLIMETRE),														 	
														 	Length.valueOf("10", LengthUnit.MILLIMETRE),
														 	Length.valueOf("6", LengthUnit.MILLIMETRE),
														 	null,														 	
														 	Length.valueOf("5", LengthUnit.MILLIMETRE),														 	
														 	null,
														 	null,
														 	null,
														 	1,														 	
														 	true);
		
		GCodeContext context = new GCodeContext();
		context.setPlane(EnumPlane.XZ_PLANE);
		assertEquals(Angle.ZERO, context.getA());
		assertEquals(Angle.ZERO, context.getB());
		assertEquals(Angle.ZERO, context.getC());
		
		instr.apply(context);
		
		assertEquals(Length.valueOf("12", LengthUnit.MILLIMETRE), context.getX());
		assertEquals(Length.valueOf("-8.32", LengthUnit.MILLIMETRE), context.getY());
		assertEquals(Length.valueOf("10", LengthUnit.MILLIMETRE), context.getZ());
		assertEquals(Angle.ZERO, context.getA());
		assertEquals(Angle.ZERO, context.getB());
		assertEquals(Angle.ZERO, context.getC());
	}
}

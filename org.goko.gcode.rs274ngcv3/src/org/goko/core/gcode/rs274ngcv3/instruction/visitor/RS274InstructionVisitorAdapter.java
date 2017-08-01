/**
 * 
 */
package org.goko.core.gcode.rs274ngcv3.instruction.visitor;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.instruction.AbstractInstruction;
import org.goko.core.gcode.rs274ngcv3.instruction.ArcFeedInstruction;
import org.goko.core.gcode.rs274ngcv3.instruction.ChangeToolInstruction;
import org.goko.core.gcode.rs274ngcv3.instruction.CommentInstruction;
import org.goko.core.gcode.rs274ngcv3.instruction.CutterCompensationLeftInstruction;
import org.goko.core.gcode.rs274ngcv3.instruction.CutterCompensationRightInstruction;
import org.goko.core.gcode.rs274ngcv3.instruction.DisableCutterCompensationInstruction;
import org.goko.core.gcode.rs274ngcv3.instruction.DwellInstruction;
import org.goko.core.gcode.rs274ngcv3.instruction.FloodOffInstruction;
import org.goko.core.gcode.rs274ngcv3.instruction.FloodOnInstruction;
import org.goko.core.gcode.rs274ngcv3.instruction.MistOnInstruction;
import org.goko.core.gcode.rs274ngcv3.instruction.OriginOffsetsOffInstruction;
import org.goko.core.gcode.rs274ngcv3.instruction.OriginOffsetsOnInstruction;
import org.goko.core.gcode.rs274ngcv3.instruction.ProgramEndInstruction;
import org.goko.core.gcode.rs274ngcv3.instruction.ResetOriginOffsetInstruction;
import org.goko.core.gcode.rs274ngcv3.instruction.SelectPlaneInstruction;
import org.goko.core.gcode.rs274ngcv3.instruction.SelectToolInstruction;
import org.goko.core.gcode.rs274ngcv3.instruction.SetCoordinateSystemDataInstruction;
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
import org.goko.core.gcode.rs274ngcv3.instruction.StraightProbeInstruction;
import org.goko.core.gcode.rs274ngcv3.instruction.StraightTraverseInstruction;
import org.goko.core.gcode.rs274ngcv3.instruction.UserLengthUnitsInstruction;

/**
 * @author Psyko
 * @date 2 juil. 2017
 */
public class RS274InstructionVisitorAdapter implements IRS274InstructionVisitor {

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.visitor.IRS274InstructionVisitor#visit(org.goko.core.gcode.rs274ngcv3.instruction.AbstractInstruction)
	 */
	@Override
	public void visit(GCodeContext context, AbstractInstruction instr) throws GkException {
		

	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.visitor.IRS274InstructionVisitor#visit(org.goko.core.gcode.rs274ngcv3.instruction.ArcFeedInstruction)
	 */
	@Override
	public void visit(GCodeContext context, ArcFeedInstruction instr) throws GkException {
		

	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.visitor.IRS274InstructionVisitor#visit(org.goko.core.gcode.rs274ngcv3.instruction.ChangeToolInstruction)
	 */
	@Override
	public void visit(GCodeContext context, ChangeToolInstruction instr) throws GkException {
		

	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.visitor.IRS274InstructionVisitor#visit(org.goko.core.gcode.rs274ngcv3.instruction.CommentInstruction)
	 */
	@Override
	public void visit(GCodeContext context, CommentInstruction instr) throws GkException {
		

	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.visitor.IRS274InstructionVisitor#visit(org.goko.core.gcode.rs274ngcv3.instruction.CutterCompensationLeftInstruction)
	 */
	@Override
	public void visit(GCodeContext context, CutterCompensationLeftInstruction instr) throws GkException {
		

	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.visitor.IRS274InstructionVisitor#visit(org.goko.core.gcode.rs274ngcv3.instruction.CutterCompensationRightInstruction)
	 */
	@Override
	public void visit(GCodeContext context, CutterCompensationRightInstruction instr) throws GkException {
		

	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.visitor.IRS274InstructionVisitor#visit(org.goko.core.gcode.rs274ngcv3.instruction.DisableCutterCompensationInstruction)
	 */
	@Override
	public void visit(GCodeContext context, DisableCutterCompensationInstruction instr) throws GkException {
		

	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.visitor.IRS274InstructionVisitor#visit(org.goko.core.gcode.rs274ngcv3.instruction.DwellInstruction)
	 */
	@Override
	public void visit(GCodeContext context, DwellInstruction instr) throws GkException {
		

	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.visitor.IRS274InstructionVisitor#visit(org.goko.core.gcode.rs274ngcv3.instruction.FloodOffInstruction)
	 */
	@Override
	public void visit(GCodeContext context, FloodOffInstruction instr) throws GkException {
		

	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.visitor.IRS274InstructionVisitor#visit(org.goko.core.gcode.rs274ngcv3.instruction.FloodOnInstruction)
	 */
	@Override
	public void visit(GCodeContext context, FloodOnInstruction instr) throws GkException {
		

	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.visitor.IRS274InstructionVisitor#visit(org.goko.core.gcode.rs274ngcv3.instruction.MistOnInstruction)
	 */
	@Override
	public void visit(GCodeContext context, MistOnInstruction instr) throws GkException {
		

	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.visitor.IRS274InstructionVisitor#visit(org.goko.core.gcode.rs274ngcv3.instruction.OriginOffsetsOnInstruction)
	 */
	@Override
	public void visit(GCodeContext context, OriginOffsetsOnInstruction instr) throws GkException {
		

	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.visitor.IRS274InstructionVisitor#visit(org.goko.core.gcode.rs274ngcv3.instruction.OriginOffsetsOffInstruction)
	 */
	@Override
	public void visit(GCodeContext context, OriginOffsetsOffInstruction instr) throws GkException {
		

	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.visitor.IRS274InstructionVisitor#visit(org.goko.core.gcode.rs274ngcv3.instruction.ProgramEndInstruction)
	 */
	@Override
	public void visit(GCodeContext context, ProgramEndInstruction instr) throws GkException {
		

	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.visitor.IRS274InstructionVisitor#visit(org.goko.core.gcode.rs274ngcv3.instruction.ResetOriginOffsetInstruction)
	 */
	@Override
	public void visit(GCodeContext context, ResetOriginOffsetInstruction instr) throws GkException {
		

	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.visitor.IRS274InstructionVisitor#visit(org.goko.core.gcode.rs274ngcv3.instruction.SelectPlaneInstruction)
	 */
	@Override
	public void visit(GCodeContext context, SelectPlaneInstruction instr) throws GkException {
		

	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.visitor.IRS274InstructionVisitor#visit(org.goko.core.gcode.rs274ngcv3.instruction.SelectToolInstruction)
	 */
	@Override
	public void visit(GCodeContext context, SelectToolInstruction instr) throws GkException {
		

	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.visitor.IRS274InstructionVisitor#visit(org.goko.core.gcode.rs274ngcv3.instruction.SetCoordinateSystemDataInstruction)
	 */
	@Override
	public void visit(GCodeContext context, SetCoordinateSystemDataInstruction instr) throws GkException {
		

	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.visitor.IRS274InstructionVisitor#visit(org.goko.core.gcode.rs274ngcv3.instruction.SetCoordinateSystemInstruction)
	 */
	@Override
	public void visit(GCodeContext context, SetCoordinateSystemInstruction instr) throws GkException {
		

	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.visitor.IRS274InstructionVisitor#visit(org.goko.core.gcode.rs274ngcv3.instruction.SetDistanceModeInstruction)
	 */
	@Override
	public void visit(GCodeContext context, SetDistanceModeInstruction instr) throws GkException {
		

	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.visitor.IRS274InstructionVisitor#visit(org.goko.core.gcode.rs274ngcv3.instruction.SetFeedRateInstruction)
	 */
	@Override
	public void visit(GCodeContext context, SetFeedRateInstruction instr) throws GkException {
		

	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.visitor.IRS274InstructionVisitor#visit(org.goko.core.gcode.rs274ngcv3.instruction.SetMotionControlModeInstruction)
	 */
	@Override
	public void visit(GCodeContext context, SetMotionControlModeInstruction instr) throws GkException {
		

	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.visitor.IRS274InstructionVisitor#visit(org.goko.core.gcode.rs274ngcv3.instruction.SetOriginOffsetInstruction)
	 */
	@Override
	public void visit(GCodeContext context, SetOriginOffsetInstruction instr) throws GkException {
		

	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.visitor.IRS274InstructionVisitor#visit(org.goko.core.gcode.rs274ngcv3.instruction.SetSpindleSpeedInstruction)
	 */
	@Override
	public void visit(GCodeContext context, SetSpindleSpeedInstruction instr) throws GkException {
		

	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.visitor.IRS274InstructionVisitor#visit(org.goko.core.gcode.rs274ngcv3.instruction.StartSpindleClockwiseInstruction)
	 */
	@Override
	public void visit(GCodeContext context, StartSpindleClockwiseInstruction instr) throws GkException {
		

	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.visitor.IRS274InstructionVisitor#visit(org.goko.core.gcode.rs274ngcv3.instruction.StartSpindleCounterClockwiseInstruction)
	 */
	@Override
	public void visit(GCodeContext context, StartSpindleCounterClockwiseInstruction instr) throws GkException {
		

	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.visitor.IRS274InstructionVisitor#visit(org.goko.core.gcode.rs274ngcv3.instruction.StopSpindleTurningInstruction)
	 */
	@Override
	public void visit(GCodeContext context, StopSpindleTurningInstruction instr) throws GkException {
		

	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.visitor.IRS274InstructionVisitor#visit(org.goko.core.gcode.rs274ngcv3.instruction.StraightFeedInstruction)
	 */
	@Override
	public void visit(GCodeContext context, StraightFeedInstruction instr) throws GkException {
		

	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.visitor.IRS274InstructionVisitor#visit(org.goko.core.gcode.rs274ngcv3.instruction.StraightProbeInstruction)
	 */
	@Override
	public void visit(GCodeContext context, StraightProbeInstruction instr) throws GkException {
		

	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.visitor.IRS274InstructionVisitor#visit(org.goko.core.gcode.rs274ngcv3.instruction.StraightTraverseInstruction)
	 */
	@Override
	public void visit(GCodeContext context, StraightTraverseInstruction instr) throws GkException {
		

	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.visitor.IRS274InstructionVisitor#visit(org.goko.core.gcode.rs274ngcv3.instruction.UserLengthUnitsInstruction)
	 */
	@Override
	public void visit(GCodeContext context, UserLengthUnitsInstruction instr) throws GkException {
		

	}

}

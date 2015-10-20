package org.goko.core.gcode.rs274ngcv3.instruction;

import java.util.ArrayList;
import java.util.List;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.element.GCodeWord;
import org.goko.core.gcode.element.IInstruction;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.instruction.builder.ArcFeedBuilder;
import org.goko.core.gcode.rs274ngcv3.instruction.builder.ChangeToolBuilder;
import org.goko.core.gcode.rs274ngcv3.instruction.builder.CommentBuilder;
import org.goko.core.gcode.rs274ngcv3.instruction.builder.DwellBuilder;
import org.goko.core.gcode.rs274ngcv3.instruction.builder.FloodOffBuilder;
import org.goko.core.gcode.rs274ngcv3.instruction.builder.FloodOnBuilder;
import org.goko.core.gcode.rs274ngcv3.instruction.builder.MistOnBuilder;
import org.goko.core.gcode.rs274ngcv3.instruction.builder.OriginOffsetsOffBuilder;
import org.goko.core.gcode.rs274ngcv3.instruction.builder.OriginOffsetsOnBuilder;
import org.goko.core.gcode.rs274ngcv3.instruction.builder.ProgramEndBuilder;
import org.goko.core.gcode.rs274ngcv3.instruction.builder.ResetOriginOffsetsBuilder;
import org.goko.core.gcode.rs274ngcv3.instruction.builder.SelectPlaneBuilder;
import org.goko.core.gcode.rs274ngcv3.instruction.builder.SelectToolBuilder;
import org.goko.core.gcode.rs274ngcv3.instruction.builder.SetCoordinateSystemBuilder;
import org.goko.core.gcode.rs274ngcv3.instruction.builder.SetCoordinateSystemDataBuilder;
import org.goko.core.gcode.rs274ngcv3.instruction.builder.SetDistanceModeBuilder;
import org.goko.core.gcode.rs274ngcv3.instruction.builder.SetFeedRateBuilder;
import org.goko.core.gcode.rs274ngcv3.instruction.builder.SetMotionControlModeBuilder;
import org.goko.core.gcode.rs274ngcv3.instruction.builder.SetOriginOffsetsBuilder;
import org.goko.core.gcode.rs274ngcv3.instruction.builder.SetSpindleSpeedBuilder;
import org.goko.core.gcode.rs274ngcv3.instruction.builder.StartSpindleClockwiseBuilder;
import org.goko.core.gcode.rs274ngcv3.instruction.builder.StartSpindleCounterClockwiseBuilder;
import org.goko.core.gcode.rs274ngcv3.instruction.builder.StopSpindleTurningBuilder;
import org.goko.core.gcode.rs274ngcv3.instruction.builder.StraightFeedBuilder;
import org.goko.core.gcode.rs274ngcv3.instruction.builder.StraightProbeBuilder;
import org.goko.core.gcode.rs274ngcv3.instruction.builder.StraightTraverseBuilder;
import org.goko.core.gcode.rs274ngcv3.instruction.builder.UserLengthUnitsBuilder;

/**
 * Instruction factory
 *
 * @author Psyko
 */
public class InstructionFactory {
	/** The list of known builders*/
	public List<IInstructionBuilder<? extends AbstractInstruction>> builders;
	
	/** Constructor */
	public InstructionFactory() {
		builders = new ArrayList<IInstructionBuilder<? extends AbstractInstruction>>();
		builders.add(new CommentBuilder());		
		builders.add(new SetFeedRateBuilder());
		builders.add(new SetSpindleSpeedBuilder());
		builders.add(new SelectToolBuilder());
		builders.add(new ChangeToolBuilder());				
		
		builders.add(new StartSpindleClockwiseBuilder());		
		builders.add(new StartSpindleCounterClockwiseBuilder());		
		builders.add(new StopSpindleTurningBuilder());
		
		builders.add(new MistOnBuilder());
		builders.add(new FloodOnBuilder());
		builders.add(new FloodOffBuilder());		
		
		builders.add(new DwellBuilder());
		
		builders.add(new SelectPlaneBuilder());		
		builders.add(new UserLengthUnitsBuilder());		
						
		builders.add(new SetCoordinateSystemBuilder());
		
		builders.add(new SetMotionControlModeBuilder());		
		builders.add(new SetDistanceModeBuilder());		
		// set retract mode (g98 g99)		
		// home (g28, g30)
		builders.add(new SetCoordinateSystemDataBuilder());
		builders.add(new SetOriginOffsetsBuilder());
		builders.add(new ResetOriginOffsetsBuilder());
		builders.add(new OriginOffsetsOnBuilder());
		builders.add(new OriginOffsetsOffBuilder());
		// set axis offsets (G92, G92.1, G92.2, G94)
		
		builders.add(new StraightProbeBuilder());
		builders.add(new ArcFeedBuilder());
		builders.add(new StraightFeedBuilder());		
		builders.add(new StraightTraverseBuilder());
		
		builders.add(new ProgramEndBuilder());		
	}
	
	/**
	 * Returns the first possible creatable instruction from the list of word.
	 * The instruction creation can consume one or more words. The instruction creation can use 
	 * only some of the provided words.
	 * The used words are always removed from the given list  
	 * @param context the context 
	 * @param words the words to create the instruction 
	 * @return IInstruction
	 * @throws GkException GkException
	 */
	public AbstractInstruction build(GCodeContext context, List<GCodeWord> words) throws GkException{
		AbstractInstruction result = null;
				
		for (IInstructionBuilder<? extends AbstractInstruction> builder : builders) {
			if(builder.match(context, words)){
				result = builder.toInstruction(context, words);
				break;
			}			
		}
		return result;
	}
}

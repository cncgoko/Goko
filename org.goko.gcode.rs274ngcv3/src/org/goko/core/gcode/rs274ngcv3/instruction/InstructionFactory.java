package org.goko.core.gcode.rs274ngcv3.instruction;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.element.GCodeLine;
import org.goko.core.gcode.element.GCodeWord;
import org.goko.core.gcode.rs274ngcv3.RenderingFormat;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.InstructionSet;
import org.goko.core.gcode.rs274ngcv3.instruction.builder.ArcFeedBuilder;
import org.goko.core.gcode.rs274ngcv3.instruction.builder.ChangeToolBuilder;
import org.goko.core.gcode.rs274ngcv3.instruction.builder.CommentBuilder;
import org.goko.core.gcode.rs274ngcv3.instruction.builder.CutterCompensationLeftInstructionBuilder;
import org.goko.core.gcode.rs274ngcv3.instruction.builder.CutterCompensationRightInstructionBuilder;
import org.goko.core.gcode.rs274ngcv3.instruction.builder.DisableCutterCompensationInstructionBuilder;
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
import org.goko.core.gcode.rs274ngcv3.instruction.exporter.ArcFeedExporter;
import org.goko.core.gcode.rs274ngcv3.instruction.exporter.ChangeToolExporter;
import org.goko.core.gcode.rs274ngcv3.instruction.exporter.CommentExporter;
import org.goko.core.gcode.rs274ngcv3.instruction.exporter.CutterCompensationLeftInstructionExporter;
import org.goko.core.gcode.rs274ngcv3.instruction.exporter.CutterCompensationRightInstructionExporter;
import org.goko.core.gcode.rs274ngcv3.instruction.exporter.DisableCutterCompensationInstructionExporter;
import org.goko.core.gcode.rs274ngcv3.instruction.exporter.DwellExporter;
import org.goko.core.gcode.rs274ngcv3.instruction.exporter.FloodOffExporter;
import org.goko.core.gcode.rs274ngcv3.instruction.exporter.FloodOnExporter;
import org.goko.core.gcode.rs274ngcv3.instruction.exporter.MistOnExporter;
import org.goko.core.gcode.rs274ngcv3.instruction.exporter.OriginOffsetsOffExporter;
import org.goko.core.gcode.rs274ngcv3.instruction.exporter.OriginOffsetsOnExporter;
import org.goko.core.gcode.rs274ngcv3.instruction.exporter.ProgramEndExporter;
import org.goko.core.gcode.rs274ngcv3.instruction.exporter.ResetOriginOffsetsExporter;
import org.goko.core.gcode.rs274ngcv3.instruction.exporter.SelectPlaneExporter;
import org.goko.core.gcode.rs274ngcv3.instruction.exporter.SelectToolExporter;
import org.goko.core.gcode.rs274ngcv3.instruction.exporter.SetCoordinateSystemDataExporter;
import org.goko.core.gcode.rs274ngcv3.instruction.exporter.SetCoordinateSystemExporter;
import org.goko.core.gcode.rs274ngcv3.instruction.exporter.SetDistanceModeExporter;
import org.goko.core.gcode.rs274ngcv3.instruction.exporter.SetFeedRateExporter;
import org.goko.core.gcode.rs274ngcv3.instruction.exporter.SetMotionControlModeExporter;
import org.goko.core.gcode.rs274ngcv3.instruction.exporter.SetOriginOffsetsExporter;
import org.goko.core.gcode.rs274ngcv3.instruction.exporter.SetSpindleSpeedExporter;
import org.goko.core.gcode.rs274ngcv3.instruction.exporter.StartSpindleClockwiseExporter;
import org.goko.core.gcode.rs274ngcv3.instruction.exporter.StartSpindleCounterClockwiseExporter;
import org.goko.core.gcode.rs274ngcv3.instruction.exporter.StopSpindleTurningExporter;
import org.goko.core.gcode.rs274ngcv3.instruction.exporter.StraightFeedExporter;
import org.goko.core.gcode.rs274ngcv3.instruction.exporter.StraightProbeExporter;
import org.goko.core.gcode.rs274ngcv3.instruction.exporter.StraightTraverseExporter;
import org.goko.core.gcode.rs274ngcv3.instruction.exporter.UserLengthUnitsExporter;

/**
 * Instruction factory
 *
 * @author Psyko
 */
public class InstructionFactory {
	/** The list of known builders*/
	public List<IInstructionBuilder<? extends AbstractInstruction>> builders;
	/** The list of known exporters */
	public List<IInstructionExporter> exporters;
	
	/** Constructor */
	public InstructionFactory() {
		initBuilders();
		initExporters();
	}
	
	/**
	 * Initialize the list of builders 
	 */
	private void initBuilders(){
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
		
		builders.add(new CutterCompensationLeftInstructionBuilder());
		builders.add(new CutterCompensationRightInstructionBuilder());
		builders.add(new DisableCutterCompensationInstructionBuilder());				
		
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
	 * Initialize the list of exporters 
	 */
	private void initExporters(){
		exporters = new ArrayList<IInstructionExporter>();
		exporters.add(new CommentExporter());		
		exporters.add(new SetFeedRateExporter());
		exporters.add(new SetSpindleSpeedExporter());
		exporters.add(new SelectToolExporter());
		exporters.add(new ChangeToolExporter());				
		
		exporters.add(new StartSpindleClockwiseExporter());		
		exporters.add(new StartSpindleCounterClockwiseExporter());		
		exporters.add(new StopSpindleTurningExporter());
		
		exporters.add(new MistOnExporter());
		exporters.add(new FloodOnExporter());
		exporters.add(new FloodOffExporter());		
		
		exporters.add(new CutterCompensationLeftInstructionExporter());
		exporters.add(new CutterCompensationRightInstructionExporter());
		exporters.add(new DisableCutterCompensationInstructionExporter());	
		
		exporters.add(new DwellExporter());
		
		exporters.add(new SelectPlaneExporter());		
		exporters.add(new UserLengthUnitsExporter());		
						
		exporters.add(new SetCoordinateSystemExporter());
		
		exporters.add(new SetMotionControlModeExporter());		
		exporters.add(new SetDistanceModeExporter());		
		// set retract mode (g98 g99)		
		// home (g28, g30)
		exporters.add(new SetCoordinateSystemDataExporter());
		exporters.add(new SetOriginOffsetsExporter());
		exporters.add(new ResetOriginOffsetsExporter());
		exporters.add(new OriginOffsetsOnExporter());
		exporters.add(new OriginOffsetsOffExporter());
		// set axis offsets (G92, G92.1, G92.2, G94)
		
		exporters.add(new StraightProbeExporter());
		exporters.add(new ArcFeedExporter());
		exporters.add(new StraightFeedExporter());		
		exporters.add(new StraightTraverseExporter());
		
		exporters.add(new ProgramEndExporter());		
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
	
	/**
	 * Translates the given instruction set into GCodeWord list
	 * @param context the context
	 * @param instructionSet the instruction set 
	 * @return GCodeLine
	 * @throws GkException GkException
	 */
	public GCodeLine getLine(GCodeContext context, InstructionSet instructionSet, RenderingFormat format) throws GkException{		
		List<GCodeWord> lstWords = new ArrayList<GCodeWord>();
		List<AbstractInstruction> localInstructions = instructionSet.getInstructions();
		
		while(CollectionUtils.isNotEmpty(localInstructions)){
			for (IInstructionExporter exporter : exporters) {			
				if(exporter.match(context, instructionSet.getInstructions())){
					AbstractInstruction instruction = exporter.toWords(context, localInstructions, format, lstWords);
					instruction.apply(context);
					break;
				}
			}
		}
		GCodeLine line = new GCodeLine();
		line.addWords(lstWords);
		return line;
	}
}

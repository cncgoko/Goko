package org.goko.core.gcode.rs274ngcv3.instruction.exporter;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.SpeedUnit;
import org.goko.core.gcode.element.GCodeWord;
import org.goko.core.gcode.rs274ngcv3.RenderingFormat;
import org.goko.core.gcode.rs274ngcv3.context.EnumUnit;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.InstructionType;
import org.goko.core.gcode.rs274ngcv3.instruction.SetFeedRateInstruction;

public class SetFeedRateExporter extends AbstractInstructionExporter<SetFeedRateInstruction>{

	public SetFeedRateExporter() {
		super(InstructionType.SET_FEED_RATE);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.exporter.AbstractInstructionExporter#getWords(org.goko.core.gcode.rs274ngcv3.context.GCodeContext, org.goko.core.gcode.element.IInstruction)
	 */
	@Override
	protected List<GCodeWord> getWords(GCodeContext context, SetFeedRateInstruction instruction, RenderingFormat format) throws GkException {
		String feedrate = StringUtils.EMPTY;
		if(context.getUnit() == EnumUnit.INCHES){
			feedrate = format.format(instruction.getFeedrate(), SpeedUnit.INCH_PER_MINUTE);
		}else{
			feedrate = format.format(instruction.getFeedrate(), SpeedUnit.MILLIMETRE_PER_MINUTE);
		}
		return wrap(new GCodeWord("F", feedrate));
	}

}

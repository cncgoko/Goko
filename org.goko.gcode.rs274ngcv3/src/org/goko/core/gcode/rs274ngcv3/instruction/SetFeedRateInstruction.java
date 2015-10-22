package org.goko.core.gcode.rs274ngcv3.instruction;

import java.math.BigDecimal;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.InstructionType;

public class SetFeedRateInstruction extends AbstractInstruction {
	/** The feedrate of the instruction */
	private BigDecimal feedrate;
	
	/** Constructor */
	public SetFeedRateInstruction(BigDecimal feedrate) {
		super(InstructionType.SET_FEED_RATE);
		this.feedrate = feedrate;
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.element.IInstruction#apply(org.goko.core.gcode.rs274ngcv3.context.GCodeContext)
	 */
	@Override
	public void apply(GCodeContext context) throws GkException {
		context.setFeedrate(feedrate);
	}

	/**
	 * @return the feedrate
	 */
	public BigDecimal getFeedrate() {
		return feedrate;
	}

}

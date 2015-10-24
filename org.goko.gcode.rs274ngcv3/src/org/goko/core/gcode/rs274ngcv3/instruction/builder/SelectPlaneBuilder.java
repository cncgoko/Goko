package org.goko.core.gcode.rs274ngcv3.instruction.builder;

import java.util.List;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.element.GCodeWord;
import org.goko.core.gcode.rs274ngcv3.context.EnumPlane;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.InstructionType;
import org.goko.core.gcode.rs274ngcv3.instruction.SelectPlaneInstruction;
import org.goko.core.gcode.rs274ngcv3.utils.GCodeWordUtils;

public class SelectPlaneBuilder extends AbstractInstructionBuilder<SelectPlaneInstruction> {
	/** Constructor*/
	public SelectPlaneBuilder() {
		super(InstructionType.SELECT_PLANE);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.IInstructionBuilder#match(org.goko.core.gcode.rs274ngcv3.context.GCodeContext, java.util.List)
	 */
	@Override
	public boolean match(GCodeContext context, List<GCodeWord> words) throws GkException {		
		return GCodeWordUtils.containsWord("G17", words) || GCodeWordUtils.containsWord("G18", words) || GCodeWordUtils.containsWord("G19", words);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.IInstructionBuilder#toInstruction(org.goko.core.gcode.rs274ngcv3.context.GCodeContext, java.util.List)
	 */
	@Override
	protected SelectPlaneInstruction getInstruction(GCodeContext context, List<GCodeWord> words) throws GkException {
		EnumPlane plane = null;
		
		GCodeWord word = GCodeWordUtils.findAndRemoveWord("G17", words);
		if(word != null){
			plane = EnumPlane.XY_PLANE;
		}else{
			word = GCodeWordUtils.findAndRemoveWord("G18", words);
			if(word != null){
				plane = EnumPlane.XZ_PLANE;	
			}else{
				word = GCodeWordUtils.findAndRemoveWord("G19", words);
				plane = EnumPlane.YZ_PLANE;
			}
		}
		return new SelectPlaneInstruction(plane);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.instruction.IInstructionBuilder#toGCodeWord(org.goko.core.gcode.rs274ngcv3.context.GCodeContext, org.goko.core.gcode.rs274ngcv3.element.IInstruction)
	 */
	@Override
	public List<GCodeWord> toGCodeWord(GCodeContext context, SelectPlaneInstruction instruction) throws GkException {
		// TODO Auto-generated method stub
		return null;
	}

}

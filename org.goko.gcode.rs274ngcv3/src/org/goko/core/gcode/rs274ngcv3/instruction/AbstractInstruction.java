package org.goko.core.gcode.rs274ngcv3.instruction;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.element.IInstruction;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.InstructionType;

public abstract class AbstractInstruction implements IInstruction {
	/** Internal ID */
	private Integer id;
	/** Id of the GCode line that generated this instruction */
	private Integer idGCodeLine;
	/** The type of the instruction */
	private InstructionType type;
	
	/**
	 * Constructor
	 * @param type the instruction type
	 */
	public AbstractInstruction(InstructionType type) {
		this.type = type;
	}
	/** (inheritDoc)
	 * @see org.goko.core.gcode.element.IInstruction#getType()
	 */
	@Override
	public InstructionType getType() {
		return type;
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.element.IInstruction#getIdGCodeLine()
	 */
	@Override
	public Integer getIdGCodeLine() {		
		return idGCodeLine;
	}
	/**
	 * @param idGCodeLine the idGCodeLine to set
	 */
	public void setIdGCodeLine(Integer idGCodeLine) {
		this.idGCodeLine = idGCodeLine;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(InstructionType type) {
		this.type = type;
	}
	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}	
	
	/**
	 * Apply this instruction to the given GCodeContext 
	 * @param context the context 
	 * @throws GkException GkException
	 */
	public abstract void apply(GCodeContext context) throws GkException;
}

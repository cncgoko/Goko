package org.goko.core.gcode.rs274ngcv3.instruction;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.element.IInstruction;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.InstructionType;
import org.goko.core.gcode.rs274ngcv3.instruction.visitor.RS274InstructionVisitorAdapter;
import org.goko.core.log.GkLog;

public abstract class AbstractInstruction implements IInstruction, Cloneable {
	/** LOG */
	private static final GkLog LOG = GkLog.getLogger(AbstractInstruction.class);
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
	/** (inheritDoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((idGCodeLine == null) ? 0 : idGCodeLine.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}
	/** (inheritDoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractInstruction other = (AbstractInstruction) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (idGCodeLine == null) {
			if (other.idGCodeLine != null)
				return false;
		} else if (!idGCodeLine.equals(other.idGCodeLine))
			return false;
		if (type != other.type)
			return false;
		return true;
	}
	
	/** (inheritDoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public AbstractInstruction clone() {		
		AbstractInstruction clone = null;
		try {
			clone = (AbstractInstruction) super.clone();
			clone.setId(null);
			clone.setIdGCodeLine(null);
		} catch (CloneNotSupportedException e) {
			LOG.error(e);
		}		
		return clone;
	}

	public void accept(GCodeContext context, RS274InstructionVisitorAdapter visitor) throws GkException{
		visitor.visit(context, this);
	}
}

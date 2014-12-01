package org.goko.core.gcode.bean;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.bean.commands.EnumGCodeCommandType;

/**
 * @author PsyKo
 *
 */
public abstract class GCodeCommand {
	/** Identifier */
	private Integer id;
	/** Line number if any */
	private Integer lineNumber;
	/** The type of the command */
	private EnumGCodeCommandType type;
	/** The raw command as a string */
	private String stringCommand;
	/** Bounds of the command */
	private BoundingTuple6b bounds;

	/**
	 * Constructor
	 */
	public GCodeCommand() {
		this(EnumGCodeCommandType.RAW);
	}

	/**
	 * Constructor for subclasses
	 * @param type the type of the command
	 */
	protected GCodeCommand(EnumGCodeCommandType type) {
		this.type = type;
	}

	public abstract void accept(IGCodeCommandVisitor visitor) throws GkException;
	/**
	 * Update the given {@link GCodeContext}
	 * @param context the context to update
	 */
	public abstract void updateContext(GCodeContext context);

	public abstract void initFromContext(GCodeContext context);

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
	 * @return the lineNumber
	 */
	public Integer getLineNumber() {
		return lineNumber;
	}

	/**
	 * @param lineNumber the lineNumber to set
	 */
	public void setLineNumber(Integer lineNumber) {
		this.lineNumber = lineNumber;
	}

	/**
	 * @return the type
	 */
	public EnumGCodeCommandType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(EnumGCodeCommandType type) {
		this.type = type;
	}

	/**
	 * @return the stringCommand
	 */
	public String getStringCommand() {
		return stringCommand;
	}

	/**
	 * @param stringCommand the stringCommand to set
	 */
	public void setStringCommand(String stringCommand) {
		this.stringCommand = stringCommand;
	}

	/** (inheritDoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getStringCommand();
	}

	/**
	 * @return the bounds
	 */
	public BoundingTuple6b getBounds() {
		return bounds;
	}

	/**
	 * @param bounds the bounds to set
	 */
	protected void setBounds(BoundingTuple6b bounds) {
		this.bounds = bounds;
	}

}

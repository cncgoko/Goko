package org.goko.core.gcode.rs274ngcv3.jogl.renderer.builder;

import java.util.List;

import javax.vecmath.Point3d;

import org.goko.core.gcode.element.IInstruction;
import org.goko.core.gcode.element.IInstructionType;

public abstract class AbstractInstructionGeometryBuilder {
	/** The type of supported instruction */
	private IInstructionType type;

	/**
	 * Constructor
	 * @param type the supported type
	 */
	public AbstractInstructionGeometryBuilder(IInstructionType type) {
		super();
		this.type = type;
	}

	/**
	 * Determines if this builder is able to generate geometry for the given instruction
	 * @param instruction the target instruction
	 * @return <code>true</code> if this builder is able to generate geometry for the given instruction, <code>false</code> otherwise
	 */
	public boolean supports(IInstruction instruction){
		return type.equals(instruction.getType());
	}
	
	/**
	 * Builds the geometry for the given instruction 
	 * @param instruction the instruction 
	 * @return the list of generated Point3d
	 */
	public abstract List<Point3d> buildGeometry(IInstruction instruction);
	
	/**
	 * @return the type
	 */
	public IInstructionType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(IInstructionType type) {
		this.type = type;
	}
	
	
	
}

package org.goko.core.gcode.rs274ngcv3.jogl.renderer.builder;

import java.util.List;

import javax.vecmath.Point3d;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkTechnicalException;
import org.goko.core.gcode.element.IInstruction;
import org.goko.core.gcode.element.IInstructionType;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;

public abstract class AbstractInstructionGeometryBuilder<T extends IInstruction> {
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
	 * @param context the context 
	 * @param instruction the instruction  
	 * @return the list of generated Point3d
	 * @throws GkTechnicalException GkTechnicalException 
	 */
	public List<Point3d> buildGeometry(GCodeContext context, IInstruction instruction) throws GkException{
		if(type.equals( instruction.getType() )){
			
			return buildInstructionGeometry(context, ((T) instruction));
		}
		throw new GkTechnicalException("Incompatible instruction type...");
	}
	
	/**
	 * Builds the geometry for the given instruction
	 * @param context the context 
	 * @param instruction the instruction  
	 * @return the list of generated Point3d
	 */
	public abstract List<Point3d> buildInstructionGeometry(GCodeContext context, T instruction)  throws GkException;
		
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

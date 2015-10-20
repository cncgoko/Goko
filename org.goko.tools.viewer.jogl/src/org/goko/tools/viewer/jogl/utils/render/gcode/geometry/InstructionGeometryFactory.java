package org.goko.tools.viewer.jogl.utils.render.gcode.geometry;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Point3d;

import org.apache.commons.collections.CollectionUtils;
import org.goko.core.gcode.element.IInstruction;
import org.goko.tools.viewer.jogl.utils.render.gcode.geometry.builder.AbstractInstructionGeometryBuilder;

public class InstructionGeometryFactory {
	/** Singleton instance */
	private static InstructionGeometryFactory instance;
	/** List of available builders */
	private List<AbstractInstructionGeometryBuilder> lstBuilders;
		
	/**
	 *  Private constructor 
	 */
	private InstructionGeometryFactory(){
		this.lstBuilders = new ArrayList<AbstractInstructionGeometryBuilder>();
	}
	
	public static InstructionGeometryFactory getInstance(){
		if(instance == null){
			instance = new InstructionGeometryFactory();
		}
		return instance;
	}
	
	/**
	 * Builds the geometry for the given instruction  
	 * @param instruction the instruction to build geometry for 
	 * @return a list of Point3d
	 */
	public static List<Point3d> build(IInstruction instruction){
		return getInstance().buildGeometry(instruction);
	}
	
	private List<Point3d> buildGeometry(IInstruction instruction){
		List<Point3d> lstPoints = new ArrayList<Point3d>();
		AbstractInstructionGeometryBuilder builder = findBuilder(instruction);
		if(builder != null){
			return builder.buildGeometry(instruction);
		}		
		return lstPoints;
	}
	
	/**
	 * Find the builder for the given instruction or <code>null</code> if none found
	 * @param instruction the instruction to render
	 * @return the builder for the given instruction or <code>null</code> if none found
	 */
	private AbstractInstructionGeometryBuilder findBuilder(IInstruction instruction){
		if(CollectionUtils.isNotEmpty(lstBuilders)){
			for (AbstractInstructionGeometryBuilder builder : lstBuilders) {
				if(builder.supports(instruction)){
					return builder;
				}
			}
		}		
		return null;
	}
}

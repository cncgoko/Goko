package org.goko.core.gcode.rs274ngcv3.jogl.renderer;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Point3d;

import org.apache.commons.collections.CollectionUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.element.IInstruction;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.instruction.AbstractInstruction;
import org.goko.core.gcode.rs274ngcv3.jogl.renderer.builder.AbstractInstructionGeometryBuilder;
import org.goko.core.gcode.rs274ngcv3.jogl.renderer.builder.ArcFeedGeometryBuilder;
import org.goko.core.gcode.rs274ngcv3.jogl.renderer.builder.StraightFeedGeometryBuilder;
import org.goko.core.gcode.rs274ngcv3.jogl.renderer.builder.StraightProbeGeometryBuilder;
import org.goko.core.gcode.rs274ngcv3.jogl.renderer.builder.StraightTraverseGeometryBuilder;

public class InstructionGeometryFactory {
	/** Singleton instance */
	private static InstructionGeometryFactory instance;
	/** List of available builders */
	private List<AbstractInstructionGeometryBuilder<? extends AbstractInstruction>> lstBuilders;
		
	/**
	 *  Private constructor 
	 */
	private InstructionGeometryFactory(){
		this.lstBuilders = new ArrayList<AbstractInstructionGeometryBuilder<? extends AbstractInstruction>>();
		this.lstBuilders.add( new StraightFeedGeometryBuilder() );
		this.lstBuilders.add( new StraightTraverseGeometryBuilder() );
		this.lstBuilders.add( new ArcFeedGeometryBuilder() );
		this.lstBuilders.add( new StraightProbeGeometryBuilder() );
	}
	
	public static InstructionGeometryFactory getInstance(){
		if(instance == null){
			instance = new InstructionGeometryFactory();
		}
		return instance;
	}
	
	/**
	 * Builds the geometry for the given instruction  
	 * @param context the context 
	 * @param instruction the instruction to build geometry for 
	 * @return a list of Point3d
	 * @throws GkException GkException 
	 */
	public static List<Point3d> build(GCodeContext context, IInstruction instruction) throws GkException{
		return getInstance().buildGeometry(context, instruction);
	}
	
	/**
	 * Builds the geometry for the given instruction  
	 * @param context the context 
	 * @param instruction the instruction to build geometry for 
	 * @return a list of Point3d
	 * @throws GkException GkException
	 */
	private List<Point3d> buildGeometry(GCodeContext context, IInstruction instruction) throws GkException{
		List<Point3d> lstPoints = new ArrayList<Point3d>();
		AbstractInstructionGeometryBuilder<? extends AbstractInstruction> builder = findBuilder(instruction);
		if(builder != null){
			List<Point3d> tmpLst = builder.buildGeometry(context, instruction);
			if(CollectionUtils.isNotEmpty(tmpLst)){
				lstPoints.addAll(tmpLst);
			}
		}		
		return lstPoints;
	}
	
	/**
	 * Find the builder for the given instruction or <code>null</code> if none found
	 * @param instruction the instruction to render
	 * @return the builder for the given instruction or <code>null</code> if none found
	 */
	private AbstractInstructionGeometryBuilder<? extends AbstractInstruction> findBuilder(IInstruction instruction){
		if(CollectionUtils.isNotEmpty(lstBuilders)){
			for (AbstractInstructionGeometryBuilder<? extends AbstractInstruction> builder : lstBuilders) {
				if(builder.supports(instruction)){
					return builder;
				}
			}
		}		
		return null;
	}
}

package org.goko.core.gcode.rs274ngcv3.jogl.renderer.builder;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Point3d;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.InstructionType;
import org.goko.core.gcode.rs274ngcv3.instruction.ArcFeedInstruction;
import org.goko.core.gcode.rs274ngcv3.utils.InstructionUtils;
import org.goko.core.math.Arc3b;
import org.goko.core.math.Tuple6b;
import org.goko.tools.viewer.jogl.service.JoglUtils;

public class ArcFeedGeometryBuilder extends AbstractInstructionGeometryBuilder<ArcFeedInstruction>{
	
	/**
	 * Constructor
	 */
	public ArcFeedGeometryBuilder() {
		super(InstructionType.ARC_FEED);
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.jogl.renderer.builder.AbstractInstructionGeometryBuilder#buildInstructionGeometry(org.goko.core.gcode.rs274ngcv3.context.GCodeContext, org.goko.core.gcode.element.IInstruction)
	 */
	@Override
	public List<Point3d> buildInstructionGeometry(GCodeContext context, ArcFeedInstruction instruction) throws GkException {
		List<Point3d> vertices = new ArrayList<Point3d>();		
		Arc3b arc = InstructionUtils.getArc(context, instruction);
		
		int nbPoints = 8;
		// Adaptive points count
		double arcLength = arc.getLength().doubleValue(JoglUtils.JOGL_UNIT);
		nbPoints =  2 + (int) (arcLength * 8 ); // At least 2 points

		for(float i = 0; i <= nbPoints; i++){
			Tuple6b p = arc.point( i/nbPoints);
			vertices.add(p.toPoint3d(JoglUtils.JOGL_UNIT));			
		}
		
		return vertices;
	}		
}

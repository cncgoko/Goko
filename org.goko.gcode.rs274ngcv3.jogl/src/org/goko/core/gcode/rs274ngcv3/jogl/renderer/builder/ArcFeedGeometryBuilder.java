package org.goko.core.gcode.rs274ngcv3.jogl.renderer.builder;

import java.util.List;

import javax.vecmath.Point3d;

import org.goko.core.gcode.element.IInstruction;
import org.goko.core.gcode.rs274ngcv3.element.InstructionType;

public class ArcFeedGeometryBuilder extends AbstractInstructionGeometryBuilder{
	
	/**
	 * Constructor
	 */
	public ArcFeedGeometryBuilder() {
		super(InstructionType.ARC_FEED);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.jogl.renderer.builder.AbstractInstructionGeometryBuilder#buildGeometry(org.goko.core.gcode.element.IInstruction)
	 */
	@Override
	public List<Point3d> buildGeometry(IInstruction instruction) {
		// TODO Auto-generated method stub
		return null;
	}

}

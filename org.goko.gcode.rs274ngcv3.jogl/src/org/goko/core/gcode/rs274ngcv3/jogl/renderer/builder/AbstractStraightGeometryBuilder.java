package org.goko.core.gcode.rs274ngcv3.jogl.renderer.builder;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Matrix4d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Angle;
import org.goko.core.common.measure.quantity.AngleUnit;
import org.goko.core.gcode.element.IInstructionType;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.instruction.AbstractStraightInstruction;
import org.goko.core.math.Tuple6b;
import org.goko.tools.viewer.jogl.preferences.JoglViewerPreference;
import org.goko.tools.viewer.jogl.service.JoglUtils;

public abstract class AbstractStraightGeometryBuilder<T extends AbstractStraightInstruction> extends AbstractInstructionGeometryBuilder<T> {

	/**
	 * Constructor
	 * @param type
	 */
	public AbstractStraightGeometryBuilder(IInstructionType type) {
		super(type);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.jogl.renderer.builder.AbstractInstructionGeometryBuilder#buildInstructionGeometry(org.goko.core.gcode.rs274ngcv3.context.GCodeContext, org.goko.core.gcode.element.IInstruction)
	 */
	@Override
	public List<Point3d> buildInstructionGeometry(GCodeContext context, T instruction) throws GkException {
		JoglViewerPreference settings = JoglViewerPreference.getInstance();		
		if(instruction != null){			
			if(settings.isRotaryAxisEnabled()){
				GCodeContext postContext = new GCodeContext(context);
				instruction.apply(postContext);
				// The complete angle around the 4th axis
				Angle deltaAngle = Angle.ZERO;
				// FIXME Use a setting to define the 4th axis and then do a dynamic angle detection around the axe A,B or C
				if( postContext.getA() != null){
					deltaAngle = context.getA().subtract(postContext.getA());
				}

				if(deltaAngle.abs().lowerThan(Angle.valueOf("0.0001", AngleUnit.DEGREE_ANGLE))){
					return renderLinearLine(context, instruction);
				}else{
					return renderRotaryLine(context, instruction);
				}
			}else{
				return renderLinearLine(context, instruction);
			}
		}
		return new ArrayList<Point3d>();
	}

//verifier la prise en compte des coordinates systems dans toutes les commandes

	private List<Point3d> renderLinearLine(GCodeContext context, T instruction) throws GkException {
		JoglViewerPreference settings = JoglViewerPreference.getInstance();
		List<Point3d> vertices = new ArrayList<Point3d>();
		Tuple6b offset = context.getCoordinateSystemData(context.getCoordinateSystem());
		Tuple6b startTuple = context.getPosition();//new Tuple6b(context.getX(), context.getY(), context.getZ(), context.getA(), context.getB(), context.getC());
		GCodeContext postContext = new GCodeContext(context);
		instruction.apply(postContext);
		Tuple6b 		endTuple 	=  postContext.getPosition();
		//Tuple6b endTuple   = new Tuple6b(instruction.getX(), instruction.getY(), instruction.getZ(), instruction.getA(), instruction.getB(), instruction.getC());

		startTuple = startTuple.add(offset);
		endTuple = endTuple.add(offset);

		Point3d startPoint 	= startTuple.toPoint3d(JoglUtils.JOGL_UNIT);
		Point3d endPoint 	=   endTuple.toPoint3d(JoglUtils.JOGL_UNIT);

		if(settings.isRotaryAxisEnabled()){
			// It's a simple line, but it doesn't mean the rotary (A, B or C) value is at 0
			if(postContext.getA() != null){
				Point3d endAngle 	=   endTuple.angleToPoint3d(AngleUnit.DEGREE_ANGLE);

				Matrix4d rotationMatrix = new Matrix4d();
				rotateMatrix(rotationMatrix, Math.toRadians(endAngle.x));

				if(settings.getRotaryAxisPosition() != null){
					startPoint.sub(settings.getRotaryAxisPosition().toPoint3d(JoglUtils.JOGL_UNIT));
					endPoint.sub(settings.getRotaryAxisPosition().toPoint3d(JoglUtils.JOGL_UNIT));
				}
				rotationMatrix.transform(startPoint);
				rotationMatrix.transform(endPoint);
				if(settings.getRotaryAxisPosition() != null){
					startPoint.add(settings.getRotaryAxisPosition().toPoint3d(JoglUtils.JOGL_UNIT));
					endPoint.add(settings.getRotaryAxisPosition().toPoint3d(JoglUtils.JOGL_UNIT));
				}
			}
		}


		vertices.add(startPoint);
		vertices.add(endPoint);
		return vertices;
	}

	private void rotateMatrix(Matrix4d matrix, double angleRadians){
		JoglViewerPreference settings = JoglViewerPreference.getInstance();
		switch(settings.getRotaryAxisDirection()){
			case X:matrix.rotX( angleRadians );
			break;
			case Y:matrix.rotY( angleRadians );
			break;
			case Z:matrix.rotZ( angleRadians );
			break;
			default:matrix.rotY( angleRadians );
		}
	}
	private List<Point3d> renderRotaryLine(GCodeContext context, T instruction) throws GkException {
		JoglViewerPreference settings = JoglViewerPreference.getInstance();
		ArrayList<Point3d> vertices = new ArrayList<Point3d>();

		Tuple6b startTuple = context.getPosition();//new Tuple6b(context.getX(), context.getY(), context.getZ(), context.getA(), context.getB(), context.getC());
		GCodeContext postContext = new GCodeContext(context);
		instruction.apply(postContext);
		Tuple6b 		endTuple 	=  postContext.getPosition();
		
		Point3d startPoint 	= startTuple.toPoint3d(JoglUtils.JOGL_UNIT);
		Point3d endPoint 	=   endTuple.toPoint3d(JoglUtils.JOGL_UNIT);

		Matrix4d rotationMatrix = new Matrix4d();

		double deltaAngleDeg = 0;

		Vector3d deltaVector = new Vector3d(endPoint);
		deltaVector.sub(startPoint);

		Point3d startAngle3d	= startTuple.angleToPoint3d(AngleUnit.DEGREE_ANGLE);
		Point3d endAngle3d	 	=   endTuple.angleToPoint3d(AngleUnit.DEGREE_ANGLE);
		double startAngle = startAngle3d.x;
		deltaAngleDeg = endAngle3d.x - startAngle3d.x;

		int nbPoints = (int) Math.max(1,Math.abs(deltaAngleDeg) / 5);
		deltaVector.scale(1.0/nbPoints);
		double startAngleRad = Math.toRadians(startAngle);
		double stepAngleRad = Math.toRadians(deltaAngleDeg) / nbPoints;

		Point3d rotaryAxisPosition = settings.getRotaryAxisPosition().toPoint3d(JoglUtils.JOGL_UNIT);
		Point3d generatorVector = new Point3d(startPoint);
		generatorVector.sub(rotaryAxisPosition);
		Point3d tmpVector 		= new Point3d(generatorVector);


		for(int i = 0; i <= nbPoints; i++){
			rotationMatrix.setIdentity();
			rotateMatrix(rotationMatrix, startAngleRad + i*stepAngleRad);
			rotationMatrix.transform(generatorVector,tmpVector);
			tmpVector.add(rotaryAxisPosition);
			vertices.add( new Point3d(tmpVector.x, tmpVector.y, tmpVector.z) );
			generatorVector.add( deltaVector );
		}
		return vertices;
	}
}

package org.goko.viewer.jogl.utils.render.gcode.geometry;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Matrix4d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.bean.commands.LinearMotionCommand;
import org.goko.viewer.jogl.preferences.JoglViewerPreference;
import org.goko.viewer.jogl.service.JoglUtils;

/**
 * Linear motion renderer
 *
 * @author PsyKo
 *
 */
public class LinearMotionRenderer{

	public List<Point3d> render(LinearMotionCommand command) throws GkException{
		JoglViewerPreference settings = JoglViewerPreference.getInstance();
		if(command != null){
			if(settings.isRotaryAxisEnabled()){
				// The complete angle around the 4th axis
				double deltaAngle = 0;
				// FIXME Use a setting to define the 4th axis and then do a dynamic angle detection around the axe A,B or C
				if( command.getAbsoluteEndCoordinate().getA() != null){
					deltaAngle = command.getAbsoluteEndCoordinate().getA().doubleValue() - command.getAbsoluteStartCoordinate().getA().doubleValue();
				}

				if(Math.abs(deltaAngle) <= 0.0001){
					return renderLinearLine(command);
				}else{
					return renderRotaryLine(command);
				}
			}else{
				return renderLinearLine(command);
			}
		}
		return new ArrayList<Point3d>();
	}

	private List<Point3d> renderLinearLine(LinearMotionCommand command) throws GkException {
		JoglViewerPreference settings = JoglViewerPreference.getInstance();
		List<Point3d> vertices = new ArrayList<Point3d>();
		Point3d startPoint 	= command.getAbsoluteStartCoordinate().to(JoglUtils.JOGL_UNIT).toPoint3d();
		Point3d endPoint 	= command.getAbsoluteEndCoordinate().to(JoglUtils.JOGL_UNIT).toPoint3d();

		if(settings.isRotaryAxisEnabled()){
			// It's a simple line, but it doesn't mean the rotary (A, B or C) value is at 0
			if(command.getAbsoluteStartCoordinate().getA() != null){
				Matrix4d rotationMatrix = new Matrix4d();
				rotateMatrix(rotationMatrix, Math.toRadians(command.getAbsoluteStartCoordinate().getA().doubleValue()));

				if(settings.getRotaryAxisPosition() != null){
					startPoint.sub(settings.getRotaryAxisPosition().toPoint3d());
					endPoint.sub(settings.getRotaryAxisPosition().toPoint3d());
				}
				rotationMatrix.transform(startPoint);
				rotationMatrix.transform(endPoint);
				if(settings.getRotaryAxisPosition() != null){
					startPoint.add(settings.getRotaryAxisPosition().toPoint3d());
					endPoint.add(settings.getRotaryAxisPosition().toPoint3d());
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
	private List<Point3d> renderRotaryLine(LinearMotionCommand command) throws GkException {
		JoglViewerPreference settings = JoglViewerPreference.getInstance();
		ArrayList<Point3d> vertices = new ArrayList<Point3d>();

		Point3d startPoint 	= command.getAbsoluteStartCoordinate().to(JoglUtils.JOGL_UNIT).toPoint3d();

		Matrix4d rotationMatrix = new Matrix4d();

		double deltaAngleDeg = 0;

		Vector3d deltaVector = new Vector3d(command.getAbsoluteEndCoordinate().to(JoglUtils.JOGL_UNIT).toPoint3d());
		deltaVector.sub(command.getAbsoluteStartCoordinate().to(JoglUtils.JOGL_UNIT).toPoint3d());

		double startAngle = command.getAbsoluteStartCoordinate().getA().doubleValue();
		deltaAngleDeg = command.getAbsoluteEndCoordinate().getA().doubleValue() - command.getAbsoluteStartCoordinate().getA().doubleValue();

		int nbPoints = (int) Math.max(1,Math.abs(deltaAngleDeg) / 5);
		deltaVector.scale(1.0/nbPoints);
		double startAngleRad = Math.toRadians(startAngle);
		double stepAngleRad = Math.toRadians(deltaAngleDeg) / nbPoints;

		//startPoint.sub(settings.getRotaryAxisPosition().toPoint3d());
		Point3d generatorVector = new Point3d(startPoint);
		generatorVector.sub(settings.getRotaryAxisPosition().toPoint3d());
		Point3d tmpVector 		= new Point3d(generatorVector);


		for(int i = 0; i <= nbPoints; i++){
			rotationMatrix.setIdentity();
			rotateMatrix(rotationMatrix, startAngleRad + i*stepAngleRad);
			rotationMatrix.transform(generatorVector,tmpVector);
			tmpVector.add(settings.getRotaryAxisPosition().toPoint3d());
			vertices.add( new Point3d(tmpVector.x, tmpVector.y, tmpVector.z) );
			generatorVector.add( deltaVector );
		}
		return vertices;
	}
}

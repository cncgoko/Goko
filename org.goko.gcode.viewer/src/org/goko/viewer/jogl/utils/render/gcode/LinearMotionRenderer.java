package org.goko.viewer.jogl.utils.render.gcode;

import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL2;
import javax.vecmath.Matrix4d;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3d;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.bean.commands.LinearMotionCommand;
import org.goko.viewer.jogl.service.JoglRendererProxy;
import org.goko.viewer.jogl.service.JoglViewerSettings;
import org.goko.viewer.jogl.utils.styler.IJoglGCodeCommandStyler;

/**
 * Linear motion renderer
 *
 * @author PsyKo
 *
 */
public class LinearMotionRenderer extends AbstractGCodeCommandRenderer<LinearMotionCommand>{

	/** (inheritDoc)
	 * @see org.goko.viewer.jogl.utils.render.IJoglRenderer#render(org.goko.viewer.jogl.service.JoglRendererProxy)
	 */
	@Override
	public void render(LinearMotionCommand command, JoglRendererProxy proxy, IJoglGCodeCommandStyler<LinearMotionCommand> styler ) throws GkException{
		JoglViewerSettings settings = JoglViewerSettings.getInstance();
		if(command != null){
			if(settings.isRotaryAxisEnabled()){
				// The complete angle around the 4th axis
				double deltaAngle = 0;
				// FIXME Use a setting to define the 4th axis and then do a dynamic angle detection around the axe A,B or C
				if( command.getAbsoluteEndCoordinate().getA() != null){
					deltaAngle = command.getAbsoluteEndCoordinate().getA().doubleValue() - command.getAbsoluteStartCoordinate().getA().doubleValue();
				}

				if(Math.abs(deltaAngle) == 0.0){
					renderLinearLine(command, proxy, styler);
				}else{
					renderRotaryLine(command, proxy, styler);
				}
			}else{
				renderLinearLine(command, proxy, styler);
			}
		}
	}

	private void renderLinearLine(LinearMotionCommand command, JoglRendererProxy proxy, IJoglGCodeCommandStyler<LinearMotionCommand> styler ) throws GkException {
		JoglViewerSettings settings = JoglViewerSettings.getInstance();

		GL2 gl = proxy.getGl();
		gl.glPushMatrix();

		styler.enableRenderingStyle(command, proxy);
		Point3f color = styler.getVertexColor(command, proxy);

		Point3d startPoint 	= command.getAbsoluteStartCoordinate().toPoint3d();
		Point3d endPoint 	= command.getAbsoluteEndCoordinate().toPoint3d();

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

		gl.glBegin(GL2.GL_LINES);


		gl.glColor3d(color.x, color.y, color.z);
		gl.glVertex3d(startPoint.x,startPoint.y,startPoint.z);
		gl.glColor3d(color.x, color.y, color.z);
		gl.glVertex3d(endPoint.x,endPoint.y,endPoint.z);

		gl.glEnd();

		gl.glPopMatrix();
		styler.disableRenderingStyle(command, proxy);
	}

	private void rotateMatrix(Matrix4d matrix, double angleRadians){
		JoglViewerSettings settings = JoglViewerSettings.getInstance();
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
	private void renderRotaryLine(LinearMotionCommand command, JoglRendererProxy proxy, IJoglGCodeCommandStyler<LinearMotionCommand> styler ) throws GkException {
		JoglViewerSettings settings = JoglViewerSettings.getInstance();
		GL2 gl = proxy.getGl();
		gl.glPushMatrix();

		styler.enableRenderingStyle(command, proxy);
		Point3f color = styler.getVertexColor(command, proxy);

		Point3d startPoint 	= command.getAbsoluteStartCoordinate().toPoint3d();

		Matrix4d rotationMatrix = new Matrix4d();

		double deltaAngleDeg = 0;

		Vector3d deltaVector = new Vector3d(command.getAbsoluteEndCoordinate().toPoint3d());
		deltaVector.sub(command.getAbsoluteStartCoordinate().toPoint3d());

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

		List<Float> lstVertices = new ArrayList<Float>();

		for(int i = 0; i <= nbPoints; i++){
			rotationMatrix.setIdentity();
			rotateMatrix(rotationMatrix, startAngleRad + i*stepAngleRad);
			rotationMatrix.transform(generatorVector,tmpVector);
			tmpVector.add(settings.getRotaryAxisPosition().toPoint3d());
			lstVertices.add( (float)tmpVector.x);
			lstVertices.add( (float)tmpVector.y);
			lstVertices.add( (float)tmpVector.z);
			generatorVector.add( deltaVector );
		}

		gl.glBegin(GL2.GL_LINE_STRIP);

		for(int i = 0; i <= (nbPoints)*3 ;i+=3){
			gl.glColor3d(color.x, color.y, color.z);
			gl.glVertex3f(lstVertices.get(i),lstVertices.get(i+1),lstVertices.get(i+2));
		}

		gl.glEnd();

		gl.glPopMatrix();
		styler.disableRenderingStyle(command, proxy);
	}
}

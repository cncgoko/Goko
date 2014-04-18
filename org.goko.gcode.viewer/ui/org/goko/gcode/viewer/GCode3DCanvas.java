/*
 *
 *   Goko
 *   Copyright (C) 2013  PsyKo
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.goko.gcode.viewer;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GL2ES1;
import javax.media.opengl.GL2GL3;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLContext;
import javax.media.opengl.GLDrawableFactory;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.opengl.GLCanvas;
import org.eclipse.swt.opengl.GLData;
import org.eclipse.swt.widgets.Composite;
import org.goko.core.common.exception.GkException;
import org.goko.core.controller.IControllerService;
import org.goko.core.gcode.bean.GCodeCommand;
import org.goko.core.gcode.bean.GCodeContext;
import org.goko.core.gcode.bean.GCodeFile;
import org.goko.core.gcode.bean.IGCodeProvider;
import org.goko.gcode.viewer.camera.AbstractCamera;
import org.goko.gcode.viewer.camera.OrthographicCamera;
import org.goko.gcode.viewer.camera.PerspectiveCamera;
import org.goko.gcode.viewer.generator.AbstractGCodeGlRenderer;
import org.goko.gcode.viewer.generator.buffered.GlGCodeBufferedRendererFactory;

public class GCode3DCanvas extends GLCanvas implements GLEventListener, PaintListener {
	/** Display grid ??*/
	private boolean showGrid = true;
	private Point3d currentPosition;
	private GLContext glcontext;
	private AbstractCamera camera;
	private IGCodeProvider commandProvider;
	private GlGCodeBufferedRendererFactory rendererFactory;
	private boolean renderEnabled;

	@Inject
	IControllerService conteollerService;

	public GCode3DCanvas(Composite parent, int style, GLData data) {
		super(parent, style, data);
		renderEnabled = true;
		//rendererFactory = new GlGCodeBufferedRendererFactory();
		rendererFactory = new GlGCodeBufferedRendererFactory();
		camera 			= new PerspectiveCamera(this);
		setCurrent();
		GLProfile glprofile = GLProfile.get(GLProfile.GL2);

		glcontext = GLDrawableFactory.getFactory(glprofile).createExternalGLContext();

		addPaintListener(this);
	}

	@Override
	public void display(GLAutoDrawable gl) {

	}

	@Inject
	@Optional
	private void getNotified(@UIEventTopic("gcodefile") GCodeFile file){
		rendererFactory.clear();
		setGCodeFile(file);

	}

	public void setGCodeFile(GCodeFile gCodeFile){
		this.commandProvider = gCodeFile;
		redraw();
	}

	public void setPerspectiveCamera(){
		camera = new PerspectiveCamera(this);
	}

	public void setOrthographicCamera(){
		camera = new OrthographicCamera(this);
	}

	protected void setup(GL2 gl){
		Rectangle bounds = getBounds();
		float width 	= bounds.width;
		float height 	= bounds.height;
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glEnable(GL2.GL_BLEND);

		gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
		gl.glHint(GL2ES1.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);
        gl.glClearDepth(1.0);
        gl.glPolygonMode(GL2.GL_BACK, GL2GL3.GL_FILL);
        gl.glLineWidth(1);
        gl.glEnable(GL.GL_DEPTH_TEST);

        float pos[] = {10,10,10};
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, pos, 0);
        float dif[] = {1.0f,1.0f,1.0f,1.0f};
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, dif, 0);
        float amb[] = {0.5f,0.5f, 0.5f, 1.0f};
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, amb, 0);

        camera.setup();
        camera.updatePosition();
      	gl.glMatrixMode( GL2.GL_MODELVIEW);


		gl.glViewport(0, 0, (int)width, (int)height);
	}

	protected void drawAxis(GL2 gl) {
		 gl.glLineWidth(2);

		gl.glBegin(GL2.GL_LINE_STRIP);
		gl.glColor3d(1, 0, 0);
		gl.glVertex3d(0, 0, 0);
		gl.glVertex3d(10, 0, 0);
		gl.glColor3d(0, 1, 0);
		gl.glVertex3d(0, 0, 0);
		gl.glVertex3d(0, 10, 0);
		gl.glColor3d(0, 0, 1);
		gl.glVertex3d(0, 0, 0);
		gl.glVertex3d(0, 0, 10);
		gl.glEnd();
	}

	protected void drawTool(GL2 gl){
		if(currentPosition == null){
			currentPosition = new Point3d(0,0,0);
		}

		if(true){
			gl.glEnable(GL2.GL_LIGHTING);
			gl.glEnable(GL2.GL_LIGHT0);

			gl.glBegin(GL2.GL_TRIANGLE_FAN);

			gl.glLineWidth(0f);
			gl.glColor4d(1, 1, 0,1);
			float[] diffuse = {1,1,0,0.4f};
			float[] ambient = {1,1,0,0.4f};
			gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, diffuse,0);
			gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, ambient,0);
			gl.glNormal3d(0,0,-1);
			gl.glVertex3d(currentPosition.x, currentPosition.y, currentPosition.z);
			int radius = 3;
			int height = 3;
			int nbPts = 10;
			Vector3d u = new Vector3d();
			double angle = Math.PI * 2 / 10;

			for(int i = 0; i <= nbPts; i++){
				double cosA = Math.cos( i * angle);
				double sinA = Math.sin( i * angle);
				u.x =  (radius * cosA);
				u.y =  (radius * sinA);
				u.z =  0;
				u.normalize();
				gl.glNormal3d(u.x, u.y, u.z);
				gl.glVertex3d(currentPosition.x + radius * cosA, currentPosition.y + radius * sinA, currentPosition.z + height);
			}
			gl.glEnd();

			gl.glBegin(GL2.GL_TRIANGLE_STRIP);

			for(int i = 0; i <= nbPts; i++){
				double cosA = Math.cos( i * angle);
				double sinA = Math.sin( i * angle);
				u.x =  (radius *cosA);
				u.y =  (radius * sinA);
				u.z = 0;
				u.normalize();
				gl.glNormal3d(u.x, u.y, u.z);
				gl.glVertex3d(currentPosition.x + radius *cosA, currentPosition.y + radius * sinA, currentPosition.z + height);
				gl.glNormal3d(u.x, u.y, u.z);
				gl.glVertex3d(currentPosition.x + radius *cosA, currentPosition.y + radius * sinA, currentPosition.z + height + 6);

			}

			gl.glEnd();

			gl.glBegin(GL2.GL_TRIANGLE_STRIP);
			for(int i = 0; i <= nbPts; i++){
				double cosA = Math.cos( i * angle);
				double sinA = Math.sin( i * angle);
				gl.glVertex3d(currentPosition.x + radius *cosA, currentPosition.y + radius * sinA, currentPosition.z + height);
				gl.glVertex3d(currentPosition.x + radius *cosA, currentPosition.y + radius * sinA, currentPosition.z + height + 6);
			}
			gl.glEnd();
			gl.glDisable(GL2.GL_LIGHTING);
			gl.glDisable(GL2.GL_LIGHT0);

		}
	}
	protected void drawGCode(GL2 gl) throws GkException{
		gl.glLineWidth(1.3f);

		if(commandProvider != null){
			GCodeContext context =  new GCodeContext();

			List<GCodeCommand> lstGcode = new ArrayList<GCodeCommand>(commandProvider.getGCodeCommands());
			for (GCodeCommand gCodeCommand : lstGcode) {
				AbstractGCodeGlRenderer<GCodeCommand> renderer = rendererFactory.getRenderer(gCodeCommand);
				if(renderer != null){
					renderer.render(context, gCodeCommand, gl);
				}
				gCodeCommand.updateContext(context);
			}
		}

	}

	/**
	 * Draw grids
	 * @param gl
	 */
	protected void drawGrid(GL2 gl){

		 gl.glLineWidth(0.1f);

		gl.glBegin(GL2.GL_LINES);
		// Main divisions
		gl.glColor4d(0.4, 0.4, 0.4, 0.7);
		int size = 30;
		for(int i = -size; i <= size ; i+=10){
			gl.glVertex3d(i, -size, 0);
			gl.glVertex3d(i, size, 0);
			gl.glVertex3d(-size,i, 0);
			gl.glVertex3d(size,i, 0);
		}
		gl.glEnd();
		gl.glLineWidth(0.5f);
		gl.glLineStipple(4, (short)0xA0A0);
		gl.glBegin(GL2.GL_LINES);

		gl.glColor4d(0.6, 0.6, 0.6, 0.1);
		for(int i = -size; i <= size ; i++){
			if(i % 10 != 0){
				gl.glVertex3d(i, -size, 0);
				gl.glVertex3d(i, size, 0);
				gl.glVertex3d(-size,i, 0);
				gl.glVertex3d(size,i, 0);
			}
		}
		gl.glDisable(GL2.GL_LINE_STIPPLE);
		// sub divisions
		gl.glEnd();
	}

	@Override
	public void dispose(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3, int arg4) {
		// TODO Auto-generated method stub

	}

	@Override
	public void paintControl(PaintEvent e) {
		setCurrent();
		glcontext.makeCurrent();

		render();
		swapBuffers();

		glcontext.release();

	}

	protected void render(){
		GL2 gl = glcontext.getGL().getGL2();
		if(renderEnabled){
			gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

			gl.glClearColor(.19f, .19f, .23f, 1.0f);

			setup(gl);
			if(showGrid){
				drawGrid(gl);
			}
			drawAxis(gl);

			try {
				drawGCode(gl);
			} catch (GkException e) {
				e.printStackTrace();
			}

			drawTool(gl);
		}
	}

	/**
	 * @return the showGrid
	 */
	public boolean isShowGrid() {
		return showGrid;
	}
	public boolean getShowGrid() {
		return isShowGrid();
	}

	/**
	 * @param showGrid the showGrid to set
	 */
	public void setShowGrid(boolean showGrid) {
		this.showGrid = showGrid;
		redraw();
	}

	/**
	 * @return the currentPosition
	 */
	public Point3d getCurrentPosition() {
		return currentPosition;
	}

	/**
	 * @param currentPosition the currentPosition to set
	 */
	public void setCurrentPosition(Point3d currentPosition) {
		this.currentPosition = currentPosition;
		redraw();
	}

	/**
	 * @return the commandProvider
	 */
	public IGCodeProvider getCommandProvider() {
		return commandProvider;
	}

	/**
	 * @param commandProvider the commandProvider to set
	 */
	public void setCommandProvider(IGCodeProvider commandProvider) {
		this.commandProvider = commandProvider;
	}


	public boolean isRenderEnabled(){
		return renderEnabled;
	}

	public boolean getRenderEnabled(){
		return renderEnabled;
	}

	public void setRenderEnabled(boolean enabled){
		renderEnabled = enabled;
		redraw();
	}

}

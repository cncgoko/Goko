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
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLContext;
import javax.media.opengl.GLDrawableFactory;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.glu.GLU;
import javax.vecmath.Point3d;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.opengl.GLCanvas;
import org.eclipse.swt.opengl.GLData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.wb.swt.SWTResourceManager;
import org.goko.core.common.exception.GkException;
import org.goko.core.controller.IControllerService;
import org.goko.core.gcode.bean.GCodeCommand;
import org.goko.core.gcode.bean.GCodeContext;
import org.goko.core.gcode.bean.GCodeFile;
import org.goko.core.gcode.bean.IGCodeProvider;
import org.goko.core.gcode.service.IGCodeService;
import org.goko.gcode.viewer.camera.AbstractCamera;
import org.goko.gcode.viewer.camera.OrthographicCamera;
import org.goko.gcode.viewer.camera.PerspectiveCamera;
import org.goko.gcode.viewer.generator.GlGCodeRendererFactory;

public class GCode3DCanvas extends GLCanvas implements GLEventListener, PaintListener {
	/** Display grid ??*/
	private boolean showGrid = true;
	private Point3d currentPosition;
	private GLContext glcontext;
	private AbstractCamera camera;
	private IGCodeProvider commandProvider;
	private GlGCodeRendererFactory rendererFactory;
	private boolean renderEnabled;
	private Font overlayFont;
	private boolean forceRedraw = false;
	private boolean followTool = true;
	@Inject
	IControllerService conteollerService;
	@Inject
	IGCodeService gcodeService;

	public GCode3DCanvas(Composite parent, int style, GLData data) {
		super(parent, style, data);

		renderEnabled = true;
		rendererFactory = new GlGCodeRendererFactory();
		camera 			= new PerspectiveCamera(this);
		overlayFont = new Font(getDisplay(), "Tahoma", 10, SWT.BOLD);
		setCurrent();
		GLProfile glprofile = GLProfile.get(GLProfile.GL2);
		GLCapabilities caps = new GLCapabilities(glprofile);
		caps.setHardwareAccelerated(true);
		glcontext = GLDrawableFactory.getFactory(glprofile).createExternalGLContext();
		GL2 gl = glcontext.getGL().getGL2();
		//init(gl);
		setup(gl);
	//	setup(glcontext.getGL().getGL2());
		addPaintListener(this);
	}


	@Override
	public void display(GLAutoDrawable drawable) {
		render();
	}

	@Inject
	@Optional
	private void getNotified(@UIEventTopic("gcodefile") GCodeFile file){

		setGCodeFile(file);

	}

	public void setGCodeFile(GCodeFile gCodeFile){
		this.commandProvider = gCodeFile;
		//rendererFactory.clear();
		forceRedraw = true;
		redraw();
	}

	public void setPerspectiveCamera(){
		camera = new PerspectiveCamera(this);
	}

	public void setOrthographicCamera(){
		camera = new OrthographicCamera(this);
	}

	protected void setup(GL2 gl){
		setCurrent();
		glcontext.makeCurrent();
		GLU glu = new GLU();
		Rectangle bounds = getBounds();
		float width 	= bounds.width;
		float height 	= bounds.height;
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();

		gl.glHint(GL2ES1.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);
        gl.glClearDepth(1.0);
        gl.glPolygonMode(GL2.GL_FRONT, GL2GL3.GL_FILL);
        gl.glLineWidth(1);

        gl.glEnable(GL.GL_DEPTH_TEST);
        gl.glDepthFunc(GL.GL_LEQUAL);

        gl.glEnable(GL2.GL_BLEND);
     //   gl.glBlendFuncSeparate(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA, GL2.GL_ONE, GL2.GL_ONE);
		gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
        //gl.glBlendFunc(GL2.GL_ZERO, GL2.GL_SRC_COLOR);

        float pos[] = {10,10,10};
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, pos, 0);
        float dif[] = {1.0f,1.0f,1.0f,1.0f};
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, dif, 0);
        float amb[] = {0.5f,0.5f, 0.5f, 1.0f};
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, amb, 0);

        float pos1[] = {-10,-8,-5};
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_POSITION, pos1, 0);
        float dif1[] = {0.3f,0.3f,0.3f,1.0f};
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_DIFFUSE, dif1, 0);
        float amb1[] = {0.1f,0.1f, 0.1f, 1.0f};
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_AMBIENT, amb1, 0);

        camera.setup();
        camera.updatePosition();
      	gl.glMatrixMode( GL2.GL_MODELVIEW);
//
//
		gl.glViewport(0, 0, (int)width, (int)height);
		glcontext.release();
	}

	protected void drawAxis(GL2 gl) {
		 gl.glLineWidth(2);
		gl.glBegin(GL2.GL_LINES);

		gl.glColor3d(1, 0, 0);
		gl.glVertex3d(0, 0, 0);
		gl.glColor3d(1, 0, 0);
		gl.glVertex3d(10, 0, 0);

		gl.glColor3d(0, 1, 0);
		gl.glVertex3d(0, 0, 0);
		gl.glColor3d(0, 1, 0);
		gl.glVertex3d(0, 10, 0);

		gl.glColor3d(0, 0, 1);
		gl.glVertex3d(0, 0, 0);
		gl.glColor3d(0, 0, 1);
		gl.glVertex3d(0, 0, 10);
		gl.glEnd();
	}

	protected void drawPathToTool(GL2 gl){
		if(currentPosition == null){
			currentPosition = new Point3d(0,0,0);
		}

		gl.glLineWidth(1.5f);

		gl.glEnable(GL2.GL_LINE_STIPPLE);
		gl.glLineStipple(4, (short)0xAAAA);

		gl.glBegin(GL2.GL_LINE_STRIP);
		gl.glColor3d(1, 0, 0);
		gl.glVertex3d(0, 0, 0);
		gl.glVertex3d(currentPosition.x, 0, 0);
		gl.glColor3d(0, 1, 0);
		gl.glVertex3d(currentPosition.x, 0, 0);
		gl.glVertex3d(currentPosition.x, currentPosition.y, 0);
		gl.glColor3d(0, 0, 1);
		gl.glVertex3d(currentPosition.x, currentPosition.y, 0);
		gl.glVertex3d(currentPosition.x, currentPosition.y, currentPosition.z);

		gl.glEnd();
		gl.glDisable(GL2.GL_LINE_STIPPLE);


	}
	protected void drawTool(GL2 gl){
		if(currentPosition == null){
			currentPosition = new Point3d(0,0,0);
		}

		if(followTool){
			camera.lookAt(currentPosition);
		}
		gl.glPushMatrix();
		gl.glTranslated(currentPosition.x, currentPosition.y, currentPosition.z);
		/*GLU glu = new GLU();
		gl.glDisable(GL2.GL_DEPTH_TEST);
		gl.glEnable(GL2.GL_LIGHTING);
		gl.glEnable(GL2.GL_LIGHT0);
		gl.glEnable(GL2.GL_LIGHT1);
		float[] diffuse = {0.8f,0.8f,0,1f,0.1f};
		float[] ambient = {0.3f,0.3f,0,1f,0.1f};
		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_DIFFUSE, diffuse,0);
		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT, ambient,0);
		//GLUquadric quad = glu.gluNewQuadric();
		//glu.gluCylinder(quad, 0.0f, 3.0/2, 4.0, 32, 1);
		gl.glDisable(GL2.GL_LIGHTING);
		gl.glDisable(GL2.GL_LIGHT0);
		gl.glDisable(GL2.GL_LIGHT1);*/

		gl.glEnable(GL2.GL_DEPTH_TEST);

		gl.glLineWidth(3.0f);
		gl.glBegin(GL2.GL_LINE_STRIP);
		gl.glColor4f(0.87f, 0.31f, 0.43f,1f);
		int segmentCount = 16;
		int radius = 3/2;
		float angle = (float) ((Math.PI * 2) / segmentCount);
		for(int i = 0; i <= segmentCount; i++){
			double x = radius*Math.cos( i*angle);
			double y = radius*Math.sin( i*angle);
			gl.glVertex3d(x, y, 6);
		}
		gl.glEnd();
		gl.glBegin(GL2.GL_LINES);
		gl.glVertex3d(radius, 0, 6);
		gl.glVertex3d(0, 0, 0);
		gl.glVertex3d(0, 0, 0);
		gl.glVertex3d(-radius, 0, 6);
		gl.glVertex3d(0,radius, 6);
		gl.glVertex3d(0, 0, 0);
		gl.glVertex3d(0, 0, 0);
		gl.glVertex3d(0,-radius, 6);
		gl.glEnd();
		gl.glPopMatrix();
	}
	protected void drawGCode(GL2 gl) throws GkException{
		gl.glLineWidth(1.3f);
		if(forceRedraw){
			forceGCodeRedraw(gl);
		}else{
			if(commandProvider != null){

				List<GCodeCommand> lstGcode = new ArrayList<GCodeCommand>(commandProvider.getGCodeCommands());
				for (GCodeCommand gCodeCommand : lstGcode) {
					rendererFactory.render(gCodeCommand, gl);
					//AbstractGCodeGlRenderer<? extends GCodeCommand> renderer = rendererFactory.getRenderer(gCodeCommand);
//					if(renderer != null){
//						renderer.render(gCodeCommand, gl);
//					}
				}
			}
		}

	}

	protected void drawGCodeBounds(GL2 gl) throws GkException{

	}
	protected void forceGCodeRedraw(GL2 gl) throws GkException{
		if(commandProvider != null){
			GCodeContext context =  new GCodeContext();
			GCodeContext postContext = null;

			List<GCodeCommand> lstGcode = new ArrayList<GCodeCommand>(commandProvider.getGCodeCommands());
			for (GCodeCommand gCodeCommand : lstGcode) {
				rendererFactory.render(gCodeCommand, gl);
				/*postContext =  new GCodeContext(context);
				gcodeService.update(postContext, gCodeCommand);
				AbstractGCodeGlRendererOld renderer = rendererFactory.getRenderer(postContext, gCodeCommand);
				if(renderer != null){
					renderer.render(context, postContext, gCodeCommand, gl);
				}
				gcodeService.update(context, gCodeCommand);*/
			}
		}
		forceRedraw = false;
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
		int size = 100;
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
			if(i == 0){
				gl.glColor4d(0.8, 0.8, 0.8, 0.50);
				gl.glVertex3d(i, -size, 0);
				gl.glVertex3d(i, size, 0);
				gl.glVertex3d(-size,i, 0);
				gl.glVertex3d(size,i, 0);
			}else{
				if(i % 10 != 0){
					gl.glColor4d(0.6, 0.6, 0.6, 0.1);
					gl.glVertex3d(i, -size, 0);
					gl.glVertex3d(i, size, 0);
					gl.glVertex3d(-size,i, 0);
					gl.glVertex3d(size,i, 0);
				}
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
	public void init(GLAutoDrawable drawable) {
		System.err.println("ini");
		final GL gl = drawable.getGL();
		setup(gl.getGL2());
	}

	private void init(GL2 gl) {
		System.err.println("init(GL2)");
		setCurrent();
		glcontext.makeCurrent();
		Rectangle bounds = getBounds();
		float width 	= bounds.width;
		float height 	= bounds.height;
		gl.glMatrixMode(GL2.GL_PROJECTION);


		gl.glHint(GL2ES1.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);
        gl.glClearDepth(1.0);
        gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2GL3.GL_FILL);
        gl.glLineWidth(1);

        gl.glEnable(GL.GL_DEPTH_TEST);
        gl.glDepthFunc(GL.GL_LEQUAL);

        //gl.glEnable(GL2.GL_BLEND);
		//gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);


    /*    float pos[] = {10,10,10};
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, pos, 0);
        float dif[] = {1.0f,1.0f,1.0f,1.0f};
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, dif, 0);
        float amb[] = {0.5f,0.5f, 0.5f, 1.0f};
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, amb, 0);
*/
        camera.setup();
        camera.updatePosition();
      	gl.glMatrixMode( GL2.GL_MODELVIEW);

		gl.glViewport(0, 0, (int)width, (int)height);
		glcontext.release();
	}

	@Override
	public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3, int arg4) {
		init(arg0);
	}
	@Override
	public void setFont(Font font) {
		// TODO Auto-generated method stub
		super.setFont(font);
	}

	@Override
	public void paintControl(PaintEvent e) {
		if(renderEnabled){
			setCurrent();
			glcontext.makeCurrent();

			render();
			swapBuffers();

			glcontext.release();
		}else{
			e.gc.setBackground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
			e.gc.fillRectangle(new Rectangle(e.x,e.y,e.width,e.height));
	        e.gc.setFont(overlayFont);
	        e.gc.setForeground(getDisplay().getSystemColor(SWT.COLOR_YELLOW));
	        e.gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_BLACK));
			String disabled = "-- Disabled --";
			int width = e.gc.getFontMetrics().getAverageCharWidth() * StringUtils.length(disabled);
			int height = e.gc.getFontMetrics().getHeight();
	        e.gc.drawString(disabled, e.x + e.width / 2 - width /2, e.y + e.height / 2 - height);


		}
	}

	protected void render(){
        GL2 gl = glcontext.getGL().getGL2();
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		setup(gl);
		gl.glClearColor(.19f, .19f, .23f, 1.0f);

		drawTool(gl);

		if(showGrid){
			drawGrid(gl);
		}
		drawAxis(gl);

		try {
			drawGCode(gl);
		} catch (GkException e) {
			e.printStackTrace();
		}

//		drawTool(gl);
		drawPathToTool(gl);


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
		this.redraw();
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


	/**
	 * @return the followTool
	 */
	public boolean getFollowTool() {
		return followTool;
	}


	/**
	 * @param followTool the followTool to set
	 */
	public void setFollowTool(boolean followTool) {
		this.followTool = followTool;
	}

}

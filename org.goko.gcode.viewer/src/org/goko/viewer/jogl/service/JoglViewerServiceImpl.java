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
package org.goko.viewer.jogl.service;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkFunctionalException;
import org.goko.core.controller.IControllerService;
import org.goko.core.gcode.bean.IGCodeProvider;
import org.goko.core.log.GkLog;
import org.goko.core.viewer.renderer.IViewer3DRenderer;
import org.goko.core.workspace.service.GCodeProviderEvent;
import org.goko.core.workspace.service.IWorkspaceListener;
import org.goko.core.workspace.service.IWorkspaceService;
import org.goko.viewer.jogl.GokoJoglCanvas;
import org.goko.viewer.jogl.camera.AbstractCamera;
import org.goko.viewer.jogl.camera.OrthographicCamera;
import org.goko.viewer.jogl.camera.PerspectiveCamera;
import org.goko.viewer.jogl.utils.render.AxisRenderer;
import org.goko.viewer.jogl.utils.render.GridRenderer;
import org.goko.viewer.jogl.utils.render.IJoglRenderer;
import org.goko.viewer.jogl.utils.render.JoglRendererWrapper;
import org.goko.viewer.jogl.utils.render.ToolRenderer;
import org.goko.viewer.jogl.utils.render.gcode.BoundsRenderer;
import org.goko.viewer.jogl.utils.render.gcode.GCodeProviderRenderer;

import com.jogamp.opengl.util.awt.Overlay;

/**
 * Jogl implementation of the viewer service
 *
 * @author PsyKo
 *
 */
public class JoglViewerServiceImpl implements IJoglViewerService, IWorkspaceListener{
	/** LOG */
	private static final GkLog LOG = GkLog.getLogger(JoglViewerServiceImpl.class);
	/** SERVICE_ID */
	private static final String SERVICE_ID = "org.goko.viewer.jogl";
	/** The list of renderer */
	private List<IJoglRenderer> renderers;
	/** The map of disabled renderers by Id */
	private Map<String, Boolean> mapDisabledRenderers;
	/** Current camera */
	private AbstractCamera camera;
	/** The list of supported camera */
	private List<AbstractCamera> supportedCamera;
	/** Display canvas */
	private GokoJoglCanvas canvas;
	/** Rendering proxy */
	private JoglRendererProxy proxy;
	/** The current controller service*/
	private IControllerService controllerService;
	/** The workspace service */
	private IWorkspaceService workspaceService;
	/** Bind camera on tool position ? */
	private boolean lockCameraOnTool;
	/** Flag to enable/disable the render*/
	private boolean enabled = true;
	private GCodeProviderRenderer gcodeRenderer;
	private BoundsRenderer boundsRenderer;
	private Overlay overlay;
	private Font overlayFont;
	private int x;
	private int y;
	private int width;
	private int height;
	/** (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#getServiceId()
	 */
	@Override
	public String getServiceId() throws GkException {
		return SERVICE_ID;
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#start()
	 */
	@Override
	public void start() throws GkException {
		LOG.info("Starting "+this.getServiceId());
		this.mapDisabledRenderers = new HashMap<String, Boolean>();
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#stop()
	 */
	@Override
	public void stop() throws GkException {

	}


	/** {@inheritDoc}
	 * @see org.goko.viewer.jogl.service.IJoglViewerService#createCanvas(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public GokoJoglCanvas createCanvas(Composite parent) throws GkException {
		if(canvas != null){
			return canvas;
		}


		canvas = new GokoJoglCanvas(parent, SWT.NO_BACKGROUND, this);
		proxy 		= new JoglRendererProxy(null);

		addCamera(new PerspectiveCamera(canvas));
		addCamera(new OrthographicCamera(canvas));
		setActiveCamera(PerspectiveCamera.ID);

		addRenderer(new AxisRenderer());
		addRenderer(new GridRenderer());
		ToolRenderer toolRenderer = new ToolRenderer(controllerService);
		addRenderer(toolRenderer);
		overlayFont = new Font("SansSerif", Font.PLAIN, 12);
		return canvas;
	}



	/** {@inheritDoc}
	 * @see org.goko.core.viewer.service.IViewer3DService#addRenderer(org.goko.core.viewer.renderer.gcode.IViewer3DRenderer)
	 */
	@Override
	public void addRenderer(IViewer3DRenderer renderer) throws GkException {
		addRenderer(new JoglRendererWrapper(renderer));
	}

	/** (inheritDoc)
	 * @see org.goko.viewer.jogl.service.IJoglViewerService#addRenderer(org.goko.viewer.jogl.utils.render.IJoglRenderer)
	 */
	@Override
	public void addRenderer(IJoglRenderer renderer) throws GkException {
		getRenderers().add(renderer);
		setRendererEnabled(renderer.getId(), true);
	}

	/** (inheritDoc)
	 * @see org.goko.core.viewer.service.IViewer3DService#removeRenderer(org.goko.core.viewer.renderer.IViewer3DRenderer)
	 */
	@Override
	public void removeRenderer(IViewer3DRenderer renderer) throws GkException {
		getRenderers().remove(renderer);
	}

	/**
	 * @return the renderers
	 */
	private List<IJoglRenderer> getRenderers() {
		if(renderers == null){
			renderers = new ArrayList<IJoglRenderer>();
		}
		return renderers;
	}

	@Override
	public void renderGCode(IGCodeProvider provider) throws GkException {
		if(gcodeRenderer != null){
			//removeRenderer(gcodeRenderer);
		}
		gcodeRenderer = new GCodeProviderRenderer(provider);
		boundsRenderer = new BoundsRenderer();
		boundsRenderer.setBounds(provider.getBounds());
		//addRenderer(gcodeRenderer);
	}

	/**
	 * @return the controllerService
	 */
	public IControllerService getControllerService() {
		return controllerService;
	}

	/**
	 * @param controllerService the controllerService to set
	 */
	public void setControllerService(IControllerService controllerService) {
		this.controllerService = controllerService;
	}

	/** (inheritDoc)
	 * @see javax.media.opengl.GLEventListener#display(javax.media.opengl.GLAutoDrawable)
	 */
	@Override
	public void display(GLAutoDrawable gLAutoDrawable) {
		GL2 gl = gLAutoDrawable.getGL().getGL2();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		if(!isEnabled()){
			gl.glClearColor(0f, 0f, 0f, 1.0f);
			drawOverlay();
			return;
		}

        gl.glClearColor(.19f, .19f, .23f, 1.0f);
		gl.glLoadIdentity();
		if(camera == null){
			return;
		}
		if(!camera.isInitialized()){
			camera.reshape(gLAutoDrawable, x, y, width, height);
			camera.setInitialized(true);
		}
		if(isLockCameraOnTool() && controllerService != null){
			try {
				camera.lookAt( controllerService.getPosition());
			} catch (GkException e) {
				e.printStackTrace();
			}
		}
		camera.updateViewport(x, y, width, height);
		camera.updatePosition();
		gl.glMatrixMode(GL2.GL_MODELVIEW);
	/*	BigDecimal aPos = new BigDecimal("0");
		try {
			MachineValue<BigDecimal> valueA = controllerService.getMachineValue(DefaultControllerValues.POSITION_A, BigDecimal.class);
			aPos = valueA.getValue();
		} catch (GkException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/

		proxy.setGl(gl);
		for (IJoglRenderer renderer : getRenderers()) {
			gl.glPushMatrix();
			if (isRendererEnabled(renderer.getId())) {
				try {
					renderer.render(proxy);
				} catch (GkException e) {
					e.printStackTrace();
				}
			}
			gl.glPopMatrix();
		}
		if(gcodeRenderer!=null){
			gl.glPushMatrix();
		//	gl.glRotated(aPos.doubleValue(), 0, 1, 0);
			try {
				gcodeRenderer.render(proxy);
			} catch (GkException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			gl.glPopMatrix();
		}
		if(boundsRenderer!=null){
			gl.glPushMatrix();
		//	gl.glRotated(aPos.doubleValue(), 0, 1, 0);
			try {
				boundsRenderer.render(proxy);
			} catch (GkException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			gl.glPopMatrix();
		}
		drawOverlay();

	}


	private void drawOverlay() {
		overlay.beginRendering();
		Graphics2D g2d = overlay.createGraphics();
		try{
			if(getActiveCamera() != null){
				FontRenderContext 	frc = g2d.getFontRenderContext();
				String 				cameraString = getActiveCamera().getLabel();
				GlyphVector 		gv = overlayFont.createGlyphVector(frc, cameraString);
			    Rectangle 			bounds = gv.getPixelBounds(frc, 0, 0);
			    int x = 5;
			    int y = 5 + bounds.height;
			    g2d.setFont(overlayFont);
			    Color overlayColor = new Color(0.8f,0.8f,0.8f);
			    Color transparentColor = new Color(0,0,0,0);
			    g2d.setBackground(transparentColor);
			    g2d.setColor(overlayColor);
			    g2d.clearRect(0, 0, width, height);
			    if(isEnabled()){
			    	g2d.drawString(cameraString,x,y);
			    }else{
			    	g2d.drawString("Disabled",x,y);
			    }
				overlay.markDirty(0, 0, width, height);
				overlay.drawAll();

			}
		}catch(GkException e){
			LOG.error(e);
		}
		g2d.dispose();
		overlay.endRendering();
	}
	/** (inheritDoc)
	 * @see javax.media.opengl.GLEventListener#dispose(javax.media.opengl.GLAutoDrawable)
	 */
	@Override
	public void dispose(GLAutoDrawable arg0) {


	}

	/** (inheritDoc)
	 * @see javax.media.opengl.GLEventListener#init(javax.media.opengl.GLAutoDrawable)
	 */
	@Override
	public void init(GLAutoDrawable gLAutoDrawable) {
		GL2 gl = gLAutoDrawable.getGL().getGL2(); // get the OpenGL graphics context
		gl.glClearColor(.19f, .19f, .23f, 1.0f); // set background (clear) color
		gl.glClearDepth(1.0f); // set clear depth value to farthest
		gl.glEnable(GL2.GL_DEPTH_TEST); // enables depth testing
		gl.glDepthFunc(GL2.GL_LEQUAL); // the type of depth test to do
		gl.glEnable(GL2.GL_BLEND);
		gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
		gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST); // best perspective correction
		gl.glShadeModel(GL2.GL_SMOOTH); // blends colors nicely, and smo
		overlay = new Overlay(gLAutoDrawable);
		overlay.createGraphics();
	}

	/** (inheritDoc)
	 * @see javax.media.opengl.GLEventListener#reshape(javax.media.opengl.GLAutoDrawable, int, int, int, int)
	 */
	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		if(camera != null){
			camera.reshape(drawable, x, y, width, height);
		}
	}

	/** (inheritDoc)
	 * @see org.goko.viewer.jogl.service.IJoglViewerService#getSupportedCamera()
	 */
	@Override
	public List<AbstractCamera> getSupportedCamera() throws GkException {
		if(supportedCamera == null){
			this.supportedCamera = new ArrayList<AbstractCamera>();
		}
		return supportedCamera;
	}

	/** (inheritDoc)
	 * @see org.goko.viewer.jogl.service.IJoglViewerService#setActiveCamera(java.lang.String)
	 */
	@Override
	public void setActiveCamera(String idCamera) throws GkException {
		for (AbstractCamera tmpCamera : getSupportedCamera()) {
			if(StringUtils.equals(idCamera, tmpCamera.getId())){
				if(camera != null){
					camera.setActivated(false);
				}
				camera = tmpCamera;
				camera.updateViewport(x, y, width, height);
				camera.setActivated(true);
				return;
			}
		}
	}

	/** (inheritDoc)
	 * @see org.goko.viewer.jogl.service.IJoglViewerService#addCamera(org.goko.viewer.jogl.camera.AbstractCamera)
	 */
	@Override
	public void addCamera(AbstractCamera camera) throws GkException{
		getSupportedCamera().add(camera);
	}

	/** (inheritDoc)
	 * @see org.goko.viewer.jogl.service.IJoglViewerService#getActiveCamera()
	 */
	@Override
	public AbstractCamera getActiveCamera() throws GkException {
		return camera;
	}

	/**
	 * @return the lockCameraOnTool
	 */
	@Override
	public boolean isLockCameraOnTool() {
		return lockCameraOnTool;
	}

	/**
	 * @param lockCameraOnTool the lockCameraOnTool to set
	 */
	@Override
	public void setLockCameraOnTool(boolean lockCameraOnTool) {
		this.lockCameraOnTool = lockCameraOnTool;
	}

	/** (inheritDoc)
	 * @see org.goko.core.viewer.service.IViewer3DService#setRendererEnabled(java.lang.String, boolean)
	 */
	@Override
	public void setRendererEnabled(String idRenderer, boolean enabled) throws GkException {
		mapDisabledRenderers.put(idRenderer, enabled);
	}

	private boolean isRendererEnabled(String idRenderer){
		return mapDisabledRenderers.containsKey(idRenderer) && mapDisabledRenderers.get(idRenderer) == true ;
	}

	/** (inheritDoc)
	 * @see org.goko.viewer.jogl.service.IJoglViewerService#getJoglRenderer(java.lang.String)
	 */
	@Override
	public IJoglRenderer getJoglRenderer(String idRenderer) throws GkException {
		if(CollectionUtils.isNotEmpty(renderers)){
			for (IJoglRenderer renderer : renderers) {
				if(StringUtils.equals(renderer.getId(), idRenderer)){
					return renderer;
				}
			}
		}
		throw new GkFunctionalException("Renderer '"+idRenderer+"' does not exist.");
	}

	/**
	 * @return the enabled
	 */
	@Override
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * @param enabled the enabled to set
	 */
	@Override
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * @return the workspaceService
	 */
	public IWorkspaceService getWorkspaceService() {
		return workspaceService;
	}

	/**
	 * @param workspaceService the workspaceService to set
	 * @throws GkException
	 */
	public void setWorkspaceService(IWorkspaceService workspaceService) throws GkException {
		this.workspaceService = workspaceService;
		this.workspaceService.addWorkspaceListener(this);
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IWorkspaceListener#onGCodeProviderEvent(org.goko.core.workspace.service.GCodeProviderEvent)
	 */
	@Override
	public void onGCodeProviderEvent(GCodeProviderEvent event) throws GkException {
		if(getWorkspaceService().getCurrentGCodeProvider() != null){
			this.renderGCode(getWorkspaceService().getCurrentGCodeProvider());
		}
	}

}

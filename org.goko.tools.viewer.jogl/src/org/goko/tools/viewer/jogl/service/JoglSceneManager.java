/*
 *	This file is part of Goko.
 *
 *  Goko is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Goko is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Goko.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.goko.tools.viewer.jogl.service;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.vecmath.Color3f;
import javax.vecmath.Color4f;
import javax.vecmath.Point2i;
import javax.vecmath.Point3f;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.widgets.Composite;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkFunctionalException;
import org.goko.core.common.utils.CacheById;
import org.goko.core.common.utils.SequentialIdGenerator;
import org.goko.core.log.GkLog;
import org.goko.core.math.BoundingTuple6b;
import org.goko.core.viewer.renderer.IViewer3DRenderer;
import org.goko.tools.viewer.jogl.GokoJoglCanvas;
import org.goko.tools.viewer.jogl.camera.AbstractCamera;
import org.goko.tools.viewer.jogl.camera.PerspectiveCamera;
import org.goko.tools.viewer.jogl.camera.orthographic.FrontCamera;
import org.goko.tools.viewer.jogl.camera.orthographic.LeftCamera;
import org.goko.tools.viewer.jogl.camera.orthographic.TopCamera;
import org.goko.tools.viewer.jogl.preferences.JoglViewerPreference;
import org.goko.tools.viewer.jogl.service.overlay.CameraNameOverlay;
import org.goko.tools.viewer.jogl.service.utils.CoreJoglRendererAlphaComparator;
import org.goko.tools.viewer.jogl.shaders.EnumGokoShaderProgram;
import org.goko.tools.viewer.jogl.shaders.ShaderLoader;
import org.goko.tools.viewer.jogl.utils.light.Light;
import org.goko.tools.viewer.jogl.utils.overlay.IOverlayRenderer;

import com.jogamp.opengl.DebugGL3;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.PMVMatrix;
import com.jogamp.opengl.util.awt.Overlay;

public abstract class JoglSceneManager implements GLEventListener, IPropertyChangeListener{
	/** LOG */
	private static final GkLog LOG = GkLog.getLogger(JoglSceneManager.class);
	/** Flag to enable/disable the render*/
	private boolean enabled = true;
	private Overlay overlay; 
	private int x;
	private int y;
	private int width;
	private int height;

	/** Current camera */
	private AbstractCamera camera;
	/** The list of supported camera */
	private List<AbstractCamera> supportedCamera;
	/** Display canvas */
	private GokoJoglCanvas canvas;	
	/** The list of renderer */
	private List<ICoreJoglRenderer> renderers;
	/** The list of renderer to remove */
	private List<ICoreJoglRenderer> renderersToRemove;
	/** The list of overlay renderer */
	private CacheById<IOverlayRenderer> overlayRenderers;

	private GLCapabilities canvasCapabilities;
	private Map<Integer, Boolean> layerVisibility;
	private Light light0;
	private Light light1;
	private Color3f backgroundColor;
	private boolean updateBackgroundColor;
	private Point2i mouseCanvasPosition;
	private boolean displayPositionOverlay = true;
	
	public JoglSceneManager() {
		getRenderers();
		initLayers();
		this.renderersToRemove 	= new ArrayList<ICoreJoglRenderer>();
		this.overlayRenderers = new CacheById<IOverlayRenderer>(new SequentialIdGenerator());		
		JoglViewerPreference.getInstance().addPropertyChangeListener(this);
		displayPositionOverlay = JoglViewerPreference.getInstance().isDisplayPositionOverlay();
	}

	private void initLayers() {
		this.layerVisibility 	= new HashMap<Integer, Boolean>();
		this.layerVisibility.put(Layer.LAYER_GRIDS, true);
		this.layerVisibility.put(Layer.LAYER_BOUNDS, true);
		this.layerVisibility.put(Layer.LAYER_DEFAULT, true);
	}

	public GokoJoglCanvas createCanvas(Composite parent) throws GkException {
		LOG.info("GokoJoglCanvas createCanvas(Composite parent) throws GkException");
		if(canvas != null){
			return canvas;
		}
		
		GLProfile profile = GLProfile.getMaxProgrammable(true);//GLProfile.getMaxFixedFunc(true);//getMaxProgrammable(true);//GLProfile.getMaxFixedFunc(true);//getDefault();
		LOG.info("GLProfile.getMaxFixedFunc(true)");
		canvasCapabilities = new GLCapabilities(profile);
		canvasCapabilities.setSampleBuffers(true);
		
		
	    canvasCapabilities.setNumSamples(JoglViewerPreference.getInstance().getMultisampling());
	    canvasCapabilities.setHardwareAccelerated(true);
	    canvasCapabilities.setDoubleBuffered(true);

		canvas 		= new GokoJoglCanvas(parent, SWT.NO_BACKGROUND, canvasCapabilities);
		canvas.addGLEventListener(this);
		LOG.info(" new GokoJoglCanvas(parent, SWT.NO_BACKGROUND, canvasCapabilities);");
		
		addCamera(new PerspectiveCamera(canvas, this));
		addCamera(new TopCamera(canvas, this));
		addCamera(new LeftCamera(canvas, this));
		addCamera(new FrontCamera(canvas, this));
		String defaultCamera = JoglViewerPreference.getInstance().getDefaultCamera();
		LOG.info("defaultCamera is "+defaultCamera);
		if(isSupportedCamera(defaultCamera)){
			setActiveCamera(defaultCamera);	
		}else{
			setActiveCamera(PerspectiveCamera.ID);
			JoglViewerPreference.getInstance().setDefaultCamera(PerspectiveCamera.ID);
		}
		

		addOverlayRenderer(new CameraNameOverlay(this));
		
		canvas.addMouseMoveListener(new MouseMoveListener() {
			
			@Override
			public void mouseMove(MouseEvent e) {
				mouseCanvasPosition = new Point2i(e.x, e.y);
			}
		});
		onCanvasCreated(canvas);
				
		return canvas;
	}

	protected abstract void onCanvasCreated(GokoJoglCanvas canvas) throws GkException;

	/**
	 * Initialization of the lights
	 */
	protected void initLights(){
		light0 = new Light(new Point3f(1000,1000,1000), new Color4f(0.5f,0.5f,0.45f,1), new Color4f(0.25f,0.2f,0.2f,1));
		light1 = new Light(new Point3f(-500,-1000,-600), new Color4f(0.3f,0.3f,0.31f,1), new Color4f(0.1f,0.1f,0.15f,1));
	}

	/** (inheritDoc)
	 * @see javax.media.opengl.GLEventListener#display(javax.media.opengl.GLAutoDrawable)
	 */
	@Override
	public void display(GLAutoDrawable gLAutoDrawable) {		
		LOG.info("GL Profile: "+gLAutoDrawable.getGL().glGetString(GL.GL_VERSION));
		GL3 gl = JoglUtils.getSupportedGL(gLAutoDrawable);//gLAutoDrawable.getGL().getGL4();
		if(updateBackgroundColor){
			gl.glClearColor(backgroundColor.x, backgroundColor.y, backgroundColor.z, 1.0f); // reset background (clear) color
			updateBackgroundColor = false;
		}
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

        if(!isEnabled()){
        	return;
        }
		if(camera == null){
			return;
		}
		updateCamera(gLAutoDrawable, gl);

		PMVMatrix cameraMatrix = camera.getPmvMatrix();
		ShaderLoader.getInstance().updateProjectionMatrix(gl, cameraMatrix);

		ShaderLoader.getInstance().updateLightData(gl, light0, light1);

		try {
			displayRenderers(gl);
		} catch (GkException e) {
			LOG.error(e);
		}
		gl.glUseProgram(0);
		//drawOverlay();
	}

	
	/**
	 * Display the registered renderers
	 * @param gl the GL to draw on
	 * @throws GkException GkException
	 */
	private void displayRenderers(GL3 gl) throws GkException{
		synchronized (renderers) {
			for (ICoreJoglRenderer renderer : getRenderers()) {
				if(renderer.shouldDestroy()){
					renderersToRemove.add(renderer);
				}else{
					if(isLayerVisible(renderer.getLayerId())){
						renderer.render(gl, camera.getPmvMatrix());
					}
				}
			}

			if(CollectionUtils.isNotEmpty(renderersToRemove)){
				for (ICoreJoglRenderer renderer : renderersToRemove) {
					renderer.performDestroy(gl);
					LOG.info("Destroying renderer "+renderer.getCode()+" ["+renderer.toString()+"]");
					renderers.remove(renderer);
				}
				renderersToRemove.clear();
			}
		}
	}

	public boolean isLayerVisible(int layerId) {
		if(layerVisibility.containsKey(layerId)){
			return layerVisibility.get(layerId);
		}
		return true;
	}

	public void addRenderer(IViewer3DRenderer renderer) throws GkException {
		addRenderer(renderer);
	}

	public void addRenderer(ICoreJoglRenderer renderer) throws GkException {
		renderers.add(renderer);
		LOG.info("Adding renderer "+renderer.getCode()+" ["+renderer.toString()+"]");
		synchronized (renderers) {
			// Make sure that renderer using alpha get rendered last
			Collections.sort(getRenderers(), new CoreJoglRendererAlphaComparator());
		}
	}

	public void removeRenderer(ICoreJoglRenderer renderer) throws GkException {
		synchronized (renderers) {
			getRenderers().remove(renderer);
		}
	}
	public void removeRenderer(IViewer3DRenderer renderer) throws GkException {
		synchronized (renderers) {
			getRenderers().remove(renderer);
		}
	}
	/**
	 * Removes the given JOGL Renderer
	 * @param renderer the renderer to remove
	 * @throws GkException GkException
	 */
	protected void removeRenderer(AbstractCoreJoglRenderer renderer) throws GkException {
		getRenderers().remove(renderer);
	}

	/**
	 * @return the renderers
	 */
	protected List<ICoreJoglRenderer> getRenderers() {
		if(renderers == null){
			renderers = Collections.synchronizedList(new ArrayList<ICoreJoglRenderer>());
		}
		return renderers;
	}

	public void setRendererEnabled(String idRenderer, boolean enabled) throws GkException{
		getJoglRenderer(idRenderer).setEnabled(enabled);
	}

	/** (inheritDoc)
	 * @see javax.media.opengl.GLEventListener#dispose(javax.media.opengl.GLAutoDrawable)
	 */
	@Override
	public void dispose(GLAutoDrawable gLAutoDrawable) {

	}

	/** (inheritDoc)
	 * @see javax.media.opengl.GLEventListener#init(javax.media.opengl.GLAutoDrawable)
	 */
	@Override
	public void init(GLAutoDrawable gLAutoDrawable) {
		LOG.info("public void init(GLAutoDrawable gLAutoDrawable)");
		GL3 gl = JoglUtils.getSupportedGL(gLAutoDrawable); // get the OpenGL graphics context
		//gl.glClearColor(.19f, .19f, .23f, 1.0f); // set background (clear) color
		backgroundColor = JoglViewerPreference.getInstance().getBackgroundColor();
		gl.glClearColor(backgroundColor.x, backgroundColor.y, backgroundColor.z, 1.0f); 
		gl.glClearDepth(1.0f); // set clear depth value to farthest
		gl.getMaxRenderbufferSamples();
		
		// Enable blending
		gl.glEnable(GL.GL_BLEND);
		gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
		LOG.info("gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);");
		// Enable ZBuffer

		gl.glEnable(GL.GL_DEPTH_TEST);
		// Accept fragment if it closer to the camera than the former one
		gl.glDepthFunc(GL.GL_LEQUAL);
		// Perspective correction
		//gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST); // best perspective correction
		//LOG.info("gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);");
		// Line smooth
	    gl.glEnable(GL.GL_LINE_SMOOTH);
	    gl.glHint(GL.GL_LINE_SMOOTH_HINT, GL.GL_DONT_CARE);
	    
	    int shaderProgram = ShaderLoader.loadShader(new DebugGL3(gl), EnumGokoShaderProgram.LINE_SHADER);
	    gl.glBindAttribLocation(shaderProgram, 0, "vertexPosition_modelspace");
	    gl.glUseProgram(shaderProgram);

	    initLights();
	    LOG.info("initLights();");
	    
		overlay = new Overlay(gLAutoDrawable);
		overlay.createGraphics();
		LOG.info("public void init(GLAutoDrawable gLAutoDrawable) ended");
		
	}

	/** (inheritDoc)
	 * @see javax.media.opengl.GLEventListener#reshape(javax.media.opengl.GLAutoDrawable, int, int, int, int)
	 */
	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.setWidth(width);
		this.height = height;
		if(camera != null){
			camera.reshape(drawable, x, y, width, height);
		}
	}


	public List<AbstractCamera> getSupportedCamera() throws GkException {
		if(supportedCamera == null){
			this.supportedCamera = new ArrayList<AbstractCamera>();
		}
		return supportedCamera;
	}
	
	public boolean isSupportedCamera(String cameraId) throws GkException{
		for (AbstractCamera tmpCamera : getSupportedCamera()) {
			if(StringUtils.equals(cameraId, tmpCamera.getId())){
				return true;
			}
		}
		return false;
	}

	public void setActiveCamera(String idCamera) throws GkException {
		for (AbstractCamera tmpCamera : getSupportedCamera()) {
			if(StringUtils.equals(idCamera, tmpCamera.getId())){
				if(camera != null){
					camera.setActivated(false);
					if(camera.getPositionOverlay() != null){
						removeOverlayRenderer(camera.getPositionOverlay());
					}
				}
				camera = tmpCamera;
				camera.updateViewport(x, y, getWidth(), height);
				camera.setActivated(true);		
				if(camera.getPositionOverlay() != null){
					addOverlayRenderer(camera.getPositionOverlay());
					camera.getPositionOverlay().setOverlayEnabled(displayPositionOverlay);
				}
				return;
			}
		}
	}

	/**
	 * Update the camera informations
	 * @param gLAutoDrawable the drawable
	 * @param gl the GL3
	 */
	private void updateCamera(GLAutoDrawable gLAutoDrawable, GL3 gl){
		if(!camera.isInitialized()){
			camera.reshape(gLAutoDrawable, x, y, getWidth(), height);
			camera.setInitialized(true);
		}

		camera.updateViewport(x, y, getWidth(), height);
		camera.updatePosition();
	}

	private void drawOverlay() {
		//overlay.beginRendering();		
		Graphics2D g2d = overlay.createGraphics();
		try{		
			Color transparentColor = new Color(0,0,0,0);
		    g2d.setBackground(transparentColor);
			g2d.clearRect(0, 0, width, height);
			drawOverlayRenderer(g2d);			
		}catch(GkException e){
			LOG.error(e);
		}

		overlay.markDirty(0, 0, width, height);
		overlay.drawAll();
		g2d.dispose();
		//overlay.endRendering();
	}

	/**
	 * Registers the given overlay renderer
	 * @param overlayRenderer the renderer to register
	 * @throws GkException GkException
	 */
	public void addOverlayRenderer(IOverlayRenderer overlayRenderer) throws GkException{
		overlayRenderers.add(overlayRenderer);
	}
	
	/**
	 * Removes the given overlay renderer
	 * @param overlayRenderer the renderer to register
	 * @throws GkException GkException
	 */
	public void removeOverlayRenderer(IOverlayRenderer overlayRenderer) throws GkException{
		overlayRenderers.remove(overlayRenderer);
	}
	
	/**
	 * Draws the registered overlays
	 * @param g2d the target graphic
	 * @throws GkException GkException
	 */
	protected void drawOverlayRenderer(Graphics2D g2d) throws GkException{
		List<IOverlayRenderer> lstRenderers = overlayRenderers.get();
		Rectangle bounds = new Rectangle(x, y, width, height);
		for (IOverlayRenderer overlayRenderer : lstRenderers) {
			if(overlayRenderer.isOverlayEnabled()){
				overlayRenderer.drawOverlayData(g2d, bounds);
			}
		}
	}

	public ICoreJoglRenderer getJoglRenderer(String idRenderer) throws GkException {
		if(CollectionUtils.isNotEmpty(renderers)){
			for (ICoreJoglRenderer renderer : renderers) {
				if(StringUtils.equals(renderer.getCode(), idRenderer)){
					return renderer;
				}
			}
		}
		throw new GkFunctionalException("Renderer '"+idRenderer+"' does not exist.");
	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.util.IPropertyChangeListener#propertyChange(org.eclipse.jface.util.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		// Property change listener
		if(canvasCapabilities != null){
			canvasCapabilities.setNumSamples(JoglViewerPreference.getInstance().getMultisampling());
		}
		
		setBackgroundColor(JoglViewerPreference.getInstance().getBackgroundColor());
	}

	public void addCamera(AbstractCamera camera) throws GkException{
		getSupportedCamera().add(camera);
	}


	public AbstractCamera getActiveCamera() throws GkException {
		return camera;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * @return the camera
	 */
	protected AbstractCamera getCamera() {
		return camera;
	}

	/**
	 * @param camera the camera to set
	 */
	protected void setCamera(AbstractCamera camera) {
		this.camera = camera;
	}
	/**
	 * @return the canvas
	 */
	protected GokoJoglCanvas getCanvas() {
		return canvas;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setLayerVisible(int layerId, boolean visible){
		this.layerVisibility.put(layerId, visible);
	}

	public void zoomToFit() throws GkException {
		BoundingTuple6b contentBounds = getContentBounds();
		if(contentBounds != null){
			getCamera().zoomToFit(contentBounds);
		}
	}

	public BoundingTuple6b getContentBounds(){
		BoundingTuple6b result = null;
		if(CollectionUtils.isNotEmpty(renderers)){
			for (ICoreJoglRenderer joglRenderer : renderers) {
				BoundingTuple6b bound = joglRenderer.getBounds();
				if(bound != null){
					if(result == null){
						result = new BoundingTuple6b(bound.getMin(), bound.getMax());
					}else{
						result.add(bound);
					}
				}
			}
		}
		return result;
	}

	/**
	 * @return the backgroundColor
	 */
	public Color3f getBackgroundColor() {
		return backgroundColor;
	}

	/**
	 * @param backgroundColor the backgroundColor to set
	 */
	public void setBackgroundColor(Color3f backgroundColor) {
		this.backgroundColor = backgroundColor;
		this.updateBackgroundColor = true;
	}

	/**
	 * @return the mouseCanvasPosition
	 */
	public Point2i getMouseCanvasPosition() {
		return mouseCanvasPosition;
	}

	/**
	 * @param mouseCanvasPosition the mouseCanvasPosition to set
	 */
	public void setMouseCanvasPosition(Point2i mouseCanvasPosition) {
		this.mouseCanvasPosition = mouseCanvasPosition;
	}

	/**
	 * @return the displayPositionOverlay
	 */
	public boolean isDisplayPositionOverlay() {
		return displayPositionOverlay;
	}

	/**
	 * @param displayPositionOverlay the displayPositionOverlay to set
	 */
	public void setDisplayPositionOverlay(boolean displayPositionOverlay) {
		this.displayPositionOverlay = displayPositionOverlay;
		if(camera.getPositionOverlay() != null){
			camera.getPositionOverlay().setOverlayEnabled(displayPositionOverlay);
		}
	}
}

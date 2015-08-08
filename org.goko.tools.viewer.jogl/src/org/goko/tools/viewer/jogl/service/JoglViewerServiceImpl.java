/*******************************************************************************
 * 	This file is part of Goko.
 *
 *   Goko is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   Goko is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with Goko.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package org.goko.tools.viewer.jogl.service;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.Rectangle2D;

import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.fixedfunc.GLMatrixFunc;
import javax.vecmath.Vector3d;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.type.NumberQuantity;
import org.goko.core.config.GokoPreference;
import org.goko.core.controller.IContinuousJogService;
import org.goko.core.controller.ICoordinateSystemAdapter;
import org.goko.core.controller.IFourAxisControllerAdapter;
import org.goko.core.controller.IThreeAxisControllerAdapter;
import org.goko.core.controller.IWorkVolumeProvider;
import org.goko.core.controller.ThreeToFourAxisAdapterWrapper;
import org.goko.core.gcode.bean.BoundingTuple6b;
import org.goko.core.gcode.bean.IGCodeProvider;
import org.goko.core.gcode.bean.commands.EnumCoordinateSystem;
import org.goko.core.gcode.service.IGCodeExecutionMonitorService;
import org.goko.core.log.GkLog;
import org.goko.core.workspace.service.GCodeProviderEvent;
import org.goko.core.workspace.service.GCodeProviderEvent.GCodeProviderEventType;
import org.goko.tools.viewer.jogl.GokoJoglCanvas;
import org.goko.tools.viewer.jogl.camera.orthographic.FrontCamera;
import org.goko.tools.viewer.jogl.camera.orthographic.LeftCamera;
import org.goko.tools.viewer.jogl.preferences.JoglViewerPreference;
import org.goko.tools.viewer.jogl.utils.render.GridRenderer;
import org.goko.tools.viewer.jogl.utils.render.ToolRenderer;
import org.goko.tools.viewer.jogl.utils.render.coordinate.CoordinateSystemSetRenderer;
import org.goko.tools.viewer.jogl.utils.render.coordinate.FourAxisOriginRenderer;
import org.goko.tools.viewer.jogl.utils.render.gcode.BoundsRenderer;
import org.goko.tools.viewer.jogl.utils.render.gcode.DefaultGCodeProviderRenderer;
import org.goko.tools.viewer.jogl.utils.render.gcode.IGCodeProviderRenderer;
import org.goko.tools.viewer.jogl.utils.render.gcode.RotaryAxisAdapter;
import org.goko.core.workspace.service.IWorkspaceListener;
import org.goko.core.workspace.service.IWorkspaceService;

import com.jogamp.opengl.util.PMVMatrix;

/**
 * Jogl implementation of the viewer service
 *
 * @author PsyKo
 *
 */
public class JoglViewerServiceImpl extends JoglSceneManager implements IJoglViewerService, IWorkspaceListener, IPropertyChangeListener{
	/** LOG */
	private static final GkLog LOG = GkLog.getLogger(JoglViewerServiceImpl.class);
	/** SERVICE_ID */
	private static final String SERVICE_ID = "org.goko.viewer.jogl";
	/** The current controller service*/
	private IFourAxisControllerAdapter controllerAdapter;
	/** The coordinate system adapter */
	private ICoordinateSystemAdapter coordinateSystemAdapter;
	/** Jog service */
	private IContinuousJogService continuousJogService;
	/** GCode execution monitor service */
	private IGCodeExecutionMonitorService executionMonitorService;
	/** The workspace service */
	private IWorkspaceService workspaceService;
	/** Work volume provider */
	private IWorkVolumeProvider workVolumeProvider;
	/** Bind camera on tool position ? */
	private boolean lockCameraOnTool;

	private IGCodeProviderRenderer gcodeRenderer;
	private BoundsRenderer boundsRenderer;
	private CoordinateSystemSetRenderer coordinateSystemRenderer;
	private GridRenderer xyGridRenderer;
	private GridRenderer xzGridRenderer;
	private GridRenderer yzGridRenderer;
	private FourAxisOriginRenderer zeroRenderer;
	private BoundingTuple6b bounds;
	private KeyboardJogAdatper keyboardJogAdapter;
	private ToolRenderer toolRenderer;
	private Font jogWarnFont;

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
		JoglViewerPreference.getInstance().addPropertyChangeListener(this);		
		GokoPreference.getInstance().addPropertyChangeListener(this);
		
		jogWarnFont = new Font("SansSerif", Font.BOLD, 16);
		LOG.info("Starting "+this.getServiceId());
		zeroRenderer = new FourAxisOriginRenderer(JoglViewerPreference.getInstance().isRotaryAxisEnabled());
		addRenderer(zeroRenderer);
		boundsRenderer = new BoundsRenderer(null);
		addRenderer(boundsRenderer);	
		this.xyGridRenderer = new GridRenderer(JoglUtils.XY_GRID_ID);
		this.xzGridRenderer = new GridRenderer(JoglUtils.XZ_GRID_ID);
		this.yzGridRenderer = new GridRenderer(JoglUtils.YZ_GRID_ID);
		this.xyGridRenderer.setNormal(JoglUtils.Z_AXIS);
		this.xzGridRenderer.setNormal(JoglUtils.Y_AXIS);
		this.yzGridRenderer.setNormal(JoglUtils.X_AXIS);
		updateGridRenderer(xyGridRenderer);
		updateGridRenderer(xzGridRenderer);
		updateGridRenderer(yzGridRenderer);
		addRenderer(xyGridRenderer);
		addRenderer(xzGridRenderer);
		addRenderer(yzGridRenderer);
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#stop()
	 */
	@Override
	public void stop() throws GkException {

	}

	@Override
	public void renderGCode(IGCodeProvider provider) throws GkException {
		if(gcodeRenderer != null){
			removeRenderer(gcodeRenderer);
			executionMonitorService.removeExecutionListener(gcodeRenderer);
			gcodeRenderer.destroy();
		}
		gcodeRenderer = new RotaryAxisAdapter(this.controllerAdapter, new DefaultGCodeProviderRenderer(provider));
		executionMonitorService.addExecutionListener(gcodeRenderer);
		bounds = provider.getBounds();
		if(boundsRenderer != null){
			removeRenderer(boundsRenderer);
		}
		boundsRenderer = new BoundsRenderer(provider.getBounds());
		addRenderer(boundsRenderer);
		addRenderer(gcodeRenderer);

	}

	/**<
	 * @return the controllerService
	 */
	public IFourAxisControllerAdapter getControllerAdapter() {
		return controllerAdapter;
	}

	/**
	 * @param controllerService the controllerService to set
	 */
	public void setControllerAdapter(IThreeAxisControllerAdapter controllerService) throws GkException {
		if(controllerService instanceof IFourAxisControllerAdapter){
			setControllerAdapter((IFourAxisControllerAdapter)controllerService);
		}else{
			setControllerAdapter(new ThreeToFourAxisAdapterWrapper(controllerService));
		}
	}
	/**
	 * @param controllerService the controllerService to set
	 * @throws GkException GkException 
	 */
	public void setControllerAdapter(IFourAxisControllerAdapter controllerService) throws GkException {
		this.controllerAdapter = controllerService;
		if(toolRenderer == null){
			toolRenderer = new ToolRenderer(getControllerAdapter());
			addRenderer(toolRenderer);
		}
	}


	protected void applyRotationAngle(PMVMatrix matrix) throws GkException{
		double angle = controllerAdapter.getA().doubleValue();
		Vector3d rotaryVector = new Vector3d(JoglViewerPreference.getInstance().getRotaryAxisDirection().getVector3f());
		PMVMatrix rotMatrix = new PMVMatrix();
		rotMatrix.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
		rotMatrix.glLoadIdentity();
		rotMatrix.glRotatef((float)-angle, (float)rotaryVector.x, (float)rotaryVector.y, (float)rotaryVector.z);
		rotMatrix.update();
		matrix.glMultMatrixf(rotMatrix.glGetMvMatrixf());
		matrix.update();
	}
	
	/** (inheritDoc)
	 * @see javax.media.opengl.GLEventListener#dispose(javax.media.opengl.GLAutoDrawable)
	 */
	@Override
	public void dispose(GLAutoDrawable arg0) {


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
		if(event.getType() == GCodeProviderEventType.CURRENT_UPDATE){
			if(getWorkspaceService().getCurrentGCodeProvider() != null){
				this.renderGCode(getWorkspaceService().getCurrentGCodeProvider());
			}
		}
	}

	/**
	 * @return the coordinateSystemAdapter
	 */
	@Override
	public ICoordinateSystemAdapter getCoordinateSystemAdapter() {
		return coordinateSystemAdapter;
	}

	/**
	 * @param coordinateSystemAdapter the coordinateSystemAdapter to set
	 * @throws GkException
	 */
	public void setCoordinateSystemAdapter(ICoordinateSystemAdapter coordinateSystemAdapter) throws GkException {
		this.coordinateSystemAdapter = coordinateSystemAdapter;
		if(this.coordinateSystemRenderer == null){
			this.coordinateSystemRenderer = new CoordinateSystemSetRenderer();
			addRenderer(coordinateSystemRenderer);
		}
		this.coordinateSystemRenderer.setAdapter(coordinateSystemAdapter);
	}

	/**
	 * @return the continuousJogService
	 */
	public IContinuousJogService getContinuousJogService() {
		return continuousJogService;
	}

	/**
	 * @param continuousJogService the continuousJogService to set
	 */
	public void setContinuousJogService(IContinuousJogService continuousJogService) {
		this.continuousJogService = continuousJogService;
	}

	/**
	 * @param executionMonitorService the executionMonitorService to set
	 */
	public void setGCodeExecutionMonitorService(IGCodeExecutionMonitorService executionMonitorService) {
		this.executionMonitorService = executionMonitorService;
	}


	/** (inheritDoc)
	 * @see org.eclipse.jface.util.IPropertyChangeListener#propertyChange(org.eclipse.jface.util.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		super.propertyChange(event);		
		try {
			zeroRenderer.setDisplayRotaryAxis(JoglViewerPreference.getInstance().isRotaryAxisEnabled());
			zeroRenderer.setRotationAxis(JoglViewerPreference.getInstance().getRotaryAxisDirection());
			// Update the grid
			if(StringUtils.startsWith(event.getProperty(), JoglViewerPreference.GROUP_GRID)){
				boolean xyDisplay = xyGridRenderer.isEnabled();
				boolean xzDisplay = xzGridRenderer.isEnabled();
				boolean yzDisplay = yzGridRenderer.isEnabled();
				this.xyGridRenderer.destroy();
				this.xzGridRenderer.destroy();
				this.yzGridRenderer.destroy();
				this.xyGridRenderer = new GridRenderer(JoglUtils.XY_GRID_ID);
				this.xyGridRenderer.setNormal(JoglUtils.Z_AXIS);
				this.xzGridRenderer = new GridRenderer(JoglUtils.XZ_GRID_ID);
				this.xzGridRenderer.setNormal(JoglUtils.Y_AXIS);
				this.yzGridRenderer = new GridRenderer(JoglUtils.YZ_GRID_ID);
				this.yzGridRenderer.setNormal(JoglUtils.X_AXIS);
				updateGridRenderer(xyGridRenderer);
				updateGridRenderer(xzGridRenderer);
				updateGridRenderer(yzGridRenderer);
				xyGridRenderer.setEnabled(xyDisplay);
				xzGridRenderer.setEnabled(xzDisplay);
				yzGridRenderer.setEnabled(yzDisplay);
				addRenderer(xyGridRenderer);
				addRenderer(xzGridRenderer);
				addRenderer(yzGridRenderer);
			}
			if(gcodeRenderer != null){
				gcodeRenderer.update();
			}
		} catch (GkException e) {
			LOG.error(e);
		}
	}

	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.service.JoglSceneManager#setActiveCamera(java.lang.String)
	 */
	@Override
	public void setActiveCamera(String idCamera) throws GkException {		
		super.setActiveCamera(idCamera);
		if(xyGridRenderer != null){
			setRendererEnabled(JoglUtils.XY_GRID_ID, false);
			setRendererEnabled(JoglUtils.XZ_GRID_ID, false);
			setRendererEnabled(JoglUtils.YZ_GRID_ID, false);
			
			if(StringUtils.equals(idCamera, FrontCamera.ID)){
				setRendererEnabled(JoglUtils.XZ_GRID_ID, true);	
			}else if(StringUtils.equals(idCamera, LeftCamera.ID)){
				setRendererEnabled(JoglUtils.YZ_GRID_ID, true);	
			}else{
				setRendererEnabled(JoglUtils.XY_GRID_ID, true);	
			}
		}
	}
	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.service.IJoglViewerService#zoomToFit()
	 */
	@Override
	public void zoomToFit() throws GkException {
		if(bounds != null){
			getCamera().zoomToFit(bounds);
		}
	}

	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.service.JoglSceneManager#onCanvasCreated(org.goko.tools.viewer.jogl.GokoJoglCanvas)
	 */
	@Override
	protected void onCanvasCreated(GokoJoglCanvas canvas) {
		if(continuousJogService != null){
			this.keyboardJogAdapter = new KeyboardJogAdatper(getCanvas(), continuousJogService);
			canvas.addKeyListener( keyboardJogAdapter );
		}
	}

	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.service.JoglSceneManager#drawOverlayData(java.awt.Graphics2D)
	 */
	@Override
	protected void drawOverlayData(Graphics2D g2d) throws GkException {
		if(getCanvas().isKeyboardJogEnabled()){
			// Draw a big red warning saying jog is enabled
			FontRenderContext 	frc = g2d.getFontRenderContext();
			String warn = "Keyboard jog enabled";
			GlyphVector gv =		 jogWarnFont.createGlyphVector(frc, warn);
		    Rectangle 	bounds = gv.getPixelBounds(frc, 0, 0);
		    int x = (getWidth() - bounds.width) / 2;
		    int y = 5 + bounds.height;
		    Rectangle2D bg = new Rectangle2D.Double(x-5,2, bounds.width + 15, bounds.height + 10);
		    g2d.setFont(jogWarnFont);
		    g2d.setColor(Color.RED);//new Color(0.9f,0,0,0.5f));
		    g2d.fill(bg);
		    g2d.setColor(Color.WHITE);
		    g2d.drawString(warn ,x, y);
		}

	}

	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.service.IJoglViewerService#setCoordinateSystemEnabled(org.goko.core.gcode.bean.commands.EnumCoordinateSystem, boolean)
	 */
	@Override
	public void setCoordinateSystemEnabled(EnumCoordinateSystem cs, boolean enabled) {
		if(coordinateSystemRenderer != null){
			coordinateSystemRenderer.setCoordinateSystemEnabled(cs, enabled);
		}
	}

	private void updateGridRenderer(GridRenderer gridRenderer) throws GkException{		
		gridRenderer.setStart(JoglViewerPreference.getInstance().getGridStart());
		gridRenderer.setEnd(JoglViewerPreference.getInstance().getGridEnd());
		
		gridRenderer.setMajorIncrement(NumberQuantity.of(JoglViewerPreference.getInstance().getMajorGridSpacing(), JoglUtils.JOGL_UNIT));
		gridRenderer.setMinorIncrement(NumberQuantity.of(JoglViewerPreference.getInstance().getMinorGridSpacing(), JoglUtils.JOGL_UNIT));
		gridRenderer.setMajorUnitColor( JoglViewerPreference.getInstance().getMajorColor());
		gridRenderer.setMinorUnitColor( JoglViewerPreference.getInstance().getMinorColor());
		gridRenderer.setOpacity(JoglViewerPreference.getInstance().getGridOpacity());
		
		gridRenderer.update();		
	}

	/**
	 * @return the workVolumeProvider
	 */
	public IWorkVolumeProvider getWorkVolumeProvider() {
		return workVolumeProvider;
	}

	/**
	 * @param workVolumeProvider the workVolumeProvider to set
	 */
	public void setWorkVolumeProvider(IWorkVolumeProvider workVolumeProvider) {
		this.workVolumeProvider = workVolumeProvider;
	}
	
	
}

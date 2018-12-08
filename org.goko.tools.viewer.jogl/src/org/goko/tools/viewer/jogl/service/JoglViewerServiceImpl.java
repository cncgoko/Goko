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

import javax.vecmath.Color4f;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.config.GokoPreference;
import org.goko.core.controller.IFourAxisControllerAdapter;
import org.goko.core.controller.IGCodeContextProvider;
import org.goko.core.controller.IJogService;
import org.goko.core.controller.IThreeAxisControllerAdapter;
import org.goko.core.controller.IWorkVolumeProvider;
import org.goko.core.controller.ThreeToFourAxisAdapterWrapper;
import org.goko.core.controller.listener.IWorkVolumeUpdateListener;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.log.GkLog;
import org.goko.core.math.Tuple6b;
import org.goko.tools.viewer.jogl.GokoJoglCanvas;
import org.goko.tools.viewer.jogl.camera.orthographic.FrontCamera;
import org.goko.tools.viewer.jogl.camera.orthographic.LeftCamera;
import org.goko.tools.viewer.jogl.preferences.JoglViewerPreference;
import org.goko.tools.viewer.jogl.service.overlay.KeyboardJogOverlay;
import org.goko.tools.viewer.jogl.utils.render.coordinate.FourAxisOriginRenderer;
import org.goko.tools.viewer.jogl.utils.render.coordinate.OriginRenderer;
import org.goko.tools.viewer.jogl.utils.render.grid.IGridRenderer;
import org.goko.tools.viewer.jogl.utils.render.tool.ToolLinePrintRenderer;
import org.goko.tools.viewer.jogl.utils.render.tool.ToolRenderer;

import com.jogamp.opengl.GLAutoDrawable;

/**
 * Jogl implementation of the viewer service
 *
 * @author PsyKo
 *
 */
public class JoglViewerServiceImpl extends JoglSceneManager implements IJoglViewerService, IPropertyChangeListener, IWorkVolumeUpdateListener{
	/** LOG */
	private static final GkLog LOG = GkLog.getLogger(JoglViewerServiceImpl.class);
	/** SERVICE_ID */
	private static final String SERVICE_ID = "org.goko.viewer.jogl";
	/** Jog service */
	private IJogService jogService;
	/** Work volume provider */
	private IWorkVolumeProvider workVolumeProvider;
	/** Bind camera on tool position ? */
	private boolean lockCameraOnTool;
		
	private IGridRenderer xyGridRenderer;
	private IGridRenderer xzGridRenderer;
	private IGridRenderer yzGridRenderer;
	private FourAxisOriginRenderer zeroRenderer;	
	private KeyboardJogAdatper keyboardJogAdapter;
	private ToolRenderer toolRenderer;	
	private IFourAxisControllerAdapter controllerAdapter;
	private IGCodeContextProvider<GCodeContext> gcodeContextProvider;
	
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
		LOG.info("Starting "+getServiceId());
		JoglViewerPreference.getInstance().addPropertyChangeListener(this);		
		GokoPreference.getInstance().addPropertyChangeListener(this);
				
		zeroRenderer = new FourAxisOriginRenderer(JoglViewerPreference.getInstance().isRotaryAxisEnabled());
		addRenderer(zeroRenderer);			
		this.xyGridRenderer = JoglUtils.drawXYGrid(gcodeContextProvider);
		this.xzGridRenderer = JoglUtils.drawXZGrid(gcodeContextProvider);
		this.yzGridRenderer = JoglUtils.drawYZGrid(gcodeContextProvider);
		
		this.toolRenderer = new ToolRenderer();
		this.toolRenderer.setGcodeContextProvider(gcodeContextProvider);
		this.toolRenderer.setControllerAdapter(controllerAdapter);		
		addRenderer(toolRenderer);
		addRenderer(new ToolLinePrintRenderer(controllerAdapter, gcodeContextProvider));
		
		addRenderer(new OriginRenderer(5, new Color4f(1,0,0,1), new Tuple6b()));
		
		updateGridRenderer(xyGridRenderer);
		updateGridRenderer(xzGridRenderer);
		updateGridRenderer(yzGridRenderer);
		addRenderer(xyGridRenderer);
		addRenderer(xzGridRenderer);
		addRenderer(yzGridRenderer);		
		if(workVolumeProvider != null){
			workVolumeProvider.addUpdateListener(this);
		}
		LOG.info("Successfully started " + getServiceId());
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#stop()
	 */
	@Override
	public void stop() throws GkException {

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
	 * @return the continuousJogService
	 */
	public IJogService getJogService() {
		return jogService;
	}

	/**
	 * @param continuousJogService the continuousJogService to set
	 */
	public void setJogService(IJogService continuousJogService) {
		this.jogService = continuousJogService;
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
			zeroRenderer.update();
			// Update the grid
			if(StringUtils.startsWith(event.getProperty(), JoglViewerPreference.GROUP_GRID)
				|| StringUtils.startsWith(event.getProperty(), GokoPreference.KEY_DISTANCE_UNIT)){
				boolean xyDisplay = xyGridRenderer.isEnabled();
				boolean xzDisplay = xzGridRenderer.isEnabled();
				boolean yzDisplay = yzGridRenderer.isEnabled();
				this.xyGridRenderer.destroy();
				this.xzGridRenderer.destroy();
				this.yzGridRenderer.destroy();
				
				this.xyGridRenderer = JoglUtils.drawXYGrid(gcodeContextProvider);
				this.xzGridRenderer = JoglUtils.drawXZGrid(gcodeContextProvider);
				this.yzGridRenderer = JoglUtils.drawYZGrid(gcodeContextProvider);
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
		super.zoomToFit();		
	}

	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.service.JoglSceneManager#onCanvasCreated(org.goko.tools.viewer.jogl.GokoJoglCanvas)
	 */
	@Override
	protected void onCanvasCreated(GokoJoglCanvas canvas) throws GkException {
		if(jogService != null){
			this.keyboardJogAdapter = new KeyboardJogAdatper(getCanvas(), jogService);
			canvas.addKeyListener( keyboardJogAdapter );
			addOverlayRenderer(new KeyboardJogOverlay(canvas, keyboardJogAdapter));
		}		
	}
	
	private void updateGridRenderer(IGridRenderer gridRenderer) throws GkException{
		Tuple6b workMin = null;
		Tuple6b workMax = null;
		if(JoglViewerPreference.getInstance().isUseWorkVolumeProvider()
			&& workVolumeProvider != null
			&& workVolumeProvider.findWorkVolumeMinimalPosition() != null
			&& workVolumeProvider.findWorkVolumeMaximalPosition() != null
			&& workVolumeProvider.findWorkVolumeMinimalPosition().distance(workVolumeProvider.findWorkVolumeMaximalPosition()).greaterThan(Length.ZERO)){
			workMin = workVolumeProvider.getWorkVolumeMinimalPosition();
			workMax = workVolumeProvider.getWorkVolumeMaximalPosition();
			
		}else{
			workMin = JoglViewerPreference.getInstance().getGridStart();
			workMax = JoglViewerPreference.getInstance().getGridEnd();
		}
		gridRenderer.setWorldBounds(workMin, workMax);	
		
		gridRenderer.setMajorIncrement( JoglViewerPreference.getInstance().getMajorGridSpacing());
		gridRenderer.setMinorIncrement( JoglViewerPreference.getInstance().getMinorGridSpacing());
		gridRenderer.setMajorUnitColor( JoglViewerPreference.getInstance().getMajorColor());
		gridRenderer.setMinorUnitColor( JoglViewerPreference.getInstance().getMinorColor());
		gridRenderer.setMinorOpacity(JoglViewerPreference.getInstance().getMinorGridOpacity());
		gridRenderer.setMajorOpacity(JoglViewerPreference.getInstance().getMajorGridOpacity());
		gridRenderer.setAxisOpacity(JoglViewerPreference.getInstance().getAxisGridOpacity());
		
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

	/**
	 * @return the gcodeContextProvider
	 */
	public IGCodeContextProvider<GCodeContext> getGcodeContextProvider() {
		return gcodeContextProvider;
	}

	/**
	 * @param gcodeContextProvider the gcodeContextProvider to set
	 */
	public void setGcodeContextProvider(IGCodeContextProvider<GCodeContext> gcodeContextProvider) {
		this.gcodeContextProvider = gcodeContextProvider;
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.listener.IWorkVolumeUpdateListener#onWorkVolumeUpdate()
	 */
	@Override
	public void onWorkVolumeUpdate() {
		try{
			updateGridRenderer(xyGridRenderer);
			updateGridRenderer(xzGridRenderer);
			updateGridRenderer(yzGridRenderer);
		}catch(GkException e){
			LOG.error(e);
		}
		
	}	
}

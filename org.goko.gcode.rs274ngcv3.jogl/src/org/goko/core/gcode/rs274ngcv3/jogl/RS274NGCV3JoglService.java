package org.goko.core.gcode.rs274ngcv3.jogl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkTechnicalException;
import org.goko.core.common.service.IGokoService;
import org.goko.core.common.utils.CacheById;
import org.goko.core.common.utils.SequentialIdGenerator;
import org.goko.core.controller.ICoordinateSystemAdapter;
import org.goko.core.gcode.element.IGCodeProvider;
import org.goko.core.gcode.execution.ExecutionToken;
import org.goko.core.gcode.execution.ExecutionTokenState;
import org.goko.core.gcode.rs274ngcv3.IRS274NGCService;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.InstructionProvider;
import org.goko.core.gcode.rs274ngcv3.jogl.internal.Activator;
import org.goko.core.gcode.rs274ngcv3.jogl.renderer.RS274GCodeRenderer;
import org.goko.core.gcode.service.IExecutionService;
import org.goko.core.gcode.service.IGCodeProviderRepositoryListener;
import org.goko.core.log.GkLog;
import org.goko.core.math.BoundingTuple6b;
import org.goko.tools.viewer.jogl.utils.render.basic.BoundsRenderer;
import org.goko.tools.viewer.jogl.utils.render.coordinate.CoordinateSystemSetRenderer;

public class RS274NGCV3JoglService implements IGokoService, IGCodeProviderRepositoryListener{
	/** LOG */
	private static final GkLog LOG = GkLog.getLogger(RS274NGCV3JoglService.class);
	/** ID of the service */
	private static final String SERVICE_ID = "org.goko.core.gcode.rs274ngcv3.jogl.RS274NGCV3JoglService";
	/** The list of managed renderer */
	private CacheById<RS274GCodeRenderer> cacheRenderer;
	/** The RS274 GCode service */
	private IRS274NGCService rs274Service;
	/** The bounds of all the loaded gcode */
	private BoundsRenderer contentBoundsRenderer;
	/** ICoordinateSystemAdapter */
	private ICoordinateSystemAdapter coordinateSystemAdapter;
	/** Execution service */
	private IExecutionService<ExecutionTokenState, ExecutionToken<ExecutionTokenState>> executionService;

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

		this.cacheRenderer = new CacheById<RS274GCodeRenderer>(new SequentialIdGenerator());
		CoordinateSystemSetRenderer csrenderer = new CoordinateSystemSetRenderer();
		csrenderer.setAdapter(coordinateSystemAdapter);
		Activator.getJoglViewerService().addRenderer(csrenderer);
		LOG.info("Successfully started " + getServiceId());
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#stop()
	 */
	@Override
	public void stop() throws GkException {
		LOG.info("Stopping "+getServiceId());
		LOG.info("Successfully stopped " + getServiceId());
	}

	private void updateContentBounds() throws GkException {
		List<RS274GCodeRenderer> lstRenderer = cacheRenderer.get();

		if(contentBoundsRenderer != null){
			Activator.getJoglViewerService().removeRenderer(contentBoundsRenderer);
		}

		if(CollectionUtils.isNotEmpty(lstRenderer)){
			BoundingTuple6b result = null;
			for (RS274GCodeRenderer renderer : lstRenderer) {
				IGCodeProvider provider = Activator.getRS274NGCService().getGCodeProvider(renderer.getIdGCodeProvider());
				InstructionProvider instructionProvider = Activator.getRS274NGCService().getInstructions(new GCodeContext(), provider);
				BoundingTuple6b bounds = Activator.getRS274NGCService().getBounds(new GCodeContext(), instructionProvider);
				renderer.setBounds(bounds);
				if(result == null){
					result = bounds;
				}else{
					result.add(bounds);
				}
			}

			contentBoundsRenderer = new BoundsRenderer(result);
			Activator.getJoglViewerService().addRenderer(contentBoundsRenderer);
		}
	}

	/**
	 * Creates the renderer for the given GCodeProvider
	 * @param idGCodeProvider the id of the GCodeProvider
	 * @throws GkException GkException
	 */
	public void createRenderer(Integer idGCodeProvider) throws GkException{
		getRS274NGCService().getGCodeProvider(idGCodeProvider);
		RS274GCodeRenderer renderer = new RS274GCodeRenderer(idGCodeProvider);
		renderer.setIdGCodeProvider(idGCodeProvider);
		executionService.addExecutionListener(renderer);
		this.cacheRenderer.add(renderer);
		Activator.getJoglViewerService().addRenderer(renderer);
	}

	public void updateRenderer(Integer idGCodeProvider) throws GkException{
		RS274GCodeRenderer renderer = getRendererByGCodeProvider(idGCodeProvider);
//		createRenderer(idGCodeProvider);
//		cacheRenderer.remove(renderer); a modifier
//		renderer.destroy();
		renderer.updateGeometry();
	}

	/**
	 * Removes the renderer for the given GCodeProvider
	 * @param idGCodeProvider the id of the GCodeProvider
	 * @throws GkException GkException
	 */
	public void removeRenderer(Integer idGCodeProvider) throws GkException{
		RS274GCodeRenderer renderer = getRendererByGCodeProvider(idGCodeProvider);
		executionService.removeExecutionListener(renderer);
		cacheRenderer.remove(renderer);
		renderer.destroy();
	}

	/**
	 * Returns the renderer for the given gcodeProvider
	 * @param idGCodeProvider the id of the gcode provider
	 * @return an RS274GCodeRenderer
	 * @throws GkException GkException
	 */
	public RS274GCodeRenderer getRendererByGCodeProvider(Integer idGCodeProvider) throws GkException{
		for (RS274GCodeRenderer renderer : cacheRenderer.get()) {
			if(ObjectUtils.equals(idGCodeProvider, renderer.getIdGCodeProvider())){
				return renderer;
			}
		}
		throw new GkTechnicalException("Renderer for GCodeProvider with internal id ["+idGCodeProvider+"] does not exist");
	}

	/**
	 * @param service the IRS274NGCService to set
	 * @throws GkException GkException
	 */
	public void setRS274NGCService(IRS274NGCService service) throws GkException{
		this.rs274Service = service;
		this.rs274Service.addListener(this);
	}

	/**
	 * Returns the current IRS274NGCService
	 * @return IRS274NGCService
	 */
	public IRS274NGCService getRS274NGCService(){
		return this.rs274Service;
	}

	/**
	 * @return the executionService
	 */
	public IExecutionService<ExecutionTokenState, ExecutionToken<ExecutionTokenState>> getExecutionService() {
		return executionService;
	}

	/**
	 * @param executionService the executionService to set
	 */
	public void setExecutionService(IExecutionService<ExecutionTokenState, ExecutionToken<ExecutionTokenState>> executionService) {
		this.executionService = executionService;
	}

	/**
	 * @return the coordinateSystemAdapter
	 */
	protected ICoordinateSystemAdapter getCoordinateSystemAdapter() {
		return coordinateSystemAdapter;
	}

	/**
	 * @param coordinateSystemAdapter the coordinateSystemAdapter to set
	 */
	protected void setCoordinateSystemAdapter(ICoordinateSystemAdapter coordinateSystemAdapter) {
		this.coordinateSystemAdapter = coordinateSystemAdapter;
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeProviderRepositoryListener#onGCodeProviderCreate(org.goko.core.gcode.element.IGCodeProvider)
	 */
	@Override
	public void onGCodeProviderCreate(IGCodeProvider provider) throws GkException {
		createRenderer(provider.getId());
		updateContentBounds();
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeProviderRepositoryListener#onGCodeProviderUpdate(org.goko.core.gcode.element.IGCodeProvider)
	 */
	@Override
	public void onGCodeProviderUpdate(IGCodeProvider provider) throws GkException {
		updateRenderer(provider.getId());
		updateContentBounds();
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeProviderRepositoryListener#onGCodeProviderDelete(org.goko.core.gcode.element.IGCodeProvider)
	 */
	@Override
	public void onGCodeProviderDelete(IGCodeProvider provider) throws GkException {
		removeRenderer(provider.getId());
		updateContentBounds();
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeProviderRepositoryListener#onGCodeProviderLocked(org.goko.core.gcode.element.IGCodeProvider)
	 */
	@Override
	public void onGCodeProviderLocked(IGCodeProvider provider) throws GkException { }

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeProviderRepositoryListener#onGCodeProviderUnlocked(org.goko.core.gcode.element.IGCodeProvider)
	 */
	@Override
	public void onGCodeProviderUnlocked(IGCodeProvider provider) throws GkException { }
}

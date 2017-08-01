package org.goko.core.gcode.rs274ngcv3.jogl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkTechnicalException;
import org.goko.core.common.service.AbstractGokoService;
import org.goko.core.common.service.IGokoService;
import org.goko.core.common.utils.CacheByKey;
import org.goko.core.controller.ICoordinateSystemAdapter;
import org.goko.core.controller.IFourAxisControllerAdapter;
import org.goko.core.controller.IGCodeContextProvider;
import org.goko.core.gcode.element.IGCodeProvider;
import org.goko.core.gcode.execution.ExecutionQueue;
import org.goko.core.gcode.execution.ExecutionQueueType;
import org.goko.core.gcode.execution.ExecutionToken;
import org.goko.core.gcode.execution.ExecutionTokenState;
import org.goko.core.gcode.execution.IExecutionToken;
import org.goko.core.gcode.rs274ngcv3.IRS274NGCService;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.InstructionProvider;
import org.goko.core.gcode.rs274ngcv3.jogl.internal.Activator;
import org.goko.core.gcode.rs274ngcv3.jogl.internal.BaseGCodeContextProvider;
import org.goko.core.gcode.rs274ngcv3.jogl.internal.GCodeContextProviderLinkedList;
import org.goko.core.gcode.rs274ngcv3.jogl.internal.LinkedGCodeContextProvider;
import org.goko.core.gcode.rs274ngcv3.jogl.renderer.RS274GCodeRenderer;
import org.goko.core.gcode.rs274ngcv3.jogl.renderer.colorizer.AbstractInstructionColorizer;
import org.goko.core.gcode.rs274ngcv3.jogl.renderer.colorizer.MotionModeColorizer;
import org.goko.core.gcode.service.IExecutionQueueListener;
import org.goko.core.gcode.service.IExecutionService;
import org.goko.core.gcode.service.IGCodeProviderRepositoryListener;
import org.goko.core.log.GkLog;
import org.goko.core.math.BoundingTuple6b;
import org.goko.tools.viewer.jogl.utils.render.basic.BoundsRenderer;
import org.goko.tools.viewer.jogl.utils.render.coordinate.CoordinateSystemSetRenderer;

public class RS274NGCV3JoglService extends AbstractGokoService implements IGokoService, IGCodeProviderRepositoryListener, IExecutionQueueListener<ExecutionTokenState, ExecutionToken<ExecutionTokenState>>{
	/** LOG */
	private static final GkLog LOG = GkLog.getLogger(RS274NGCV3JoglService.class);
	/** ID of the service */
	private static final String SERVICE_ID = "org.goko.core.gcode.rs274ngcv3.jogl.RS274NGCV3JoglService";
	/** The list of managed renderer */
	private CacheByKey<IGCodeProvider, RS274GCodeRenderer> cacheRenderer;
	/** The list of managed renderer */
	private CacheByKey<IExecutionToken, RS274GCodeRenderer> cacheRendererByExecutionToken;
	/** The RS274 GCode service */
	private IRS274NGCService rs274Service;	
	/** The bounds of all the loaded gcode */
	private BoundsRenderer contentBoundsRenderer;
	/** ICoordinateSystemAdapter */
	private ICoordinateSystemAdapter coordinateSystemAdapter;
	/** ICoordinateSystemAdapter */
	private IGCodeContextProvider<GCodeContext> gcodeContextProvider;
	/** Execution service */
	private IExecutionService<ExecutionTokenState, ExecutionToken<ExecutionTokenState>> executionService;
	/** The IFourAxisControllerAdapter */
	private IFourAxisControllerAdapter fourAxisControllerAdapter;
	/** The linked list of Context provider */
	private GCodeContextProviderLinkedList lstContextProvider;
	/** Active command colorizer */
	private AbstractInstructionColorizer colorizer;
	
	/**
	 * Constructor
	 * @throws GkException 
	 */
	public RS274NGCV3JoglService() throws GkException {
		this.lstContextProvider = new GCodeContextProviderLinkedList();
		this.cacheRenderer = new CacheByKey<IGCodeProvider, RS274GCodeRenderer>();
		this.cacheRendererByExecutionToken = new CacheByKey<IExecutionToken, RS274GCodeRenderer>();		
	}
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
	public void startService() throws GkException {				
		CoordinateSystemSetRenderer csrenderer = new CoordinateSystemSetRenderer();
		csrenderer.setAdapter(coordinateSystemAdapter);
		rs274Service.addListener(this);
		Activator.getJoglViewerService().addRenderer(csrenderer);
		changeColorizer(new MotionModeColorizer());
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#stop()
	 */
	@Override
	public void stopService() throws GkException {
		
	}

	/**
	 * Update content bounds
	 * Synchronized keyword is required to avoid conflicting update 
	 * @throws GkException GkException
	 */
	private synchronized void updateContentBounds() throws GkException {
		List<RS274GCodeRenderer> lstRenderer = cacheRenderer.get();
		lstRenderer.addAll(cacheRendererByExecutionToken.get());
		
		if(contentBoundsRenderer != null){
			contentBoundsRenderer.destroy();
			LOG.info("Ivoking destroy on ["+contentBoundsRenderer.toString()+"]");
			//Activator.getJoglViewerService().removeRenderer(contentBoundsRenderer);
		}

		if(CollectionUtils.isNotEmpty(lstRenderer)){
			BoundingTuple6b result = null;
			for (RS274GCodeRenderer renderer : lstRenderer) {
				if(renderer.getBounds() == null){  // Only update if bound is null
					IGCodeProvider provider = renderer.getGCodeProvider();
					InstructionProvider instructionProvider = Activator.getRS274NGCService().getInstructions(new GCodeContext(), provider);
					BoundingTuple6b bounds = Activator.getRS274NGCService().getBounds(new GCodeContext(), instructionProvider);
					renderer.setBounds(bounds);				
				}
				if(result == null){
					result = new BoundingTuple6b(renderer.getBounds());
				}else{
					result.add(renderer.getBounds());
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
	public void createRenderer(IGCodeProvider provider) throws GkException{		
		RS274GCodeRenderer renderer = new RS274GCodeRenderer(provider, gcodeContextProvider, fourAxisControllerAdapter);
		renderer.setColorizer(colorizer);
		executionService.addExecutionListener(ExecutionQueueType.DEFAULT, renderer);
		this.cacheRenderer.add(provider, renderer);
		Activator.getJoglViewerService().addRenderer(renderer);
	}
	
	/**
	 * Creates the renderer for the given GCodeProvider
	 * @param idGCodeProvider the id of the GCodeProvider
	 * @return the created RS274GCodeRenderer
	 * @throws GkException GkException
	 */
	public RS274GCodeRenderer createRenderer(IExecutionToken executionToken) throws GkException{		
		RS274GCodeRenderer renderer = new RS274GCodeRenderer(executionToken.getGCodeProvider(), gcodeContextProvider, fourAxisControllerAdapter);
		renderer.setColorizer(colorizer);
		Activator.getJoglViewerService().addRenderer(renderer);
		return renderer;
	}

	public void updateRenderer(IGCodeProvider gcodeProvider) throws GkException{
		RS274GCodeRenderer renderer = getRendererByGCodeProvider(gcodeProvider);
		renderer.updateGeometry();
	}

	/**
	 * Removes the renderer for the given GCodeProvider
	 * @param gcodeProvider the gcode provider
	 * @throws GkException GkException
	 */
	public void removeRenderer(IGCodeProvider gcodeProvider) throws GkException{
		RS274GCodeRenderer renderer = findRendererByGCodeProvider(gcodeProvider);
		if(renderer != null){			
			cacheRenderer.remove(gcodeProvider);
			renderer.destroy();
		}
	}

	/**
	 * Returns the renderer for the given gcodeProvider
	 * @param gcodeProvider the gcode provider
	 * @return an RS274GCodeRenderer
	 * @throws GkException GkException
	 */
	public RS274GCodeRenderer getRendererByGCodeProvider(IGCodeProvider gcodeProvider) throws GkException{
		RS274GCodeRenderer renderer = findRendererByGCodeProvider(gcodeProvider);
		if(renderer == null){
			throw new GkTechnicalException("Renderer for GCodeProvider with internal id ["+gcodeProvider.getId()+"] does not exist");
		}
		return renderer;
	}
	
	/**
	 * Returns the renderer for the given IExecutionToken
	 * @param gcodeProvider the gcode provider
	 * @return an RS274GCodeRenderer
	 * @throws GkException GkException
	 */
	public RS274GCodeRenderer getRendererByExecutionToken(IExecutionToken token) throws GkException{
		RS274GCodeRenderer renderer = findRendererByExecutionToken(token);
		if(renderer == null){
			throw new GkTechnicalException("Renderer for IExecutionToken with internal id ["+token.getId()+"] does not exist");
		}
		return renderer;
	}
	
	/**
	 * Returns the renderer for the given gcodeProvider
	 * @param idGCodeProvider the id of the gcode provider
	 * @return an RS274GCodeRenderer or <code>null</code> if none found
	 * @throws GkException GkException
	 */
	public RS274GCodeRenderer findRendererByGCodeProvider(IGCodeProvider gcodeProvider) throws GkException{
		return cacheRenderer.find(gcodeProvider);
	}

	/**
	 * Returns the renderer for the given execution token
	 * @param idGCodeProvider the id of the gcode provider
	 * @return an RS274GCodeRenderer or <code>null</code> if none found
	 * @throws GkException GkException
	 */
	public RS274GCodeRenderer findRendererByExecutionToken(IExecutionToken token) throws GkException{
		return cacheRendererByExecutionToken.find(token);
	}
	
	/**
	 * @param service the IRS274NGCService to set
	 * @throws GkException GkException
	 */
	public void setRS274NGCService(IRS274NGCService service) throws GkException{
		this.rs274Service = service;		
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
	 * @throws GkException GkException 
	 */
	public void setExecutionService(IExecutionService<ExecutionTokenState, ExecutionToken<ExecutionTokenState>> executionService) throws GkException {
		this.executionService = executionService;
		executionService.addExecutionQueueListener(this);
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
		createRenderer(provider);
		updateContentBounds();
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeProviderRepositoryListener#onGCodeProviderUpdate(org.goko.core.gcode.element.IGCodeProvider)
	 */
	@Override
	public void onGCodeProviderUpdate(IGCodeProvider provider) throws GkException {
		RS274GCodeRenderer renderer = findRendererByGCodeProvider(provider);
		if(renderer != null){
			renderer.setBounds(null); // Force update by setting bounds to null
			updateRenderer(provider);
			updateContentBounds();
		}
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeProviderRepositoryListener#afterGCodeProviderDelete(org.goko.core.gcode.element.IGCodeProvider)
	 */
	@Override
	public void afterGCodeProviderDelete(IGCodeProvider provider) throws GkException {
		// Nothing yet
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeProviderRepositoryListener#beforeGCodeProviderDelete(org.goko.core.gcode.element.IGCodeProvider)
	 */
	@Override
	public void beforeGCodeProviderDelete(IGCodeProvider provider) throws GkException {
		removeRenderer(provider);
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

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IExecutionQueueListener#onTokenCreate(org.goko.core.gcode.execution.IExecutionToken)
	 */
	@Override
	public void onTokenCreate(ExecutionToken<ExecutionTokenState> token) {
		try{
			RS274GCodeRenderer renderer = findRendererByGCodeProvider(token.getGCodeProvider());
			if(renderer == null){
				// Create a renderer from the execution token itself
				renderer = createRenderer(token);				
			}
			// Ad the renderer as listener for execution 
			executionService.addExecutionListener(ExecutionQueueType.DEFAULT, renderer);
			executionService.addExecutionListener(ExecutionQueueType.SYSTEM, renderer); // FIXME : remove double listener addition
			
			cacheRendererByExecutionToken.add(token, renderer);
			// Make sure we get the updated context from latest executed token
			if(lstContextProvider.isEmpty()){					
				lstContextProvider.add(new BaseGCodeContextProvider(gcodeContextProvider, token, rs274Service));
			}
							
			// We link the GCodeContext to the context right after the previous token execution
			renderer.setGCodeContextProvider(lstContextProvider.getLast());
			LinkedGCodeContextProvider contextProvider = new LinkedGCodeContextProvider(lstContextProvider.getLast(), token, rs274Service);
			lstContextProvider.addLast(contextProvider);
			renderer.updateGeometry();
			updateContentBounds();
		}catch(GkException e){
			LOG.error(e);
		}
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IExecutionQueueListener#onTokenDelete(org.goko.core.gcode.execution.IExecutionToken)
	 */
	@Override
	public void onTokenDelete(ExecutionToken<ExecutionTokenState> token) {
		try{											
			LinkedGCodeContextProvider contextProvider = lstContextProvider.findExecutionTokenByIdExecutionToken(token.getId());
			LinkedGCodeContextProvider childContextProvider = lstContextProvider.findExecutionTokenAfter(token.getId());
			// Search the matching context provider			
			if(contextProvider != null){
				lstContextProvider.remove(contextProvider);
				if(childContextProvider != null){
					RS274GCodeRenderer childRenderer = getRendererByExecutionToken(childContextProvider.getToken());					
					childContextProvider.setPrevious(contextProvider.getPrevious());
					// Remap the renderer of the child token to link on the new parent
					childRenderer.setGCodeContextProvider(childContextProvider.getPrevious());
					childContextProvider.update();
				}			
			}
			
			// Update the rendering of the GCodeProvider 
			RS274GCodeRenderer renderer = findRendererByExecutionToken(token);			
			if(renderer != null){				
				executionService.removeExecutionListener(renderer);
				cacheRendererByExecutionToken.remove(token);
				// Do we have a renderer for the provider itself ? 
				RS274GCodeRenderer providerRenderer = findRendererByGCodeProvider(token.getGCodeProvider());
				if(providerRenderer != null){
					// Set the deleted token's renderer provider back to default
					renderer.setGCodeContextProvider(gcodeContextProvider);
					renderer.updateGeometry();					
				}else{
					renderer.destroy();					
				}
			}
			updateGeometryRendererInExecutionQueue();
			updateContentBounds();
		}catch(GkException e){
			LOG.error(e);
		}
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IExecutionQueueListener#onTokenUpdate(org.goko.core.gcode.execution.IExecutionToken)
	 */
	@Override
	public void onTokenUpdate(ExecutionToken<ExecutionTokenState> token) {
		try{
			LinkedGCodeContextProvider contextProvider = lstContextProvider.findExecutionTokenByIdExecutionToken(token.getId());		
		// Search the matching context provider
//		Iterator<LinkedGCodeContextProvider> iter = lstContextProvider.descendingIterator();
//		while (iter.hasNext()) {
//			LinkedGCodeContextProvider lclContextProvider = (LinkedGCodeContextProvider) iter.next();			
//			if(ObjectUtils.equals(lclContextProvider.getToken().getId(), token.getId())){
//				contextProvider = lclContextProvider;
//				break;
//			}
//		}
		
			if(contextProvider != null){
				contextProvider.update();				
				updateGeometryRendererInExecutionQueue();		
				updateContentBounds();
			}		
		}catch(GkException e){
			LOG.error(e);
		}
	}
	
	
	/**
	 * Force a redraw of the renderer in the execution queue
	 * @throws GkException GkException
	 */
	public void updateGeometryRendererInExecutionQueue() throws GkException {
		for(ExecutionQueue<ExecutionTokenState, ExecutionToken<ExecutionTokenState>> queue : executionService.getExecutionQueue()){
			List<ExecutionToken<ExecutionTokenState>> lstTokens = queue.getExecutionToken();
			if(CollectionUtils.isNotEmpty(lstTokens)){
				for (ExecutionToken<ExecutionTokenState> token : lstTokens) {
					RS274GCodeRenderer renderer = getRendererByExecutionToken(token);
					renderer.updateGeometry();
				}
			}
		}
	}
	
	/**
	 * @return the gcodeContextProvider
	 */
	public IGCodeContextProvider<GCodeContext> getGCodeContextProvider() {
		return gcodeContextProvider;
	}

	/**
	 * @param gcodeContextProvider the gcodeContextProvider to set
	 */
	public void setGCodeContextProvider(IGCodeContextProvider<GCodeContext> gcodeContextProvider) {
		this.gcodeContextProvider = gcodeContextProvider;
	}

	/**
	 * @return the fourAxisControllerAdapter
	 */
	public IFourAxisControllerAdapter getFourAxisControllerAdapter() {
		return fourAxisControllerAdapter;
	}

	/**
	 * @param fourAxisControllerAdapter the fourAxisControllerAdapter to set
	 */
	public void setFourAxisControllerAdapter(IFourAxisControllerAdapter fourAxisControllerAdapter) {
		this.fourAxisControllerAdapter = fourAxisControllerAdapter;
	}
	/**
	 * @return the colorizer
	 */
	public AbstractInstructionColorizer getColorizer() {
		return colorizer;
	}
	/**
	 * @param colorizer the colorizer to set
	 */
	public void setColorizer(AbstractInstructionColorizer colorizer) {
		this.colorizer = colorizer;
	}

	public void changeColorizer(AbstractInstructionColorizer newColorizer) throws GkException{		
		if(this.colorizer != null && this.getColorizer().getOverlay() != null){
			Activator.getJoglViewerService().removeOverlayRenderer(this.colorizer.getOverlay());
		}
		
		setColorizer(newColorizer);		
		if(newColorizer.getOverlay() != null){			
			Activator.getJoglViewerService().addOverlayRenderer(newColorizer.getOverlay());
		}
		for (RS274GCodeRenderer cachedRenderer : cacheRenderer.get()) {
			cachedRenderer.setColorizer(newColorizer);
			cachedRenderer.updateGeometry();
		}
	}

}

package org.goko.core.gcode.rs274ngcv3.jogl;

import org.apache.commons.lang3.ObjectUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkTechnicalException;
import org.goko.core.common.service.IGokoService;
import org.goko.core.common.utils.CacheById;
import org.goko.core.gcode.element.IGCodeProvider;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.InstructionProvider;
import org.goko.core.gcode.rs274ngcv3.jogl.internal.Activator;
import org.goko.core.gcode.rs274ngcv3.jogl.renderer.RS274GCodeRenderer;
import org.goko.core.log.GkLog;
import org.goko.core.workspace.service.GCodeProviderEvent;
import org.goko.core.workspace.service.GCodeProviderEvent.GCodeProviderEventType;
import org.goko.core.workspace.service.IWorkspaceListener;
import org.goko.core.workspace.service.IWorkspaceService;

public class RS274NGCV3JoglService implements IGokoService, IWorkspaceListener{
	/** LOG */
	private static final GkLog LOG = GkLog.getLogger(RS274NGCV3JoglService.class);
	/** ID of the service */
	private static final String SERVICE_ID = "org.goko.core.gcode.rs274ngcv3.jogl.RS274NGCV3JoglService";
	/** The list of managed renderer */
	private CacheById<RS274GCodeRenderer> lstRenderer;
	/** The workspace service */
	private IWorkspaceService workspaceService;
	
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
		
		this.lstRenderer = new CacheById<RS274GCodeRenderer>();
		
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

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IWorkspaceListener#onGCodeProviderEvent(org.goko.core.workspace.service.GCodeProviderEvent)
	 */
	@Override
	public void onGCodeProviderEvent(GCodeProviderEvent event) throws GkException {
		if(event.getType() == GCodeProviderEventType.INSERT){
			createRenderer(event.getTargetId());
		}else if(event.getType() == GCodeProviderEventType.DELETE){
			removeRenderer(event.getTargetId());
		}
	}

	/**
	 * Creates the renderer for the given GCodeProvider
	 * @param idGCodeProvider the id of the GCodeProvider
	 * @throws GkException GkException
	 */
	public void createRenderer(Integer idGCodeProvider) throws GkException{
		IGCodeProvider provider = getWorkspaceService().getGCodeProvider(idGCodeProvider);
		InstructionProvider instructionSet = Activator.getRS274NGCService().getInstructions(new GCodeContext(), provider);
		RS274GCodeRenderer renderer = new RS274GCodeRenderer(instructionSet);
		renderer.setIdGCodeProvider(idGCodeProvider);
		this.lstRenderer.add(renderer);
		Activator.getJoglViewerService().addRenderer(renderer);
	}
	
	/**
	 * Removes the renderer for the given GCodeProvider
	 * @param idGCodeProvider the id of the GCodeProvider
	 * @throws GkException GkException
	 */
	public void removeRenderer(Integer idGCodeProvider) throws GkException{
		RS274GCodeRenderer renderer = getRendererByGCodeProvider(idGCodeProvider);
		this.lstRenderer.remove(renderer.getId());
		Activator.getJoglViewerService().removeRenderer(renderer);
	}
	
	public RS274GCodeRenderer getRendererByGCodeProvider(Integer idGCodeProvider) throws GkException{
		for (RS274GCodeRenderer renderer : lstRenderer.get()) {
			if(ObjectUtils.equals(idGCodeProvider, renderer.getIdGCodeProvider())){
				return renderer;
			}
		}
		throw new GkTechnicalException("Renderer for GCodeProvider with internal id ["+idGCodeProvider+"] does not exist");
	}

	/**
	 * @return the workspaceService
	 */
	public IWorkspaceService getWorkspaceService() {
		return workspaceService;
	}

	/**
	 * @param workspaceService the workspaceService to set
	 * @throws GkException GkException 
	 */
	public void setWorkspaceService(IWorkspaceService workspaceService) throws GkException {
		if(this.workspaceService != null){
			this.workspaceService.removeWorkspaceListener(this);
		}
		this.workspaceService = workspaceService;
		if(this.workspaceService != null){
			this.workspaceService.addWorkspaceListener(this);
		}
	}
}

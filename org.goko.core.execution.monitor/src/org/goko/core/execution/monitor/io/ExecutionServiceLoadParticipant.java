/**
 * 
 */
package org.goko.core.execution.monitor.io;

import java.util.ArrayList;

import org.apache.commons.collections.CollectionUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.io.xml.IXmlPersistenceService;
import org.goko.core.common.service.IGokoService;
import org.goko.core.execution.monitor.io.xml.XmlExecutionService;
import org.goko.core.execution.monitor.io.xml.XmlExecutionToken;
import org.goko.core.execution.monitor.service.ExecutionServiceImpl;
import org.goko.core.gcode.element.IGCodeProvider;
import org.goko.core.gcode.execution.ExecutionToken;
import org.goko.core.gcode.execution.ExecutionTokenState;
import org.goko.core.gcode.service.IGCodeProviderRepository;
import org.goko.core.log.GkLog;
import org.goko.core.workspace.io.LoadContext;
import org.goko.core.workspace.service.IProjectLoadParticipant;

/**
 * @author PsyKo
 * @date 1 janv. 2016
 */
public class ExecutionServiceLoadParticipant implements IProjectLoadParticipant<XmlExecutionService>, IGokoService {
	/** LOG */
	private static final GkLog LOG = GkLog.getLogger(ExecutionServiceLoadParticipant.class);
	/** Service ID */
	private static final String SERVICE_ID = "org.goko.core.execution.monitor.io.ExecutionServiceLoadParticipant";
	/** XML persistence service */
	private IXmlPersistenceService xmlPersistenceService;
	/** The target execution service */
	private ExecutionServiceImpl executionService;
	/** GCode provider repository */
	private IGCodeProviderRepository gcodeRepository;
	
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
		LOG.info("Starting  "+getServiceId());
		
		LOG.info("Successfully started "+getServiceId());
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#stop()
	 */
	@Override
	public void stop() throws GkException {
		// TODO Auto-generated method stub
		
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IProjectLoadParticipant#getContainerClass()
	 */
	@Override
	public Class<XmlExecutionService> getContainerClass() {		
		return XmlExecutionService.class;
	}
	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IProjectLoadParticipant#load(org.goko.core.workspace.io.LoadContext, org.goko.core.workspace.io.XmlProjectContainer)
	 */
	@Override
	public void load(LoadContext context, XmlExecutionService container, IProgressMonitor monitor) throws GkException {
		ArrayList<XmlExecutionToken> lstToken = container.getLstExecutionToken();
		
		if(CollectionUtils.isNotEmpty(lstToken)){
			for (XmlExecutionToken xmlExecutionToken : lstToken) {
				IGCodeProvider provider = gcodeRepository.getGCodeProvider(xmlExecutionToken.getCodeGCodeProvider());
				ExecutionToken<ExecutionTokenState> token = new ExecutionToken<ExecutionTokenState>(gcodeRepository, provider, ExecutionTokenState.NONE);
				executionService.addToExecutionQueue(token);
			}
		}
	}

	/**
	 * @return the xmlPersistenceService
	 */
	public IXmlPersistenceService getXmlPersistenceService() {
		return xmlPersistenceService;
	}

	/**
	 * @param xmlPersistenceService the xmlPersistenceService to set
	 */
	public void setXmlPersistenceService(IXmlPersistenceService xmlPersistenceService) {
		this.xmlPersistenceService = xmlPersistenceService;
	}

	/**
	 * @return the executionService
	 */
	public ExecutionServiceImpl getExecutionService() {
		return executionService;
	}

	/**
	 * @param executionService the executionService to set
	 */
	public void setExecutionService(ExecutionServiceImpl executionService) {
		this.executionService = executionService;
	}

	/**
	 * @return the gcodeRepository
	 */
	public IGCodeProviderRepository getGcodeRepository() {
		return gcodeRepository;
	}

	/**
	 * @param gcodeRepository the gcodeRepository to set
	 */
	public void setGcodeRepository(IGCodeProviderRepository gcodeRepository) {
		this.gcodeRepository = gcodeRepository;
	}
}

/**
 * 
 */
package org.goko.core.execution.monitor.io;

import java.io.File;
import java.util.ArrayList;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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
import org.goko.core.workspace.io.XmlProjectContainer;
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
	/** Type of laoded container */
	private static final String EXECUTION_SERVICE_CONTENT_TYPE = "executionService";
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
	 * @see org.goko.core.workspace.service.IProjectLoadParticipant#load(org.goko.core.workspace.io.LoadContext, org.goko.core.workspace.io.XmlProjectContainer)
	 */
	@Override
	public void load(LoadContext context, XmlProjectContainer container, IProgressMonitor monitor) throws GkException {
		String filePath = container.getPath();
		File file = new File(context.getResourcesFolder().getParentFile(), filePath);

		XmlExecutionService content = xmlPersistenceService.read(XmlExecutionService.class, file);
		load(content);
		
	}

	/**
	 * Load the content of the given XmlExecutionService
	 * @param content the content to load
	 * @throws GkException GkException 
	 */
	private void load(XmlExecutionService content) throws GkException {
		ArrayList<XmlExecutionToken> lstToken = content.getLstExecutionToken();
		
		if(CollectionUtils.isNotEmpty(lstToken)){
			for (XmlExecutionToken xmlExecutionToken : lstToken) {
				IGCodeProvider provider = gcodeRepository.getGCodeProvider(xmlExecutionToken.getCodeGCodeProvider());
				ExecutionToken<ExecutionTokenState> token = new ExecutionToken<ExecutionTokenState>(gcodeRepository, provider, ExecutionTokenState.NONE);
				executionService.addToExecutionQueue(token);
			}
		}		
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IProjectLoadParticipant#canLoad(org.goko.core.workspace.io.XmlProjectContainer)
	 */
	@Override
	public boolean canLoad(XmlProjectContainer container) throws GkException {
		return StringUtils.equals(EXECUTION_SERVICE_CONTENT_TYPE, container.getType());
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

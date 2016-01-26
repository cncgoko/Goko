/**
 * 
 */
package org.goko.core.execution.monitor.io;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.io.xml.IXmlPersistenceService;
import org.goko.core.common.service.IGokoService;
import org.goko.core.execution.monitor.io.xml.XmlExecutionService;
import org.goko.core.execution.monitor.io.xml.XmlExecutionToken;
import org.goko.core.execution.monitor.service.ExecutionServiceImpl;
import org.goko.core.gcode.execution.ExecutionQueue;
import org.goko.core.gcode.execution.ExecutionTokenState;
import org.goko.core.gcode.execution.ExecutionToken;
import org.goko.core.log.GkLog;
import org.goko.core.workspace.io.SaveContext;
import org.goko.core.workspace.io.XmlProjectContainer;
import org.goko.core.workspace.service.IProjectSaveParticipant;

/**
 * @author PsyKo
 * @date 1 janv. 2016
 */
public class ExecutionServiceSaveParticipant implements IProjectSaveParticipant<XmlExecutionService>, IGokoService {
	/** LOG */
	private static final GkLog LOG = GkLog.getLogger(ExecutionServiceSaveParticipant.class);
	/** Service ID */
	private static final String SERVICE_ID = "org.goko.core.execution.monitor.io.ExecutionServiceSaveParticipant";
	/** Type of loaded container */
	private static final String EXECUTION_SERVICE_CONTENT_TYPE = "executionService";
	/** Target file for the container */
	private static final String EXECUTION_SERVICE_CONTENT_FILE_NAME = "executionService.xml";
	/** XML persistence service */
	private IXmlPersistenceService xmlPersistenceService;
	/** The target execution service */
	private ExecutionServiceImpl executionService;
	
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
	 * @see org.goko.core.workspace.service.IProjectSaveParticipant#isDirty()
	 */
	@Override
	public boolean isDirty() {
		// TODO Auto-generated method stub
		return false;
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IProjectSaveParticipant#save(org.goko.core.workspace.io.SaveContext)
	 */
	@Override
	public List<XmlProjectContainer> save(SaveContext context) throws GkException {
		List<XmlProjectContainer> containers = new ArrayList<XmlProjectContainer>();

		XmlProjectContainer rs274Container = new XmlProjectContainer();
		rs274Container.setType(EXECUTION_SERVICE_CONTENT_TYPE);
		rs274Container.setPath(context.getResourcePath(EXECUTION_SERVICE_CONTENT_FILE_NAME));
		containers.add(rs274Container);
		persistContent(new File(context.getResourcesFolder(), EXECUTION_SERVICE_CONTENT_FILE_NAME));
		return containers;
	}

	private void persistContent(File target) throws GkException{
		XmlExecutionService content = new XmlExecutionService();
		ExecutionQueue<ExecutionTokenState, ExecutionToken<ExecutionTokenState>> queue = executionService.getExecutionQueue();
		List<ExecutionToken<ExecutionTokenState>> tokens = queue.getExecutionToken();
		
		ArrayList<XmlExecutionToken> lstExecutionToken = new ArrayList<XmlExecutionToken>();
		if(CollectionUtils.isNotEmpty(tokens)){
			for (ExecutionToken<ExecutionTokenState> executionToken : tokens) {
				XmlExecutionToken xmlToken = new XmlExecutionToken();
				xmlToken.setExecutionOrder(executionToken.getExecutionOrder());
				xmlToken.setCodeGCodeProvider(executionToken.getGCodeProvider().getCode());
				lstExecutionToken.add(xmlToken);
			}
		}
		
		content.setLstExecutionToken(lstExecutionToken);
		xmlPersistenceService.write(content, target);
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IProjectSaveParticipant#rollback()
	 */
	@Override
	public void rollback() {
		// TODO Auto-generated method stub
		
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IProjectSaveParticipant#saveComplete()
	 */
	@Override
	public void saveComplete() {
		// TODO Auto-generated method stub
		
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

}

/**
 * 
 */
package org.goko.core.execution.monitor.io;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.io.xml.IXmlPersistenceService;
import org.goko.core.common.service.IGokoService;
import org.goko.core.execution.monitor.io.bean.XmlExecutionService;
import org.goko.core.execution.monitor.io.bean.XmlExecutionToken;
import org.goko.core.execution.monitor.io.exporter.XmlExecutionTokenExporter;
import org.goko.core.execution.monitor.service.ExecutionServiceImpl;
import org.goko.core.gcode.execution.ExecutionQueue;
import org.goko.core.gcode.execution.ExecutionQueueType;
import org.goko.core.gcode.execution.ExecutionToken;
import org.goko.core.gcode.execution.ExecutionTokenState;
import org.goko.core.log.GkLog;
import org.goko.core.workspace.io.IProjectLocation;
import org.goko.core.workspace.io.XmlProjectContainer;
import org.goko.core.workspace.service.IMapperService;
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
	/** Target file for the container */
	private static final String EXECUTION_SERVICE_CONTENT_FILE_NAME = "executionService.xml";
	/** XML persistence service */
	private IXmlPersistenceService xmlPersistenceService;
	/** The target execution service */
	private ExecutionServiceImpl executionService;
	/** Mapper service */
	private IMapperService mapperService;
	
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
		xmlPersistenceService.register(XmlExecutionService.class);
		xmlPersistenceService.register(XmlExecutionToken.class);
		mapperService.addExporter(new XmlExecutionTokenExporter());
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
	 * @see org.goko.core.workspace.service.IProjectSaveParticipant#save(org.goko.core.workspace.io.IProjectOutputLocation)
	 */
	@Override
	public List<XmlProjectContainer> save(IProjectLocation outputLocation) throws GkException {
		List<XmlProjectContainer> containers = new ArrayList<XmlProjectContainer>();

		XmlExecutionService container = persistContent();
		containers.add(container);		
		return containers;
	}

	private XmlExecutionService persistContent() throws GkException{
		XmlExecutionService content = new XmlExecutionService();
		ExecutionQueue<ExecutionTokenState, ExecutionToken<ExecutionTokenState>> queue = executionService.getExecutionQueue(ExecutionQueueType.DEFAULT);
		List<ExecutionToken<ExecutionTokenState>> tokens = queue.getExecutionToken();
		
		ArrayList<XmlExecutionToken> lstExecutionToken = new ArrayList<XmlExecutionToken>();
		if(CollectionUtils.isNotEmpty(tokens)){
			for (ExecutionToken<ExecutionTokenState> executionToken : tokens) {
				XmlExecutionToken xmlToken = mapperService.export(executionToken, XmlExecutionToken.class);
				lstExecutionToken.add(xmlToken);				
			}
		}
		
		content.setLstExecutionToken(lstExecutionToken);		
		return content;
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

	/**
	 * @return the mapperService
	 */
	public IMapperService getMapperService() {
		return mapperService;
	}

	/**
	 * @param mapperService the mapperService to set
	 */
	public void setMapperService(IMapperService mapperService) {
		this.mapperService = mapperService;
	}

}

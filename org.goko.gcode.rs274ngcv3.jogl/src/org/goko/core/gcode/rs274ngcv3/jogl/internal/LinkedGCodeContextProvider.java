/**
 * 
 */
package org.goko.core.gcode.rs274ngcv3.jogl.internal;

import java.util.Date;

import org.goko.core.common.exception.GkException;
import org.goko.core.controller.IGCodeContextProvider;
import org.goko.core.controller.event.IGCodeContextListener;
import org.goko.core.gcode.element.IInstructionSetIterator;
import org.goko.core.gcode.execution.ExecutionToken;
import org.goko.core.gcode.rs274ngcv3.IRS274NGCService;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.InstructionProvider;
import org.goko.core.gcode.rs274ngcv3.instruction.AbstractInstruction;
import org.goko.core.log.GkLog;

/**
 * @author Psyko
 * @date 10 juil. 2016
 */
public class LinkedGCodeContextProvider implements IGCodeContextProvider<GCodeContext> {
	private static final GkLog LOG = GkLog.getLogger(LinkedGCodeContextProvider.class);
	private LinkedGCodeContextProvider previous;
	private ExecutionToken token;
	private IRS274NGCService gcodeService;
	private GCodeContext cachedContext;
	private Date updateDate;
	
	
	/**
	 * @param previous
	 * @param token
	 * @param gcodeService
	 */
	public LinkedGCodeContextProvider(LinkedGCodeContextProvider previous, ExecutionToken token, IRS274NGCService gcodeService) {
		super();
		this.previous = previous;
		this.token = token;
		this.gcodeService = gcodeService;
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.event.IObservable#addObserver(java.lang.Object)
	 */
	@Override
	public void addObserver(IGCodeContextListener<GCodeContext> observer) { 
		throw new RuntimeException("Not implemented");
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.common.event.IObservable#removeObserver(java.lang.Object)
	 */
	@Override
	public boolean removeObserver(IGCodeContextListener<GCodeContext> observer) {
		throw new RuntimeException("Not implemented");
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.controller.IGCodeContextProvider#getGCodeContext()
	 */
	@Override
	public GCodeContext getGCodeContext() throws GkException {
		computeGCodeContext();
		return new GCodeContext(cachedContext);
	}
	
	protected void computeGCodeContext(){
		try{
			previous.getGCodeContext();
			if(updateDate == null || cachedContext == null || previous.getUpdateDate().getTime() > updateDate.getTime()){			
				InstructionProvider instrProvider = gcodeService.getInstructions(previous.getGCodeContext(), gcodeService.getGCodeProvider(token.getIdGCodeProvider()));
				IInstructionSetIterator<GCodeContext, AbstractInstruction> iterator = gcodeService.getIterator(instrProvider, previous.getGCodeContext());
				while (iterator.hasNext()) {
					iterator.next();
				}
				cachedContext = new GCodeContext(iterator.getContext());
				updateDate = new Date();
			}
		}catch(GkException e){
			LOG.error(e);
		}
	}

	/**
	 * @return the updateDate
	 */
	public Date getUpdateDate() {
		return updateDate;
	}

	/**
	 * @param updateDate the updateDate to set
	 */
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	/**
	 * @return the gcodeService
	 */
	protected IRS274NGCService getGcodeService() {
		return gcodeService;
	}

	/**
	 * @param gcodeService the gcodeService to set
	 */
	protected void setGcodeService(IRS274NGCService gcodeService) {
		this.gcodeService = gcodeService;
	}

	/**
	 * @return the cachedContext
	 */
	protected GCodeContext getCachedContext() {
		return cachedContext;
	}

	/**
	 * @param cachedContext the cachedContext to set
	 */
	protected void setCachedContext(GCodeContext cachedContext) {
		this.cachedContext = cachedContext;
	}

	/**
	 * @return the token
	 */
	public ExecutionToken getToken() {
		return token;
	}

	/**
	 * @param token the token to set
	 */
	protected void setToken(ExecutionToken token) {
		this.token = token;
	}

	/**
	 * @return the previous
	 */
	public LinkedGCodeContextProvider getPrevious() {
		return previous;
	}

	/**
	 * @param previous the previous to set
	 */
	public void setPrevious(LinkedGCodeContextProvider previous) {
		this.previous = previous;
	}

	/**
	 * Force update
	 */
	public void update() {
		updateDate = null;
	}
}

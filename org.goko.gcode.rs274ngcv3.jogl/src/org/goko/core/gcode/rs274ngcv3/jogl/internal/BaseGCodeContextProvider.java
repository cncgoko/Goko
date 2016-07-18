/**
 * 
 */
package org.goko.core.gcode.rs274ngcv3.jogl.internal;

import java.util.Date;

import org.goko.core.common.exception.GkException;
import org.goko.core.controller.IGCodeContextProvider;
import org.goko.core.controller.event.IGCodeContextListener;
import org.goko.core.gcode.execution.ExecutionToken;
import org.goko.core.gcode.rs274ngcv3.IRS274NGCService;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.log.GkLog;

/**
 * @author Psyko
 * @date 10 juil. 2016
 */
public class BaseGCodeContextProvider extends LinkedGCodeContextProvider implements IGCodeContextListener<GCodeContext> {
	private static final GkLog LOG = GkLog.getLogger(BaseGCodeContextProvider.class);
	/** The wrapped provider */
	private IGCodeContextProvider<GCodeContext> baseContextProvider;

	/**
	 * @param previous
	 * @param token
	 * @param gcodeService
	 */
	public BaseGCodeContextProvider(IGCodeContextProvider<GCodeContext> baseContextProvider, ExecutionToken token, IRS274NGCService gcodeService) {
		super(null, token, gcodeService);
		this.baseContextProvider = baseContextProvider;
		
		setUpdateDate(new Date());
		baseContextProvider.addObserver(this);
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.jogl.internal.LinkedGCodeContextProvider#computeGCodeContext()
	 */
	@Override
	protected void computeGCodeContext() {
		try {
			if(getCachedContext() == null){
				setCachedContext(baseContextProvider.getGCodeContext());
			}
		} catch (GkException e) {
			LOG.error(e);
		}		
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.event.IGCodeContextListener#onGCodeContextEvent(org.goko.core.gcode.element.IGCodeContext)
	 */
	@Override
	public void onGCodeContextEvent(GCodeContext context) {
		setUpdateDate(new Date());
		setCachedContext(new GCodeContext(context));	
	}
}

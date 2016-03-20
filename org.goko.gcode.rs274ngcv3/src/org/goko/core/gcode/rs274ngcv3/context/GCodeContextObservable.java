/**
 * 
 */
package org.goko.core.gcode.rs274ngcv3.context;

import org.goko.core.common.event.ObservableDelegate;
import org.goko.core.controller.event.IGCodeContextListener;

/**
 * @author PsyKo
 * @date 24 févr. 2016
 */
public class GCodeContextObservable extends ObservableDelegate<IGCodeContextListener<GCodeContext>> {
						
	/**
	 * Constructor
	 */
	@SuppressWarnings("unchecked")
	public GCodeContextObservable() {
		super((Class<IGCodeContextListener<GCodeContext>>)(Object)IGCodeContextListener.class);
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.common.event.AbstractObservable#getEventDispatcher()
	 */
	@Override
	public IGCodeContextListener<GCodeContext> getEventDispatcher() {
		return super.getEventDispatcher();
	}
}

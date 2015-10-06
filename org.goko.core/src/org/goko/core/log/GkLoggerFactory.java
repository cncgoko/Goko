/**
 * 
 */
package org.goko.core.log;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;

/**
 * @author PsyKo
 *
 */
public class GkLoggerFactory implements ILoggerFactory {
	private org.eclipse.e4.core.services.log.Logger eclipseLogger;
	/** (inheritDoc)
	 * @see org.slf4j.ILoggerFactory#getLogger(java.lang.String)
	 */
	@Override
	public Logger getLogger(String name) {		
		return new GkLogger(name, eclipseLogger);
	}

	public void setEclipseLogger(org.eclipse.e4.core.services.log.Logger logger){
		eclipseLogger = logger;
	}
}

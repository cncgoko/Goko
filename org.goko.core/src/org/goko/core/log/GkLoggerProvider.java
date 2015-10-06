/**
 * 
 */
package org.goko.core.log;

import org.eclipse.e4.core.services.log.ILoggerProvider;
import org.eclipse.e4.core.services.log.Logger;

/**
 * @author PsyKo
 *
 */
public class GkLoggerProvider implements ILoggerProvider {

	/** (inheritDoc)
	 * @see org.eclipse.e4.core.services.log.ILoggerProvider#getClassLogger(java.lang.Class)
	 */
	@Override
	public Logger getClassLogger(Class<?> clazz) {
		return null;
	}

}

/**
 * 
 */
package org.goko.core.gcode.rs274ngcv3.element.source;

import java.io.InputStream;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.element.IGCodeProviderSource;

/**
 * @author PsyKo
 * @date 30 mars 2016
 */
public class DefaultGCodeProviderSource implements IGCodeProviderSource{

	/** (inheritDoc)
	 * @see org.goko.core.gcode.element.IGCodeProviderSource#openInputStream()
	 */
	@Override
	public InputStream openInputStream() throws GkException {		
		return null;
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.element.IGCodeProviderSource#delete()
	 */
	@Override
	public void delete() throws GkException {
		
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.element.IGCodeProviderSource#bind()
	 */
	@Override
	public void bind() throws GkException {
		
	}

}

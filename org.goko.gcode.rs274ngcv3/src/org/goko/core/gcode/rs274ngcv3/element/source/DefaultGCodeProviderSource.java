/**
 * 
 */
package org.goko.core.gcode.rs274ngcv3.element.source;

import java.io.InputStream;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.element.AbstractGCodeProviderSource;

/**
 * @author PsyKo
 * @date 30 mars 2016
 */
public class DefaultGCodeProviderSource extends AbstractGCodeProviderSource{

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

	/** (inheritDoc)
	 * @see org.goko.core.gcode.element.IGCodeProviderSource#canWrite()
	 */
	@Override
	public boolean canWrite() {		
		return false;
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.element.IGCodeProviderSource#write(java.io.InputStream)
	 */
	@Override
	public void write(InputStream input) throws GkException {
		throw new UnsupportedOperationException();
	}

}

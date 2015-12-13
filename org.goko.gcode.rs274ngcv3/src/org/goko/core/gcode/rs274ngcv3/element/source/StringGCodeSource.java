/**
 * 
 */
package org.goko.core.gcode.rs274ngcv3.element.source;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.element.IGCodeProviderSource;

/**
 * @author PsyKo
 * @date 13 déc. 2015
 */
public class StringGCodeSource implements IGCodeProviderSource {
	/** The source array */
	private ByteArrayInputStream source; 
		
	/**
	 * Constructor
	 * @param source the source string
	 */
	public StringGCodeSource(String source) {
		super();
		this.source = new ByteArrayInputStream(source.getBytes());
	}


	/** (inheritDoc)
	 * @see org.goko.core.gcode.element.IGCodeProviderSource#getInputStream()
	 */
	@Override
	public InputStream getInputStream() throws GkException {		
		return source;
	}

}

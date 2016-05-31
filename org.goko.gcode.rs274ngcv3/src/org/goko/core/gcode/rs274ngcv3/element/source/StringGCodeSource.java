/**
 * 
 */
package org.goko.core.gcode.rs274ngcv3.element.source;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.element.AbstractGCodeProviderSource;

/**
 * @author PsyKo
 * @date 13 d�c. 2015
 */
public class StringGCodeSource extends AbstractGCodeProviderSource {
	/** The source array */
	private String source; 
		
	/**
	 * Constructor
	 * @param source the source string
	 */
	public StringGCodeSource(String source) {
		super();
		this.source = source;
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.workspace.io.IResourceLocation#openInputStream()
	 */
	@Override
	public InputStream openInputStream() throws GkException {
		return new ByteArrayInputStream(source.getBytes());
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

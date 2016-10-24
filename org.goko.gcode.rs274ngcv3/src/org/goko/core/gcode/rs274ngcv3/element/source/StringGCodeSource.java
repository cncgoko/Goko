/**
 * 
 */
package org.goko.core.gcode.rs274ngcv3.element.source;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkTechnicalException;
import org.goko.core.gcode.element.AbstractGCodeProviderSource;

/**
 * @author PsyKo
 * @date 13 d�c. 2015
 */
public class StringGCodeSource extends AbstractGCodeProviderSource {
	/** The source array */
	private String content; 
		
	/**
	 * Constructor
	 * @param source the source string
	 */
	public StringGCodeSource(String source) {
		super();
		this.content = source;
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.workspace.io.IResourceLocation#openInputStream()
	 */
	@Override
	public InputStream openInputStream() throws GkException {
		return new ByteArrayInputStream(content.getBytes());
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
		return true;
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.element.IGCodeProviderSource#write(java.io.InputStream)
	 */
	@Override
	public void write(InputStream input) throws GkException {	
		try {
			content = IOUtils.toString(input);
			IOUtils.closeQuietly(input);
		} catch (IOException e) {
			throw new GkTechnicalException(e);
		}
				
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}
}

/**
 * 
 */
package org.goko.core.gcode.element;

import java.io.InputStream;

import org.goko.core.common.exception.GkException;

/**
 * @author PsyKo
 * @date 13 d�c. 2015
 */
public interface IGCodeProviderSource {
	
	InputStream openInputStream() throws GkException;
	
	boolean canWrite();
	
	void write(InputStream input) throws GkException;
	
	void delete() throws GkException;
	
	void bind() throws GkException;
	
	void addListener(IGCodeProviderSourceListener listener);
}

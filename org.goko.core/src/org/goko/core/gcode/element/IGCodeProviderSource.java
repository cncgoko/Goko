/**
 * 
 */
package org.goko.core.gcode.element;

import java.io.InputStream;

import org.goko.core.common.exception.GkException;

/**
 * @author PsyKo
 * @date 13 déc. 2015
 */
public interface IGCodeProviderSource {
	
	InputStream getInputStream() throws GkException;
}

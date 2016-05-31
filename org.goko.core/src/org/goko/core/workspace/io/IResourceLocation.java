/**
 * 
 */
package org.goko.core.workspace.io;

import java.io.InputStream;

import org.goko.core.common.exception.GkException;

/**
 * @author PsyKo
 * @date 18 mars 2016
 */
public interface IResourceLocation {

	InputStream openInputStream() throws GkException;
	
	void bind() throws GkException;
	
	void release() throws GkException;
	
	int getReferenceCount();
	
	boolean canWrite();
	
	void write(InputStream input) throws GkException;
	
	void addListener(IResourceLocationListener listener);
}

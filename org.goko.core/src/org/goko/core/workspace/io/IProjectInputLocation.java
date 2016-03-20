/**
 * 
 */
package org.goko.core.workspace.io;

import java.io.InputStream;

import org.goko.core.common.exception.GkException;

/**
 * @author PsyKo
 * @date 9 mars 2016
 */
public interface IProjectInputLocation extends IProjectLocation{
		
	InputStream getProjectFile() throws GkException;
		
	InputStream getFile(String name) throws GkException;
	
	void read() throws GkException;
}

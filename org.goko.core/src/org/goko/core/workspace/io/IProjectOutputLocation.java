/**
 * 
 */
package org.goko.core.workspace.io;

import java.io.File;
import java.io.InputStream;

import org.goko.core.common.exception.GkException;

/**
 * @author PsyKo
 * @date 9 mars 2016
 */
public interface IProjectOutputLocation{
		
	void setName(String name);
	
	void setProjectFile(InputStream stream) throws GkException;
	
	void addFile(File file, String name) throws GkException;
	
	void addFile(InputStream stream, String name) throws GkException;
	
	void write() throws GkException;
	
	IProjectLocation getProjectLocation() throws GkException;
}

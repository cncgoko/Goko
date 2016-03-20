/**
 * 
 */
package org.goko.core.workspace.io;

import java.io.InputStream;
import java.net.URI;

import org.goko.core.common.exception.GkException;

/**
 * @author PsyKo
 * @date 14 mars 2016
 */
public interface IProjectLocation {

	String getName();
	
	boolean isLocationDefined();
	
	void setLocation(URI target);
	
	URI getLocation();
	
	void setProjectDescriptor(InputStream projectDescriptor) throws GkException;
	
	InputStream getProjectDescriptor() throws GkException;
	
	void setName(String name);
	
	IResourceLocation addResource(String name, URI uri) throws GkException;
	
	void removeResource(String name)  throws GkException;
	
	IResourceLocation readResource(String name) throws GkException;
	
	void write() throws GkException;
	
	void read() throws GkException;
	
	void importProjectDependencies() throws GkException;
}

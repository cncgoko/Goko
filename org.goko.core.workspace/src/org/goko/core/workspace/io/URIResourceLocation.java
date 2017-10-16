/**
 * 
 */
package org.goko.core.workspace.io;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.io.IOUtils;
import org.eclipse.core.runtime.URIUtil;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkTechnicalException;

/**
 * @author PsyKo
 * @date 19 mars 2016
 */
public class URIResourceLocation implements IResourceLocation {
	/** The URI referencing the resource in the project */
	private URI uri;
	/** The absolute URI referencing the resource */
	private URI absoluteUri;
	/** The number of references to this location */
	private int referenceCount;	
	/** The listeners */
	private List<IResourceLocationListener> listeners;
	
	/**
	 * @param uri
	 */
	public URIResourceLocation(URI uri) {
		this(uri, null);
	}
	
	/**
	 * @param uri
	 */
	public URIResourceLocation(URI uri, URI absoluteUri) {
		super();
		this.uri = uri;
		this.referenceCount = 0;
		this.absoluteUri = absoluteUri;
		this.listeners = new CopyOnWriteArrayList<IResourceLocationListener>();
	}


	/** (inheritDoc)
	 * @see org.goko.core.workspace.io.IResourceLocation#openInputStream()
	 */
	@Override
	public InputStream openInputStream() throws GkException {
		try {
			return absoluteUri.toURL().openStream();
		} catch (IOException e) {
			throw new GkTechnicalException(e);
		}
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.io.IResourceLocation#canWrite()
	 */
	@Override
	public boolean canWrite() {		
		return URIUtil.isFileURI(absoluteUri);
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.workspace.io.IResourceLocation#write(java.io.InputStream)
	 */
	@Override
	public void write(InputStream input) throws GkException {		
		try {
			Files.write(Paths.get(absoluteUri), IOUtils.toByteArray(input), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
			notifyChange();
		} catch (AccessDeniedException e) {
			throw new GkTechnicalException("Cannot write to the target file '"+Paths.get(absoluteUri).toAbsolutePath()+"'. Make sure it is not a read only file.");
		} catch (IOException e) {
			throw new GkTechnicalException(e);
		}
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.workspace.io.IResourceLocation#bind()
	 */
	@Override
	public void bind() throws GkException {
		this.referenceCount += 1;
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.workspace.io.IResourceLocation#delete()
	 */
	@Override
	public void release() throws GkException {
		this.referenceCount -= 1;		
	}
	
	/**
	 * @return the uri
	 */
	public URI getUri() {
		return uri;
	}


	/**
	 * @param uri the uri to set
	 */
	public void setUri(URI uri) {
		this.uri = uri;
	}


	/**
	 * @return the referenceCount
	 */
	public int getReferenceCount() {
		return referenceCount;
	}

	/**
	 * @return the absoluteUri
	 */
	public URI getAbsoluteUri() {
		return absoluteUri;
	}

	/**
	 * @param absoluteUri the absoluteUri to set
	 */
	public void setAbsoluteUri(URI absoluteUri) {
		this.absoluteUri = absoluteUri;
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.io.IResourceLocation#addListener(org.goko.core.workspace.io.IResourceLocationListener)
	 */
	@Override
	public void addListener(IResourceLocationListener listener) {
		if(!listeners.contains(listener)){
			listeners.add(listener);
		}
	}
	
	/**
	 * Notifies registered listener for changes on this resource
	 */
	protected void notifyChange(){
		for (IResourceLocationListener listener : listeners) {
			listener.onResourceChanged(this);
		}
	}
}

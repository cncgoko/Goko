/**
 * 
 */
package org.goko.core.workspace.io;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

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
	
	/**
	 * @param uri
	 */
	public URIResourceLocation(URI uri) {
		super();
		this.uri = uri;
		this.referenceCount = 0;
		this.absoluteUri = null;
	}
	
	/**
	 * @param uri
	 */
	public URIResourceLocation(URI uri, URI absoluteUri) {
		super();
		this.uri = uri;
		this.referenceCount = 0;
		this.absoluteUri = absoluteUri;
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

}

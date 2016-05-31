/**
 * 
 */
package org.goko.core.gcode.rs274ngcv3.element.source;

import java.io.InputStream;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.element.AbstractGCodeProviderSource;
import org.goko.core.workspace.io.IResourceLocation;
import org.goko.core.workspace.io.IResourceLocationListener;

/**
 * @author PsyKo
 * @date 15 mars 2016
 */
public class ResourceLocationGCodeSource extends AbstractGCodeProviderSource implements IResourceLocationListener{
	/** The target resource */
	private IResourceLocation resourceLocation;
		
	/**
	 * Constructor
	 * @param file the target file
	 */
	public ResourceLocationGCodeSource(IResourceLocation location) {
		super();
		this.resourceLocation = location;
		this.resourceLocation.addListener(this);
	}


	/** (inheritDoc)
	 * @see org.goko.core.gcode.element.IGCodeProviderSource#getInputStream()
	 */
	@Override
	public InputStream openInputStream() throws GkException {		
		return resourceLocation.openInputStream();
	}


	/**
	 * @return the resourceLocation
	 */
	public IResourceLocation getResourceLocation() {
		return resourceLocation;
	}


	/**
	 * @param resourceLocation the resourceLocation to set
	 */
	public void setResourceLocation(IResourceLocation resourceLocation) {
		this.resourceLocation = resourceLocation;
	}


	/** (inheritDoc)
	 * @see org.goko.core.gcode.element.IGCodeProviderSource#delete()
	 */
	@Override
	public void delete() throws GkException {
		this.resourceLocation.release();
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.element.IGCodeProviderSource#bind()
	 */
	@Override
	public void bind() throws GkException {
		this.resourceLocation.bind();
	}


	/** (inheritDoc)
	 * @see org.goko.core.gcode.element.IGCodeProviderSource#canWrite()
	 */
	@Override
	public boolean canWrite() {	
		return resourceLocation.canWrite();
	}


	/** (inheritDoc)
	 * @see org.goko.core.gcode.element.IGCodeProviderSource#write(java.io.InputStream)
	 */
	@Override
	public void write(InputStream input) throws GkException {
		resourceLocation.write(input);
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.io.IResourceLocationListener#onResourceChanged(org.goko.core.workspace.io.IResourceLocation)
	 */
	@Override
	public void onResourceChanged(IResourceLocation resourceLocation) {
		// Propagate the underlying resource change as a change on this source 
		notifyChange();		
	}
}

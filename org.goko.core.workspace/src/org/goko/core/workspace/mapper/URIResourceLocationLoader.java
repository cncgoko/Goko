/**
 * 
 */
package org.goko.core.workspace.mapper;

import java.net.URI;
import java.net.URISyntaxException;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkTechnicalException;
import org.goko.core.workspace.io.URIResourceLocation;
import org.goko.core.workspace.io.xml.XmlURIResourceLocation;
import org.goko.core.workspace.service.ILoader;
import org.goko.core.workspace.service.IMapperService;
import org.goko.core.workspace.service.IWorkspaceService;

/**
 * @author PsyKo
 * @date 19 mars 2016
 */
public class URIResourceLocationLoader implements ILoader<XmlURIResourceLocation, URIResourceLocation>{
	/** The workspace service */
	private IWorkspaceService workspaceService;
	
	
	/**
	 * @param workspaceService
	 */
	public URIResourceLocationLoader(IWorkspaceService workspaceService) {
		super();
		this.workspaceService = workspaceService;
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.ILoader#load(java.lang.Object, org.goko.core.workspace.service.IMapperService)
	 */
	@Override
	public URIResourceLocation load(XmlURIResourceLocation input, IMapperService mapperService) throws GkException {		
		URIResourceLocation location = null;
		try {			
			URI uri  = new URI(input.getUri());			
			location = (URIResourceLocation) workspaceService.getProject().getLocation().addResource(input.getUri(), uri);
		} catch (URISyntaxException e) {
			throw new GkTechnicalException(e);
		}
		return location;
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.ILoader#getOutputClass()
	 */
	@Override
	public Class<URIResourceLocation> getOutputClass() {
		return URIResourceLocation.class;
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.ILoader#getInputClass()
	 */
	@Override
	public Class<XmlURIResourceLocation> getInputClass() {
		return XmlURIResourceLocation.class;
	}
	
}

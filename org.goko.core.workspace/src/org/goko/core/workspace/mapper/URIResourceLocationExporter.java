/**
 * 
 */
package org.goko.core.workspace.mapper;

import org.goko.core.common.exception.GkException;
import org.goko.core.workspace.io.URIResourceLocation;
import org.goko.core.workspace.io.xml.XmlURIResourceLocation;
import org.goko.core.workspace.service.IExporter;
import org.goko.core.workspace.service.IMapperService;

/**
 * @author PsyKo
 * @date 19 mars 2016
 */
public class URIResourceLocationExporter implements IExporter<URIResourceLocation, XmlURIResourceLocation>{
	
	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IExporter#export(java.lang.Object, org.goko.core.workspace.service.IMapperService)
	 */
	@Override
	public XmlURIResourceLocation export(URIResourceLocation input, IMapperService mapperService) throws GkException {
		XmlURIResourceLocation xmlUriResourceLocation = new XmlURIResourceLocation();
		xmlUriResourceLocation.setUri(input.getUri().toString());		
		return xmlUriResourceLocation;
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IExporter#getInputClass()
	 */
	@Override
	public Class<URIResourceLocation> getInputClass(){
		return URIResourceLocation.class;
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IExporter#getOutputClass()
	 */
	@Override
	public Class<XmlURIResourceLocation> getOutputClass() {
		return XmlURIResourceLocation.class;
	}
	
}

/**
 *
 */
package org.goko.gcode.rs274ngcv3.xml.loader.source;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.rs274ngcv3.element.source.ResourceLocationGCodeSource;
import org.goko.core.workspace.io.IResourceLocation;
import org.goko.core.workspace.service.ILoader;
import org.goko.core.workspace.service.IMapperService;
import org.goko.gcode.rs274ngcv3.xml.bean.source.XmlResourceLocationGCodeSource;

/**
 * @author PsyKo
 * @date 13 d�c. 2015
 */
public class ResourceLocationGCodeSourceLoader implements ILoader<XmlResourceLocationGCodeSource, ResourceLocationGCodeSource>{
	
	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.ILoader#load(java.lang.Object, org.goko.core.workspace.service.IMapperService)
	 */
	@Override
	public ResourceLocationGCodeSource load(XmlResourceLocationGCodeSource input, IMapperService mapperService) throws GkException {
		IResourceLocation resource = mapperService.load(input.getResourceLocation(), IResourceLocation.class);
		return new ResourceLocationGCodeSource(resource);
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.ILoader#getOutputClass()
	 */
	@Override
	public Class<ResourceLocationGCodeSource> getOutputClass() {
		return ResourceLocationGCodeSource.class;
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.ILoader#getInputClass()
	 */
	@Override
	public Class<XmlResourceLocationGCodeSource> getInputClass() {
		return XmlResourceLocationGCodeSource.class;
	}	
}

/**
 *
 */
package org.goko.gcode.rs274ngcv3.xml.exporter.source;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.rs274ngcv3.element.source.ResourceLocationGCodeSource;
import org.goko.core.workspace.io.xml.XmlResourceLocation;
import org.goko.core.workspace.service.IExporter;
import org.goko.core.workspace.service.IMapperService;
import org.goko.gcode.rs274ngcv3.xml.bean.source.XmlResourceLocationGCodeSource;

/**
 * @author PsyKo
 * @date 13 d�c. 2015
 */
public class ResourceLocationGCodeSourceExporter implements IExporter<ResourceLocationGCodeSource, XmlResourceLocationGCodeSource>{
		
	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IExporter#export(java.lang.Object, org.goko.core.workspace.service.IMapperService)
	 */
	@Override
	public XmlResourceLocationGCodeSource export(ResourceLocationGCodeSource input, IMapperService mapperService) throws GkException {
		XmlResourceLocation xmlResourceLocation = mapperService.export(input.getResourceLocation(), XmlResourceLocation.class);
		XmlResourceLocationGCodeSource xml = new XmlResourceLocationGCodeSource();
		xml.setResourceLocation(xmlResourceLocation);
		return xml;
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IExporter#getOutputClass()
	 */
	@Override
	public Class<XmlResourceLocationGCodeSource> getOutputClass() {
		return XmlResourceLocationGCodeSource.class;
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IExporter#getInputClass()
	 */
	@Override
	public Class<ResourceLocationGCodeSource> getInputClass() {
		return ResourceLocationGCodeSource.class;
	}	
}

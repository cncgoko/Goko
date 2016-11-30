/**
 * 
 */
package org.goko.gcode.rs274ngcv3.xml.exporter.reference;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.rs274ngcv3.RS274GCodeReference;
import org.goko.core.workspace.service.IExporter;
import org.goko.core.workspace.service.IMapperService;
import org.goko.gcode.rs274ngcv3.xml.bean.XmlRS274GCodeReference;

/**
 * @author Psyko
 * @date 23 nov. 2016
 */
public class RS274GCodeReferenceExporter implements IExporter<RS274GCodeReference, XmlRS274GCodeReference> {

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IExporter#export(java.lang.Object, org.goko.core.workspace.service.IMapperService)
	 */
	@Override
	public XmlRS274GCodeReference export(RS274GCodeReference input, IMapperService mapperService) throws GkException {
		XmlRS274GCodeReference reference = new XmlRS274GCodeReference();
		reference.setCode(input.getCode());
		return reference;
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IExporter#getOutputClass()
	 */
	@Override
	public Class<XmlRS274GCodeReference> getOutputClass() {
		return XmlRS274GCodeReference.class;
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IExporter#getInputClass()
	 */
	@Override
	public Class<RS274GCodeReference> getInputClass() {
		return RS274GCodeReference.class;
	}

}

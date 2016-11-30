/**
 * 
 */
package org.goko.gcode.rs274ngcv3.xml.loader.reference;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.element.IGCodeProvider;
import org.goko.core.gcode.rs274ngcv3.IRS274NGCService;
import org.goko.core.gcode.rs274ngcv3.RS274GCodeReference;
import org.goko.core.workspace.service.ILoader;
import org.goko.core.workspace.service.IMapperService;
import org.goko.gcode.rs274ngcv3.xml.bean.XmlRS274GCodeReference;

/**
 * @author Psyko
 * @date 23 nov. 2016
 */
public class RS274GCodeReferenceLoader implements ILoader<XmlRS274GCodeReference, RS274GCodeReference> {
	/** The RS274 GCode service*/
	private IRS274NGCService rs274service;
	
	/**
	 * @param rs274service
	 */
	public RS274GCodeReferenceLoader(IRS274NGCService rs274service) {
		super();
		this.rs274service = rs274service;
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.ILoader#load(java.lang.Object, org.goko.core.workspace.service.IMapperService)
	 */
	@Override
	public RS274GCodeReference load(XmlRS274GCodeReference input, IMapperService mapperService) throws GkException {
		IGCodeProvider provider = rs274service.getGCodeProvider(input.getCode());
		return new RS274GCodeReference(rs274service, provider.getId());
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.ILoader#getOutputClass()
	 */
	@Override
	public Class<RS274GCodeReference> getOutputClass() {
		return RS274GCodeReference.class;
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.ILoader#getInputClass()
	 */
	@Override
	public Class<XmlRS274GCodeReference> getInputClass() {
		return XmlRS274GCodeReference.class;
	}

}

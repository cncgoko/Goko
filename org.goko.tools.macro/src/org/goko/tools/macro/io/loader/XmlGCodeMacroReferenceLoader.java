/**
 * 
 */
package org.goko.tools.macro.io.loader;

import org.goko.core.common.exception.GkException;
import org.goko.core.workspace.service.ILoader;
import org.goko.core.workspace.service.IMapperService;
import org.goko.tools.macro.bean.GCodeMacroReference;
import org.goko.tools.macro.io.bean.XmlGCodeMacroReference;
import org.goko.tools.macro.service.IGCodeMacroService;

/**
 * @author Psyko
 * @date 23 nov. 2016
 */
public class XmlGCodeMacroReferenceLoader implements ILoader<XmlGCodeMacroReference, GCodeMacroReference> {
	/** The macro service */
	private IGCodeMacroService macroService;
	
	/**
	 * @param macroService
	 */
	public XmlGCodeMacroReferenceLoader(IGCodeMacroService macroService) {
		super();
		this.macroService = macroService;
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.ILoader#load(java.lang.Object, org.goko.core.workspace.service.IMapperService)
	 */
	@Override
	public GCodeMacroReference load(XmlGCodeMacroReference input, IMapperService mapperService) throws GkException {
		return new GCodeMacroReference(macroService, input.getCode());
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IExporter#getOutputClass()
	 */
	@Override
	public Class<GCodeMacroReference> getOutputClass() {
		return GCodeMacroReference.class;
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IExporter#getInputClass()
	 */
	@Override
	public Class<XmlGCodeMacroReference> getInputClass() {
		return XmlGCodeMacroReference.class;
	}


}

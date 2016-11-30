/**
 * 
 */
package org.goko.tools.macro.io.exporter;

import org.goko.core.common.exception.GkException;
import org.goko.core.workspace.service.IExporter;
import org.goko.core.workspace.service.IMapperService;
import org.goko.tools.macro.bean.GCodeMacroReference;
import org.goko.tools.macro.io.bean.XmlGCodeMacroReference;

/**
 * @author Psyko
 * @date 23 nov. 2016
 */
public class XmlGCodeMacroReferenceExporter implements IExporter<GCodeMacroReference, XmlGCodeMacroReference> {

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IExporter#export(java.lang.Object, org.goko.core.workspace.service.IMapperService)
	 */
	@Override
	public XmlGCodeMacroReference export(GCodeMacroReference input, IMapperService mapperService) throws GkException {
		XmlGCodeMacroReference reference = new XmlGCodeMacroReference();
		reference.setCode(input.getCode());
		return reference;
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IExporter#getOutputClass()
	 */
	@Override
	public Class<XmlGCodeMacroReference> getOutputClass() {
		return XmlGCodeMacroReference.class;
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IExporter#getInputClass()
	 */
	@Override
	public Class<GCodeMacroReference> getInputClass() {
		return GCodeMacroReference.class;
	}

}

/**
 * 
 */
package org.goko.tools.macro.io.exporter;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.io.xml.bean.XmlColor;
import org.goko.core.workspace.service.IExporter;
import org.goko.core.workspace.service.IMapperService;
import org.goko.gcode.rs274ngcv3.xml.bean.source.XmlGCodeProviderSource;
import org.goko.tools.macro.bean.GCodeMacro;
import org.goko.tools.macro.io.bean.XmlGCodeMacro;

/**
 * @author Psyko
 * @date 16 oct. 2016
 */
public class XmlGCodeMacroExporter implements IExporter<GCodeMacro, XmlGCodeMacro>{

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IExporter#export(java.lang.Object, org.goko.core.workspace.service.IMapperService)
	 */
	@Override
	public XmlGCodeMacro export(GCodeMacro input, IMapperService mapperService) throws GkException {
		XmlGCodeMacro xmlMacro = new XmlGCodeMacro();
		xmlMacro.setCode(input.getCode());
		xmlMacro.setRequestConfirmation(input.isRequestConfirmBeforeExecution());
		xmlMacro.setShowInPanel(input.isShowInMacroPanel());
		if(input.getButtonColor() != null){
			xmlMacro.setButtonColor( new XmlColor(input.getButtonColor()) );
		}
		if(input.getTextColor() != null){
			xmlMacro.setTextColor( new XmlColor(input.getTextColor()) );
		}
		xmlMacro.setGcodeContent(mapperService.export(input.getContent(), XmlGCodeProviderSource.class));
		return xmlMacro;
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IExporter#getOutputClass()
	 */
	@Override
	public Class<XmlGCodeMacro> getOutputClass() {
		return XmlGCodeMacro.class;
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IExporter#getInputClass()
	 */
	@Override
	public Class<GCodeMacro> getInputClass() {
		return GCodeMacro.class;
	}

}

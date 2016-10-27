/**
 * 
 */
package org.goko.tools.macro.io.loader;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.element.IGCodeProviderSource;
import org.goko.core.workspace.service.ILoader;
import org.goko.core.workspace.service.IMapperService;
import org.goko.tools.macro.bean.GCodeMacro;
import org.goko.tools.macro.io.XmlGCodeMacro;

/**
 * @author Psyko
 * @date 16 oct. 2016
 */
public class XmlGCodeMacroLoader implements ILoader<XmlGCodeMacro, GCodeMacro>{

	/**
	 * 
	 */
	public XmlGCodeMacroLoader() {
		System.out.println("");// TODO Auto-generated constructor stub
	}
	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.ILoader#load(java.lang.Object, org.goko.core.workspace.service.IMapperService)
	 */
	@Override
	public GCodeMacro load(XmlGCodeMacro input, IMapperService mapperService) throws GkException {
		GCodeMacro macro = new GCodeMacro();
		macro.setCode(input.getCode());
		macro.setRequestConfirmBeforeExecution(input.isRequestConfirmation());
		macro.setShowInMacroPanel(input.isShowInPanel());
		if(input.getButtonColor() != null){
			macro.setButtonColor( input.getButtonColor().getColor());
		}
		if(input.getTextColor() != null){
			macro.setTextColor( input.getTextColor().getColor());
		}
		IGCodeProviderSource source = mapperService.load(input.getGcodeContent(), IGCodeProviderSource.class);
		macro.setContent(source);
		return macro;
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.ILoader#getOutputClass()
	 */
	@Override
	public Class<GCodeMacro> getOutputClass() {
		return GCodeMacro.class;
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.ILoader#getInputClass()
	 */
	@Override
	public Class<XmlGCodeMacro> getInputClass() {
		return XmlGCodeMacro.class;
	}

	

}

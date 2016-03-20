/**
 *
 */
package org.goko.gcode.rs274ngcv3.ui.workspace.io.exporter.modifier;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.io.xml.quantity.XmlLength;
import org.goko.core.gcode.rs274ngcv3.modifier.translate.TranslateModifier;
import org.goko.core.workspace.service.IMapperService;
import org.goko.gcode.rs274ngcv3.ui.workspace.io.bean.modifier.XmlTranslateModifier;

/**
 * @author PsyKo
 * @date 13 déc. 2015
 */
public class TranslateModifierExporter extends AbstractModifierExporter<TranslateModifier, XmlTranslateModifier>{

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.ILoader#getOutputClass()
	 */
	@Override
	public Class<XmlTranslateModifier> getOutputClass() {
		return XmlTranslateModifier.class;
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.ILoader#getInputClass()
	 */
	@Override
	public Class<TranslateModifier> getInputClass() {
		return TranslateModifier.class;
	}

	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.ui.workspace.io.exporter.modifier.AbstractModifierExporter#exportModifierData(org.goko.gcode.rs274ngcv3.ui.workspace.io.XmlGCodeModifier, org.goko.core.gcode.rs274ngcv3.element.IModifier, org.goko.core.workspace.service.IMapperService)
	 */
	@Override
	protected void exportModifierData(XmlTranslateModifier output, TranslateModifier input, IMapperService mapperService) throws GkException {
		output.setX( new XmlLength(input.getTranslationX()) );
		output.setY( new XmlLength(input.getTranslationY()) );
		output.setZ( new XmlLength(input.getTranslationZ()) );		
	}

	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.ui.workspace.io.exporter.modifier.AbstractModifierExporter#createOutputInstance()
	 */
	@Override
	protected XmlTranslateModifier createOutputInstance() {
		return new XmlTranslateModifier();
	}
	
}

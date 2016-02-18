/**
 * 
 */
package org.goko.gcode.rs274ngcv3.ui.workspace.io.loader;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.io.xml.quantity.XmlLength;
import org.goko.core.gcode.rs274ngcv3.modifier.translate.TranslateModifier;
import org.goko.core.workspace.service.IMapperService;
import org.goko.gcode.rs274ngcv3.ui.workspace.io.bean.XmlTranslateModifier;

/**
 * @author PsyKo
 * @date 16 févr. 2016
 */
public class TranslateModifierLoader extends AbstractModifierLoader<XmlTranslateModifier, TranslateModifier> {

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.ILoader#getOutputClass()
	 */
	@Override
	public Class<TranslateModifier> getOutputClass() {
		return TranslateModifier.class;
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.ILoader#getInputClass()
	 */
	@Override
	public Class<XmlTranslateModifier> getInputClass() {
		return XmlTranslateModifier.class;
	}

	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.ui.workspace.io.loader.AbstractModifierLoader#loadModifierData(org.goko.core.gcode.rs274ngcv3.element.IModifier, org.goko.gcode.rs274ngcv3.ui.workspace.io.XmlGCodeModifier, org.goko.core.workspace.service.IMapperService)
	 */
	@Override
	protected void loadModifierData(TranslateModifier output, XmlTranslateModifier input, IMapperService mapperService) throws GkException {
		output.setTranslationX(XmlLength.valueOf(input.getX()));
		output.setTranslationY(XmlLength.valueOf(input.getY()));
		output.setTranslationZ(XmlLength.valueOf(input.getZ()));
	}

	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.ui.workspace.io.loader.AbstractModifierLoader#createOutputInstance()
	 */
	@Override
	protected TranslateModifier createOutputInstance() {
		return new TranslateModifier();
	}

}

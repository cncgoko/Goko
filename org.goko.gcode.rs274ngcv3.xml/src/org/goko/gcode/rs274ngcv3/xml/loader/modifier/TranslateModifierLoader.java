/**
 * 
 */
package org.goko.gcode.rs274ngcv3.xml.loader.modifier;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.io.xml.quantity.XmlAngle;
import org.goko.core.common.io.xml.quantity.XmlLength;
import org.goko.core.common.measure.quantity.Angle;
import org.goko.core.gcode.rs274ngcv3.modifier.translate.TranslateModifier;
import org.goko.core.workspace.service.IMapperService;
import org.goko.gcode.rs274ngcv3.xml.bean.modifier.XmlTranslateModifier;

/**
 * @author PsyKo
 * @date 16 f�vr. 2016
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
	 * @see org.goko.gcode.rs274ngcv3.ui.workspace.io.loader.modifier.AbstractModifierLoader#loadModifierData(org.goko.core.gcode.rs274ngcv3.element.IModifier, org.goko.gcode.rs274ngcv3.ui.workspace.io.XmlGCodeModifier, org.goko.core.workspace.service.IMapperService)
	 */
	@Override
	protected void loadModifierData(TranslateModifier output, XmlTranslateModifier input, IMapperService mapperService) throws GkException {
		output.setTranslationX(XmlLength.valueOf(input.getX()));
		output.setTranslationY(XmlLength.valueOf(input.getY()));
		output.setTranslationZ(XmlLength.valueOf(input.getZ()));
		// Null check for backward compatibility
		if(input.getA() != null){
			output.setTranslationA(XmlAngle.valueOf(input.getA()));
		}else{
			output.setTranslationA(Angle.ZERO);
		}
		// Null check for backward compatibility
		if(input.getB() != null){
			output.setTranslationB(XmlAngle.valueOf(input.getB()));
		}else{
			output.setTranslationB(Angle.ZERO);
		}
		// Null check for backward compatibility
		if(input.getC() != null){
			output.setTranslationC(XmlAngle.valueOf(input.getC()));
		}else{
			output.setTranslationC(Angle.ZERO);
		}
	}

	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.ui.workspace.io.loader.modifier.AbstractModifierLoader#createOutputInstance()
	 */
	@Override
	protected TranslateModifier createOutputInstance() {
		return new TranslateModifier();
	}

}

/**
 * 
 */
package org.goko.gcode.rs274ngcv3.xml.loader.modifier;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.io.xml.quantity.XmlAngle;
import org.goko.core.controller.bean.EnumControllerAxis;
import org.goko.core.gcode.rs274ngcv3.modifier.rotate.RotateModifier;
import org.goko.core.workspace.service.IMapperService;
import org.goko.gcode.rs274ngcv3.xml.bean.modifier.XmlRotateModifier;

/**
 * @author PsyKo
 * @date 16 f�vr. 2016
 */
public class RotateModifierLoader extends AbstractModifierLoader<XmlRotateModifier, RotateModifier> {

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.ILoader#getOutputClass()
	 */
	@Override
	public Class<RotateModifier> getOutputClass() {
		return RotateModifier.class;
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.ILoader#getInputClass()
	 */
	@Override
	public Class<XmlRotateModifier> getInputClass() {
		return XmlRotateModifier.class;
	}

	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.ui.workspace.io.loader.modifier.AbstractModifierLoader#loadModifierData(org.goko.core.gcode.rs274ngcv3.element.IModifier, org.goko.gcode.rs274ngcv3.ui.workspace.io.XmlGCodeModifier, org.goko.core.workspace.service.IMapperService)
	 */
	@Override
	protected void loadModifierData(RotateModifier output, XmlRotateModifier input, IMapperService mapperService) throws GkException {
		output.setRotationAngle(XmlAngle.valueOf(input.getRotationAngle()));
		output.setRotationAxis(EnumControllerAxis.getEnum(input.getAxis()));		
	}

	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.ui.workspace.io.loader.modifier.AbstractModifierLoader#createOutputInstance()
	 */
	@Override
	protected RotateModifier createOutputInstance() {
		return new RotateModifier();
	}

}

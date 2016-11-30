/**
 *
 */
package org.goko.gcode.rs274ngcv3.xml.exporter.modifier;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.io.xml.quantity.XmlAngle;
import org.goko.core.gcode.rs274ngcv3.modifier.rotate.RotateModifier;
import org.goko.core.workspace.service.IMapperService;
import org.goko.gcode.rs274ngcv3.xml.bean.modifier.XmlRotateModifier;

/**
 * @author PsyKo
 * @date 13 d�c. 2015
 */
public class RotateModifierExporter extends AbstractModifierExporter<RotateModifier, XmlRotateModifier>{

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.ILoader#getOutputClass()
	 */
	@Override
	public Class<XmlRotateModifier> getOutputClass() {
		return XmlRotateModifier.class;
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.ILoader#getInputClass()
	 */
	@Override
	public Class<RotateModifier> getInputClass() {
		return RotateModifier.class;
	}

	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.ui.workspace.io.exporter.modifier.AbstractModifierExporter#exportModifierData(org.goko.gcode.rs274ngcv3.ui.workspace.io.XmlGCodeModifier, org.goko.core.gcode.rs274ngcv3.element.IModifier, org.goko.core.workspace.service.IMapperService)
	 */
	@Override
	protected void exportModifierData(XmlRotateModifier output, RotateModifier input, IMapperService mapperService) throws GkException {
		output.setAxis( input.getRotationAxis().getCode() );
		output.setRotationAngle( new XmlAngle(input.getRotationAngle()) );				
	}

	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.ui.workspace.io.exporter.modifier.AbstractModifierExporter#createOutputInstance()
	 */
	@Override
	protected XmlRotateModifier createOutputInstance() {
		return new XmlRotateModifier();
	}
	
}

/**
 * 
 */
package org.goko.gcode.rs274ngcv3.ui.workspace.io.exporter.modifier;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.io.xml.quantity.XmlLength;
import org.goko.core.gcode.rs274ngcv3.modifier.wrap.WrapModifier;
import org.goko.core.workspace.service.IMapperService;
import org.goko.gcode.rs274ngcv3.ui.workspace.io.bean.modifier.XmlWrapModifier;

/**
 * Exporter for the wrap modifier
 * @author Psyko
 * @date 30 avr. 2016
 */
public class WrapModifierExporter extends AbstractModifierExporter<WrapModifier, XmlWrapModifier> {

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IExporter#getOutputClass()
	 */
	@Override
	public Class<XmlWrapModifier> getOutputClass() {
		return XmlWrapModifier.class;
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IExporter#getInputClass()
	 */
	@Override
	public Class<WrapModifier> getInputClass() {
		return WrapModifier.class;
	}

	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.ui.workspace.io.exporter.modifier.AbstractModifierExporter#exportModifierData(org.goko.gcode.rs274ngcv3.ui.workspace.io.bean.XmlGCodeModifier, org.goko.core.gcode.rs274ngcv3.element.IModifier, org.goko.core.workspace.service.IMapperService)
	 */
	@Override
	protected void exportModifierData(XmlWrapModifier output, WrapModifier input, IMapperService mapperService) throws GkException {
		output.setAxis( input.getAxis().getCode() );
		output.setRadius( new XmlLength(input.getRadius()) );
	}

	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.ui.workspace.io.exporter.modifier.AbstractModifierExporter#createOutputInstance()
	 */
	@Override
	protected XmlWrapModifier createOutputInstance() {
		return new XmlWrapModifier();
	}

}

/**
 * 
 */
package org.goko.gcode.rs274ngcv3.ui.workspace.io.loader.modifier;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.rs274ngcv3.modifier.wrap.WrapModifier;
import org.goko.core.gcode.rs274ngcv3.modifier.wrap.WrapModifierAxis;
import org.goko.core.workspace.service.IMapperService;
import org.goko.gcode.rs274ngcv3.ui.workspace.io.bean.modifier.XmlWrapModifier;

/**
 * Exporter for a wrap modifier
 * @author Psyko
 * @date 30 avr. 2016
 */
public class WrapModifierLoader extends AbstractModifierLoader<XmlWrapModifier, WrapModifier> {

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.ILoader#getOutputClass()
	 */
	@Override
	public Class<WrapModifier> getOutputClass() {
		return WrapModifier.class;
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.ILoader#getInputClass()
	 */
	@Override
	public Class<XmlWrapModifier> getInputClass() {
		return XmlWrapModifier.class;
	}

	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.ui.workspace.io.loader.modifier.AbstractModifierLoader#loadModifierData(org.goko.core.gcode.rs274ngcv3.element.IModifier, org.goko.gcode.rs274ngcv3.ui.workspace.io.bean.XmlGCodeModifier, org.goko.core.workspace.service.IMapperService)
	 */
	@Override
	protected void loadModifierData(WrapModifier output, XmlWrapModifier input, IMapperService mapperService) throws GkException {
		output.setRadius( input.getRadius().getQuantity() );
		output.setAxis(WrapModifierAxis.getEnum(input.getAxis()));
	}

	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.ui.workspace.io.loader.modifier.AbstractModifierLoader#createOutputInstance()
	 */
	@Override
	protected WrapModifier createOutputInstance() {
		return new WrapModifier();
	}

}

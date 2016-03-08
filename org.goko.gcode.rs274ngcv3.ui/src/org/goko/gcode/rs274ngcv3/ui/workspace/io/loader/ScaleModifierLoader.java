/**
 * 
 */
package org.goko.gcode.rs274ngcv3.ui.workspace.io.loader;

import java.math.BigDecimal;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.rs274ngcv3.modifier.scale.ScaleModifier;
import org.goko.core.workspace.service.IMapperService;
import org.goko.gcode.rs274ngcv3.ui.workspace.io.bean.XmlScaleModifier;

/**
 * @author PsyKo
 * @date 16 févr. 2016
 */
public class ScaleModifierLoader extends AbstractModifierLoader<XmlScaleModifier, ScaleModifier> {

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.ILoader#getOutputClass()
	 */
	@Override
	public Class<ScaleModifier> getOutputClass() {
		return ScaleModifier.class;
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.ILoader#getInputClass()
	 */
	@Override
	public Class<XmlScaleModifier> getInputClass() {
		return XmlScaleModifier.class;
	}

	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.ui.workspace.io.loader.AbstractModifierLoader#loadModifierData(org.goko.core.gcode.rs274ngcv3.element.IModifier, org.goko.gcode.rs274ngcv3.ui.workspace.io.XmlGCodeModifier, org.goko.core.workspace.service.IMapperService)
	 */
	@Override
	protected void loadModifierData(ScaleModifier output, XmlScaleModifier input, IMapperService mapperService) throws GkException {
		output.setScaleFactor(new BigDecimal(input.getScaleFActor()));		
	}

	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.ui.workspace.io.loader.AbstractModifierLoader#createOutputInstance()
	 */
	@Override
	protected ScaleModifier createOutputInstance() {
		return new ScaleModifier();
	}

}

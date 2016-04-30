/**
 *
 */
package org.goko.gcode.rs274ngcv3.ui.workspace.io.exporter.modifier;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.rs274ngcv3.modifier.scale.ScaleModifier;
import org.goko.core.workspace.service.IMapperService;
import org.goko.gcode.rs274ngcv3.ui.workspace.io.bean.modifier.XmlScaleModifier;

/**
 * @author PsyKo
 * @date 13 déc. 2015
 */
public class ScaleModifierExporter extends AbstractModifierExporter<ScaleModifier, XmlScaleModifier>{

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.ILoader#getOutputClass()
	 */
	@Override
	public Class<XmlScaleModifier> getOutputClass() {
		return XmlScaleModifier.class;
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.ILoader#getInputClass()
	 */
	@Override
	public Class<ScaleModifier> getInputClass() {
		return ScaleModifier.class;
	}

	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.ui.workspace.io.exporter.modifier.AbstractModifierExporter#exportModifierData(org.goko.gcode.rs274ngcv3.ui.workspace.io.XmlGCodeModifier, org.goko.core.gcode.rs274ngcv3.element.IModifier, org.goko.core.workspace.service.IMapperService)
	 */
	@Override
	protected void exportModifierData(XmlScaleModifier output, ScaleModifier input, IMapperService mapperService) throws GkException {
		if(input.getScaleFactor() != null){
			output.setScaleFactor(input.getScaleFactor().toPlainString());				
		}else{
			output.setScaleFactor("1");
		}
	}

	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.ui.workspace.io.exporter.modifier.AbstractModifierExporter#createOutputInstance()
	 */
	@Override
	protected XmlScaleModifier createOutputInstance() {
		return new XmlScaleModifier();
	}
	
}

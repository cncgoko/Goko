/**
 *
 */
package org.goko.gcode.rs274ngcv3.ui.workspace.io.exporter.modifier;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.io.xml.quantity.XmlLength;
import org.goko.core.gcode.rs274ngcv3.modifier.segmentize.SegmentizeModifier;
import org.goko.core.workspace.service.IMapperService;
import org.goko.gcode.rs274ngcv3.ui.workspace.io.bean.modifier.XmlSegmentizeModifier;

/**
 * @author PsyKo
 * @date 13 déc. 2015
 */
public class SegmentizeModifierExporter extends AbstractModifierExporter<SegmentizeModifier, XmlSegmentizeModifier>{

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.ILoader#getOutputClass()
	 */
	@Override
	public Class<XmlSegmentizeModifier> getOutputClass() {
		return XmlSegmentizeModifier.class;
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.ILoader#getInputClass()
	 */
	@Override
	public Class<SegmentizeModifier> getInputClass() {
		return SegmentizeModifier.class;
	}

	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.ui.workspace.io.exporter.modifier.AbstractModifierExporter#exportModifierData(org.goko.gcode.rs274ngcv3.ui.workspace.io.XmlGCodeModifier, org.goko.core.gcode.rs274ngcv3.element.IModifier, org.goko.core.workspace.service.IMapperService)
	 */
	@Override
	protected void exportModifierData(XmlSegmentizeModifier output, SegmentizeModifier input, IMapperService mapperService) throws GkException {
		output.setChordalTolerance( new XmlLength(input.getChordalTolerance()) );		
	}

	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.ui.workspace.io.exporter.modifier.AbstractModifierExporter#createOutputInstance()
	 */
	@Override
	protected XmlSegmentizeModifier createOutputInstance() {
		return new XmlSegmentizeModifier();
	}
	
}

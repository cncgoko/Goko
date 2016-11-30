/**
 * 
 */
package org.goko.gcode.rs274ngcv3.xml.loader.modifier;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.rs274ngcv3.modifier.segmentize.SegmentizeModifier;
import org.goko.core.workspace.service.IMapperService;
import org.goko.gcode.rs274ngcv3.xml.bean.modifier.XmlSegmentizeModifier;

/**
 * @author PsyKo
 * @date 16 f�vr. 2016
 */
public class SegmentizeModifierLoader extends AbstractModifierLoader<XmlSegmentizeModifier, SegmentizeModifier> {

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.ILoader#getOutputClass()
	 */
	@Override
	public Class<SegmentizeModifier> getOutputClass() {
		return SegmentizeModifier.class;
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.ILoader#getInputClass()
	 */
	@Override
	public Class<XmlSegmentizeModifier> getInputClass() {
		return XmlSegmentizeModifier.class;
	}

	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.ui.workspace.io.loader.modifier.AbstractModifierLoader#loadModifierData(org.goko.core.gcode.rs274ngcv3.element.IModifier, org.goko.gcode.rs274ngcv3.ui.workspace.io.XmlGCodeModifier, org.goko.core.workspace.service.IMapperService)
	 */
	@Override
	protected void loadModifierData(SegmentizeModifier output, XmlSegmentizeModifier input, IMapperService mapperService) throws GkException {
		output.setChordalTolerance(input.getChordalTolerance().getQuantity());
	}

	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.ui.workspace.io.loader.modifier.AbstractModifierLoader#createOutputInstance()
	 */
	@Override
	protected SegmentizeModifier createOutputInstance() {
		return new SegmentizeModifier();
	}

}

/**
 * 
 */
package org.goko.tools.autoleveler.io.xml;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.io.xml.quantity.XmlLength;
import org.goko.core.workspace.service.IMapperService;
import org.goko.gcode.rs274ngcv3.ui.workspace.io.exporter.modifier.AbstractModifierExporter;
import org.goko.tools.autoleveler.modifier.GridAutoLevelerModifier;

/**
 * @author PsyKo
 * @date 16 févr. 2016
 */
public class GridAutoLevelerModifierExporter extends AbstractModifierExporter<GridAutoLevelerModifier, XmlGridAutoLevelerModifier> {

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.ILoader#getInputClass()
	 */
	@Override
	public Class<GridAutoLevelerModifier> getInputClass() {
		return GridAutoLevelerModifier.class;
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.ILoader#getOutputClass()
	 */
	@Override
	public Class<XmlGridAutoLevelerModifier> getOutputClass() {
		return XmlGridAutoLevelerModifier.class;
	}

	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.ui.workspace.io.exporter.modifier.AbstractModifierExporter#exportModifierData(org.goko.gcode.rs274ngcv3.ui.workspace.io.XmlGCodeModifier, org.goko.core.gcode.rs274ngcv3.element.IModifier, org.goko.core.workspace.service.IMapperService)
	 */
	@Override
	protected void exportModifierData(XmlGridAutoLevelerModifier output, GridAutoLevelerModifier input, IMapperService mapperService) throws GkException {
		output.setTheoricHeight(XmlLength.valueOf(input.getTheoricHeight()));
		output.setHeightMap(mapperService.export(input.getHeightMap(), AbstractXmlHeightMap.class));
	}
	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.ui.workspace.io.loader.modifier.AbstractModifierLoader#createOutputInstance()
	 */
	@Override
	protected XmlGridAutoLevelerModifier createOutputInstance() {
		return new XmlGridAutoLevelerModifier();
	}

}

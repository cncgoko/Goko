/**
 * 
 */
package org.goko.tools.autoleveler.io.xml;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.io.xml.quantity.XmlLength;
import org.goko.core.workspace.service.IMapperService;
import org.goko.gcode.rs274ngcv3.ui.workspace.io.loader.AbstractModifierLoader;
import org.goko.tools.autoleveler.bean.grid.GridHeightMap;
import org.goko.tools.autoleveler.modifier.GridAutoLevelerModifier;

/**
 * @author PsyKo
 * @date 16 févr. 2016
 */
public class GridAutoLevelerModifierLoader extends AbstractModifierLoader<XmlGridAutoLevelerModifier, GridAutoLevelerModifier> {

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.ILoader#getOutputClass()
	 */
	@Override
	public Class<GridAutoLevelerModifier> getOutputClass() {
		return GridAutoLevelerModifier.class;
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.ILoader#getInputClass()
	 */
	@Override
	public Class<XmlGridAutoLevelerModifier> getInputClass() {
		return XmlGridAutoLevelerModifier.class;
	}

	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.ui.workspace.io.loader.AbstractModifierLoader#loadModifierData(org.goko.core.gcode.rs274ngcv3.element.IModifier, org.goko.gcode.rs274ngcv3.ui.workspace.io.XmlGCodeModifier, org.goko.core.workspace.service.IMapperService)
	 */
	@Override
	protected void loadModifierData(GridAutoLevelerModifier output, XmlGridAutoLevelerModifier input, IMapperService mapperService) throws GkException {
		output.setTheoricHeight(XmlLength.valueOf(input.getTheoricHeight()));
		output.setHeightMap(mapperService.load(input.getHeightMap(), GridHeightMap.class));
	}

	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.ui.workspace.io.loader.AbstractModifierLoader#createOutputInstance()
	 */
	@Override
	protected GridAutoLevelerModifier createOutputInstance() {
		return new GridAutoLevelerModifier();
	}

}

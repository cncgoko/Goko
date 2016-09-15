/**
 *
 */
package org.goko.gcode.rs274ngcv3.ui.workspace.io.exporter.modifier;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.io.xml.quantity.XmlAngle;
import org.goko.core.common.io.xml.quantity.XmlLength;
import org.goko.core.gcode.rs274ngcv3.modifier.array.ArrayModifier;
import org.goko.core.workspace.service.IMapperService;
import org.goko.gcode.rs274ngcv3.ui.workspace.io.bean.modifier.XmlArrayModifier;

/**
 * Array modifier exporter
 * @author Psyko
 * @date 15 sept. 2016
 */
public class ArrayModifierExporter extends AbstractModifierExporter<ArrayModifier, XmlArrayModifier>{

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.ILoader#getOutputClass()
	 */
	@Override
	public Class<XmlArrayModifier> getOutputClass() {
		return XmlArrayModifier.class;
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.ILoader#getInputClass()
	 */
	@Override
	public Class<ArrayModifier> getInputClass() {
		return ArrayModifier.class;
	}

	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.ui.workspace.io.exporter.modifier.AbstractModifierExporter#exportModifierData(org.goko.gcode.rs274ngcv3.ui.workspace.io.XmlGCodeModifier, org.goko.core.gcode.rs274ngcv3.element.IModifier, org.goko.core.workspace.service.IMapperService)
	 */
	@Override
	protected void exportModifierData(XmlArrayModifier output, ArrayModifier input, IMapperService mapperService) throws GkException {
		output.setCopyCount(input.getCount());
		output.setX( new XmlLength(input.getOffset().getX()) );
		output.setY( new XmlLength(input.getOffset().getY()) );
		output.setZ( new XmlLength(input.getOffset().getZ()) );
		output.setA( new XmlAngle(input.getOffset().getA()) );
		output.setB( new XmlAngle(input.getOffset().getB()) );
		output.setC( new XmlAngle(input.getOffset().getC()) );
				
	}

	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.ui.workspace.io.exporter.modifier.AbstractModifierExporter#createOutputInstance()
	 */
	@Override
	protected XmlArrayModifier createOutputInstance() {
		return new XmlArrayModifier();
	}
	
}

/**
 * 
 */
package org.goko.gcode.rs274ngcv3.ui.workspace.io.loader.modifier;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.io.xml.quantity.XmlAngle;
import org.goko.core.common.io.xml.quantity.XmlLength;
import org.goko.core.gcode.rs274ngcv3.modifier.array.ArrayModifier;
import org.goko.core.math.Tuple6b;
import org.goko.core.workspace.service.IMapperService;
import org.goko.gcode.rs274ngcv3.ui.workspace.io.bean.modifier.XmlArrayModifier;

/**
 * Array modifier loader 
 * @author Psyko
 * @date 15 sept. 2016
 */
public class ArrayModifierLoader extends AbstractModifierLoader<XmlArrayModifier, ArrayModifier> {

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.ILoader#getOutputClass()
	 */
	@Override
	public Class<ArrayModifier> getOutputClass() {
		return ArrayModifier.class;
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.ILoader#getInputClass()
	 */
	@Override
	public Class<XmlArrayModifier> getInputClass() {
		return XmlArrayModifier.class;
	}

	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.ui.workspace.io.loader.modifier.AbstractModifierLoader#loadModifierData(org.goko.core.gcode.rs274ngcv3.element.IModifier, org.goko.gcode.rs274ngcv3.ui.workspace.io.XmlGCodeModifier, org.goko.core.workspace.service.IMapperService)
	 */
	@Override
	protected void loadModifierData(ArrayModifier output, XmlArrayModifier input, IMapperService mapperService) throws GkException {
		output.setCount(input.getCopyCount());
		output.setOffset(new Tuple6b(XmlLength.valueOf(input.getX()),
									 XmlLength.valueOf(input.getY()),
									 XmlLength.valueOf(input.getZ()),
									 XmlAngle.valueOf(input.getA()),
									 XmlAngle.valueOf(input.getB()),
									 XmlAngle.valueOf(input.getC())));
	}

	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.ui.workspace.io.loader.modifier.AbstractModifierLoader#createOutputInstance()
	 */
	@Override
	protected ArrayModifier createOutputInstance() {
		return new ArrayModifier();
	}

}

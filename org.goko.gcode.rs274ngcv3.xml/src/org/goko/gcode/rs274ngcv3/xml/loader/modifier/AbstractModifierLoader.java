/**
 *
 */
package org.goko.gcode.rs274ngcv3.xml.loader.modifier;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.rs274ngcv3.element.GCodeProvider;
import org.goko.core.gcode.rs274ngcv3.element.IModifier;
import org.goko.core.workspace.service.ILoader;
import org.goko.core.workspace.service.IMapperService;
import org.goko.gcode.rs274ngcv3.xml.bean.XmlGCodeModifier;

/**
 * @author PsyKo
 * @date 13 d�c. 2015
 */
public abstract class AbstractModifierLoader<X extends XmlGCodeModifier, M extends IModifier<GCodeProvider>> implements ILoader<X, M>{
	
	protected void loadAbstract(M output, X input, IMapperService mapperService) throws GkException {
		output.setEnabled(input.isEnabled());
		output.setOrder(input.getOrder());		
	}
	

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.ILoader#load(java.lang.Object, org.goko.core.workspace.service.IMapperService)
	 */
	@Override
	public final M load(X input, IMapperService mapperService) throws GkException {
		M output = createOutputInstance();
		loadAbstract(output, input, mapperService);
		loadModifierData(output, input, mapperService);
		return output;
	}

	protected abstract void loadModifierData(M output, X input, IMapperService mapperService) throws GkException;
	/**
	 * @return an instance of the output object
	 */
	protected abstract M createOutputInstance();
	
}

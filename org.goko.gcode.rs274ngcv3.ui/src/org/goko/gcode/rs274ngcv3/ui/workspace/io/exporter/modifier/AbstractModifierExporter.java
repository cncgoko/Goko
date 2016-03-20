/**
 *
 */
package org.goko.gcode.rs274ngcv3.ui.workspace.io.exporter.modifier;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.rs274ngcv3.element.GCodeProvider;
import org.goko.core.gcode.rs274ngcv3.element.IModifier;
import org.goko.core.workspace.service.IExporter;
import org.goko.core.workspace.service.IMapperService;
import org.goko.gcode.rs274ngcv3.ui.workspace.io.bean.XmlGCodeModifier;

/**
 * @author PsyKo
 * @date 13 déc. 2015
 */
public abstract class AbstractModifierExporter<M extends IModifier<GCodeProvider>, X extends XmlGCodeModifier> implements IExporter<M, X>{
	
	protected void exportAbstract(X output, M input, IMapperService mapperService) throws GkException {
		output.setEnabled(input.isEnabled());
		output.setOrder(input.getOrder());		
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IExporter#export(java.lang.Object, org.goko.core.workspace.service.IMapperService)
	 */
	@Override
	public final X export(M input, IMapperService mapperService) throws GkException {
		X output = createOutputInstance();
		exportAbstract(output, input, mapperService);
		exportModifierData(output, input, mapperService);
		return output;
	}

	protected abstract void exportModifierData(X output, M input, IMapperService mapperService) throws GkException;
	/**
	 * @return an instance of the output object
	 */
	protected abstract X createOutputInstance();
	
}

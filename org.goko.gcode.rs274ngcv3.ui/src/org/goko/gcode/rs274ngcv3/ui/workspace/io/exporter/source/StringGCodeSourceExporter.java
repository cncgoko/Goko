/**
 *
 */
package org.goko.gcode.rs274ngcv3.ui.workspace.io.exporter.source;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.rs274ngcv3.element.source.StringGCodeSource;
import org.goko.core.workspace.service.IExporter;
import org.goko.core.workspace.service.IMapperService;
import org.goko.gcode.rs274ngcv3.ui.workspace.io.bean.source.XmlStringGCodeSource;

/**
 * @author PsyKo
 * @date 13 d�c. 2015
 */
public class StringGCodeSourceExporter implements IExporter<StringGCodeSource, XmlStringGCodeSource>{
		
	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IExporter#export(java.lang.Object, org.goko.core.workspace.service.IMapperService)
	 */
	@Override
	public XmlStringGCodeSource export(StringGCodeSource input, IMapperService mapperService) throws GkException {		
		return new XmlStringGCodeSource(input.getContent());
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IExporter#getOutputClass()
	 */
	@Override
	public Class<XmlStringGCodeSource> getOutputClass() {
		return XmlStringGCodeSource.class;
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IExporter#getInputClass()
	 */
	@Override
	public Class<StringGCodeSource> getInputClass() {
		return StringGCodeSource.class;
	}	
}

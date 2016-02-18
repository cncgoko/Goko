/**
 *
 */
package org.goko.gcode.rs274ngcv3.ui.workspace.io.exporter;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.rs274ngcv3.element.source.FileGCodeSource;
import org.goko.core.workspace.service.IExporter;
import org.goko.core.workspace.service.IMapperService;
import org.goko.gcode.rs274ngcv3.ui.workspace.io.bean.XmlFileGCodeSource;

/**
 * @author PsyKo
 * @date 13 déc. 2015
 */
public class FileGCodeSourceExporter implements IExporter<FileGCodeSource, XmlFileGCodeSource>{

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.ILoader#load(java.lang.Object, org.goko.core.workspace.service.IMapperService)
	 */
	@Override
	public XmlFileGCodeSource export(FileGCodeSource input, IMapperService mapperService) throws GkException {
		XmlFileGCodeSource output = new XmlFileGCodeSource();
		output.setPath(input.getFile().getAbsolutePath());
		return output;
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.ILoader#getOutputClass()
	 */
	@Override
	public Class<XmlFileGCodeSource> getOutputClass() {
		return XmlFileGCodeSource.class;
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.ILoader#getInputClass()
	 */
	@Override
	public Class<FileGCodeSource> getInputClass() {
		return FileGCodeSource.class;
	}
	
}

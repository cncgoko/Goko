/**
 *
 */
package org.goko.gcode.rs274ngcv3.ui.workspace.io.loader;

import java.io.File;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.rs274ngcv3.element.source.FileGCodeSource;
import org.goko.core.workspace.service.ILoader;
import org.goko.core.workspace.service.IMapperService;
import org.goko.gcode.rs274ngcv3.ui.workspace.io.bean.XmlFileGCodeSource;

/**
 * @author PsyKo
 * @date 13 déc. 2015
 */
public class FileGCodeSourceLoader implements ILoader<XmlFileGCodeSource, FileGCodeSource>{

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.ILoader#load(java.lang.Object, org.goko.core.workspace.service.IMapperService)
	 */
	@Override
	public FileGCodeSource load(XmlFileGCodeSource input, IMapperService mapperService) throws GkException {
		String path = input.getPath();
		return new FileGCodeSource(new File(path));
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.ILoader#getOutputClass()
	 */
	@Override
	public Class<FileGCodeSource> getOutputClass() {
		return FileGCodeSource.class;
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.ILoader#getInputClass()
	 */
	@Override
	public Class<XmlFileGCodeSource> getInputClass() {
		return XmlFileGCodeSource.class;
	}
	
}

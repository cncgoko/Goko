/**
 *
 */
package org.goko.gcode.rs274ngcv3.ui.workspace.io.source;

import java.io.File;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.element.IGCodeProviderSource;
import org.goko.core.gcode.rs274ngcv3.element.source.FileGCodeSource;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

/**
 * @author PsyKo
 * @date 13 déc. 2015
 */
@Root(name="file")
public class XmlFileGCodeSource extends XmlGCodeProviderSource{
	/** Paht of the file */
	@Attribute
	private String path;

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.ui.workspace.io.source.XmlGCodeProviderSource#getSource()
	 */
	@Override
	public IGCodeProviderSource getSource() throws GkException {
		return new FileGCodeSource(new File(path));
	}
}

package org.goko.gcode.rs274ngcv3.ui.workspace.io;

import org.goko.gcode.rs274ngcv3.ui.workspace.io.source.XmlFileGCodeSource;
import org.goko.gcode.rs274ngcv3.ui.workspace.io.source.XmlGCodeProviderSource;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementUnion;
import org.simpleframework.xml.Root;

@Root(name="gcodeProvider")
public class XmlGCodeProvider {
	@Attribute
	private String code;

    @ElementUnion({
	      @Element(name="file", type=XmlFileGCodeSource.class)
	})
	private XmlGCodeProviderSource source;

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the source
	 */
	public XmlGCodeProviderSource getSource() {
		return source;
	}

	/**
	 * @param source the source to set
	 */
	public void setSource(XmlGCodeProviderSource source) {
		this.source = source;
	}
}

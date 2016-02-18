package org.goko.gcode.rs274ngcv3.ui.workspace.io;

import java.util.ArrayList;

import org.goko.gcode.rs274ngcv3.ui.workspace.io.bean.XmlGCodeProviderSource;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name="gcodeProvider")
public class XmlGCodeProvider {
	@Attribute
	private String code;

	@Element
	private XmlGCodeProviderSource source;

   @ElementList(name="modifiers")
    private ArrayList<XmlGCodeModifier> modifiers;

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

	/**
	 * @return the modifiers
	 */
	public ArrayList<XmlGCodeModifier> getModifiers() {
		return modifiers;
	}

	/**
	 * @param modifiers the modifiers to set
	 */
	public void setModifiers(ArrayList<XmlGCodeModifier> modifiers) {
		this.modifiers = modifiers;
	}
}

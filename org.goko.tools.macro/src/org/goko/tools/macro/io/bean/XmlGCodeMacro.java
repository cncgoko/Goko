/**
 * 
 */
package org.goko.tools.macro.io.bean;

import org.goko.core.common.io.xml.bean.XmlColor;
import org.goko.gcode.rs274ngcv3.xml.bean.source.XmlGCodeProviderSource;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * @author Psyko
 * @date 16 oct. 2016
 */
@Root(name="gcodeMacro")
public class XmlGCodeMacro {
	/** The name of the macro */
	@Attribute
	private String code;
	/** Flag for requesting confirmation before execution*/
	@Attribute
	private boolean requestConfirmation;
	/** Flag to show the button in the macro panel */
	@Attribute
	private boolean showInPanel;
	/** Color of the text in the button */
	@Attribute(required=false)
	private XmlColor textColor;
	/** Background color of the button */
	@Attribute(required=false)
	private XmlColor buttonColor;	
	/** The actual content of the macro */
	@Element
	private XmlGCodeProviderSource gcodeContent;	

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
	 * @return the gcodeContent
	 */
	public XmlGCodeProviderSource getGcodeContent() {
		return gcodeContent;
	}

	/**
	 * @param gcodeContent the gcodeContent to set
	 */
	public void setGcodeContent(XmlGCodeProviderSource gcodeContent) {
		this.gcodeContent = gcodeContent;
	}

	/**
	 * @return the requestConfirmation
	 */
	public boolean isRequestConfirmation() {
		return requestConfirmation;
	}

	/**
	 * @param requestConfirmation the requestConfirmation to set
	 */
	public void setRequestConfirmation(boolean requestConfirmation) {
		this.requestConfirmation = requestConfirmation;
	}

	/**
	 * @return the showInPanel
	 */
	public boolean isShowInPanel() {
		return showInPanel;
	}

	/**
	 * @param showInPanel the showInPanel to set
	 */
	public void setShowInPanel(boolean showInPanel) {
		this.showInPanel = showInPanel;
	}

	/**
	 * @return the textColor
	 */
	public XmlColor getTextColor() {
		return textColor;
	}

	/**
	 * @param textColor the textColor to set
	 */
	public void setTextColor(XmlColor textColor) {
		this.textColor = textColor;
	}

	/**
	 * @return the buttonColor
	 */
	public XmlColor getButtonColor() {
		return buttonColor;
	}

	/**
	 * @param buttonColor the buttonColor to set
	 */
	public void setButtonColor(XmlColor buttonColor) {
		this.buttonColor = buttonColor;
	}	
}

/**
 * 
 */
package org.goko.tools.macro.bean;

import javax.vecmath.Color3f;

import org.goko.core.common.utils.ICodeBean;
import org.goko.core.common.utils.IIdBean;
import org.goko.core.gcode.element.IGCodeProviderSource;

/**
 * @author Psyko
 * @date 16 oct. 2016
 */
public class GCodeMacro implements IIdBean, ICodeBean{
	/** Id of the macro */
	private Integer id;
	/** Code of the macro */
	private String code;
	/** The GCode source */
	private IGCodeProviderSource source;
	/** Confirmation request */
	private boolean requestConfirmBeforeExecution;
	/** Text color */
	private Color3f textColor;
	/** Button color */
	private Color3f buttonColor;
	/** Displayed in panel */
	private boolean showInMacroPanel;
	
	/** (inheritDoc)
	 * @see org.goko.core.common.utils.IIdBean#getId()
	 */
	@Override
	public Integer getId() {
		return id;
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.utils.IIdBean#setId(java.lang.Integer)
	 */
	@Override
	public void setId(Integer id) {
		this.id = id;	}

	/** (inheritDoc)
	 * @see org.goko.core.common.utils.ICodeBean#getCode()
	 */
	@Override
	public String getCode() {
		return code;
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.utils.ICodeBean#setCode(java.lang.String)
	 */
	@Override
	public void setCode(String code) {
		this.code = code;
	}
	
	public IGCodeProviderSource getContent() {
		return source;
	}

	public void setContent(IGCodeProviderSource content) {
		this.source = content;
	}
	public boolean isRequestConfirmBeforeExecution() {
		return requestConfirmBeforeExecution;
	}

	public void setRequestConfirmBeforeExecution(boolean confirm) {
		requestConfirmBeforeExecution = confirm;
	}

	/**
	 * @return the textColor
	 */
	public Color3f getTextColor() {
		return textColor;
	}

	/**
	 * @param textColor the textColor to set
	 */
	public void setTextColor(Color3f textColor) {
		this.textColor = textColor;
	}

	/**
	 * @return the buttonColor
	 */
	public Color3f getButtonColor() {
		return buttonColor;
	}

	/**
	 * @param buttonColor the buttonColor to set
	 */
	public void setButtonColor(Color3f buttonColor) {
		this.buttonColor = buttonColor;
	}

	/**
	 * @return the showInMacroPanel
	 */
	public boolean isShowInMacroPanel() {
		return showInMacroPanel;
	}

	/**
	 * @param showInMacroPanel the showInMacroPanel to set
	 */
	public void setShowInMacroPanel(boolean showInMacroPanel) {
		this.showInMacroPanel = showInMacroPanel;
	}

}

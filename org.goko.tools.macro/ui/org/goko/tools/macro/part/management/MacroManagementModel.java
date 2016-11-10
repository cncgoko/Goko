/**
 * 
 */
package org.goko.tools.macro.part.management;

import java.util.ArrayList;

import javax.vecmath.Color3f;

import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.goko.common.bindings.AbstractModelObject;
import org.goko.tools.macro.bean.GCodeMacro;

/**
 * @author Psyko
 * @date 17 oct. 2016
 */
public class MacroManagementModel extends AbstractModelObject {	
	static final String AVAILABLE_MACRO = "availableMacro";
	static final String SELECTED_MACRO = "selectedMacro";
	static final String REQUEST_CONFIRM = "requestConfirmation";
	static final String DISPLAY_BUTTON = "displayMacroButton";
	static final String MACRO_NAME = "macroName";
	static final String BUTTON_COLOR = "buttonColor";
	static final String OVERRIDE_BUTTON_COLOR = "overrideButtonColor";
	static final String DIRTY = "dirty";
	static final String EDITION_MODE = "editionMode";
	private IObservableList availableMacro;
	private GCodeMacro selectedMacro;
	private String macroName;
	private boolean requestConfirmation;
	private boolean displayMacroButton;
	private boolean dirty;
	private boolean editionMode;
	private Color3f buttonColor;
	private boolean overrideButtonColor;
	/**
	 * 
	 */
	public MacroManagementModel() {
		availableMacro = new WritableList(new ArrayList<GCodeMacro>(), GCodeMacro.class);
	}
	/**
	 * @return the resolutionList
	 */
	public IObservableList getAvailableMacro() {
		return availableMacro;
	}

	/**
	 * @param resolutionList the resolutionList to set
	 */
	public void setAvailableMacro(IObservableList availableMacro) {
		firePropertyChangeWithDirty(AVAILABLE_MACRO,this.availableMacro, this.availableMacro = availableMacro);	
	}
	/**
	 * @return the selectedMacro
	 */
	public GCodeMacro getSelectedMacro() {
		return selectedMacro;
	}
	/**
	 * @param selectedMacro the selectedMacro to set
	 */
	public void setSelectedMacro(GCodeMacro selectedMacro) {
		firePropertyChangeWithDirty(SELECTED_MACRO,this.selectedMacro, this.selectedMacro = selectedMacro);	
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
		firePropertyChangeWithDirty(REQUEST_CONFIRM,this.requestConfirmation, this.requestConfirmation = requestConfirmation);	
	}
	/**
	 * @return the displayMacroButton
	 */
	public boolean isDisplayMacroButton() {
		return displayMacroButton;
	}
	/**
	 * @param displayMacroButton the displayMacroButton to set
	 */
	public void setDisplayMacroButton(boolean displayMacroButton) {
		firePropertyChangeWithDirty(DISPLAY_BUTTON,this.displayMacroButton, this.displayMacroButton = displayMacroButton);	
	}
	/**
	 * @return the macroName
	 */
	public String getMacroName() {
		return macroName;
	}
	/**
	 * @param macroName the macroName to set
	 */
	public void setMacroName(String macroName) {
		firePropertyChangeWithDirty(MACRO_NAME,this.macroName, this.macroName = macroName);	
	}
	/**
	 * @return the dirty
	 */
	public boolean isDirty() {
		return dirty;
	}
	/**
	 * @param dirty the dirty to set
	 */
	public void setDirty(boolean dirty) {
		firePropertyChange(DIRTY, this.dirty, this.dirty = dirty);		
	}
	
	protected void firePropertyChangeWithDirty(String propertyName, Object oldValue, Object newValue) {
		super.firePropertyChange(propertyName, oldValue, newValue);
		setDirty(true);
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
		firePropertyChangeWithDirty(BUTTON_COLOR, this.buttonColor, this.buttonColor = buttonColor);	
	}
	/**
	 * @return the overrideButtonColor
	 */
	public boolean isOverrideButtonColor() {
		return overrideButtonColor;
	}
	/**
	 * @param overrideButtonColor the overrideButtonColor to set
	 */
	public void setOverrideButtonColor(boolean overrideButtonColor) {
		firePropertyChangeWithDirty(OVERRIDE_BUTTON_COLOR, this.overrideButtonColor, this.overrideButtonColor = overrideButtonColor);
	}
	/**
	 * @return the editionMode
	 */
	public boolean isEditionMode() {
		return editionMode;
	}
	/**
	 * @param editionMode the editionMode to set
	 */
	public void setEditionMode(boolean editionMode) {
		firePropertyChange(EDITION_MODE, this.editionMode, this.editionMode = editionMode);	
	}
}
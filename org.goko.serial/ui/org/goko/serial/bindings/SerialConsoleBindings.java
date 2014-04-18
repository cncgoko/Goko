/*
 *
 *   Goko
 *   Copyright (C) 2013  PsyKo
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.goko.serial.bindings;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.wb.swt.SWTResourceManager;
import org.goko.common.bindings.AbstractModelObject;
import org.goko.common.elements.combo.LabeledValue;

/**
 * Binding class for Serial console
 *
 * @author PsyKo
 *
 */
public class SerialConsoleBindings extends AbstractModelObject {

	private StringBuffer buffer;
	private String console;
	private boolean enabled;
	private Color background;
	private List<String> commandHistory;
	private int commandHistoryIndex;
	private String currentCommand;
	private LabeledValue<String> endLineToken;
	private List<LabeledValue<String>> choiceEndLineToken;
	private boolean lockScroll;

	public SerialConsoleBindings() {
		this.buffer = new StringBuffer();
		setEnabled(true);
		commandHistory = new ArrayList<String>();
		commandHistoryIndex = -1;
		choiceEndLineToken = new ArrayList<LabeledValue<String>>();
		choiceEndLineToken.add( new LabeledValue<String>("\r", "CR"));
		choiceEndLineToken.add( new LabeledValue<String>("\n", "LF"));
		choiceEndLineToken.add( new LabeledValue<String>("\r\n", "CR & LF"));
		endLineToken =  new LabeledValue<String>("\r\n", "CR & LF");
	}

	/**
	 * @return the buffer
	 */
//	public StringBuffer getBuffer() {
//		return buffer;
	//}

	/**
	 * @param buffer the buffer to set
	 */
//	public void setBuffer(StringBuffer buffer) {
//		firePropertyChange("buffer", this.buffer, this.buffer = buffer);
//	}

	/**
	 * @return the console
	 */
	public String getConsole() {
		return console;
	}

	/**
	 * @param console the console to set
	 */
	public void setConsole(String console) {
		firePropertyChange("console", this.console, this.console =console);
	}

	/**
	 * @return the enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}
	public boolean getEnabled() {
		return isEnabled();
	}

	/**
	 * @param enabled the enabled to set
	 */
	public void setEnabled(boolean enabled) {
		firePropertyChange("enabled", this.enabled, this.enabled =enabled);
		if(enabled){
			setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		}else{
			setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		}
	}

	/**
	 * @return the background
	 */
	public Color getBackground() {
		return background;
	}

	/**
	 * @param background the background to set
	 */
	public void setBackground(Color background) {
		firePropertyChange("background", this.background, this.background =background);
	}
	/**
	 * @return the currentCommand
	 */
	public String getCurrentCommand() {
		return currentCommand;
	}

	/**
	 * @param currentCommand the currentCommand to set
	 */
	public void setCurrentCommand(String currentCommand) {
		firePropertyChange("currentCommand", this.currentCommand, this.currentCommand = currentCommand);
	}

	/**
	 * @return the commandHistory
	 */
	public List<String> getCommandHistory() {
		return commandHistory;
	}

	/**
	 * @param commandHistory the commandHistory to set
	 */
	public void setCommandHistory(List<String> commandHistory) {
		this.commandHistory = commandHistory;
	}

	/**
	 * @return the commandHistoryIndex
	 */
	public int getCommandHistoryIndex() {
		return commandHistoryIndex;
	}

	/**
	 * @param commandHistoryIndex the commandHistoryIndex to set
	 */
	public void setCommandHistoryIndex(int commandHistoryIndex) {
		this.commandHistoryIndex = commandHistoryIndex;
	}

	/**
	 * @return the choiceEndLineToken
	 */
	public List<LabeledValue<String>> getChoiceEndLineToken() {
		return choiceEndLineToken;
	}

	/**
	 * @param choiceEndLineToken the choiceEndLineToken to set
	 */
	public void setChoiceEndLineToken(List<LabeledValue<String>> choiceEndLineToken) {
		this.choiceEndLineToken = choiceEndLineToken;
	}

	/**
	 * @return the endLineToken
	 */
	public LabeledValue<String> getEndLineToken() {
		return endLineToken;
	}

	/**
	 * @param endLineToken the endLineToken to set
	 */
	public void setEndLineToken(LabeledValue<String> endLineToken) {
		firePropertyChange("endLineToken", this.endLineToken, this.endLineToken = endLineToken);
	}

	/**
	 * @return the lockScroll
	 */
	public boolean isLockScroll() {
		return lockScroll;
	}
	public boolean getLockScroll() {
		return isLockScroll();
	}

	/**
	 * @param lockScroll the lockScroll to set
	 */
	public void setLockScroll(boolean lockScroll) {
		firePropertyChange("lockScroll", this.lockScroll, this.lockScroll = lockScroll);
	}


}

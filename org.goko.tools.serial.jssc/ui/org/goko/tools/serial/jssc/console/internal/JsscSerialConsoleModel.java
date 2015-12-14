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
package org.goko.tools.serial.jssc.console.internal;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.goko.common.bindings.AbstractModelObject;
import org.goko.common.elements.combo.LabeledValue;

public class JsscSerialConsoleModel extends AbstractModelObject {
	private boolean consoleEnabled;
	private boolean scrollLock;
	private List<LabeledValue<String>> endLineChars;
	private LabeledValue<String> endLineToken;
	private String command;
	private LinkedList <String> commandHistory;
	private int currentHistoryIndex;

	public JsscSerialConsoleModel(){
		this.commandHistory = new LinkedList<String>();
		this.currentHistoryIndex = -1;
		this.endLineChars = new ArrayList<LabeledValue<String>>();
		this.endLineChars.add( new LabeledValue<String>("\r", "CR"));
		this.endLineChars.add( new LabeledValue<String>("\n", "LF"));
		this.endLineChars.add( new LabeledValue<String>("\r\n", "CR & LF"));
		this.endLineToken =  new LabeledValue<String>("\r\n", "CR & LF");
	}
	/**
	 * @return the isConsoleEnabled
	 */
	public boolean isConsoleEnabled() {
		return consoleEnabled;
	}
	public boolean getConsoleEnabled() {
		return consoleEnabled;
	}

	/**
	 * @param isConsoleEnabled the isConsoleEnabled to set
	 */
	public void setConsoleEnabled(boolean consoleEnabled) {
		firePropertyChange("consoleEnabled", this.consoleEnabled, this.consoleEnabled = consoleEnabled);
	}
	/**
	 * @return the scrollLock
	 */
	public boolean isScrollLock() {
		return scrollLock;
	}
	public boolean getScrollLock() {
		return scrollLock;
	}
	/**
	 * @param scrollLock the scrollLock to set
	 */
	public void setScrollLock(boolean scrollLock) {
		firePropertyChange("scrollLock", this.scrollLock, this.scrollLock = scrollLock);
	}
	/**
	 * @return the endLineChars
	 */
	public List<LabeledValue<String>> getEndLineChars() {
		return endLineChars;
	}
	/**
	 * @param endLineChars the endLineChars to set
	 */
	public void setEndLineChars(List<LabeledValue<String>> endLineChars) {
		firePropertyChange("endLineChars", this.endLineChars, this.endLineChars = endLineChars);
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
	 * @return the command
	 */
	public String getCommand() {
		return command;
	}
	/**
	 * @param command the command to set
	 */
	public void setCommand(String command) {
		firePropertyChange("command", this.command, this.command = command);
	}
	/**
	 * @return the commandHistory
	 */
	public LinkedList<String> getCommandHistory() {
		return commandHistory;
	}
	/**
	 * @param commandHistory the commandHistory to set
	 */
	public void setCommandHistory(LinkedList<String> commandHistory) {
		this.commandHistory = commandHistory;
	}
	/**
	 * @return the currentHistoryIndex
	 */
	public int getCurrentHistoryIndex() {
		return currentHistoryIndex;
	}
	/**
	 * @param currentHistoryIndex the currentHistoryIndex to set
	 */
	public void setCurrentHistoryIndex(int currentHistoryIndex) {
		this.currentHistoryIndex = currentHistoryIndex;
	}



}

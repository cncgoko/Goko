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
package org.goko.log.part.model;

import java.util.ArrayList;
import java.util.List;

import org.goko.core.common.applicative.logging.ApplicativeLogEvent;

public class LogLevelNode {
	private int errorLevel;
	private String label;
	private List<ApplicativeLogEvent> messages;


	LogLevelNode(int errorLevel, String label) {
		super();
		this.errorLevel = errorLevel;
		this.label = label;
		this.messages = new ArrayList<ApplicativeLogEvent>();
	}

	public void addMessage(ApplicativeLogEvent message){
		this.messages.add(message);
	}

	/**
	 * @return the errorLevel
	 */
	public int getErrorLevel() {
		return errorLevel;
	}

	/**
	 * @param errorLevel the errorLevel to set
	 */
	public void setErrorLevel(int errorLevel) {
		this.errorLevel = errorLevel;
	}

	/**
	 * @return the messages
	 */
	public List<ApplicativeLogEvent> getMessages() {
		return messages;
	}

	/**
	 * @param messages the messages to set
	 */
	public void setMessages(List<ApplicativeLogEvent> messages) {
		this.messages = messages;
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}


}

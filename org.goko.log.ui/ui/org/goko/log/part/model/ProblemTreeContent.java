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

import org.apache.commons.lang3.StringUtils;
import org.goko.common.bindings.AbstractModelObject;
import org.goko.core.common.applicative.logging.ApplicativeLogEvent;

public class ProblemTreeContent extends AbstractModelObject {
	public static final int ERROR 	= 1;
	public static final int WARNING = 2;
	private LogLevelNode errors;
	private LogLevelNode warnings;


	public ProblemTreeContent() {
		this.errors  = new LogLevelNode(ERROR, "Errors");
		this.warnings= new LogLevelNode(WARNING, "Warnings");
	}


	public void addError(ApplicativeLogEvent message){
		this.errors.addMessage(message);
		firePropertyChange("message", null, message);
	}

	public void addWarning(ApplicativeLogEvent message){
		this.warnings.addMessage(message);
		firePropertyChange("message", null, message);
	}

	public void clearAll(){
		this.warnings.getMessages().clear();
		this.errors.getMessages().clear();
		firePropertyChange("message", null, StringUtils.EMPTY);
	}
	/**
	 * @return the errors
	 */
	public LogLevelNode getErrors() {
		return errors;
	}


	/**
	 * @param errors the errors to set
	 */
	public void setErrors(LogLevelNode errors) {
		this.errors = errors;
	}


	/**
	 * @return the warnings
	 */
	public LogLevelNode getWarnings() {
		return warnings;
	}


	/**
	 * @param warnings the warnings to set
	 */
	public void setWarnings(LogLevelNode warnings) {
		this.warnings = warnings;
	}
}

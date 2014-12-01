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

import org.goko.common.bindings.AbstractModelObject;

/**
 * Problems view model
 *
 * @author PsyKo
 *
 */
public class ProblemsLogModel extends AbstractModelObject {
	private ProblemTreeContent tableContent;

	/**
	 * @return the tableContent
	 */
	public ProblemTreeContent getTableContent() {
		return tableContent;
	}

	/**
	 * @param tableContent the tableContent to set
	 */
	public void setTableContent(ProblemTreeContent tableContent) {
		this.tableContent = tableContent;
	}

}

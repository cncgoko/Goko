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
package org.goko.core.gcode.bean;

public class GCodeCommandState {
	public static final int NONE = 0;
	public static final int SENT = 1;
	public static final int EXECUTED = 2;
	public static final int CONFIRMED = 3;
	public static final int IGNORED = 4;
	public static final int ERROR = 5;

	public int state;

	/**
	 * @param state
	 */
	public GCodeCommandState(int state) {
		super();
		this.state = state;
	}

	public boolean isState(int state){
		return this.state == state;
	}
}

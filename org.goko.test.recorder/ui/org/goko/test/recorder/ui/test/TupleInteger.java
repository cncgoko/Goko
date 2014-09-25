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
package org.goko.test.recorder.ui.test;

public class TupleInteger extends Tuple<Integer> {

	public TupleInteger(String name, Integer v1, Integer v2, Integer v3, Integer v4) {
		super(name, v1, v2, v3, v4);
	}

	@Override
	public void putObject(int columnIndex, Object userInputValue) {
		put(columnIndex, Integer.valueOf(String.valueOf(userInputValue)));
	}

}

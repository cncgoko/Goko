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
package org.goko.core.common.event;

public class UpdateEvent<T> implements Event {
	private T target;

	public UpdateEvent(T target){
		this.target = target;
	}
	/**
	 * @return the target
	 */
	public T getTarget() {
		return target;
	}

	/**
	 * @param target the target to set
	 */
	public void setTarget(T target) {
		this.target = target;
	}

}

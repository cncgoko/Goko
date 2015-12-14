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
package org.goko.controller.grbl.v08.configuration;

public abstract class GrblSetting<T> {
	/** The string identifier of the setting */
	private String identifier;
	/** The value of the setting */
	private T value;
	/** Read only mode */
	private boolean readOnly;
	/** Type of setting */
	private Class<T> type;

	/**
	 * @param identifier
	 * @param value
	 * @param type
	 */
	GrblSetting(String identifier, T value, Class<T> type) {
		super();
		this.identifier = identifier;
		this.value = value;
		this.type = type;
	}

	/**
	 * @return the identifier
	 */
	public String getIdentifier() {
		return identifier;
	}

	/**
	 * @param identifier the identifier to set
	 */
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	/**
	 * Set the value of this setting from a string
	 * @param value the value as a string
	 */
	public abstract void setValueFromString(String value);
	/**
	 * Return the value of this setting as a string
	 * @return the value as a string
	 */
	public abstract String getValueAsString();
	/**
	 * @return the value
	 */
	public T getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(T value) {
		this.value = value;
	}
	/**
	 * @return the readOnly
	 */
	public boolean isReadOnly() {
		return readOnly;
	}
	/**
	 * @param readOnly the readOnly to set
	 */
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}
	/**
	 * @return the type
	 */
	public Class<T> getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(Class<T> type) {
		this.type = type;
	}

}

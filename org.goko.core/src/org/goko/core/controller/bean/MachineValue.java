/*******************************************************************************
 * 	This file is part of Goko.
 *
 *   Goko is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   Goko is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with Goko.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package org.goko.core.controller.bean;

/**
 * Bean representing a machine state value, such as position, or velocity
 * @author PsyKo
 *
 * @param <T>
 */
public class MachineValue<T> {
	private String idDescriptor;
	private T value;
	private String unit;

	/**
	 * @param idDescriptor
	 * @param value
	 */
	public MachineValue(String idDescriptor, T value) {
		super();
		this.idDescriptor = idDescriptor;
		this.value = value;
	}

	public MachineValue(MachineValue<T> machineValue) {
		super();
		this.idDescriptor = machineValue.getIdDescriptor();
		this.value = machineValue.getValue();
	}

	/**
	 * @param idDescriptor
	 * @param value
	 */
	public MachineValue(MachineValueDefinition descriptor, T value) {
		super();
		this.idDescriptor = descriptor.getId();
		this.value = value;
	}

	/**
	 * @return the value
	 */
	public T getValue() {
		return value;
	}

	public String getStringValue(){
		return String.valueOf(value);
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(T value) {
		this.value = value;
	}

	/** (inheritDoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((idDescriptor == null) ? 0 : idDescriptor.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	/** (inheritDoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		MachineValue other = (MachineValue) obj;
		if (idDescriptor == null) {
			if (other.idDescriptor != null) {
				return false;
			}
		} else if (!idDescriptor.equals(other.idDescriptor)) {
			return false;
		}
		if (value == null) {
			if (other.value != null) {
				return false;
			}
		} else if (!value.equals(other.value)) {
			return false;
		}
		return true;
	}

	/**
	 * @return the idDescriptor
	 */
	public String getIdDescriptor() {
		return idDescriptor;
	}

	/**
	 * @param idDescriptor the idDescriptor to set
	 */
	public void setIdDescriptor(String idDescriptor) {
		this.idDescriptor = idDescriptor;
	}

	/**
	 * @return the unit
	 */
	public String getUnit() {
		return unit;
	}

	/**
	 * @param unit the unit to set
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}



}

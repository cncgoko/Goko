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
package org.goko.common.bindings;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

import org.eclipse.core.internal.databinding.BindingStatus;

/**
 * Base object for model data bindings
 *
 * @author PsyKo
 *
 */
public abstract class AbstractModelObject {
	protected List<BindingStatus> validationMessages;
	private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		changeSupport.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		changeSupport.removePropertyChangeListener(listener);
	}

	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		changeSupport.addPropertyChangeListener(propertyName, listener);
	}

	public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		changeSupport.removePropertyChangeListener(propertyName, listener);
	}

	protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
		if(oldValue != null){
			boolean v = oldValue.equals(newValue);			
		}
		changeSupport.firePropertyChange(propertyName, oldValue, newValue);
	}

	/**
	 * @return the bindingStatus
	 */
	public List<BindingStatus> getValidationMessages() {
		return validationMessages;
	}

	/**
	 * @param bindingStatus
	 *            the bindingStatus to set
	 */
	public void setValidationMessages(List<BindingStatus> bindingStatus) {
		firePropertyChange("validationMessages", this.validationMessages, this.validationMessages = bindingStatus);
	}

}
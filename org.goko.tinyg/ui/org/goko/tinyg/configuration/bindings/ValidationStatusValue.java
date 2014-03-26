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
package org.goko.tinyg.configuration.bindings;

import java.util.Collection;
import java.util.Iterator;

import org.eclipse.core.databinding.ValidationStatusProvider;
import org.eclipse.core.databinding.observable.IObservableCollection;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.value.ComputedValue;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

public class ValidationStatusValue extends ComputedValue{

	private IObservableCollection validationStatusProviders;

	public ValidationStatusValue(final IObservableCollection validationStatusProviders) {
		super(Realm.getDefault(), IStatus.class);
		this.validationStatusProviders = validationStatusProviders;
	}

	@Override
	protected Object calculate() {
		IStatus status = getStatusMaxSeverity(validationStatusProviders);		
		return status;
	}

	public static IStatus getStatusMaxSeverity( Collection validationStatusProviders) {
		int maxSeverity = IStatus.OK;
		IStatus maxStatus = Status.OK_STATUS;
		for (Iterator it = validationStatusProviders.iterator(); it.hasNext();) {
			ValidationStatusProvider validationStatusProvider = (ValidationStatusProvider) it
					.next();
			IStatus status = (IStatus) validationStatusProvider
					.getValidationStatus().getValue();
			if (status.getSeverity() > maxSeverity) {
				maxSeverity = status.getSeverity();
				maxStatus = status;
			}
		}
		return maxStatus;
	}
}

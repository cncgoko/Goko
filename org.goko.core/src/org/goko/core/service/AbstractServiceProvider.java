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
package org.goko.core.service;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.adaptor.EclipseStarter;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkTechnicalException;
import org.goko.core.common.service.IGokoService;
import org.osgi.framework.BundleContext;

/**
 * Describes a Service provider for a {@link IGokoService}
 * It allows to have multiple implementation of a service interface at the same time
 *
 * @author PsyKo
 *
 * @param <I>
 */
public abstract class AbstractServiceProvider<I extends IGokoService> {
	/** The map of known implementations */
	Map<String, I> registeredImplementations;

	/**
	 * Constructor
	 */
	public AbstractServiceProvider() {
		this.registeredImplementations = new HashMap<String, I>();
	}

	/**
	 * Registers a service implementation
	 * @param service the service instance to register
	 * @throws GkException GkException
	 */
	public final void registerService(I service) throws GkException{
		if(service == null){
			throw new GkTechnicalException("Cannot register a null service implementation...");
		}
		if(isRegisteredService(service.getServiceId())){
			throw new GkTechnicalException("Cannot register a duplicate service implementation for id '"+service.getServiceId()+"'");
		}
		getRegisteredImplementations().put(service.getServiceId(), service);
	}

	/**
	 * Unregisters a service implementation
	 * @param service the service instance to remove
	 * @throws GkException GkException
	 */
	public final void unregisterService(I service) throws GkException{
		if(service == null){
			throw new GkTechnicalException("Cannot register a null service implementation...");
		}
		getRegisteredImplementations().remove(service.getServiceId());
	}

	/**
	 * Detects if the given service implementation is registered
	 * @param serviceIdentifier the identifier of the service implementation
	 * @return <code>true</code> if the service implementation is registered, <code>false</code> otherwise
	 */
	public boolean isRegisteredService(String serviceIdentifier){
		BundleContext bc = EclipseStarter.getSystemBundleContext();
		//BundleWiring.
		return getRegisteredImplementations().containsKey(serviceIdentifier);

	}
	/**
	 * @return the registeredImplementations
	 */
	public Map<String, I> getRegisteredImplementations() {
		return registeredImplementations;
	}

}

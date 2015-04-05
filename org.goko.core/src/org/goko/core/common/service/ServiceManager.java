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
package org.goko.core.common.service;

import java.util.HashMap;
import java.util.Map;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkFunctionalException;
import org.goko.core.connection.IConnectionService;
import org.goko.core.controller.IControllerService;
import org.goko.core.log.GkLog;
import org.osgi.service.log.LogService;

public class ServiceManager {
	/** LOG */
	private static final GkLog LOG = GkLog.getLogger(ServiceManager.class);

	IControllerService controllerService;

	private static Map<Class<?>, Object> mapServices = new HashMap<Class<?>, Object>();

	/** Log service */
	private static LogService logService;

	void startup(){
		LOG.info("Starting service manager");
	}

	/**
	 * Register a service
	 * @param s the instance of the service
	 * @throws GkException GkException
	 */
	public static <T extends IGokoService, S extends T> void  register(Class<T> serviceInterface, S s) throws GkException{
		mapServices.put(serviceInterface, s);
	}

	/**
	 * Unregister a service
	 * @param s the instance of the service
	 * @throws GkException GkException
	 */
	<T extends IGokoService> void  unregister(T s) throws GkException{
		mapServices.remove(s.getClass());
	}


	public static IConnectionService getConnectionService() throws GkException{
		return (IConnectionService) mapServices.get(IConnectionService.class);
	}

	public static LogService getLogService() throws GkException{
		return logService; //(LogService) mapServices.get(LogService.class);

	}

	void setLogService(LogService logService){
		this.logService = logService;
	}

	void removeLogService(LogService logService){
		this.logService = null;
	}

	@SuppressWarnings("unchecked")
	public static <K extends IGokoService, T extends K> T getServiceAs(Class<K> clazz, Class<T> castClazz) throws GkException{
		Object storedService = mapServices.get(clazz);

		if( !castClazz.isInstance(storedService) ){
			throw new GkFunctionalException("The requested service '"+clazz.getName()+"' cannot be cast to '"+castClazz.getName()+"'");
		}
		return (T) storedService;
	}
}

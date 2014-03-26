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
	static LogService logService;

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
		System.out.println("Setting log service");
		this.logService = logService;
	}

	void removeLogService(LogService logService){
		System.out.println("Removing log service");
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

package org.goko.core.log;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.service.ServiceManager;
import org.osgi.service.log.LogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GkLog {
	protected Logger logger;// = LoggerFactory.getLogger("chapters.introduction.HelloWorld2");

	private GkLog(Logger logger){
		this.logger = logger;
	}

	public void info(String msg) {
		/*try{
			if(ServiceManager.getLogService() != null){
				ServiceManager.getLogService().log(LogService.LOG_INFO, msg);
			}else{
				System.out.println(msg);
			}
		}catch(GkException e){
			e.printStackTrace();
		}*/
		logger.info(msg);
	}

	public void debug(String msg) {
		/*try{
			if(ServiceManager.getLogService() != null){
				ServiceManager.getLogService().log(LogService.LOG_DEBUG, msg);
			}else{
				System.out.println(msg);
			}
		}catch(GkException e){
			e.printStackTrace();
		}*/
		logger.debug(msg);
	}

	public void error(String msg) {
		/*try{
			if(ServiceManager.getLogService() != null){
				ServiceManager.getLogService().log(LogService.LOG_ERROR, msg);
			}else{
				System.err.println(msg);
			}
		}catch(GkException e){
			e.printStackTrace();
		}*/
		logger.error(msg);
	}
	/**
	 * Error logging
	 * @param e the exception to log
	 */
	public void error(Exception e) {
		try{
			if(ServiceManager.getLogService() != null){
				ServiceManager.getLogService().log(LogService.LOG_ERROR, "Error occured :", e);
			}else{
				e.printStackTrace();
			}
		}catch(GkException gke){
			gke.printStackTrace();
		}
	}

	public static GkLog getLogger(Class<?> clazz) {
		return new GkLog(LoggerFactory.getLogger(clazz));
	}
}

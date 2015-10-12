package org.goko.log;
import org.eclipse.equinox.log.ExtendedLogReaderService;
import org.eclipse.equinox.log.LogFilter;
import org.osgi.framework.Bundle;
import org.osgi.service.log.LogEntry;
import org.osgi.service.log.LogListener;
import org.osgi.service.log.LogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EquinoxSlf4jBridge implements LogListener, LogFilter {
	private static final Logger LOG = LoggerFactory.getLogger("OSGI Log");
	
	private ExtendedLogReaderService logReaderService;

	public void addLogReaderService(ExtendedLogReaderService lrs){
		logReaderService = lrs;
	}

	public void removeLogReaderService(ExtendedLogReaderService lrs){
		if(logReaderService == lrs){
			logReaderService = null;
		}
	}

	public void startup(){
		logReaderService.addLogListener(this);
	}

	public void shutdown(){
		logReaderService.removeLogListener(this);
	}
	
//continuer la migration avec logback en p2
//rediriger OSGI vers logback SLF4j
//http://devblog.virtage.com/2012/07/logback-and-eclipse-attaching-logback-xml/
	public String getLevelAsString(int level){
		switch(level){
		case LogService.LOG_INFO  : return "Info";
		case LogService.LOG_ERROR : return "Error";
		case LogService.LOG_WARNING : return "Warn";
		case LogService.LOG_DEBUG : return "Debug";
		default: return "Info";
		}

	}
	
	@Override
	public void logged(LogEntry entry) {
		if(isLoggable(entry.getBundle(), null, entry.getLevel())){
			StringBuffer buffer = new StringBuffer();
			buffer.append( " [ Bundle = ");
			buffer.append( entry.getBundle().getSymbolicName());
			buffer.append("] ");
			buffer.append(getLevelAsString(entry.getLevel()));
			buffer.append( entry.getMessage());
			switch(entry.getLevel()){
			case LogService.LOG_INFO  : LOG.info(buffer.toString());
			break;
			case LogService.LOG_ERROR : LOG.error(buffer.toString());
			break;
			case LogService.LOG_WARNING : LOG.warn(buffer.toString());;
			break;
			case LogService.LOG_DEBUG : LOG.debug(buffer.toString());
			break;
			}
		}
	}

	@Override
	public boolean isLoggable(Bundle bundle, String loggerName, int logLevel) {
		return true;//StringUtils.startsWithIgnoreCase(bundle.getSymbolicName(), BUNDLE_PREFIX);
	}
}

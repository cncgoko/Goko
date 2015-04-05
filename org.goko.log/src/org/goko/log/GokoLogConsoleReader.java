package org.goko.log;
import org.eclipse.equinox.log.LogFilter;
import org.osgi.framework.Bundle;
import org.osgi.service.log.LogEntry;
import org.osgi.service.log.LogListener;
import org.osgi.service.log.LogReaderService;
import org.osgi.service.log.LogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GokoLogConsoleReader implements LogListener, LogFilter {
	private static final Logger LOG = LoggerFactory.getLogger("OSGI Log");

	private LogReaderService logReaderService;

	public void addLogReaderService(LogReaderService lrs){
		logReaderService = lrs;
	}

	public void removeLogReaderService(LogReaderService lrs){
		logReaderService = lrs;
	}

	public void startup(){
		logReaderService.addLogListener(this);
	}

	public void shutdown(){
		logReaderService.removeLogListener(this);
	}

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
			LOG.info(buffer.toString());		
		}
	}

	@Override
	public boolean isLoggable(Bundle bundle, String loggerName, int logLevel) {
		return true;//StringUtils.startsWithIgnoreCase(bundle.getSymbolicName(), BUNDLE_PREFIX);
	}
}

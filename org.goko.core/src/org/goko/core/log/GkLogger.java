/**
 * 
 */
package org.goko.core.log;

import org.slf4j.Logger;
import org.slf4j.Marker;

/**
 * @author PsyKo
 *
 */
public class GkLogger implements Logger{
	/** The name*/
	private String name;
	private org.eclipse.e4.core.services.log.Logger eclipseLogger;
	
	/**
	 * @param name2
	 * @param eclipseLogger
	 */
	public GkLogger(String name, org.eclipse.e4.core.services.log.Logger eclipseLogger) {
		this.name = name;
		this.eclipseLogger = eclipseLogger;
	}

	/** (inheritDoc)
	 * @see org.slf4j.Logger#debug(java.lang.String)
	 */
	@Override
	public void debug(String arg0) {
		// TODO Auto-generated method stub
		
	}

	/** (inheritDoc)
	 * @see org.slf4j.Logger#debug(java.lang.String, java.lang.Object)
	 */
	@Override
	public void debug(String arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}

	/** (inheritDoc)
	 * @see org.slf4j.Logger#debug(java.lang.String, java.lang.Object[])
	 */
	@Override
	public void debug(String arg0, Object... arg1) {
		// TODO Auto-generated method stub
		
	}

	/** (inheritDoc)
	 * @see org.slf4j.Logger#debug(java.lang.String, java.lang.Throwable)
	 */
	@Override
	public void debug(String arg0, Throwable arg1) {
		// TODO Auto-generated method stub
		
	}

	/** (inheritDoc)
	 * @see org.slf4j.Logger#debug(org.slf4j.Marker, java.lang.String)
	 */
	@Override
	public void debug(Marker arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	/** (inheritDoc)
	 * @see org.slf4j.Logger#debug(java.lang.String, java.lang.Object, java.lang.Object)
	 */
	@Override
	public void debug(String arg0, Object arg1, Object arg2) {
		// TODO Auto-generated method stub
		
	}

	/** (inheritDoc)
	 * @see org.slf4j.Logger#debug(org.slf4j.Marker, java.lang.String, java.lang.Object)
	 */
	@Override
	public void debug(Marker arg0, String arg1, Object arg2) {
		// TODO Auto-generated method stub
		
	}

	/** (inheritDoc)
	 * @see org.slf4j.Logger#debug(org.slf4j.Marker, java.lang.String, java.lang.Object[])
	 */
	@Override
	public void debug(Marker arg0, String arg1, Object... arg2) {
		// TODO Auto-generated method stub
		
	}

	/** (inheritDoc)
	 * @see org.slf4j.Logger#debug(org.slf4j.Marker, java.lang.String, java.lang.Throwable)
	 */
	@Override
	public void debug(Marker arg0, String arg1, Throwable arg2) {
		// TODO Auto-generated method stub
		
	}

	/** (inheritDoc)
	 * @see org.slf4j.Logger#debug(org.slf4j.Marker, java.lang.String, java.lang.Object, java.lang.Object)
	 */
	@Override
	public void debug(Marker arg0, String arg1, Object arg2, Object arg3) {
		// TODO Auto-generated method stub
		
	}

	/** (inheritDoc)
	 * @see org.slf4j.Logger#error(java.lang.String)
	 */
	@Override
	public void error(String arg0) {
		// TODO Auto-generated method stub
		
	}

	/** (inheritDoc)
	 * @see org.slf4j.Logger#error(java.lang.String, java.lang.Object)
	 */
	@Override
	public void error(String arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}

	/** (inheritDoc)
	 * @see org.slf4j.Logger#error(java.lang.String, java.lang.Object[])
	 */
	@Override
	public void error(String arg0, Object... arg1) {
		// TODO Auto-generated method stub
		
	}

	/** (inheritDoc)
	 * @see org.slf4j.Logger#error(java.lang.String, java.lang.Throwable)
	 */
	@Override
	public void error(String arg0, Throwable arg1) {
		// TODO Auto-generated method stub
		
	}

	/** (inheritDoc)
	 * @see org.slf4j.Logger#error(org.slf4j.Marker, java.lang.String)
	 */
	@Override
	public void error(Marker arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	/** (inheritDoc)
	 * @see org.slf4j.Logger#error(java.lang.String, java.lang.Object, java.lang.Object)
	 */
	@Override
	public void error(String arg0, Object arg1, Object arg2) {
		// TODO Auto-generated method stub
		
	}

	/** (inheritDoc)
	 * @see org.slf4j.Logger#error(org.slf4j.Marker, java.lang.String, java.lang.Object)
	 */
	@Override
	public void error(Marker arg0, String arg1, Object arg2) {
		// TODO Auto-generated method stub
		
	}

	/** (inheritDoc)
	 * @see org.slf4j.Logger#error(org.slf4j.Marker, java.lang.String, java.lang.Object[])
	 */
	@Override
	public void error(Marker arg0, String arg1, Object... arg2) {
		// TODO Auto-generated method stub
		
	}

	/** (inheritDoc)
	 * @see org.slf4j.Logger#error(org.slf4j.Marker, java.lang.String, java.lang.Throwable)
	 */
	@Override
	public void error(Marker arg0, String arg1, Throwable arg2) {
		// TODO Auto-generated method stub
		
	}

	/** (inheritDoc)
	 * @see org.slf4j.Logger#error(org.slf4j.Marker, java.lang.String, java.lang.Object, java.lang.Object)
	 */
	@Override
	public void error(Marker arg0, String arg1, Object arg2, Object arg3) {
		// TODO Auto-generated method stub
		
	}

	/** (inheritDoc)
	 * @see org.slf4j.Logger#getName()
	 */
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	/** (inheritDoc)
	 * @see org.slf4j.Logger#info(java.lang.String)
	 */
	@Override
	public void info(String arg0) {
		// TODO Auto-generated method stub
		
	}

	/** (inheritDoc)
	 * @see org.slf4j.Logger#info(java.lang.String, java.lang.Object)
	 */
	@Override
	public void info(String arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}

	/** (inheritDoc)
	 * @see org.slf4j.Logger#info(java.lang.String, java.lang.Object[])
	 */
	@Override
	public void info(String arg0, Object... arg1) {
		// TODO Auto-generated method stub
		
	}

	/** (inheritDoc)
	 * @see org.slf4j.Logger#info(java.lang.String, java.lang.Throwable)
	 */
	@Override
	public void info(String arg0, Throwable arg1) {
		// TODO Auto-generated method stub
		
	}

	/** (inheritDoc)
	 * @see org.slf4j.Logger#info(org.slf4j.Marker, java.lang.String)
	 */
	@Override
	public void info(Marker arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	/** (inheritDoc)
	 * @see org.slf4j.Logger#info(java.lang.String, java.lang.Object, java.lang.Object)
	 */
	@Override
	public void info(String arg0, Object arg1, Object arg2) {
		// TODO Auto-generated method stub
		
	}

	/** (inheritDoc)
	 * @see org.slf4j.Logger#info(org.slf4j.Marker, java.lang.String, java.lang.Object)
	 */
	@Override
	public void info(Marker arg0, String arg1, Object arg2) {
		// TODO Auto-generated method stub
		
	}

	/** (inheritDoc)
	 * @see org.slf4j.Logger#info(org.slf4j.Marker, java.lang.String, java.lang.Object[])
	 */
	@Override
	public void info(Marker arg0, String arg1, Object... arg2) {
		// TODO Auto-generated method stub
		
	}

	/** (inheritDoc)
	 * @see org.slf4j.Logger#info(org.slf4j.Marker, java.lang.String, java.lang.Throwable)
	 */
	@Override
	public void info(Marker arg0, String arg1, Throwable arg2) {
		// TODO Auto-generated method stub
		
	}

	/** (inheritDoc)
	 * @see org.slf4j.Logger#info(org.slf4j.Marker, java.lang.String, java.lang.Object, java.lang.Object)
	 */
	@Override
	public void info(Marker arg0, String arg1, Object arg2, Object arg3) {
		// TODO Auto-generated method stub
		
	}

	/** (inheritDoc)
	 * @see org.slf4j.Logger#isDebugEnabled()
	 */
	@Override
	public boolean isDebugEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	/** (inheritDoc)
	 * @see org.slf4j.Logger#isDebugEnabled(org.slf4j.Marker)
	 */
	@Override
	public boolean isDebugEnabled(Marker arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	/** (inheritDoc)
	 * @see org.slf4j.Logger#isErrorEnabled()
	 */
	@Override
	public boolean isErrorEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	/** (inheritDoc)
	 * @see org.slf4j.Logger#isErrorEnabled(org.slf4j.Marker)
	 */
	@Override
	public boolean isErrorEnabled(Marker arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	/** (inheritDoc)
	 * @see org.slf4j.Logger#isInfoEnabled()
	 */
	@Override
	public boolean isInfoEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	/** (inheritDoc)
	 * @see org.slf4j.Logger#isInfoEnabled(org.slf4j.Marker)
	 */
	@Override
	public boolean isInfoEnabled(Marker arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	/** (inheritDoc)
	 * @see org.slf4j.Logger#isTraceEnabled()
	 */
	@Override
	public boolean isTraceEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	/** (inheritDoc)
	 * @see org.slf4j.Logger#isTraceEnabled(org.slf4j.Marker)
	 */
	@Override
	public boolean isTraceEnabled(Marker arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	/** (inheritDoc)
	 * @see org.slf4j.Logger#isWarnEnabled()
	 */
	@Override
	public boolean isWarnEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	/** (inheritDoc)
	 * @see org.slf4j.Logger#isWarnEnabled(org.slf4j.Marker)
	 */
	@Override
	public boolean isWarnEnabled(Marker arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	/** (inheritDoc)
	 * @see org.slf4j.Logger#trace(java.lang.String)
	 */
	@Override
	public void trace(String arg0) {
		// TODO Auto-generated method stub
		
	}

	/** (inheritDoc)
	 * @see org.slf4j.Logger#trace(java.lang.String, java.lang.Object)
	 */
	@Override
	public void trace(String arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}

	/** (inheritDoc)
	 * @see org.slf4j.Logger#trace(java.lang.String, java.lang.Object[])
	 */
	@Override
	public void trace(String arg0, Object... arg1) {
		// TODO Auto-generated method stub
		
	}

	/** (inheritDoc)
	 * @see org.slf4j.Logger#trace(java.lang.String, java.lang.Throwable)
	 */
	@Override
	public void trace(String arg0, Throwable arg1) {
		// TODO Auto-generated method stub
		
	}

	/** (inheritDoc)
	 * @see org.slf4j.Logger#trace(org.slf4j.Marker, java.lang.String)
	 */
	@Override
	public void trace(Marker arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	/** (inheritDoc)
	 * @see org.slf4j.Logger#trace(java.lang.String, java.lang.Object, java.lang.Object)
	 */
	@Override
	public void trace(String arg0, Object arg1, Object arg2) {
		// TODO Auto-generated method stub
		
	}

	/** (inheritDoc)
	 * @see org.slf4j.Logger#trace(org.slf4j.Marker, java.lang.String, java.lang.Object)
	 */
	@Override
	public void trace(Marker arg0, String arg1, Object arg2) {
		// TODO Auto-generated method stub
		
	}

	/** (inheritDoc)
	 * @see org.slf4j.Logger#trace(org.slf4j.Marker, java.lang.String, java.lang.Object[])
	 */
	@Override
	public void trace(Marker arg0, String arg1, Object... arg2) {
		// TODO Auto-generated method stub
		
	}

	/** (inheritDoc)
	 * @see org.slf4j.Logger#trace(org.slf4j.Marker, java.lang.String, java.lang.Throwable)
	 */
	@Override
	public void trace(Marker arg0, String arg1, Throwable arg2) {
		// TODO Auto-generated method stub
		
	}

	/** (inheritDoc)
	 * @see org.slf4j.Logger#trace(org.slf4j.Marker, java.lang.String, java.lang.Object, java.lang.Object)
	 */
	@Override
	public void trace(Marker arg0, String arg1, Object arg2, Object arg3) {
		// TODO Auto-generated method stub
		
	}

	/** (inheritDoc)
	 * @see org.slf4j.Logger#warn(java.lang.String)
	 */
	@Override
	public void warn(String arg0) {
		// TODO Auto-generated method stub
		
	}

	/** (inheritDoc)
	 * @see org.slf4j.Logger#warn(java.lang.String, java.lang.Object)
	 */
	@Override
	public void warn(String arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}

	/** (inheritDoc)
	 * @see org.slf4j.Logger#warn(java.lang.String, java.lang.Object[])
	 */
	@Override
	public void warn(String arg0, Object... arg1) {
		// TODO Auto-generated method stub
		
	}

	/** (inheritDoc)
	 * @see org.slf4j.Logger#warn(java.lang.String, java.lang.Throwable)
	 */
	@Override
	public void warn(String arg0, Throwable arg1) {
		// TODO Auto-generated method stub
		
	}

	/** (inheritDoc)
	 * @see org.slf4j.Logger#warn(org.slf4j.Marker, java.lang.String)
	 */
	@Override
	public void warn(Marker arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	/** (inheritDoc)
	 * @see org.slf4j.Logger#warn(java.lang.String, java.lang.Object, java.lang.Object)
	 */
	@Override
	public void warn(String arg0, Object arg1, Object arg2) {
		// TODO Auto-generated method stub
		
	}

	/** (inheritDoc)
	 * @see org.slf4j.Logger#warn(org.slf4j.Marker, java.lang.String, java.lang.Object)
	 */
	@Override
	public void warn(Marker arg0, String arg1, Object arg2) {
		// TODO Auto-generated method stub
		
	}

	/** (inheritDoc)
	 * @see org.slf4j.Logger#warn(org.slf4j.Marker, java.lang.String, java.lang.Object[])
	 */
	@Override
	public void warn(Marker arg0, String arg1, Object... arg2) {
		// TODO Auto-generated method stub
		
	}

	/** (inheritDoc)
	 * @see org.slf4j.Logger#warn(org.slf4j.Marker, java.lang.String, java.lang.Throwable)
	 */
	@Override
	public void warn(Marker arg0, String arg1, Throwable arg2) {
		// TODO Auto-generated method stub
		
	}

	/** (inheritDoc)
	 * @see org.slf4j.Logger#warn(org.slf4j.Marker, java.lang.String, java.lang.Object, java.lang.Object)
	 */
	@Override
	public void warn(Marker arg0, String arg1, Object arg2, Object arg3) {
		// TODO Auto-generated method stub
		
	}

}

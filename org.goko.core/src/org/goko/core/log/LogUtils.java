/**
 * 
 */
package org.goko.core.log;

import org.eclipse.core.runtime.IStatus;

/**
 * @author PsyKo
 *
 */
public class LogUtils {

	public static String getMessage(IStatus status){
		StringBuffer buffer = new StringBuffer();
		buffer.append(status.getMessage());
		buffer.append(System.lineSeparator());
		if(status.getChildren() != null && status.getChildren().length > 0){
			for (IStatus child : status.getChildren()) {
				buffer.append(getMessage(child));				
			}
		}
		return buffer.toString();
	}	
}

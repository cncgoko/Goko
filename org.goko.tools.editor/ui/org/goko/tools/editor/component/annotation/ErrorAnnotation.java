/**
 * 
 */
package org.goko.tools.editor.component.annotation;

import org.eclipse.wb.swt.ResourceManager;

/**
 * @author Psyko
 * @date 16 juin 2016
 */
public class ErrorAnnotation extends IconAnnotation{
	public static final String TYPE = "org.goko.tools.editor.error";
	/**
	 * @param image
	 * @param text 
	 */
	public ErrorAnnotation(String text) {
		super(ResourceManager.getPluginImage("org.goko.tools.editor", "resources/icons/annotations/error_marker.png"));
		setType(TYPE);
		setText(text);
	}
}

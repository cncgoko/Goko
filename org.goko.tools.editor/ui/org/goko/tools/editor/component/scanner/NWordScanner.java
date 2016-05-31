/**
 * 
 */
package org.goko.tools.editor.component.scanner;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.swt.SWT;
import org.eclipse.wb.swt.SWTResourceManager;

/**
 * @author Psyko
 * @date 23 mai 2016
 */
public class NWordScanner extends SingleTokenScanner{

	/**
	 * @param attribute
	 */
	public NWordScanner() {
		super(new TextAttribute(SWTResourceManager.getColor(SWT.COLOR_GRAY), null, SWT.ITALIC));		
	}

}

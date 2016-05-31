/**
 * 
 */
package org.goko.tools.editor.component.scanner;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.swt.SWT;
import org.eclipse.wb.swt.SWTResourceManager;

/**
 * @author Psyko
 * @date 22 mai 2016
 */
public class FWordScanner extends SingleTokenScanner{

	/**
	 * @param attribute
	 */
	public FWordScanner() {
		super(new TextAttribute(SWTResourceManager.getColor(161, 105, 70), null, SWT.NONE));		
	}

}

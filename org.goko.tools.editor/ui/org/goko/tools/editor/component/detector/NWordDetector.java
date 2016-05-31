/**
 * 
 */
package org.goko.tools.editor.component.detector;

import org.eclipse.jface.text.rules.IWordDetector;

/**
 * @author Psyko
 * @date 22 mai 2016
 */
public class NWordDetector implements IWordDetector {

	/** (inheritDoc)
	 * @see org.eclipse.jface.text.rules.IWordDetector#isWordStart(char)
	 */
	@Override
	public boolean isWordStart(char c) {
		return c == 'N' || c == 'n';
	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.text.rules.IWordDetector#isWordPart(char)
	 */
	@Override
	public boolean isWordPart(char c) {		
		return Character.isDigit(c);
	}

}

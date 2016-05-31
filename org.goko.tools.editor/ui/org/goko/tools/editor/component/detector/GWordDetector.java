/**
 * 
 */
package org.goko.tools.editor.component.detector;

import org.eclipse.jface.text.rules.IWordDetector;

/**
 * @author Psyko
 * @date 22 mai 2016
 */
public class GWordDetector implements IWordDetector {

	/** (inheritDoc)
	 * @see org.eclipse.jface.text.rules.IWordDetector#isWordStart(char)
	 */
	@Override
	public boolean isWordStart(char c) {
		return c == 'G' || c == 'g';
	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.text.rules.IWordDetector#isWordPart(char)
	 */
	@Override
	public boolean isWordPart(char c) {		
		return Character.isDigit(c) || c == '.';
	}

}

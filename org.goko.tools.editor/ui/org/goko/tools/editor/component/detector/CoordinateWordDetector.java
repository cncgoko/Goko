/**
 * 
 */
package org.goko.tools.editor.component.detector;

import org.eclipse.jface.text.rules.IWordDetector;

/**
 * @author Psyko
 * @date 22 mai 2016
 */
public class CoordinateWordDetector implements IWordDetector {

	/** (inheritDoc)
	 * @see org.eclipse.jface.text.rules.IWordDetector#isWordStart(char)
	 */
	@Override
	public boolean isWordStart(char c) {
		return c == 'x' || c == 'X'
			|| c == 'y' || c == 'Y'
			|| c == 'z' || c == 'Z'
			|| c == 'a' || c == 'A'
			|| c == 'b' || c == 'B'
			|| c == 'c' || c == 'C';
	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.text.rules.IWordDetector#isWordPart(char)
	 */
	@Override
	public boolean isWordPart(char c) {		
		return false;
	}

}

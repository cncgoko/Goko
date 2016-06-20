/**
 * 
 */
package org.goko.tools.editor.component.scanner;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

/**
 * @author Psyko
 * @date 20 juin 2016
 */
public class GCodeDecimalRule implements IPredicateRule {
	/** The matching token */
	private IToken token;

	public GCodeDecimalRule(IToken token) {
		this.token = token;
	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.text.rules.IPredicateRule#getSuccessToken()
	 */
	@Override
	public IToken getSuccessToken() {
		return token;
	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.text.rules.IRule#evaluate(org.eclipse.jface.text.rules.ICharacterScanner)
	 */
	@Override
	public IToken evaluate(ICharacterScanner scanner) {		
		return evaluate(scanner, false);
	}
	
	/** (inheritDoc)
	 * @see org.eclipse.jface.text.rules.IPredicateRule#evaluate(org.eclipse.jface.text.rules.ICharacterScanner, boolean)
	 */
	@Override
	public IToken evaluate(ICharacterScanner scanner, boolean resume) {
		char c = (char) scanner.read();
		if (Character.isDigit(c)) {
			// postive numbers and zero
			do {
				c = (char) scanner.read();
			} while (Character.isDigit(c) || c == '.');
			scanner.unread();
			return token;
		} else if (c == '-' || c == '.') {
			// negative numbers
			c = (char) scanner.read();
			if (Character.isDigit(c)) {
				do {
					c = (char) scanner.read();
				} while (Character.isDigit(c) || c == '.');
				scanner.unread();
				return token;
			} else {
				scanner.unread();
				scanner.unread();
				return Token.UNDEFINED;
			}
		} else {
			scanner.unread();
			return Token.UNDEFINED;
		}
	}

}

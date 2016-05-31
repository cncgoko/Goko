/**
 * 
 */
package org.goko.tools.editor.component.scanner;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.WordRule;

/**
 * @author Psyko
 * @date 29 mai 2016
 */
public class CaseInsensitiveWordRule extends WordRule {

	/**
	 * @param detector
	 */
	public CaseInsensitiveWordRule(IWordDetector detector) {
		super(detector);
	}
	
	/** (inheritDoc)
	 * @see org.eclipse.jface.text.rules.WordRule#addWord(java.lang.String, org.eclipse.jface.text.rules.IToken)
	 */
	@Override
	public void addWord(String word, IToken token) {		
		super.addWord(StringUtils.upperCase(word), token);
		super.addWord(StringUtils.lowerCase(word), token);
	}

}

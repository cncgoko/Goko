/**
 * 
 */
package org.goko.tools.editor.component.scanner;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.ITokenScanner;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WordRule;
import org.eclipse.wb.swt.SWTResourceManager;
import org.goko.tools.editor.component.detector.GWordDetector;

/**
 * @author Psyko
 * @date 22 mai 2016
 */
public class FWordScanner extends RuleBasedScanner implements ITokenScanner {

    public FWordScanner()
    {
    	TextAttribute feedrateTextAttribute = new TextAttribute(SWTResourceManager.getColor(35, 84, 204));
        Token feedrateWord = new Token(feedrateTextAttribute);
        
        
        WordRule wr = new WordRule(new GWordDetector());                
        wr.addWord("F", feedrateWord);
        wr.addWord("f", feedrateWord);
        
        setRules(new IRule[]{ wr });//PredicateRules(rules);
    }
    
}

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
public class GWordScanner extends RuleBasedScanner implements ITokenScanner {

    public GWordScanner()
    {
    	TextAttribute feedrateMotionTextAttribute = new TextAttribute(SWTResourceManager.getColor(35, 84, 204));
        Token feedrateMotionWord = new Token(feedrateMotionTextAttribute);
        
        TextAttribute rapidMotionTextAttribute = new TextAttribute(SWTResourceManager.getColor(255, 107, 0));
        Token rapidMotionWord = new Token(rapidMotionTextAttribute);
        
        TextAttribute genericTextAttribute = new TextAttribute(SWTResourceManager.getColor(81, 196, 141));
        Token genericMotionWord = new Token(genericTextAttribute);
        
        
        WordRule wr = new WordRule(new GWordDetector());                
        wr.addWord("G0", rapidMotionWord);
        wr.addWord("G00", rapidMotionWord);
        wr.addWord("g0", rapidMotionWord);
        wr.addWord("g00", rapidMotionWord);
        
        wr.addWord("G1", feedrateMotionWord);
        wr.addWord("G01", feedrateMotionWord);
        wr.addWord("g1", feedrateMotionWord);
        wr.addWord("g01", feedrateMotionWord);
        
        setDefaultReturnToken(genericMotionWord);
//        IPredicateRule[] rules = new IPredicateRule[2];
//        
//        rules[0] = new WordPatternRule(new GWordDetector(), "G", "90", gword);
//        rules[1] = new WordPatternRule(new GWordDetector(), "g", null, gword);
        
        
        setRules(new IRule[]{ wr });//PredicateRules(rules);
    }
    
}

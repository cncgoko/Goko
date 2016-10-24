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
import org.eclipse.swt.SWT;
import org.eclipse.wb.swt.SWTResourceManager;
import org.goko.tools.editor.component.detector.GWordDetector;

/**
 * @author Psyko
 * @date 22 mai 2016
 */
public class GWordScanner extends RuleBasedScanner implements ITokenScanner {

    public GWordScanner()
    {
    	TextAttribute feedrateMotionTextAttribute = new TextAttribute(SWTResourceManager.getColor(124, 175, 194));
        Token feedrateMotionWord = new Token(feedrateMotionTextAttribute);
        
        TextAttribute rapidMotionTextAttribute = new TextAttribute(SWTResourceManager.getColor(171, 70, 66));
        Token rapidMotionWord = new Token(rapidMotionTextAttribute);
        
        TextAttribute genericTextAttribute = new TextAttribute(SWTResourceManager.getColor(SWT.COLOR_RED), null, TextAttribute.STRIKETHROUGH);
        Token genericMotionWord = new Token(genericTextAttribute);
        
        
        WordRule wr = new CaseInsensitiveWordRule(new GWordDetector());                
        wr.addWord("G0", rapidMotionWord);
        wr.addWord("G00", rapidMotionWord);        
        wr.addWord("G1", feedrateMotionWord);
        wr.addWord("G01", feedrateMotionWord);
        wr.addWord("G2", feedrateMotionWord);
        wr.addWord("G02", feedrateMotionWord);
        wr.addWord("G3", feedrateMotionWord);
        wr.addWord("G03", feedrateMotionWord);
        wr.addWord("G4", feedrateMotionWord);
        wr.addWord("G04", feedrateMotionWord);
        
        wr.addWord("G10", feedrateMotionWord);
        wr.addWord("G17", feedrateMotionWord);
        wr.addWord("G18", feedrateMotionWord);
        wr.addWord("G19", feedrateMotionWord);
        wr.addWord("G20", feedrateMotionWord);
        wr.addWord("G21", feedrateMotionWord);
        wr.addWord("G28", feedrateMotionWord);
        wr.addWord("G30", feedrateMotionWord);
        wr.addWord("G38.2", feedrateMotionWord);
        wr.addWord("G40", feedrateMotionWord);
        wr.addWord("G41", feedrateMotionWord);
        wr.addWord("G42", feedrateMotionWord);
        wr.addWord("G43", feedrateMotionWord);
        wr.addWord("G49", feedrateMotionWord);
        wr.addWord("G53", feedrateMotionWord);
        wr.addWord("G54", feedrateMotionWord);
        wr.addWord("G55", feedrateMotionWord);
        wr.addWord("G56", feedrateMotionWord);
        wr.addWord("G57", feedrateMotionWord);
        wr.addWord("G58", feedrateMotionWord);
        wr.addWord("G59.3", feedrateMotionWord);
        wr.addWord("G61", feedrateMotionWord);
        wr.addWord("G61.1", feedrateMotionWord);
        wr.addWord("G64", feedrateMotionWord);
        wr.addWord("G80", feedrateMotionWord);
        wr.addWord("G81", feedrateMotionWord);
        wr.addWord("G89", feedrateMotionWord);
        wr.addWord("G90", feedrateMotionWord);
        wr.addWord("G91", feedrateMotionWord);
        wr.addWord("G92", feedrateMotionWord);
        wr.addWord("G92.1", feedrateMotionWord);
        wr.addWord("G92.2", feedrateMotionWord);
        wr.addWord("G92.3", feedrateMotionWord);
        wr.addWord("G93", feedrateMotionWord);
        wr.addWord("G94", feedrateMotionWord);
        wr.addWord("G98", feedrateMotionWord);
        wr.addWord("G99", feedrateMotionWord);
        wr.addWord("G28.2", feedrateMotionWord);   
        
        wr.addWord("G91.1", feedrateMotionWord);
        
        
        setDefaultReturnToken(genericMotionWord);
//        IPredicateRule[] rules = new IPredicateRule[2];
//        
//        rules[0] = new WordPatternRule(new GWordDetector(), "G", "90", gword);
//        rules[1] = new WordPatternRule(new GWordDetector(), "g", null, gword);
        
        
        setRules(new IRule[]{ wr });//PredicateRules(rules);
    }
    
}

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
import org.goko.tools.editor.component.detector.CoordinateWordDetector;

/**
 * @author Psyko
 * @date 22 mai 2016
 */
public class CoordinateWordScanner extends RuleBasedScanner implements ITokenScanner {

   
    public CoordinateWordScanner()
    {

        //Token gword = new Token(GCODE_G_WORD);
    	TextAttribute coordTextAttribute = new TextAttribute(SWTResourceManager.getColor(186,139,175));
        Token coordWord = new Token(coordTextAttribute);
        
        WordRule wr = new WordRule(new CoordinateWordDetector());                
        wr.addWord("x", coordWord);
        wr.addWord("X", coordWord);
        wr.addWord("y", coordWord);
        wr.addWord("Y", coordWord);
        wr.addWord("z", coordWord);
        wr.addWord("Z", coordWord);
        wr.addWord("a", coordWord);
        wr.addWord("A", coordWord);
        wr.addWord("b", coordWord);
        wr.addWord("B", coordWord);
        wr.addWord("c", coordWord);
        wr.addWord("C", coordWord);
        wr.addWord("i", coordWord);
        wr.addWord("I", coordWord);
        wr.addWord("j", coordWord);
        wr.addWord("J", coordWord);
        wr.addWord("k", coordWord);
        wr.addWord("K", coordWord);
        
        setRules(new IRule[]{ wr });//PredicateRules(rules);
    }
    
}

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
import org.goko.tools.editor.component.detector.MWordDetector;

/**
 * @author Psyko
 * @date 22 mai 2016
 */
public class MWordScanner extends RuleBasedScanner implements ITokenScanner {

    public MWordScanner()
    {
    	TextAttribute mWordAttribute = new TextAttribute(SWTResourceManager.getColor(220,150,86));
        Token mWord = new Token(mWordAttribute);
          
        TextAttribute unknownMWordAttribute = new TextAttribute(SWTResourceManager.getColor(SWT.COLOR_RED), null, TextAttribute.STRIKETHROUGH);
        Token unknownMWord = new Token(unknownMWordAttribute);
        
        WordRule wr = new CaseInsensitiveWordRule(new MWordDetector());                
        wr.addWord("M0", mWord);
        wr.addWord("M00", mWord);
        wr.addWord("M1", mWord);
        wr.addWord("M01", mWord);
        wr.addWord("M2", mWord);
        wr.addWord("M02", mWord);
        wr.addWord("M30", mWord);
        wr.addWord("M60", mWord);
        wr.addWord("M3", mWord);
        wr.addWord("M03", mWord);
        wr.addWord("M4", mWord);
        wr.addWord("M04", mWord);
        wr.addWord("M5", mWord);
        wr.addWord("M05", mWord);
        wr.addWord("M6", mWord);
        wr.addWord("M06", mWord);
        wr.addWord("M7", mWord);
        wr.addWord("M07", mWord);
        wr.addWord("M8", mWord);
        wr.addWord("M08", mWord);
        wr.addWord("M9", mWord);
        wr.addWord("M09", mWord);
        wr.addWord("M48", mWord);
        wr.addWord("M49", mWord);
        
        
        setDefaultReturnToken(unknownMWord);
        
        setRules(new IRule[]{ wr });
    }
    
}

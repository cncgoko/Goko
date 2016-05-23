/**
 * 
 */
package org.goko.tools.editor.component;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WordPatternRule;
import org.goko.tools.editor.component.detector.CoordinateWordDetector;
import org.goko.tools.editor.component.detector.FWordDetector;
import org.goko.tools.editor.component.detector.GWordDetector;

/**
 * @author Psyko
 * @date 22 mai 2016
 */
public class GCodePartitionScanner extends RuleBasedPartitionScanner
{
    public final static String XML_DEFAULT = "__xml_default";
    public final static String GCODE_G_COMMENT = "__g_comment";
    public final static String GCODE_G_WORD = "__g_word";
    public final static String GCODE_G0_WORD = "__g0_word";
    public final static String GCODE_G1_WORD = "__g1_word";
    public final static String GCODE_COORD_WORD = "__coord_word";

    
    public GCodePartitionScanner()
    {

        Token xmlComment = new Token(GCODE_G_COMMENT);
        
        Token gword = new Token(GCODE_G_WORD);              
        Token coordinate = new Token(GCODE_COORD_WORD);
        
        List<IPredicateRule> rules = new ArrayList<IPredicateRule>();
        
        rules.add(new MultiLineRule("(", ")", xmlComment));
        rules.add(new EndOfLineRule(";", xmlComment));
        rules.add(new WordPatternRule(new GWordDetector(), "G", null, gword));
        rules.add(new WordPatternRule(new GWordDetector(), "g", null, gword));
        
        // Coordinates
        rules.add(new WordPatternRule(new CoordinateWordDetector(), "X", null, coordinate));
        rules.add(new WordPatternRule(new CoordinateWordDetector(), "x", null, coordinate));
        rules.add(new WordPatternRule(new CoordinateWordDetector(), "Y", null, coordinate));
        rules.add(new WordPatternRule(new CoordinateWordDetector(), "y", null, coordinate));
        rules.add(new WordPatternRule(new CoordinateWordDetector(), "Z", null, coordinate));
        rules.add(new WordPatternRule(new CoordinateWordDetector(), "z", null, coordinate));
        rules.add(new WordPatternRule(new CoordinateWordDetector(), "A", null, coordinate));
        rules.add(new WordPatternRule(new CoordinateWordDetector(), "a", null, coordinate));
        rules.add(new WordPatternRule(new CoordinateWordDetector(), "B", null, coordinate));
        rules.add(new WordPatternRule(new CoordinateWordDetector(), "b", null, coordinate));
        rules.add(new WordPatternRule(new CoordinateWordDetector(), "C", null, coordinate));
        rules.add(new WordPatternRule(new CoordinateWordDetector(), "c", null, coordinate));
        
        rules.add(new WordPatternRule(new FWordDetector(), "F", null, gword));
        rules.add(new WordPatternRule(new FWordDetector(), "f", null, gword));

        setPredicateRules(rules.toArray(new IPredicateRule[]{}));//PredicateRules(rules);
    }
}

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
import org.goko.tools.editor.component.detector.MWordDetector;
import org.goko.tools.editor.component.detector.NWordDetector;
import org.goko.tools.editor.component.scanner.GCodeDecimalRule;

/**
 * @author Psyko
 * @date 22 mai 2016
 */
public class GCodePartitionScanner extends RuleBasedPartitionScanner
{    
    public final static String GCODE_G_COMMENT 		= "__g_comment";
    public final static String GCODE_G_WORD 		= "__g_word";
    public final static String GCODE_M_WORD 		= "__m_word";
    public final static String GCODE_G0_WORD 		= "__g0_word";
    public final static String GCODE_G1_WORD		= "__g1_word";    
    public final static String GCODE_COORD_WORD 	= "__coord_word";
    public final static String GCODE_DECIMAL 		= "__decimal";
    public final static String GCODE_LINE_WORD 		= "__line_word";
    public final static String GCODE_FEEDRATE_WORD 	= "__feedrate_word";
    public final static String GCODE_DEFAULT 	= "__default";
    public final static String TEXT 	= "__text";
    public final static String DFTL 	= "__dftl_partition_content_type";

    
    public GCodePartitionScanner()
    {    	
        Token Comment = new Token(GCODE_G_COMMENT);
        
        Token gword 	 = new Token(GCODE_G_WORD);              
        Token coordinate = new Token(GCODE_COORD_WORD);
        Token line 		 = new Token(GCODE_LINE_WORD);
        Token feedrate 	 = new Token(GCODE_FEEDRATE_WORD);
        Token mword 	 = new Token(GCODE_M_WORD);
        Token decimal 	 = new Token(GCODE_DECIMAL);
        Token dftl	 	 = new Token(DFTL);
        
        setDefaultReturnToken(dftl);
        
        List<IPredicateRule> rules = new ArrayList<IPredicateRule>();        
        rules.add(new MultiLineRule("(", ")", Comment));
        rules.add(new EndOfLineRule(";", Comment));
        rules.add(new WordPatternRule(new GWordDetector(), "G", null, gword));
        rules.add(new WordPatternRule(new GWordDetector(), "g", null, gword));
        
        rules.add(new WordPatternRule(new NWordDetector(), "N", null, line));
        rules.add(new WordPatternRule(new NWordDetector(), "n", null, line));

        rules.add(new WordPatternRule(new FWordDetector(), "F", null, feedrate));
        rules.add(new WordPatternRule(new FWordDetector(), "f", null, feedrate));
        
        rules.add(new WordPatternRule(new MWordDetector(), "M", null, mword));
        rules.add(new WordPatternRule(new MWordDetector(), "m", null, mword));
        
        // Coordinates
        CoordinateWordDetector coordinateWordDetector = new CoordinateWordDetector();
        rules.add(new WordPatternRule(coordinateWordDetector, "X", null, coordinate));
        rules.add(new WordPatternRule(coordinateWordDetector, "x", null, coordinate));
        rules.add(new WordPatternRule(coordinateWordDetector, "Y", null, coordinate));
        rules.add(new WordPatternRule(coordinateWordDetector, "y", null, coordinate));
        rules.add(new WordPatternRule(coordinateWordDetector, "Z", null, coordinate));
        rules.add(new WordPatternRule(coordinateWordDetector, "z", null, coordinate));
        rules.add(new WordPatternRule(coordinateWordDetector, "A", null, coordinate));
        rules.add(new WordPatternRule(coordinateWordDetector, "a", null, coordinate));
        rules.add(new WordPatternRule(coordinateWordDetector, "B", null, coordinate));
        rules.add(new WordPatternRule(coordinateWordDetector, "b", null, coordinate));
        rules.add(new WordPatternRule(coordinateWordDetector, "C", null, coordinate));
        rules.add(new WordPatternRule(coordinateWordDetector, "c", null, coordinate));
        rules.add(new WordPatternRule(coordinateWordDetector, "I", null, coordinate));
        rules.add(new WordPatternRule(coordinateWordDetector, "i", null, coordinate));
        rules.add(new WordPatternRule(coordinateWordDetector, "J", null, coordinate));
        rules.add(new WordPatternRule(coordinateWordDetector, "j", null, coordinate));
        rules.add(new WordPatternRule(coordinateWordDetector, "K", null, coordinate));
        rules.add(new WordPatternRule(coordinateWordDetector, "k", null, coordinate));
        
        rules.add(new WordPatternRule(new FWordDetector(), "F", null, gword));
        rules.add(new WordPatternRule(new FWordDetector(), "f", null, gword));
        
        rules.add(new GCodeDecimalRule(decimal));
                
        setPredicateRules(rules.toArray(new IPredicateRule[]{}));//PredicateRules(rules);
    }
}

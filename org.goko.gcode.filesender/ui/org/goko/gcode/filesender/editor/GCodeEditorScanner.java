package org.goko.gcode.filesender.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.NumberRule;
import org.eclipse.jface.text.rules.PatternRule;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WordRule;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

public class GCodeEditorScanner extends RuleBasedScanner {

	private static RGB COMMENT = new RGB(51, 140, 41);
	private static RGB LINE = new RGB(200, 200,200);
	private static RGB PARAM = new RGB(140, 41, 117);
	private static RGB GKEYWORD= new RGB(2, 8, 176);
	private static RGB MKEYWORD= new RGB(222, 139, 4);
	private static RGB NUMBER = new RGB(128,128,128);
	private static RGB DEFAULT = new RGB(0,0,0);


	public GCodeEditorScanner() {
		super();
		setRules(extractRules());
	}


	private IRule[] extractRules() {
		IToken keyword = new Token(new TextAttribute(new Color(Display.getCurrent(), GKEYWORD), null, SWT.BOLD));
		IToken mkeyword = new Token(new TextAttribute(new Color(Display.getCurrent(), MKEYWORD), null, SWT.BOLD));
		IToken param = new Token(new TextAttribute(new Color(Display.getCurrent(), PARAM), null, SWT.NATIVE));
		IToken line = new Token(new TextAttribute(new Color(Display.getCurrent(), LINE), null, SWT.UNDERLINE_ERROR));
		IToken comment = new Token(new TextAttribute(new Color(Display.getCurrent(), COMMENT), null, SWT.ITALIC));
		IToken number = new Token(new TextAttribute(new Color(Display.getCurrent(), NUMBER), null, SWT.ITALIC));
		IToken defaut = new Token(new TextAttribute(new Color(Display.getCurrent(), DEFAULT)));

		WordRule rule = new WordRule(new IWordDetector() {
			@Override
			public boolean isWordPart(char c) {
				return Character.isAlphabetic(c) ;
			}

			@Override
			public boolean isWordStart(char c) {
				return Character.isAlphabetic(c) ;
			}
		}, defaut);

		for (String k : GCodeKeywords.KEYWORDS) {
            rule.addWord(k, keyword);
		}

		for (String k : GCodeKeywords.PARAMETERS) {
            rule.addWord(k, param);
		}
		NumberRule numberRule = new NumberRule(number);


		PatternRule ruleLine = new PatternRule("N", " ",line,'c',true);
		PatternRule ruleGCommand = new PatternRule("G", " ",keyword,'c',true);
		PatternRule ruleMCommand = new PatternRule("M", " ",mkeyword,'c',true);
		PatternRule ruleComment = new PatternRule("M", " ",comment,'c',true);
		PatternRule ruleSingleLineComment = new PatternRule(";", " ",comment,' ',true);

		List<IRule> rules = new ArrayList<IRule>();
		rules.add(ruleLine);
		rules.add(ruleGCommand);
		rules.add(ruleMCommand);
		rules.add(ruleComment);
		rules.add(ruleSingleLineComment);
		rules.add(rule);
		rules.add(numberRule);
		rules.add(new MultiLineRule("(", ")", comment));


		return rules.toArray(new IRule[]{});
	}

}

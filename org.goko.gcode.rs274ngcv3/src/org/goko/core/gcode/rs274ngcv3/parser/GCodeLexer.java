/*******************************************************************************
 * 	This file is part of Goko.
 *
 *   Goko is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   Goko is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with Goko.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package org.goko.core.gcode.rs274ngcv3.parser;

import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkFunctionalException;
import org.goko.core.common.i18n.MessageResource;
import org.goko.core.common.utils.Location;
import org.goko.core.gcode.element.validation.IValidationElement.ValidationSeverity;
import org.goko.core.gcode.element.validation.IValidationTarget;

import IValidationElement.ValidationElement;

/**
 * GCode file tokenizer
 * @author PsyKo
 *
 */
public class GCodeLexer {
	/** Multi line comment pattern */
	private Pattern multilineCommentPattern;
	/** Simple comment pattern*/
	private Pattern simpleCommentPattern;
	/** Line number pattern */
	private Pattern lineNumberPattern;
	/** Word pattern*/
	private Pattern wordPattern;
	/** White space detection pattern at the beginning*/
	private Pattern spacePattern;	
	/** Percent sign detection pattern at the beginning*/
	private Pattern percentPattern;
	/**
	 * Constructor
	 */
	public GCodeLexer() {
		multilineCommentPattern = Pattern.compile(GCodeTokenType.MULTILINE_COMMENT.getPattern(), Pattern.MULTILINE | Pattern.DOTALL);
		simpleCommentPattern    = Pattern.compile(GCodeTokenType.SIMPLE_COMMENT.getPattern());
		lineNumberPattern = Pattern.compile(GCodeTokenType.LINE_NUMBER.getPattern());
		wordPattern    = Pattern.compile(GCodeTokenType.WORD.getPattern());
		spacePattern    = Pattern.compile("^[ ]+");
		percentPattern    = Pattern.compile(GCodeTokenType.PERCENT.getPattern());
	}
	/**
	 * Create a list of token from a String
	 * @param stringCommand the string to extract tokens from
	 * @return a list of {@link GCodeToken}
	 * @throws GkException GkException
	 */
	public List<GCodeToken> tokenize(String stringCommand, IValidationTarget validationTarget, int lineNumber) throws GkException{
		List<GCodeToken> lstTokens = new ArrayList<GCodeToken>();
		return createTokens(stringCommand, lstTokens, validationTarget, lineNumber, 0);
	}
	
	/**
	 * Create a list of token from an InputStream
	 * @param inStream the input stream
	 * @return a list of {@link GCodeToken}
	 * @throws GkException GkException
	 */
	public List<List<GCodeToken>> tokenize(InputStream inStream, IValidationTarget validationTarget) throws GkException{
		Scanner scanner = new Scanner(inStream);

		List<List<GCodeToken>> lstFileTokens = new ArrayList<List<GCodeToken>>();
		String line = null;
		
		List<GCodeToken> tokens = null;
		int lineNumber = 0;
		while(scanner.hasNextLine()){
			line = scanner.nextLine();
			tokens = tokenize(line, validationTarget, lineNumber);
			lineNumber++;
			lstFileTokens.add(tokens);
		}

		scanner.close();
		return lstFileTokens;
	}
	
	/**
	 * Recursive method used to split the stringCommand into a list of tokens
	 * @param stringCommand the string command
	 * @param tokens the list of token
	 * @throws GkException GkException
	 */
	protected List<GCodeToken> createTokens(String pStringCommand, List<GCodeToken> tokens, IValidationTarget validationTarget, int lineNumber, int columnNumber) throws GkException{
		String stringCommand = pStringCommand;
		int totalColumn = columnNumber + StringUtils.length(stringCommand);
		if(StringUtils.isBlank(stringCommand)){
			return tokens;
		}
		Matcher spaceMatcher    = spacePattern.matcher(stringCommand);
		if(spaceMatcher.find()){
			String remainingString = spaceMatcher.replaceFirst(StringUtils.EMPTY);
			return createTokens(remainingString,tokens, validationTarget, lineNumber, totalColumn - StringUtils.length(remainingString));
		}
		Matcher multilineCommentMatcher = multilineCommentPattern.matcher(stringCommand);
		if(multilineCommentMatcher.find()){
			String remainingString = extractToken(multilineCommentMatcher, tokens, GCodeTokenType.MULTILINE_COMMENT);
			return createTokens(remainingString,tokens, validationTarget, lineNumber, totalColumn - StringUtils.length(remainingString));
		}
		Matcher simpleCommentMatcher    = simpleCommentPattern.matcher(stringCommand);
		if(simpleCommentMatcher.find()){
			String remainingString = extractToken(simpleCommentMatcher, tokens,GCodeTokenType.SIMPLE_COMMENT);
			return createTokens(remainingString,tokens, validationTarget, lineNumber, totalColumn - StringUtils.length(remainingString));
		}
		// Remove all white spaces ( comments already removed )
		while(StringUtils.startsWith(stringCommand, " ")){
			stringCommand = stringCommand.replaceFirst("\\s", StringUtils.EMPTY);
			columnNumber += 1;
		}
		Matcher lineNumberMatcher    = lineNumberPattern.matcher(stringCommand);
		if(lineNumberMatcher.find()){
			String remainingString = extractToken(lineNumberMatcher, tokens, GCodeTokenType.LINE_NUMBER);
			return createTokens(remainingString,tokens, validationTarget, lineNumber, totalColumn - StringUtils.length(remainingString));
		}
		
		Matcher wordMatcher    = wordPattern.matcher(stringCommand);
		if(wordMatcher.find()){
			String remainingString = extractToken(wordMatcher, tokens, GCodeTokenType.WORD);
			return createTokens(remainingString,tokens, validationTarget, lineNumber, totalColumn - StringUtils.length(remainingString));
		}
		
		Matcher percentMatcher    = percentPattern.matcher(stringCommand);
		if(percentMatcher.find()){
			String remainingString = extractToken(percentMatcher, tokens, GCodeTokenType.PERCENT);
			return createTokens(remainingString,tokens, validationTarget, lineNumber, totalColumn - StringUtils.length(remainingString));
		}
		if(validationTarget != null){
			ValidationElement vElement = new ValidationElement(ValidationSeverity.ERROR, new Location(lineNumber, columnNumber), MessageFormat.format(MessageResource.getMessage("GCO-101"), stringCommand));
			validationTarget.addValidationElement(vElement);
			return tokens;
		}else{
			throw new GkFunctionalException("GCO-101",stringCommand);
		}
	}
	/**
	 * Extract the first token from the given matcher
	 * @param matcher the matcher
	 * @param tokens the list of tokens
	 * @param type the type of token to create
	 * @return the remaining String after the token extraction
	 */
	protected String extractToken(Matcher matcher, List<GCodeToken> tokens, GCodeTokenType type){
		tokens.add( new GCodeToken(type, matcher.group()) );
		return matcher.replaceFirst(StringUtils.EMPTY);
	}
}

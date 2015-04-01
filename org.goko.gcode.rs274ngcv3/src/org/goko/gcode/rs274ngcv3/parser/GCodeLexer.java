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
package org.goko.gcode.rs274ngcv3.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkFunctionalException;
import org.goko.core.common.exception.GkTechnicalException;

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
	/** White space detection pattern*/
	private Pattern spacePattern;

	/**
	 * Constructor
	 */
	public GCodeLexer() {
		multilineCommentPattern = Pattern.compile(GCodeTokenType.MULTILINE_COMMENT.getPattern(), Pattern.MULTILINE | Pattern.DOTALL);
		simpleCommentPattern    = Pattern.compile(GCodeTokenType.SIMPLE_COMMENT.getPattern());
		lineNumberPattern = Pattern.compile(GCodeTokenType.LINE_NUMBER.getPattern());
		wordPattern    = Pattern.compile(GCodeTokenType.WORD.getPattern());
		spacePattern    = Pattern.compile("^[ ]+");
	}
	/**
	 * Create a list of token from a String
	 * @param stringCommand the string to extract tokens from
	 * @return a list of {@link GCodeToken}
	 * @throws GkException GkException
	 */
	public List<GCodeToken> createTokens(String stringCommand) throws GkException{
		List<GCodeToken> lstTokens = new ArrayList<GCodeToken>();

		return createTokens(stringCommand, lstTokens);
	}
	/**
	 * Create a list of token from a File
	 * @param filepath the path to the file
	 * @return a list of {@link GCodeToken}
	 * @throws GkException GkException
	 */
	public List<GCodeToken> createTokensFromFile(String filepath) throws GkException{
		File file = new File(filepath);
		try {
			return createTokensFromInputStream(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			throw new GkTechnicalException(e);
		}
	}

	public List<GCodeToken> createTokensFromInputStream(InputStream inStream) throws GkException{
		Scanner scanner = new Scanner(inStream);

		List<GCodeToken> lstFileTokens = new ArrayList<GCodeToken>();
		String line = null;
		GCodeToken newLineToken = new GCodeToken(GCodeTokenType.NEW_LINE, StringUtils.EMPTY);

		while(scanner.hasNextLine()){
			line = scanner.nextLine();
			lstFileTokens.addAll(createTokens(line));
			lstFileTokens.add(newLineToken);//new GCodeToken(GCodeTokenType.NEW_LINE, StringUtils.EMPTY));
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
	protected List<GCodeToken> createTokens(String stringCommand, List<GCodeToken> tokens) throws GkException{
		if(StringUtils.isBlank(stringCommand)){
			return tokens;
		}
		Matcher spaceMatcher    = spacePattern.matcher(stringCommand);
		if(spaceMatcher.find()){
			String remainingString = spaceMatcher.replaceFirst(StringUtils.EMPTY);
			return createTokens(remainingString,tokens);
		}
		Matcher wordMatcher    = wordPattern.matcher(stringCommand);
		if(wordMatcher.find()){
			String remainingString = extractToken(wordMatcher, tokens, GCodeTokenType.WORD);
			return createTokens(remainingString,tokens);
		}
		Matcher lineNumberMatcher    = lineNumberPattern.matcher(stringCommand);
		if(lineNumberMatcher.find()){
			String remainingString = extractToken(lineNumberMatcher, tokens, GCodeTokenType.LINE_NUMBER);
			return createTokens(remainingString,tokens);
		}
		Matcher multilineCommentMatcher = multilineCommentPattern.matcher(stringCommand);
		if(multilineCommentMatcher.find()){
			String remainingString = extractToken(multilineCommentMatcher, tokens, GCodeTokenType.MULTILINE_COMMENT);
			return createTokens(remainingString,tokens);
		}
		Matcher simpleCommentMatcher    = simpleCommentPattern.matcher(stringCommand);
		if(simpleCommentMatcher.find()){
			String remainingString = extractToken(simpleCommentMatcher, tokens,GCodeTokenType.SIMPLE_COMMENT);
			return createTokens(remainingString,tokens);
		}
		throw new GkFunctionalException("Unexpected character : "+stringCommand);
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

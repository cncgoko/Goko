package org.goko.core.gcode.rs274ngcv3.utils;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkTechnicalException;
import org.goko.core.gcode.element.GCodeWord;

public class GCodeWordUtils {
	
	public static GCodeWord getWordByLetter(String letter, List<GCodeWord> words) throws GkException{
		return getWordByLetter(letter, words, false);
	}
	
	public static GCodeWord getAndRemoveWordByLetter(String letter, List<GCodeWord> words) throws GkException{
		GCodeWord word = getWordByLetter(letter, words, false);
		words.remove(word);
		return word;
	}
	
	public static GCodeWord findAndRemoveWordByLetter(String letter, List<GCodeWord> words) throws GkException{
		GCodeWord word = findWordByLetter(letter, words, false);
		if(word != null){
			words.remove(word);
		}
		return word;
	}
	
	public static GCodeWord getWordByLetter(String letter, List<GCodeWord> words, boolean greedy) throws GkException{
		GCodeWord word = findWordByLetter(letter, words, greedy);
		if(word == null){
			throw new GkTechnicalException("Could not find word for letter ["+letter+"]");
		}
		return word;
	}
	
	public static boolean containsWordByLetter(String letter, List<GCodeWord> words){
		return findWordByLetter(letter, words, false) != null;
	}
	
	public static GCodeWord findWordByLetter(String letter, List<GCodeWord> words){
		return findWordByLetter(letter, words, false);
	}
	
	public static GCodeWord findWordByLetter(String letter, List<GCodeWord> words, boolean greedy){
		GCodeWord word = null;
		if(CollectionUtils.isNotEmpty(words)){
			for (GCodeWord gCodeWord : words) {
				if(StringUtils.equalsIgnoreCase(letter, gCodeWord.getLetter())){
					word = gCodeWord;
					if(!greedy){
						break;
					}
				}
			}
		}
		return word;		
	}

	public static GCodeWord getAndRemoveWord(String wordStr, List<GCodeWord> words) throws GkException{
		GCodeWord word = getWord(wordStr, words, false);
		words.remove(word);
		return word;
	}
	
	public static GCodeWord findAndRemoveWord(String wordStr, List<GCodeWord> words) throws GkException{
		GCodeWord word = findWord(wordStr, words, false);
		if(word != null){
			words.remove(word);
		}
		return word;
	}
	
	public static GCodeWord getWord(String wordStr, List<GCodeWord> words) throws GkException{
		return getWord(wordStr, words, false);
	}
	
	public static GCodeWord getWord(String wordStr, List<GCodeWord> words, boolean greedy) throws GkException{
		GCodeWord word = findWord(wordStr, words, greedy);
		if(word == null){
			throw new GkTechnicalException("Could not find word ["+wordStr+"]");
		}
		return word;
	}
	
	public static boolean containsWord(String wordStr, List<GCodeWord> words){
		return findWord(wordStr, words, false) != null;
	}
	
	public static GCodeWord findWord(String wordStr, List<GCodeWord> words){
		return findWord(wordStr, words, false);
	}
	
	public static GCodeWord findWord(String wordStr, List<GCodeWord> words, boolean greedy){
		GCodeWord word = null;
		if(CollectionUtils.isNotEmpty(words)){
			for (GCodeWord gCodeWord : words) {
				if(StringUtils.equalsIgnoreCase(wordStr, gCodeWord.completeString())){
					word = gCodeWord;
					if(!greedy){
						break;
					}
				}
			}
		}
		return word;		
	}
	
	public static Integer intValue(GCodeWord word){
		return Integer.valueOf(word.getValue());
	}
}

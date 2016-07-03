/**
 * 
 */
package org.goko.gcode.rs274ngcv3.assertion;

import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.element.GCodeLine;
import org.goko.core.gcode.element.GCodeWord;
import org.junit.Assert;

/**
 * @author Psyko
 * @date 30 juin 2016
 */
public class AssertGCodeLine {

	public static void assertExactWords(GCodeLine line, GCodeWord... words) throws GkException{
		Assert.assertNotNull(line);
		Assert.assertNotNull(words);
		
		List<GCodeWord> lineWords = line.getWords(); 
		for (GCodeWord word : words) {
			boolean found = false;
			for (GCodeWord wordInLine : lineWords) {
				if(ObjectUtils.equals(word, wordInLine)){
					found = true;
					break;
				}				
			}
			if(!found){
				Assert.fail("Could not find the word ["+word.completeString()+"] in given line.");
			}
		}
		Assert.assertEquals(words.length, line.getWords().size());
	}
}

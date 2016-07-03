/**
 * 
 */
package org.goko.gcode.rs274ngcv3.assertion;

import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.rs274ngcv3.element.InstructionProvider;
import org.goko.core.gcode.rs274ngcv3.element.InstructionSet;
import org.junit.Assert;

/**
 * @author Psyko
 * @date 30 juin 2016
 */
public class AssertInstructionProvider {

	public static void assertInstructionSetCount(InstructionProvider provider, int lineCount) throws GkException{
		Assert.assertNotNull(provider);
		Assert.assertEquals("The given provider does not have the required number of instruction sets.", lineCount, provider.getInstructionSets());
	}
	
	public static void assertContainsInstructionSet(InstructionProvider provider, InstructionSet set) throws GkException{
		Assert.assertNotNull(provider);
		List<InstructionSet> sets = provider.getInstructionSets();
		for (InstructionSet instructionSet : sets) {			
			if(ObjectUtils.equals(instructionSet, set)){
				return;
			}
		}
		Assert.fail("Could not find the given set in the given instruction provider.");
	}
}

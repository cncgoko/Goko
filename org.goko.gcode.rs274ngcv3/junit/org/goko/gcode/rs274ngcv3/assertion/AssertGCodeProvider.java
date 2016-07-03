/**
 * 
 */
package org.goko.gcode.rs274ngcv3.assertion;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.element.IGCodeProvider;
import org.junit.Assert;

/**
 * @author Psyko
 * @date 30 juin 2016
 */
public class AssertGCodeProvider {

	public static void assertLineCount(IGCodeProvider provider, int lineCount) throws GkException{
		Assert.assertNotNull(provider);
		Assert.assertEquals("The given provider does not have the required number of lines.", lineCount, provider.getLinesCount());
	}
}

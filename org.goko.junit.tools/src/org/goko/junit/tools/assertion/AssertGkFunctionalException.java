package org.goko.junit.tools.assertion;

import org.goko.core.common.exception.GkFunctionalException;
import org.junit.Assert;

public final class AssertGkFunctionalException{

	public static void assertException(GkFunctionalException e, String key, String... args){
		Assert.assertEquals(key, e.getKey());
		Assert.assertArrayEquals(args, e.getArguments());
	}
}

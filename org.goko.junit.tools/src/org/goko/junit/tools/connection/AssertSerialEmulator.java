package org.goko.junit.tools.connection;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.goko.core.common.GkUtils;
import org.junit.Assert;

public class AssertSerialEmulator {

	public static final void assertMessagePresent(SerialConnectionEmulator emulator, String message){
		List<List<Byte>> buffers = emulator.getSentBuffer();
		if(CollectionUtils.isNotEmpty(buffers)){			
			for (List<Byte> list : buffers) {
				String str = GkUtils.toString(list);
				if(StringUtils.equals(GkUtils.toString(list), message)){
					return;
				}
			}
		}
		Assert.fail("Message '"+message+"' not found in Serial emulator");
	}
}

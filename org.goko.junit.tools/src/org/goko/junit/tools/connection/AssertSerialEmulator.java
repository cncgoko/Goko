package org.goko.junit.tools.connection;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.goko.core.common.GkUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.connection.IConnectionDataListener;
import org.junit.Assert;

public class AssertSerialEmulator {

	public static final void assertMessagePresent(SerialConnectionEmulator emulator, String message){
		String lastMessage = StringUtils.EMPTY;
		List<List<Byte>> buffers = emulator.getSentBuffer();
		if(CollectionUtils.isNotEmpty(buffers)){			
			for (List<Byte> list : buffers) {
				lastMessage = GkUtils.toString(list);
				if(StringUtils.equalsIgnoreCase(GkUtils.toString(list), message)){
					return;
				}
			}
		}
		Assert.fail("Message '"+message+"' not found in Serial emulator. Last found was ["+lastMessage+"]");
	}
	
	/**
	 * Check the next outgoing message. Error if no output after timeout, or if first outgoing is not expected message 
	 * @param emulator the SerialConnectionEmulator
	 * @param message the expected message
	 * @param timeout the timeout
	 * @throws Exception Exception
	 */
	public static final void assertOutputMessagePresent(final SerialConnectionEmulator emulator, final String message, long timeout) throws Exception{
		final Object obj = new Object();
		
		IConnectionDataListener listener = new IConnectionDataListener() {			
			@Override
			public void onDataSent(List<Byte> data) throws GkException {				
				synchronized (obj) {
					obj.notify();					
				}				
			}			
			@Override
			public void onDataReceived(List<Byte> data) throws GkException {
				// TODO Auto-generated method stub
				
			}
		};
		emulator.addOutputDataListener(listener);
		synchronized (obj) {
			obj.wait(timeout);	
		}		
		emulator.removeOutputDataListener(listener);
		assertMessagePresent(emulator, message);
	}
}

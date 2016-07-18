/**
 * 
 */
package org.goko.core.gcode.rs274ngcv3.jogl.internal;

import java.util.Iterator;
import java.util.LinkedList;

import org.apache.commons.lang3.ObjectUtils;
import org.goko.core.common.exception.GkException;

/**
 * @author Psyko
 * @date 10 juil. 2016
 */
public class GCodeContextProviderLinkedList extends LinkedList<LinkedGCodeContextProvider> {

	public LinkedGCodeContextProvider findExecutionTokenByIdExecutionToken(Integer idExecutionToken) throws GkException {
		// Search the matching context provider
		Iterator<LinkedGCodeContextProvider> iter = descendingIterator();
		while (iter.hasNext()) {
			LinkedGCodeContextProvider lclContextProvider = (LinkedGCodeContextProvider) iter.next();
			if (ObjectUtils.equals(lclContextProvider.getToken().getId(), idExecutionToken)) {
				return lclContextProvider;
			}
		}
		return null;
	}

	public LinkedGCodeContextProvider findExecutionTokenAfter(Integer idExecutionToken) throws GkException{
		LinkedGCodeContextProvider previous = null;
		// Search the matching context provider
		Iterator<LinkedGCodeContextProvider> iter = descendingIterator();
		while (iter.hasNext()) {
			LinkedGCodeContextProvider lclContextProvider = (LinkedGCodeContextProvider) iter.next();
			if (ObjectUtils.equals(lclContextProvider.getToken().getId(), idExecutionToken)) {
				break;
			}
			previous = lclContextProvider;
		}
		return previous;
	}

	public LinkedGCodeContextProvider findExecutionTokenBefore(Integer idExecutionToken) throws GkException{
		LinkedGCodeContextProvider previous = null;
		// Search the matching context provider
		Iterator<LinkedGCodeContextProvider> iter = descendingIterator();
		while (iter.hasNext()) {
			LinkedGCodeContextProvider lclContextProvider = (LinkedGCodeContextProvider) iter.next();
			if (ObjectUtils.equals(lclContextProvider.getToken().getId(), idExecutionToken)) {
				if(iter.hasNext()){
					previous = iter.next();
				}				
				break;
			}			
		}
		return previous;
	}
}

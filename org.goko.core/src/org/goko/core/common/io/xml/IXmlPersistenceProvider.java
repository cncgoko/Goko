/**
 * 
 */
package org.goko.core.common.io.xml;

import java.util.List;

import org.goko.core.common.exception.GkException;

/**
 * @author Psyko
 * @date 27 nov. 2016
 */
public interface IXmlPersistenceProvider {

	/**
	 * Returns the list of supported XML classes
	 * @return a list of class
	 * @throws GkException GkException
	 */
	List<Class<?>> getSupportedClass() throws GkException;
}

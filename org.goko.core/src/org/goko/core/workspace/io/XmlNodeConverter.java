/**
 * 
 */
package org.goko.core.workspace.io;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkTechnicalException;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.stream.InputNode;

/**
 * @author PsyKo
 * @date 13 déc. 2015
 */
public class XmlNodeConverter<T> {
	private Class<T> clazz;
	
	/**
	 * @param clazz
	 */
	public XmlNodeConverter(Class<T> clazz) {
		super();
		this.clazz = clazz;
	}

	public T load(InputNode node) throws GkException{
		Serializer s = new Persister();
		try {
			return s.read(clazz, node);
		} catch (Exception e) {
			throw new GkTechnicalException(e);
		}		
	}
}

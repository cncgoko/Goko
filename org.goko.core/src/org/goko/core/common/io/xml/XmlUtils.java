package org.goko.core.common.io.xml;

import java.io.File;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkTechnicalException;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

public class XmlUtils {

	/**
	 * Creates an instance of the given class from the given file
	 * @param file the file 
	 * @param clazz the target class to build
	 * @return an instance of the given class
	 * @throws GkException GkException
	 */
	public static <T> T load(File file, Class<T> clazz) throws GkException{		
		try {
			Serializer serializer = new Persister();
			return serializer.read(clazz, file);
		} catch (Exception e) {
			throw new GkTechnicalException(e);
		}
	}
	
	public static <T> void write(File file, T object) throws GkException{
		try {
			Serializer serializer = new Persister();
			serializer.write(object, file);
		} catch (Exception e) {
			throw new GkTechnicalException(e);
		}
	}
}

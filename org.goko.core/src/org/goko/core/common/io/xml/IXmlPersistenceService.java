package org.goko.core.common.io.xml;

import java.io.InputStream;
import java.io.OutputStream;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.service.IGokoService;

public interface IXmlPersistenceService extends IGokoService {

	void register(Class<?> clazz) throws GkException;

	<T> T read(Class<T> type, InputStream input) throws GkException;

	<T> void write(T type, OutputStream output) throws GkException;
}

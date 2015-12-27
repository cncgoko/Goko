package org.goko.core.common.io.xml;

import java.io.File;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.service.IGokoService;

public interface IXmlPersistenceService extends IGokoService {

	void register(Class<?> clazz) throws GkException;

	<T> T read(Class<T> type, File file) throws GkException;

	<T> void write(T type, File file) throws GkException;
}

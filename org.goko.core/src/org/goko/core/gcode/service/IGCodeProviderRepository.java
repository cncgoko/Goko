package org.goko.core.gcode.service;

import java.util.List;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.service.IGokoService;
import org.goko.core.gcode.element.IGCodeProvider;

public interface IGCodeProviderRepository extends IGokoService{

	List<IGCodeProvider> getGCodeProvider() throws GkException;

	IGCodeProvider getGCodeProvider(Integer id) throws GkException;

	IGCodeProvider getGCodeProvider(String code) throws GkException;
	
	void lockGCodeProvider(Integer idGcodeProvider) throws GkException;

	void unlockGCodeProvider(Integer idGcodeProvider) throws GkException;

	void addGCodeProvider(IGCodeProvider provider) throws GkException;

	void deleteGCodeProvider(Integer id) throws GkException;

	void addListener(IGCodeProviderRepositoryListener listener) throws GkException;

	void removeListener(IGCodeProviderRepositoryListener listener) throws GkException;

	void clearAll() throws GkException;
}

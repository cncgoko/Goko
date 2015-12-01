package org.goko.core.gcode.service;

import java.util.List;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.service.IGokoService;
import org.goko.core.gcode.element.IGCodeProvider;

public interface IGCodeProviderRepository extends IGokoService{
	
	List<IGCodeProvider> getGCodeProvider() throws GkException;
	
	IGCodeProvider getGCodeProvider(Integer id) throws GkException;
	
	void addGCodeProvider(IGCodeProvider provider) throws GkException; 
	
	void deleteGCodeProvider(Integer id) throws GkException;
}

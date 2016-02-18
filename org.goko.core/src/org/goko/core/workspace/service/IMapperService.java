/**
 * 
 */
package org.goko.core.workspace.service;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.service.IGokoService;

/**
 * @author PsyKo
 * @date 10 févr. 2016
 */
public interface IMapperService extends IGokoService{

	void addLoader(ILoader<?, ?> loader) throws GkException;
	
	void addExporter(IExporter<?, ?> loader) throws GkException;
	
	<O> O load(Object object, Class<O> outputClass) throws GkException;
	
	<O> O export(Object object, Class<O> outputClass) throws GkException;
}

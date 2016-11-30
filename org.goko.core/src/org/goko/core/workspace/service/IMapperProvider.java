/**
 * 
 */
package org.goko.core.workspace.service;

import java.util.List;

import org.goko.core.common.exception.GkException;

/**
 * @author Psyko
 * @date 26 nov. 2016
 */
public interface IMapperProvider {

	List<ILoader<?, ?>> getLoader() throws GkException;
	
	List<IExporter<?, ?>> getExporter() throws GkException;
}

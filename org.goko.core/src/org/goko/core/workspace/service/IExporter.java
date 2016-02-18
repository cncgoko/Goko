/**
 * 
 */
package org.goko.core.workspace.service;

import org.goko.core.common.exception.GkException;

/**
 * 
 * @author PsyKo
 * @date 10 févr. 2016
 */
public interface IExporter<I, O> {
	
	O export(I input, IMapperService mapperService) throws GkException;
	
	Class<O> getOutputClass();

	Class<I> getInputClass();
}

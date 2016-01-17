/**
 * 
 */
package org.goko.core.workspace.bean;

import org.goko.core.common.exception.GkException;

/**
 * @author PsyKo
 * @date 16 janv. 2016
 */
public interface IPropertiesPanel {

	void beforeDiscard() throws GkException;
	
	void discard() throws GkException;
}

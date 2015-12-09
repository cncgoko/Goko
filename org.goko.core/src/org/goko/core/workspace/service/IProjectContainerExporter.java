/**
 * 
 */
package org.goko.core.workspace.service;

import org.goko.core.common.exception.GkException;
import org.goko.core.workspace.io.XmlProjectContainer;

/**
 * @author PsyKo
 * @date 9 d�c. 2015
 */
public interface IProjectContainerExporter {

	boolean isDirty();
	
	void export(XmlProjectContainer container) throws GkException;
	
	String getProjectContainerType();
}

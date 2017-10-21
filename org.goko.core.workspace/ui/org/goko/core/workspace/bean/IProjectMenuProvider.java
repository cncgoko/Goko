/**
 * 
 */
package org.goko.core.workspace.bean;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.ISelection;
import org.goko.core.common.exception.GkException;

/**
 * @author Psyko
 * @date 21 oct. 2017
 */
public interface IProjectMenuProvider {

	boolean providesMenuFor(ISelection selection) throws GkException;
	
	void createMenuFor(IMenuManager contextMenu, ISelection selection) throws GkException;
	
}

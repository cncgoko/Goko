/**
 * 
 */
package org.goko.tools.editor.component.provider;

import javax.swing.ProgressMonitor;

import org.eclipse.jface.text.IDocument;
import org.goko.core.common.exception.GkException;

/**
 * @author Psyko
 * @date 25 mai 2016
 */
public interface IDocumentProvider {
		
	boolean isModifiable() throws GkException;
	
	void saveDocument(ProgressMonitor monitor) throws GkException;
	
	IDocument getDocument() throws GkException;
	
	String getDocumentName();
	
	boolean isDirty();
	
	void setDirty(boolean dirty);
	
	void addDocumentProviderListener(IDocumentProviderListener listener);
}

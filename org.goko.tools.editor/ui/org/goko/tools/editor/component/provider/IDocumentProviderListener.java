/**
 * 
 */
package org.goko.tools.editor.component.provider;

/**
 * @author Psyko
 * @date 28 mai 2016
 */
public interface IDocumentProviderListener {
	
	/**
	 * Notify that this document is about to be closed from the model domain
	 * @param provider the provider
	 */
	void aboutToClose(IDocumentProvider provider);
	
	/**
	 * Notify that this document was closed from the model domain
	 * @param provider the provider
	 */
	void onClosed(IDocumentProvider provider);
	
	void onDirtyChanged(IDocumentProvider provider);
	
	void onModifiableChanged(IDocumentProvider provider);
}

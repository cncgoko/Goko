/**
 * 
 */
package org.goko.tools.editor.component.provider;

/**
 * @author Psyko
 * @date 28 mai 2016
 */
public class DocumentProviderAdapter implements IDocumentProviderListener{

	/** (inheritDoc)
	 * @see org.goko.tools.editor.component.provider.IDocumentProviderListener#onDirtyChanged(org.goko.tools.editor.component.provider.IDocumentProvider)
	 */
	@Override
	public void onDirtyChanged(IDocumentProvider provider) { }

	/** (inheritDoc)
	 * @see org.goko.tools.editor.component.provider.IDocumentProviderListener#onModifiableChanged(org.goko.tools.editor.component.provider.IDocumentProvider)
	 */
	@Override
	public void onModifiableChanged(IDocumentProvider provider) { }
	
	/** (inheritDoc)
	 * @see org.goko.tools.editor.component.provider.IDocumentProviderListener#aboutToClose(org.goko.tools.editor.component.provider.IDocumentProvider)
	 */
	@Override
	public void aboutToClose(IDocumentProvider provider) { }
	
	/** (inheritDoc)
	 * @see org.goko.tools.editor.component.provider.IDocumentProviderListener#onClosed(org.goko.tools.editor.component.provider.IDocumentProvider)
	 */
	@Override
	public void onClosed(IDocumentProvider provider) { }
}

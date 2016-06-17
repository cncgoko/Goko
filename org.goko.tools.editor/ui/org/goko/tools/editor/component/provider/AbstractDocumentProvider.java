/**
 * 
 */
package org.goko.tools.editor.component.provider;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.ProgressMonitor;

import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.goko.core.common.exception.GkException;

/**
 * @author Psyko
 * @date 26 mai 2016
 */
public abstract class AbstractDocumentProvider implements IDocumentProvider, IDocumentListener{
	/** The cached document */
	private IDocument document;
	/** Dirty state of the document */
	private boolean dirty;
	/** The list of listeners */
	private List<IDocumentProviderListener> documentProviderListeners;
	
	/**
	 * 
	 */
	public AbstractDocumentProvider() {
		documentProviderListeners = new CopyOnWriteArrayList<IDocumentProviderListener>();
	}
	/** (inheritDoc)
	 * @see org.goko.tools.editor.component.provider.IDocumentProvider#getDocument()
	 */
	@Override
	public IDocument getDocument() throws GkException {
		if(document == null){
			document = performGetDocument();
			document.addDocumentListener(this);
		}
		return document;
	}
	
	/** (inheritDoc)
	 * @see org.goko.tools.editor.component.provider.IDocumentProvider#saveDocument(javax.swing.ProgressMonitor)
	 */
	@Override
	public void saveDocument(ProgressMonitor monitor) throws GkException {
		performSaveDocument(monitor);		
		setDirty(false);
	}
	
	protected abstract IDocument performGetDocument() throws GkException;
	
	protected abstract void performSaveDocument(ProgressMonitor monitor) throws GkException;

	/** (inheritDoc)
	 * @see org.goko.tools.editor.component.provider.IDocumentProvider#addDocumentProviderListener(org.goko.tools.editor.component.provider.IDocumentProviderListener)
	 */
	@Override
	public void addDocumentProviderListener(IDocumentProviderListener listener) {
		if(!documentProviderListeners.contains(listener)){
			documentProviderListeners.add(listener);
		}
	}

	protected void notifyAboutToClose(){
		for (IDocumentProviderListener listener : documentProviderListeners) {
			listener.aboutToClose(this);
		}
	}
	
	protected void notifyClosed(){
		for (IDocumentProviderListener listener : documentProviderListeners) {
			listener.onClosed(this);
		}
	}
	
	protected void notifyDirtyChanged(){
		for (IDocumentProviderListener listener : documentProviderListeners) {
			listener.onDirtyChanged(this);
		}
	}
	
	protected void notifyModifiableChanged(){
		for (IDocumentProviderListener listener : documentProviderListeners) {
			listener.onModifiableChanged(this);
		}
	}
	/** (inheritDoc)
	 * @see org.eclipse.jface.text.IDocumentListener#documentAboutToBeChanged(org.eclipse.jface.text.DocumentEvent)
	 */
	@Override
	public void documentAboutToBeChanged(DocumentEvent event) { }
	
	/** (inheritDoc)
	 * @see org.eclipse.jface.text.IDocumentListener#documentChanged(org.eclipse.jface.text.DocumentEvent)
	 */
	@Override
	public void documentChanged(DocumentEvent event) {
		setDirty(true);		
	}

	/** (inheritDoc)
	 * @see org.goko.tools.editor.component.provider.IDocumentProvider#getAnnotationModel()
	 */
	@Override
	public IAnnotationModel getAnnotationModel() throws GkException{		
		return null;
	}
	/**
	 * @return the dirty
	 */
	public boolean isDirty() {
		return dirty;
	}

	/**
	 * @param dirty the dirty to set
	 */
	public void setDirty(boolean dirty) {
		boolean notify = (this.dirty != dirty);
		this.dirty = dirty;
		if(notify){
			notifyDirtyChanged();
		}
	}
	
}

/**
 * 
 */
package org.goko.tools.editor.component.provider;

import java.util.Iterator;

import javax.swing.ProgressMonitor;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.AnnotationModel;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.service.GCodeProviderDeleteEvent;
import org.goko.core.gcode.service.IGCodeProviderDeleteVetoableListener;
import org.goko.core.gcode.service.IGCodeProviderRepositoryListener;
import org.goko.tools.editor.component.GCodePartitionScanner;
import org.goko.tools.editor.component.GCodeSourceConfiguration;

/**
 * @author Psyko
 * @date 26 mai 2016
 */
public abstract class AbstractGCodeDocumentProvider extends AbstractDocumentProvider implements IGCodeProviderDeleteVetoableListener, IGCodeProviderRepositoryListener{
	/** The associated annotation model */
	private IAnnotationModel annotationModel;
	
	/** (inheritDoc)
	 * @see org.goko.tools.editor.component.provider.AbstractDocumentProvider#performGetDocument()
	 */
	@Override
	protected IDocument performGetDocument() throws GkException {
		IDocument document = getGCodeDocument();
		
		FastPartitioner partitioner = new FastPartitioner(new GCodePartitionScanner(), GCodeSourceConfiguration.CONTENT_TYPES);
		partitioner.connect(document);
		document.setDocumentPartitioner(partitioner);
		
		return document;
	}
	
	/** (inheritDoc)
	 * @see org.goko.tools.editor.component.provider.AbstractDocumentProvider#getAnnotationModel()
	 */
	@Override
	public IAnnotationModel getAnnotationModel() throws GkException{		
		if(annotationModel == null){
			annotationModel = new AnnotationModel();
			addAnnotations(annotationModel);
		}
		return annotationModel;
	}
	
	protected void addAnnotations(IAnnotationModel annotationModel) throws GkException{
		
	}
	
	/** (inheritDoc)
	 * @see org.goko.tools.editor.component.provider.AbstractDocumentProvider#saveDocument(javax.swing.ProgressMonitor)
	 */
	@Override
	public void saveDocument(ProgressMonitor monitor) throws GkException {		
		super.saveDocument(monitor);
		Iterator<Annotation> iter = annotationModel.getAnnotationIterator();
		while (iter.hasNext()) {
			Annotation annotation = (Annotation) iter.next();
			annotationModel.removeAnnotation(annotation);			
		}
		addAnnotations(annotationModel);
	}

	protected abstract IDocument getGCodeDocument() throws GkException;
		
	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeProviderDeleteVetoableListener#beforeDelete(org.goko.core.gcode.service.GCodeProviderDeleteEvent)
	 */
	@Override
	public void beforeDelete(GCodeProviderDeleteEvent event) {
		notifyAboutToClose();
		event.setDoIt(!isDirty());	
	}
	
}

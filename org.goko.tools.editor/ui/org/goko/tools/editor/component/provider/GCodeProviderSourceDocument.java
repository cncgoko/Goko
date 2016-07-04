/**
 * 
 */
package org.goko.tools.editor.component.provider;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.swing.ProgressMonitor;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkTechnicalException;
import org.goko.core.gcode.element.IGCodeProvider;
import org.goko.core.gcode.element.IGCodeProviderSource;
import org.goko.core.gcode.element.validation.IValidationElement;
import org.goko.core.gcode.service.IGCodeProviderRepository;
import org.goko.core.log.GkLog;
import org.goko.tools.editor.component.annotation.ErrorAnnotation;

/**
 * @author Psyko
 * @date 26 mai 2016
 */
public class GCodeProviderSourceDocument extends AbstractGCodeDocumentProvider {
	private static final GkLog LOG = GkLog.getLogger(GCodeProviderSourceDocument.class);
	private IGCodeProvider provider;
	private IGCodeProviderRepository gcodeRepository;
	
	/**
	 * @param source
	 */
	public GCodeProviderSourceDocument(IGCodeProviderRepository gcodeRepository, IGCodeProvider provider) {
		super();
		this.provider = provider;
		this.gcodeRepository = gcodeRepository;
		try {					
			this.gcodeRepository.addDeleteVetoableListener(this);
			this.gcodeRepository.addListener(this);
		} catch (GkException e) {
			LOG.error(e);
		}
	}

	/** (inheritDoc)
	 * @see org.goko.tools.editor.component.provider.IDocumentProvider#isModifiable()
	 */
	@Override
	public boolean isModifiable() throws GkException {		
		return getSource().canWrite() && !provider.isLocked();
	}

	/** (inheritDoc)
	 * @see org.goko.tools.editor.component.provider.IDocumentProvider#saveDocument(javax.swing.ProgressMonitor)
	 */
	@Override
	public void performSaveDocument(ProgressMonitor monitor) throws GkException {
		if(isModifiable()){			
			getSource().write(IOUtils.toInputStream(getDocument().get()));		
		}
	}
	
	/** (inheritDoc)
	 * @see org.goko.tools.editor.component.provider.AbstractGCodeDocumentProvider#addAnnotations(org.eclipse.jface.text.source.IAnnotationModel)
	 */
	@Override
	protected void addAnnotations(IAnnotationModel annotationModel) throws GkException {
		List<IValidationElement> elements = provider.getValidationElements();
		if(CollectionUtils.isNotEmpty(elements)){
			for (IValidationElement elt : elements) {
				ErrorAnnotation error = new ErrorAnnotation(elt.getDescription());
				int lineOffset = 0;
				try {
					lineOffset = getDocument().getLineOffset(elt.getLocation().getLine());
				} catch (BadLocationException e) {					
				}
				Position position = new Position(lineOffset + elt.getLocation().getColumn(), elt.getLength());
				annotationModel.addAnnotation(error, position);
			}
		}
	}
	/** (inheritDoc)
	 * @see org.goko.tools.editor.component.provider.AbstractGCodeDocumentProvider#getGCodeDocument()
	 */
	@Override
	public IDocument getGCodeDocument() throws GkException {		
		if(getSource() != null){
			InputStream inputStream = getSource().openInputStream();
			try {
				IDocument document = new Document(IOUtils.toString(inputStream));
				return document;
			} catch (IOException e) {
				throw new GkTechnicalException(e);
			}
		}		
		return null;
	}

	/** (inheritDoc)
	 * @see org.goko.tools.editor.component.provider.IDocumentProvider#getDocumentName()
	 */
	@Override
	public String getDocumentName() {		
		return provider.getCode();
	}
	
	/**
	 * Utility getter on the source of the provider
	 * @return IGCodeProviderSource 
	 * @throws GkException GkException
	 */
	private IGCodeProviderSource getSource() throws GkException {		
		return provider.getSource();
	}	
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeProviderRepositoryListener#onGCodeProviderCreate(org.goko.core.gcode.element.IGCodeProvider)
	 */
	@Override
	public void onGCodeProviderCreate(IGCodeProvider provider) throws GkException { }
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeProviderRepositoryListener#afterGCodeProviderDelete(org.goko.core.gcode.element.IGCodeProvider)
	 */
	@Override
	public void afterGCodeProviderDelete(IGCodeProvider provider) throws GkException {
		this.gcodeRepository.removeDeleteVetoableListener(this);
		this.gcodeRepository.removeListener(this);
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeProviderRepositoryListener#beforeGCodeProviderDelete(org.goko.core.gcode.element.IGCodeProvider)
	 */
	@Override
	public void beforeGCodeProviderDelete(IGCodeProvider provider) throws GkException {
		if(ObjectUtils.equals(provider.getId(),  this.provider.getId())){
			notifyClosed();
		}		
	}
		
	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeProviderRepositoryListener#onGCodeProviderLocked(org.goko.core.gcode.element.IGCodeProvider)
	 */
	@Override
	public void onGCodeProviderLocked(IGCodeProvider provider) throws GkException {
		notifyModifiableChanged();
	}
	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeProviderRepositoryListener#onGCodeProviderUnlocked(org.goko.core.gcode.element.IGCodeProvider)
	 */
	@Override
	public void onGCodeProviderUnlocked(IGCodeProvider provider) throws GkException {
		notifyModifiableChanged();
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeProviderRepositoryListener#onGCodeProviderUpdate(org.goko.core.gcode.element.IGCodeProvider)
	 */
	@Override
	public void onGCodeProviderUpdate(IGCodeProvider provider) throws GkException { }

	/** (inheritDoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((provider == null) ? 0 : provider.hashCode());
		return result;
	}

	/** (inheritDoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GCodeProviderSourceDocument other = (GCodeProviderSourceDocument) obj;
		if (provider == null) {
			if (other.provider != null)
				return false;
		} else if (!provider.equals(other.provider))
			return false;
		return true;
	}
	
}

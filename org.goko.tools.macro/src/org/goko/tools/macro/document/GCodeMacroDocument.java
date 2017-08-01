/**
 * 
 */
package org.goko.tools.macro.document;

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
import org.goko.core.gcode.element.validation.ValidationResult;
import org.goko.core.gcode.service.IGCodeValidationService;
import org.goko.core.log.GkLog;
import org.goko.tools.editor.component.annotation.ErrorAnnotation;
import org.goko.tools.editor.component.provider.AbstractGCodeDocumentProvider;
import org.goko.tools.macro.bean.GCodeMacro;
import org.goko.tools.macro.service.DefaultGCodeMacroService;


public class GCodeMacroDocument extends AbstractGCodeDocumentProvider {
	private static final GkLog LOG = GkLog.getLogger(GCodeMacroDocument.class);
	private DefaultGCodeMacroService macroService;
	private GCodeMacro macro;
	private IGCodeProvider provider;
	/** Validation service */
	private IGCodeValidationService<?,?,?> gcodeValidationService;
	
	/**
	 * @param macroService
	 * @param macro
	 * @param provider
	 */
	public GCodeMacroDocument(DefaultGCodeMacroService macroService, IGCodeValidationService<?,?,?> gcodeValidationService, GCodeMacro macro, IGCodeProvider provider) {
		super();
		this.macroService = macroService;
		this.macro = macro;
		this.provider = provider;
		this.gcodeValidationService = gcodeValidationService;
		try {
			macroService.addDeleteVetoableListener(this);
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
			// Notify the macro service
			macroService.updateGCodeMacro(macro);
		}
	}
	
	/** (inheritDoc)
	 * @see org.goko.tools.editor.component.provider.AbstractGCodeDocumentProvider#addAnnotations(org.eclipse.jface.text.source.IAnnotationModel)
	 */
	@Override
	protected void addAnnotations(IAnnotationModel annotationModel) throws GkException {
		ValidationResult result = gcodeValidationService.getValidationResult(provider.getId());
		List<IValidationElement> elements = result.getElements();
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
			}finally {
				IOUtils.closeQuietly(inputStream);
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
	public void afterGCodeProviderDelete(IGCodeProvider provider) throws GkException { }
	
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
	
	
}

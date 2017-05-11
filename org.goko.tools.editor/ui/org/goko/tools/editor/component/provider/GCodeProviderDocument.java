/**
 * 
 */
package org.goko.tools.editor.component.provider;

import java.util.List;

import javax.swing.ProgressMonitor;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.element.GCodeLine;
import org.goko.core.gcode.element.IGCodeProvider;
import org.goko.core.gcode.element.validation.IValidationElement;
import org.goko.core.gcode.rs274ngcv3.IRS274NGCService;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.InstructionProvider;
import org.goko.core.gcode.service.IGCodeProviderRepository;
import org.goko.core.log.GkLog;
import org.goko.tools.editor.component.annotation.ErrorAnnotation;

/**
 * @author Psyko
 * @date 26 mai 2016
 */
public class GCodeProviderDocument extends AbstractGCodeDocumentProvider {
	private static final GkLog LOG = GkLog.getLogger(GCodeProviderDocument.class);
	private IGCodeProvider provider;
	private IGCodeProviderRepository gcodeRepository;
	private IRS274NGCService gcodeService;
	private Document document;
	
	/**
	 * @param source
	 */
	public GCodeProviderDocument(IGCodeProviderRepository gcodeRepository, IRS274NGCService gcodeService, IGCodeProvider provider) {
		super();
		this.provider = provider;
		this.gcodeRepository = gcodeRepository;
		this.gcodeService = gcodeService;
		try {					
			this.gcodeRepository.addDeleteVetoableListener(this);
			this.gcodeRepository.addListener(this);
			getGCodeDocument();
		} catch (GkException e) {
			LOG.error(e);
		}
	}

	/** (inheritDoc)
	 * @see org.goko.tools.editor.component.provider.IDocumentProvider#isModifiable()
	 */
	@Override
	public boolean isModifiable() throws GkException {		
		return false;
	}

	/** (inheritDoc)
	 * @see org.goko.tools.editor.component.provider.IDocumentProvider#saveDocument(javax.swing.ProgressMonitor)
	 */
	@Override
	public void performSaveDocument(ProgressMonitor monitor) throws GkException {
		// Final document cannot be saved
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
		StringBuffer buffer = new StringBuffer();		
		GCodeContext context = new GCodeContext();
		InstructionProvider instructionProvider = gcodeService.getInstructions(context , provider);
		provider = gcodeService.getGCodeProvider(context , instructionProvider);
				
		List<GCodeLine> lines = provider.getLines();		
		for (GCodeLine gCodeLine : lines) {
			String strLine = gcodeService.render(gCodeLine);
			if(StringUtils.isNotEmpty(strLine)){
				buffer.append(strLine);
				buffer.append(System.lineSeparator());
			}
		}		
		if(document == null){
			document = new Document(buffer.toString());
		}else{
			document.set(buffer.toString());
		}
		return document;		
	}

	/** (inheritDoc)
	 * @see org.goko.tools.editor.component.provider.IDocumentProvider#getDocumentName()
	 */
	@Override
	public String getDocumentName() {		
		return "Rendered "+provider.getCode();
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
	 * @see org.goko.tools.editor.component.provider.AbstractDocumentProvider#isDirty()
	 */
	@Override
	public boolean isDirty() {
		return false;
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
	public void onGCodeProviderUpdate(IGCodeProvider provider) throws GkException {
		getGCodeDocument();
	}

	/** (inheritDoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 41;
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
		GCodeProviderDocument other = (GCodeProviderDocument) obj;
		if (provider == null) {
			if (other.provider != null)
				return false;
		} else if (!provider.equals(other.provider))
			return false;
		return true;
	}
	
}

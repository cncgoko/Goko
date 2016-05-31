/**
 * 
 */
package org.goko.tools.editor.component.provider;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.service.GCodeProviderDeleteEvent;
import org.goko.core.gcode.service.IGCodeProviderDeleteListener;
import org.goko.core.gcode.service.IGCodeProviderRepositoryListener;
import org.goko.tools.editor.component.GCodePartitionScanner;
import org.goko.tools.editor.component.GCodeSourceConfiguration;

/**
 * @author Psyko
 * @date 26 mai 2016
 */
public abstract class AbstractGCodeDocumentProvider extends AbstractDocumentProvider implements IGCodeProviderDeleteListener, IGCodeProviderRepositoryListener{

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
	
	protected abstract IDocument getGCodeDocument() throws GkException;
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeProviderDeleteListener#beforeDelete(org.goko.core.gcode.service.GCodeProviderDeleteEvent)
	 */
	@Override
	public void beforeDelete(GCodeProviderDeleteEvent event) {
		notifyAboutToClose();
		event.setDoIt(!isDirty());
	}
	
}

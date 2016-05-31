/**
 * 
 */
package org.goko.tools.editor.component.provider;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.ProgressMonitor;

import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.goko.core.common.exception.GkException;
import org.goko.core.log.GkLog;
import org.goko.tools.editor.component.GCodePartitionScanner;
import org.goko.tools.editor.component.GCodeSourceConfiguration;

/**
 * @author Psyko
 * @date 25 mai 2016
 */
public class FileDocumentProvider extends AbstractDocumentProvider implements IDocumentProvider {
	/** LOG */
	private static final GkLog LOG = GkLog.getLogger(FileDocumentProvider.class);
	/** The target file */
	private File file;

	/**
	 * @param file
	 */
	public FileDocumentProvider(File file) {
		super();
		this.file = file;
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
	protected void performSaveDocument(ProgressMonitor monitor) throws GkException {
		// TODO Auto-generated method stub

	}

	/** (inheritDoc)
	 * @see org.goko.tools.editor.component.provider.IDocumentProvider#getDocument()
	 */
	@Override
	public IDocument performGetDocument() throws GkException {		
		Scanner fileScanner = null;
		IDocument localDocument = null;
		try {
			fileScanner = new Scanner(file);
			StringBuffer buffer = new StringBuffer();
			while(fileScanner.hasNextLine()){
				buffer.append(fileScanner.nextLine());
				buffer.append(System.lineSeparator());
			}
			localDocument = new Document(buffer.toString());
			FastPartitioner partitioner = new FastPartitioner(new GCodePartitionScanner(), GCodeSourceConfiguration.CONTENT_TYPES);
			partitioner.connect(localDocument);
			localDocument.setDocumentPartitioner(partitioner);
		} catch (FileNotFoundException e) {
			LOG.error(e);
		}finally {
			if(fileScanner != null){
				fileScanner.close();
			}
		}	
		
		return localDocument;
	}

	/** (inheritDoc)
	 * @see org.goko.tools.editor.component.provider.IDocumentProvider#getDocumentName()
	 */
	@Override
	public String getDocumentName() {
		return file.getName();
	}

}

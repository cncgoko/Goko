 
package org.goko.tools.editor;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.goko.tools.editor.component.GCodePartitionScanner;
import org.goko.tools.editor.component.GCodeSourceConfiguration;
import org.goko.tools.editor.component.GCodeSourceViewer;

/**
 * Part for the GCode editor
 * 
 * @author Psyko
 * @date 23 mai 2016
 */
public class GCodeEditorPart {
	/** Main tab folder */
	private TabFolder mainTabFolder;
	
	@Inject
	public GCodeEditorPart() {
		
	}
	
	@PostConstruct
	public void postConstruct(Composite parent) throws FileNotFoundException {		
		mainTabFolder = new TabFolder(parent, SWT.NONE);
		TabItem tab = new TabItem(mainTabFolder, SWT.NONE);
		
		GCodeSourceViewer viewer = new GCodeSourceViewer(mainTabFolder, SWT.V_SCROLL | SWT.H_SCROLL);
		tab.setControl(viewer.getControl());
		
		File sourceFile = new File("C:/Users/Psyko/Documents/GCode/autolevel.nc");
		Scanner fileIn = new Scanner(sourceFile);
		StringBuffer buffer = new StringBuffer();
		while(fileIn.hasNextLine()){
			buffer.append(fileIn.nextLine());
			buffer.append(System.lineSeparator());
		}
		fileIn.close();
		// http://beuss.developpez.com/tutoriels/eclipse/plug-in/editor/colors/#LIV-B-2
		IDocument document = new Document(buffer.toString());		
		
		viewer.setDocument(document);
		FastPartitioner partitioner = new FastPartitioner(new GCodePartitionScanner(), GCodeSourceConfiguration.CONTENT_TYPES);
		partitioner.connect(document);
		document.setDocumentPartitioner(partitioner);
	}
	
}
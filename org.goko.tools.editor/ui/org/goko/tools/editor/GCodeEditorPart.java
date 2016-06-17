 
package org.goko.tools.editor;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.e4.ui.workbench.UIEvents;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IFindReplaceTarget;
import org.eclipse.jface.text.IUndoManager;
import org.eclipse.jface.text.source.IAnnotationAccess;
import org.eclipse.jface.text.source.IOverviewRuler;
import org.eclipse.jface.text.source.ISharedTextColors;
import org.eclipse.jface.text.source.OverviewRuler;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabFolder2Adapter;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.wb.swt.ResourceManager;
import org.goko.common.dialog.GkDialog;
import org.goko.core.common.exception.GkException;
import org.goko.tools.editor.component.GCodeSourceViewer;
import org.goko.tools.editor.component.annotation.BasicAnnotationAccess;
import org.goko.tools.editor.component.provider.DocumentProviderAdapter;
import org.goko.tools.editor.component.provider.IDocumentProvider;
import org.goko.tools.editor.component.provider.IDocumentProviderListener;

/**
 * Part for the GCode editor
 * 
 * @author Psyko
 * @date 23 mai 2016
 */
public class GCodeEditorPart{
	/** Main tab folder */
	private CTabFolder mainTabFolder;
	/** Map of Tab item for each IDocumentProvider */
	private Map<IDocumentProvider, CTabItem> mapTabItemByDocumentProvider;
	/** Map of SourceViewer for each IDocumentProvider */
	private Map<IDocumentProvider, GCodeSourceViewer> mapSourceViewerByDocumentProvider;
	/** UI synchronize */
	@Inject
	private UISynchronize uiSynchronize;
	@Inject
	private IEventBroker eventBroker;
	/** Supplier for IFindReplaceTarget */
	private Supplier<IFindReplaceTarget> findReplaceTargetSupplier;
	
	@Inject
	public GCodeEditorPart(IEclipseContext context) {
		this.mapTabItemByDocumentProvider = new HashMap<IDocumentProvider, CTabItem>();
		this.mapSourceViewerByDocumentProvider = new HashMap<IDocumentProvider, GCodeSourceViewer>();
		this.createFindReplaceTargetSupplier();
		context.set(GCodeEditorPart.class, this);
		
	}
	
	@PostConstruct
	public void postConstruct(Composite parent) throws FileNotFoundException {		
		mainTabFolder = new CTabFolder(parent, SWT.FLAT);
		mainTabFolder.addCTabFolder2Listener(new TabItemCloseListener());
	}

	@Inject
	@Optional
	public void onOpenRequest(@UIEventTopic(GCodeEditorTopic.TOPIC_OPEN_PART_EDITOR) IDocumentProvider provider) throws GkException{
		
		CTabItem targetTab = mapTabItemByDocumentProvider.get(provider);
		
		if(targetTab == null){			
			targetTab = new CTabItem(mainTabFolder, SWT.CLOSE);
			targetTab.setText(provider.getDocumentName());
			targetTab.setImage(ResourceManager.getPluginImage("org.goko.tools.editor", "resources/icons/document-attribute-g.png"));
			targetTab.setData(provider);
			
			ISharedTextColors sharedColors = new ISharedTextColors(){

				@Override
				public Color getColor(RGB rgb) {				
					return ResourceManager.getColor(rgb);
				}

				@Override
				public void dispose() {
					ResourceManager.dispose();
				}
				
			};
			IAnnotationAccess access = (IAnnotationAccess) new BasicAnnotationAccess();
			IOverviewRuler overviewRuler = new OverviewRuler(access, 12, sharedColors);
			
			GCodeSourceViewer viewer = new GCodeSourceViewer(mainTabFolder, overviewRuler, access, SWT.V_SCROLL | SWT.H_SCROLL);
			mapSourceViewerByDocumentProvider.put(provider, viewer);
			IDocument document = provider.getDocument();
			//viewer.setDocument(document);
			viewer.setDocumentProvider(provider);
		//	viewer.setEditable(provider.isModifiable());			
			targetTab.setControl(viewer.getControl());
			final CTabItem finalTargetTab = targetTab;		
		
			IDocumentProviderListener listener = new DocumentProviderAdapter() {				
				@Override
				public void onDirtyChanged(IDocumentProvider provider) {
					if(provider.isDirty()){
						finalTargetTab.setText(provider.getDocumentName()+"*");	
					}else{
						finalTargetTab.setText(provider.getDocumentName());
					}
					forceHandlerUpdate();
				}
				
				/** (inheritDoc)
				 * @see org.goko.tools.editor.component.provider.DocumentProviderAdapter#aboutToClose(org.goko.tools.editor.component.provider.IDocumentProvider)
				 */
				@Override
				public void aboutToClose(IDocumentProvider provider) {
					askForSave(getActiveDocumentProvider());					
				}
				
				/** (inheritDoc)
				 * @see org.goko.tools.editor.component.provider.DocumentProviderAdapter#onClosed(org.goko.tools.editor.component.provider.IDocumentProvider)
				 */
				@Override
				public void onClosed(IDocumentProvider provider) {
					closeByDocumentProvider(provider);				
				}
			};
			provider.addDocumentProviderListener(listener);			
			mapTabItemByDocumentProvider.put(provider, targetTab);
		}
		mainTabFolder.setSelection(targetTab);
		forceHandlerUpdate();
	}
	
	public void closeByDocumentProvider(IDocumentProvider provider) {
		mapSourceViewerByDocumentProvider.remove(provider);	
		CTabItem tabItem = mapTabItemByDocumentProvider.remove(provider);
		tabItem.dispose();	
	}
			
	public IDocumentProvider getActiveDocumentProvider(){
		CTabItem currentTab = mainTabFolder.getSelection();
		if(currentTab == null){
			return null;
		}
		return getDocumentProvider(currentTab);
	}
	
	public IDocumentProvider getDocumentProvider(CTabItem tab){		
		IDocumentProvider provider = (IDocumentProvider) tab.getData();
		return provider;
	}
	
	public IFindReplaceTarget getFindReplaceTarget(){
		if(getActiveDocumentProvider() != null){
			GCodeSourceViewer sourceViewer = mapSourceViewerByDocumentProvider.get(getActiveDocumentProvider());		
			return sourceViewer.getFindReplaceTarget();
		}
		return null;
	}
	
	public IUndoManager getUndoManager(){
		if(getActiveDocumentProvider() != null){
			GCodeSourceViewer sourceViewer = mapSourceViewerByDocumentProvider.get(getActiveDocumentProvider());		
			return sourceViewer.getUndoManager();
		}
		return null;
	}

	public void saveActiveDocument(){		
		IDocumentProvider provider = getActiveDocumentProvider();
		try {
			provider.saveDocument(null);
		} catch (GkException e) {
			GkDialog.openDialog(e);
		}		
	}
	
	public boolean isAnyUnsavedDocument(){
		Set<IDocumentProvider> providerSet = mapTabItemByDocumentProvider.keySet();
		if(CollectionUtils.isNotEmpty(providerSet)){
			for (IDocumentProvider documentProvider : providerSet) {
				if (documentProvider.isDirty()) {
					return true;
				}
			}	
		}
		return false;
	}
	
	public void saveAllDocument(){
		Set<IDocumentProvider> providerSet = mapTabItemByDocumentProvider.keySet();
		if(CollectionUtils.isNotEmpty(providerSet)){
			for (IDocumentProvider documentProvider : providerSet) {
				try {			
					documentProvider.saveDocument(null);
				} catch (GkException e) {
					GkDialog.openDialog(e);
				}	
			}				
		}
	}
	
	@Focus
	public void focus(){
		forceHandlerUpdate();
	}
	
	protected void forceHandlerUpdate(){	 
		eventBroker.send(UIEvents.REQUEST_ENABLEMENT_UPDATE_TOPIC, UIEvents.ALL_ELEMENT_ID);
	}
	
	protected void askForSave(IDocumentProvider provider){
		if(provider.isDirty()){
			uiSynchronize.syncExec(new Runnable() {
				
				@Override
				public void run() {
					MessageDialog saveDialog = new MessageDialog(null,
							"Save",
							null,
							"File '"+provider.getDocumentName()+"' has unsaved modification. Would you like to save them now ?",
							MessageDialog.QUESTION_WITH_CANCEL,
							new String[]{
								IDialogConstants.YES_LABEL,
								IDialogConstants.NO_LABEL,
								IDialogConstants.CANCEL_LABEL},
							0
							);
					int result = saveDialog.open();
					if(result == 0){ // YES Button
						saveActiveDocument();
					}else if(result == 1){ // NO button
						// Don't save but set document not dirty
						provider.setDirty(false);
					}					
				}
			});
		}	
	}
	/**
	 * Listener for tab closing event 
	 * @author Psyko
	 * @date 28 mai 2016
	 */
	class TabItemCloseListener extends CTabFolder2Adapter{		
		
		/** (inheritDoc)
		 * @see org.eclipse.swt.custom.CTabFolder2Adapter#close(org.eclipse.swt.custom.CTabFolderEvent)
		 */
		@Override
		public void close(CTabFolderEvent event) {
			IDocumentProvider provider = getActiveDocumentProvider();
			if(provider == null){
				return;
			}
			if(provider.isDirty()){				
				askForSave(provider);
				event.doit = !provider.isDirty();
			}
			if(event.doit){
				closeByDocumentProvider(provider);
			}
		}		
	}
	
	/**
	 * Creates the Supplier<IFindReplaceTarget> to make sure the search windows is always on the active document
	 */
	void createFindReplaceTargetSupplier(){
		findReplaceTargetSupplier = new Supplier<IFindReplaceTarget>() {
			
			@Override
			public IFindReplaceTarget get() {
				return getFindReplaceTarget();
			}
		};
	}

	/**
	 * @return the findReplaceTargetSupplier
	 */
	public Supplier<IFindReplaceTarget> getFindReplaceTargetSupplier() {
		return findReplaceTargetSupplier;
	}

	/**
	 * @param findReplaceTargetSupplier the findReplaceTargetSupplier to set
	 */
	public void setFindReplaceTargetSupplier(Supplier<IFindReplaceTarget> findReplaceTargetSupplier) {
		this.findReplaceTargetSupplier = findReplaceTargetSupplier;
	}
	
}
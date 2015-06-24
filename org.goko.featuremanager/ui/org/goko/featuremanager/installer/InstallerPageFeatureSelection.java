package org.goko.featuremanager.installer;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.goko.core.common.exception.GkException;
import org.goko.core.log.GkLog;
import org.goko.featuremanager.service.IFeatureManager;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.ResourceManager;

public class InstallerPageFeatureSelection extends WizardPage {
	private static final GkLog LOG = GkLog.getLogger(InstallerPageFeatureSelection.class);
	private IFeatureManager manager;
	private CheckboxTreeViewer checkboxTreeViewer;
	private InstallerWizardModel model;
	
	/**
	 * Create the wizard.
	 */
	public InstallerPageFeatureSelection(IFeatureManager manager, InstallerWizardModel model) {		
		super("featureSelection");	
		setImageDescriptor(ResourceManager.getPluginImageDescriptor("org.goko.featuremanager", "resources/icons/arrow-270.png"));
		this.model = model;
		this.manager = manager;
		setTitle("Available features");
		setDescription("Select the features to install");		
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		container.setLayout(new GridLayout(1, false));
		
		checkboxTreeViewer = new CheckboxTreeViewer(container, SWT.BORDER);
		Tree tree = checkboxTreeViewer.getTree();
		tree.setHeaderVisible(true);
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		TreeViewerColumn columnViewerName = new TreeViewerColumn(checkboxTreeViewer, SWT.NONE);
		TreeColumn trclmnName = columnViewerName.getColumn();
		trclmnName.setMoveable(true);
		trclmnName.setWidth(326);
		trclmnName.setText("Name");
		
		TreeViewerColumn columnViewerVersion = new TreeViewerColumn(checkboxTreeViewer, SWT.NONE);
		TreeColumn trclmnVersion = columnViewerVersion.getColumn();
		trclmnVersion.setMoveable(true);
		trclmnVersion.setWidth(100);
		trclmnVersion.setText("Version");
		
		Group grpDescription = new Group(container, SWT.NONE);
		grpDescription.setLayout(new GridLayout(1, false));
		grpDescription.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		grpDescription.setText("Description");
		
		final Label descriptionLabel = new Label(grpDescription, SWT.NONE);
		GridData gd_descriptionLabel = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_descriptionLabel.heightHint = 60;
		gd_descriptionLabel.minimumHeight = 50;
		descriptionLabel.setLayoutData(gd_descriptionLabel);
		
		checkboxTreeViewer.setContentProvider(new InstallableUnitContentProvider());
		columnViewerName.setLabelProvider(new InstallableUnitLabelProvider(0));
		columnViewerVersion.setLabelProvider(new InstallableUnitLabelProvider(1));
		checkboxTreeViewer.setCheckStateProvider(new InstallableUnitCheckStateProvider());
		checkboxTreeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				StructuredSelection s = (StructuredSelection) event.getSelection();
				if(s.getFirstElement() instanceof GkInstallableUnit){
		        	GkInstallableUnit unit = (GkInstallableUnit) s.getFirstElement();
		        	descriptionLabel.setText(unit.getDescription());
				}else{
					descriptionLabel.setText(StringUtils.EMPTY);
				}
				
			}
		});
		checkboxTreeViewer.addCheckStateListener(new ICheckStateListener() {			
			@Override
			public void checkStateChanged(CheckStateChangedEvent event) {				
				// If the item is checked . . .
		        if(event.getElement() instanceof GkInstallableUnit){
		        	GkInstallableUnit unit = (GkInstallableUnit) event.getElement();
		        	if(unit.isInstalled()){
		        		checkboxTreeViewer.setChecked(event.getElement(), true);		
		        	}else{
			        	if(event.getChecked()){
			        		model.getUnitsToInstall().add((GkInstallableUnit) event.getElement());
			        	}else{
			        		model.getUnitsToInstall().remove((GkInstallableUnit)event.getElement());
			        	}
			        	setPageComplete(CollectionUtils.isNotEmpty(model.getUnitsToInstall()));
		        	}
		        }
			}
		});		
		setPageComplete(false);
	}
	
	/** (inheritDoc)
	 * @see org.eclipse.jface.dialogs.DialogPage#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean visible) {		
		super.setVisible(visible);
		getShell().getDisplay().asyncExec(new Runnable() {			
			@Override
			public void run() {
				try {
					setContent(checkboxTreeViewer);						
					checkboxTreeViewer.expandAll();
				} catch (GkException e) {			
					LOG.error(e);
				}
			}
		});
		
	}

	/**
	 * Set the content of the tree 
	 * @param checkboxTreeViewer the CheckboxTreeViewer
	 * @throws GkException GkException
	 */
	protected void setContent(final CheckboxTreeViewer checkboxTreeViewer) throws GkException{
		try {
			getWizard().getContainer().run(false, false, new IRunnableWithProgress() {
				
				@Override
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
					List<GkInstallableUnit> lstUnits;
					try {						
						lstUnits = manager.getInstallableUnits(new NullProgressMonitor());					
						Map<String, GkInstallableUnitCategory> mapCategory = new HashMap<String, GkInstallableUnitCategory>();
						
						for (GkInstallableUnit gkInstallableUnit : lstUnits) {
							String category = gkInstallableUnit.getCategoryName();
							if(!mapCategory.containsKey(category)){
								mapCategory.put(category, new GkInstallableUnitCategory(category));
							}
							mapCategory.get(category).add(gkInstallableUnit);
						}
						
						checkboxTreeViewer.setInput(new ArrayList<GkInstallableUnitCategory>(mapCategory.values()));
					} catch (GkException e) {
						LOG.error(e);
					}
				}
			});
		} catch (InvocationTargetException e) {
			LOG.error(e);
		} catch (InterruptedException e) {
			LOG.error(e);
		}		
	}	
	
}

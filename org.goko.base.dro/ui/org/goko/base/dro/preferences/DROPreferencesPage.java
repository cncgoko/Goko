package org.goko.base.dro.preferences;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.eclipse.wb.swt.ResourceManager;
import org.goko.common.preferences.GkFieldEditorPreferencesPage;
import org.goko.common.preferences.fieldeditor.ObjectCollectionFieldEditor;
import org.goko.common.preferences.fieldeditor.objectcollection.CollectionObject;
import org.goko.core.common.exception.GkException;
import org.goko.core.controller.IControllerService;
import org.goko.core.controller.bean.MachineValueDefinition;
import org.goko.core.log.GkLog;

public class DROPreferencesPage extends GkFieldEditorPreferencesPage {
	private static final GkLog LOG = GkLog.getLogger(DROPreferencesPage.class);
	@Inject
	private IControllerService controller;
	
	public DROPreferencesPage() {
		setImageDescriptor(ResourceManager.getPluginImageDescriptor("org.goko.base.dro", "icons/compass.png"));
		setDescription("Configure the displayed values");
		setTitle("Digital read out");		
		setPreferenceStore(new ScopedPreferenceStore(ConfigurationScope.INSTANCE,"org.goko.base.droservice"));
	}

	/** (inheritDoc)
	 * @see org.goko.common.preferences.GkPreferencesPage#createPreferencePage(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void createPreferencePage(Composite parent) {
		GridLayout gridLayout = (GridLayout) parent.getLayout();
		gridLayout.verticalSpacing = 0;
		gridLayout.horizontalSpacing = 0;
		
		ObjectCollectionFieldEditor objectCollectionFieldEditor = new ObjectCollectionFieldEditor(parent, SWT.NONE);
		GridLayout gridLayout_1 = (GridLayout) objectCollectionFieldEditor.getLayout();
		gridLayout_1.marginHeight = 0;
		gridLayout_1.marginWidth = 0;
		objectCollectionFieldEditor.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		objectCollectionFieldEditor.setPreferenceName("dro.displayedValues.list");
		// TODO Auto-generated method stub
		addField(objectCollectionFieldEditor);
		try{
			List<MachineValueDefinition> lstValues = controller.getMachineValueDefinition();
			List<CollectionObject> lstObject = new ArrayList<CollectionObject>();
			for (MachineValueDefinition def : lstValues) {
				lstObject.add( new CollectionObject(def.getName(), def.getId(), def.getDescription()));
			}
			objectCollectionFieldEditor.setAvailableObjects(lstObject);
		}catch(GkException e){
			LOG.error(e);
		}
	}
	
	

}

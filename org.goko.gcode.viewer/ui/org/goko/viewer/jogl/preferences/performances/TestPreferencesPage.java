package org.goko.viewer.jogl.preferences.performances;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.goko.common.preferences.GkFieldEditorPreferencesPage;
import org.goko.common.preferences.fieldeditor.objectcollection.CollectionObject;
import org.goko.common.preferences.fieldeditor.preference.BigDecimalFieldEditor;
import org.goko.common.preferences.fieldeditor.preference.BooleanFieldEditor;
import org.goko.common.preferences.fieldeditor.preference.ColorFieldEditor;
import org.goko.common.preferences.fieldeditor.preference.ComboFieldEditor;
import org.goko.common.preferences.fieldeditor.preference.ObjectCollectionFieldEditor;
import org.goko.common.preferences.fieldeditor.preference.QuantityFieldEditor;
import org.goko.common.preferences.fieldeditor.preference.StringFieldEditor;
import org.goko.core.common.measure.quantity.Length;
import org.goko.viewer.jogl.preferences.JoglViewerPreference;

public class TestPreferencesPage extends GkFieldEditorPreferencesPage{
	private Text text;
	private Text text_1;
	private Group grpPerformances;
	
	public TestPreferencesPage() {	
		setPreferenceStore(JoglViewerPreference.getInstance().getPreferenceStore());
	}

	@Override
	protected void createPreferencePage(Composite parent) {		
		grpPerformances = new Group(parent, SWT.NONE);
		grpPerformances.setLayout(new GridLayout(2, false));
		grpPerformances.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		grpPerformances.setText("Performances");		
		
		StringFieldEditor fieldEditor = new StringFieldEditor(grpPerformances, SWT.NONE);
		fieldEditor.setErrorMessage("A valid string is required");
		fieldEditor.setWidthInChars(10);
		fieldEditor.setEmptyStringAllowed(false);
		fieldEditor.setLabel("Option 1 :");
		fieldEditor.setPreferenceName("grid.majorSpacing");
		fieldEditor.pack();
		addField(fieldEditor);
		fieldEditor.setErrorMessage("A valid number is required");
		
		text_1 = new Text(grpPerformances, SWT.BORDER | SWT.RIGHT);
		text_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Composite composite_1 = new Composite(parent, SWT.NONE);
		composite_1.setLayout(new GridLayout(2, false));
		
		Label lblNewLabel = new Label(composite_1, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setBounds(0, 0, 55, 15);
		lblNewLabel.setText("New Label");
		
		text = new Text(composite_1, SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		BooleanFieldEditor booleanFieldEditor = new BooleanFieldEditor(parent, SWT.NONE);
		
		ComboViewer comboViewer = new ComboViewer(booleanFieldEditor, SWT.NONE);
		Combo combo = comboViewer.getCombo();
		combo.setBounds(0, 0, 91, 23);
		
		BigDecimalFieldEditor bigDecimalFieldEditor = new BigDecimalFieldEditor(parent, SWT.NONE);
		bigDecimalFieldEditor.setLabel("Big decimal field");
		bigDecimalFieldEditor.setPreferenceName("grid.majorSpacing");
		bigDecimalFieldEditor.setWidthInChars(10);
		addField(bigDecimalFieldEditor);
		
		QuantityFieldEditor<Length> quantityFieldEditor = new QuantityFieldEditor<Length>(parent, SWT.NONE);
		quantityFieldEditor.setWidthInChars(8);
		bigDecimalFieldEditor.setPreferenceName("grid.majorSpacing");
		addField(quantityFieldEditor);
		
		ComboFieldEditor comboFieldEditor = new ComboFieldEditor(parent, SWT.NONE);
		bigDecimalFieldEditor.setPreferenceName("grid.majorSpacing");
		addField(quantityFieldEditor);
		
		ColorFieldEditor colorFieldEditor = new ColorFieldEditor(parent, SWT.NONE);
		
		ObjectCollectionFieldEditor objectCollectionFieldEditor = new ObjectCollectionFieldEditor(parent, SWT.NONE);
		objectCollectionFieldEditor.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		List<CollectionObject> selectObjects = new ArrayList<CollectionObject>();		
		selectObjects.add(new CollectionObject("salut","test","description"));
		
		List<CollectionObject> availableObjects = new ArrayList<CollectionObject>();		
		availableObjects.add(new CollectionObject("salut","test","description"));
		availableObjects.add(new CollectionObject("Label 1","retete","description du label 1"));
		availableObjects.add(new CollectionObject("Label 2","tratata","description du label 2"));
				
		objectCollectionFieldEditor.setSelectedObjects(selectObjects);
		objectCollectionFieldEditor.setAvailableObjects(availableObjects);
		
	}
}

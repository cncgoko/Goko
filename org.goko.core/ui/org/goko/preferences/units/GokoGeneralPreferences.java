package org.goko.preferences.units;

import java.util.List;

import javax.inject.Inject;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.goko.common.preferences.GkFieldEditorPreferencesPage;
import org.goko.common.preferences.fieldeditor.preference.ComboFieldEditor;
import org.goko.core.config.GokoPreference;
import org.goko.core.feature.IFeatureSetManager;
import org.goko.core.feature.TargetBoard;

/**
 * Configuration of the units used in Goko
 * 
 * @author PsyKo
 *
 */
public class GokoGeneralPreferences extends GkFieldEditorPreferencesPage {
	@Inject
	private IFeatureSetManager featureManager;
	@Inject
	private IEclipseContext context;
	
	public GokoGeneralPreferences() {
		setTitle("General");		
		setPreferenceStore(GokoPreference.getInstance());
	}

	/** (inheritDoc)
	 * @see org.goko.common.preferences.GkFieldEditorPreferencesPage#createPreferencePage(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void createPreferencePage(Composite parent) {		
		ComboFieldEditor comboTargetBoardFieldEditor = new ComboFieldEditor(parent, SWT.READ_ONLY);
		comboTargetBoardFieldEditor.setLabel("Target board");
		
		comboTargetBoardFieldEditor.setPreferenceName(GokoPreference.KEY_TARGET_BOARD);
			
		List<TargetBoard> lstBoards = featureManager.getSupportedBoards();
		String[][] entry = new String[lstBoards.size()+1][];
		int i = 1;
		entry[0] = new String[]{"Default",""};
		for (TargetBoard targetBoard : lstBoards) {
			entry[i] = new String[]{targetBoard.getLabel(), targetBoard.getId()};
			i++;
		}
		comboTargetBoardFieldEditor.setEntry(entry);
		
		addField(comboTargetBoardFieldEditor);		
	}

}

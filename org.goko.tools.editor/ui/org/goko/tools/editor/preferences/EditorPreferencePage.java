/**
 * 
 */
package org.goko.tools.editor.preferences;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.goko.common.preferences.GkFieldEditorPreferencesPage;
import org.goko.common.preferences.fieldeditor.preference.ComboFieldEditor;
import org.goko.core.common.exception.GkException;

/**
 * Preference page for the editor
 * @author Psyko
 * @date 29 mai 2016
 */
public class EditorPreferencePage extends GkFieldEditorPreferencesPage {
	
	public EditorPreferencePage() {
		setTitle("Editor");		
		setPreferenceStore(EditorPreference.getInstance());
	}

	/** (inheritDoc)
	 * @see org.goko.common.preferences.GkFieldEditorPreferencesPage#createPreferencePage(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void createPreferencePage(Composite parent) throws GkException {
		
		ComboFieldEditor fontNameCombo = new ComboFieldEditor(parent, SWT.NONE | SWT.READ_ONLY);
		fontNameCombo.setLabelWidthInChar(8);
		fontNameCombo.setLabel("Font :");
		fontNameCombo.setPreferenceName(EditorPreference.FONT_NAME);
		
		ComboFieldEditor fontSizeCombo = new ComboFieldEditor(parent, SWT.NONE | SWT.READ_ONLY);
		fontSizeCombo.setLabelWidthInChar(8);
		fontSizeCombo.setLabel("Font size :");
		fontSizeCombo.setPreferenceName(EditorPreference.FONT_SIZE);
		
		// Font family
		Display display = Display.getCurrent();
		FontData[] lstFonts = display.getFontList(null, true);
		List<String> lstFontName = new ArrayList<String>();
		for (FontData fontData : lstFonts) {
			if(!lstFontName.contains(fontData.getName())){
				lstFontName.add(fontData.getName());
			}
		}
		String[][] fontNameEntry = new String[lstFontName.size()][];
		int i = 0;		
		for (String fontName : lstFontName) {			
			fontNameEntry[i] = new String[]{fontName, fontName};			
			i++;
		}
		fontNameCombo.setEntry(fontNameEntry);
		
		// Font size 
		String[][] fontSizeEntry = new String[][]{
			{"8", "8"},
			{"9", "9"},
			{"10", "10"},
			{"11", "11"},
			{"12", "12"},
			{"14", "14"},
			{"16", "16"},
			{"18", "18"},
			{"20", "20"},
			{"22", "22"},
			{"24", "24"},
		};
		fontSizeCombo.setEntry(fontSizeEntry);
		addField(fontNameCombo);
		addField(fontSizeCombo);
		
	}

}

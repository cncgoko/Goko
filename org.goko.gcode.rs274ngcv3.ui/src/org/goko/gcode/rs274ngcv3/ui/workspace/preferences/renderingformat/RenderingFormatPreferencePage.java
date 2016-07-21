/**
 * 
 */
package org.goko.gcode.rs274ngcv3.ui.workspace.preferences.renderingformat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.goko.common.preferences.GkFieldEditorPreferencesPage;
import org.goko.common.preferences.fieldeditor.preference.BooleanFieldEditor;
import org.goko.common.preferences.fieldeditor.preference.IntegerFieldEditor;
import org.goko.core.common.exception.GkException;

/**
 * @author Psyko
 * @date 20 juil. 2016
 */
public class RenderingFormatPreferencePage extends GkFieldEditorPreferencesPage {

	public RenderingFormatPreferencePage() {
		setTitle("GCode");
		setPreferenceStore(RenderingFormatPreference.getInstance());
	}
	
	/** (inheritDoc)
	 * @see org.goko.common.preferences.GkFieldEditorPreferencesPage#createPreferencePage(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void createPreferencePage(Composite parent) throws GkException {
		
		Group grpRenderingOptions = new Group(parent, SWT.NONE);
		grpRenderingOptions.setLayout(new GridLayout(1, false));
		grpRenderingOptions.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		grpRenderingOptions.setText("Rendering options");
		
		BooleanFieldEditor skipCommentsFieldEditor = new BooleanFieldEditor(grpRenderingOptions, SWT.NONE);
		skipCommentsFieldEditor.setPreferenceName(RenderingFormatPreference.SKIP_COMMENT);
		skipCommentsFieldEditor.setLabel("Skip comments");
		
		BooleanFieldEditor skipLineNumberFieldEditor = new BooleanFieldEditor(grpRenderingOptions, SWT.NONE);
		skipLineNumberFieldEditor.setPreferenceName(RenderingFormatPreference.SKIP_LINE_NUMBER);
		skipLineNumberFieldEditor.setLabel("Skip line number");
		
		BooleanFieldEditor truncateDecimalFieldEditor = new BooleanFieldEditor(grpRenderingOptions, SWT.NONE);
		truncateDecimalFieldEditor.setPreferenceName(RenderingFormatPreference.TRUNCATE_DECIMAL);
		truncateDecimalFieldEditor.setLabel("Truncate decimal");
		
		IntegerFieldEditor decimalDigitCountFieldEditor = new IntegerFieldEditor(grpRenderingOptions, SWT.NONE);
		decimalDigitCountFieldEditor.setWidthInChars(4);
		decimalDigitCountFieldEditor.setTextLimit(4);
		decimalDigitCountFieldEditor.setPreferenceName(RenderingFormatPreference.DECIMAL_DIGIT_COUNT);
		decimalDigitCountFieldEditor.setLabel("Decimal digit count");
		
		addField(skipCommentsFieldEditor);
		addField(skipLineNumberFieldEditor);
		addField(truncateDecimalFieldEditor);
		addField(decimalDigitCountFieldEditor);
		
	}
}

/**
 * 
 */
package org.goko.gcode.rs274ngcv3.ui.workspace.preferences.renderingformat;

import javax.inject.Inject;

import org.eclipse.e4.core.di.extensions.Preference;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.goko.common.preferences.GkFieldEditorPreferencesPage;
import org.goko.common.preferences.fieldeditor.preference.BooleanFieldEditor;
import org.goko.common.preferences.fieldeditor.preference.IntegerFieldEditor;
import org.goko.common.preferences.fieldeditor.preference.quantity.LengthFieldEditor;
import org.goko.core.common.exception.GkException;
import org.goko.core.config.GokoPreference;

/**
 * @author Psyko
 * @date 20 juil. 2016
 */
public class RenderingFormatPreferencePage extends GkFieldEditorPreferencesPage {

	private LengthFieldEditor arcToleranceThresholdFieldEditor;
	private BooleanFieldEditor arcToleranceCheckEnabledFieldEditor;

	public RenderingFormatPreferencePage() {
		setTitle("GCode");
		setPreferenceStore(GCodePreference.getInstance());
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
		skipCommentsFieldEditor.setPreferenceName(GCodePreference.SKIP_COMMENT);
		skipCommentsFieldEditor.setLabel("Skip comments");
		
		BooleanFieldEditor skipLineNumberFieldEditor = new BooleanFieldEditor(grpRenderingOptions, SWT.NONE);
		skipLineNumberFieldEditor.setPreferenceName(GCodePreference.SKIP_LINE_NUMBER);
		skipLineNumberFieldEditor.setLabel("Skip line number");
		
		BooleanFieldEditor truncateDecimalFieldEditor = new BooleanFieldEditor(grpRenderingOptions, SWT.NONE);
		truncateDecimalFieldEditor.setPreferenceName(GCodePreference.TRUNCATE_DECIMAL);
		truncateDecimalFieldEditor.setLabel("Truncate decimal");
		
		IntegerFieldEditor decimalDigitCountFieldEditor = new IntegerFieldEditor(grpRenderingOptions, SWT.NONE);
		decimalDigitCountFieldEditor.setWidthInChars(6);
		decimalDigitCountFieldEditor.setTextLimit(4);
		decimalDigitCountFieldEditor.setPreferenceName(GCodePreference.DECIMAL_DIGIT_COUNT);
		decimalDigitCountFieldEditor.setLabel("Decimal digit count");
		
		addField(skipCommentsFieldEditor);
		addField(skipLineNumberFieldEditor);
		addField(truncateDecimalFieldEditor);
		addField(decimalDigitCountFieldEditor);
		
		Group grpGcodeOptions = new Group(parent, SWT.NONE);
		grpGcodeOptions.setLayout(new GridLayout(1, false));
		grpGcodeOptions.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		grpGcodeOptions.setText("GCode options");
		
		arcToleranceCheckEnabledFieldEditor = new BooleanFieldEditor(grpGcodeOptions, SWT.NONE);
		arcToleranceCheckEnabledFieldEditor.setLabel("Enable arc tolerance check");
		arcToleranceCheckEnabledFieldEditor.setPreferenceName(GCodePreference.ARC_TOLERANCE_CHECK_ENABLED);
		
		arcToleranceThresholdFieldEditor = new LengthFieldEditor(grpGcodeOptions, SWT.NONE);
		arcToleranceThresholdFieldEditor.setWidthInChars(6);
		arcToleranceThresholdFieldEditor.setLabelWidthInChar(14);
		arcToleranceThresholdFieldEditor.setLabel("Arc tolerance");
		arcToleranceThresholdFieldEditor.setPreferenceName(GCodePreference.ARC_TOLERANCE_THRESHOLD);
		arcToleranceThresholdFieldEditor.setUnit(GokoPreference.getInstance().getLengthUnit());
		
		// Add length editor first, otherwise the boolean editor (which dictates length field state) will trigger an event, and length field will not have any preference store, throwing an NPE
		addField(arcToleranceThresholdFieldEditor);
		addField(arcToleranceCheckEnabledFieldEditor);		
	}
	
	@Inject	
	public void onArcToleranceCheckEnabledChange(@Preference(nodePath = GCodePreference.NODE_ID, value = GCodePreference.ARC_TOLERANCE_CHECK_ENABLED) boolean active) throws GkException{			
		if(arcToleranceThresholdFieldEditor != null){
			arcToleranceThresholdFieldEditor.setEnabled(active);
		}
	}
	
	/** (inheritDoc)
	 * @see org.goko.common.preferences.GkFieldEditorPreferencesPage#propertyChange(org.eclipse.jface.util.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent event) {		
		super.propertyChange(event);
		arcToleranceThresholdFieldEditor.setEnabled( arcToleranceCheckEnabledFieldEditor.getControl().getSelection());
	}
	
}

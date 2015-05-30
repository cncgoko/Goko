package org.goko.common.preferences.fieldeditor;

import org.eclipse.jface.preference.ColorSelector;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.goko.core.common.exception.GkException;

public class ColorFieldEditor extends LabeledFieldEditor<Button> {
	/** Internal color selector */
	private ColorSelector colorSelector;

	
	public ColorFieldEditor(Composite parent, int style) {
		super(parent, style);
		createControls(parent, style);
	}
	
	/** (inheritDoc)
	 * @see org.goko.common.preferences.fieldeditor.FieldEditor#getControl()
	 */
	@Override
	public Button getControl() {		
		return colorSelector.getButton();
	}
	
	@Override
	protected void createControls(Composite parent, int style) {		
		super.createControls(parent, style);
		colorSelector = new ColorSelector(this);
		getControl().setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
		
		final ColorFieldEditor that = this;		
		colorSelector.addListener(new IPropertyChangeListener() {
			// forward the property change of the color selector
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				that.fireValueChanged(event.getProperty(), event.getOldValue(), event.getNewValue());			
			}
		});
		this.pack();
	}
	
	/** (inheritDoc)
	 * @see org.goko.common.preferences.fieldeditor.FieldEditor#setDefaultValue()
	 */
	@Override
	protected void setDefaultValue() throws GkException {
		colorSelector.setColorValue(PreferenceConverter.getDefaultColor(getPreferenceStore(), getPreferenceName()));		
	}

	/** (inheritDoc)
	 * @see org.goko.common.preferences.fieldeditor.FieldEditor#loadValue()
	 */
	@Override
	protected void loadValue() throws GkException {
		colorSelector.setColorValue(PreferenceConverter.getColor(getPreferenceStore(), getPreferenceName()));
	}

	/** (inheritDoc)
	 * @see org.goko.common.preferences.fieldeditor.FieldEditor#storeValue()
	 */
	@Override
	protected void storeValue() throws GkException {
		PreferenceConverter.setValue(getPreferenceStore(), getPreferenceName(), colorSelector.getColorValue());
		
	}

}

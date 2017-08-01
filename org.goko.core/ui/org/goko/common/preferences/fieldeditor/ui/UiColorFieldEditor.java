/**
 * 
 */
package org.goko.common.preferences.fieldeditor.ui;

import javax.vecmath.Color3f;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.internal.databinding.util.JFaceProperty;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.goko.common.bindings.AbstractModelObject;
import org.goko.core.common.exception.GkException;

/**
 * @author Psyko
 * @date 22 oct. 2016
 */
public class UiColorFieldEditor extends UiLabeledFieldEditor<Button>{
	/** Color selector */
	private ColorSelector colorSelector;
	private Color3f colorValue;
	
	/**
	 * @param parent
	 * @param style
	 */
	public UiColorFieldEditor(Composite parent, int style) {
		super(parent, style);
	}
	
	/** (inheritDoc)
	 * @see org.eclipse.core.databinding.validation.IValidator#validate(java.lang.Object)
	 */
	@Override
	public IStatus validate(Object value) {
		return null;
	}

	/** (inheritDoc)
	 * @see org.goko.common.preferences.fieldeditor.ui.UiLabeledFieldEditor#createControls(org.eclipse.swt.widgets.Composite, int)
	 */
	@Override
	protected void createControls(Composite parent, int style) {		
		super.createControls(parent, style);
		colorSelector = new ColorSelector(this);
		control = colorSelector.getButton();
		getControl().setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
						
		colorSelector.addListener(new IPropertyChangeListener() {
			// forward the property change of the color selector
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				RGB newValue = (RGB) event.getNewValue();
				Color3f oldColor3f = colorValue;
				colorValue = new Color3f(newValue.red  / 255.0f, newValue.green  / 255.0f, newValue.blue  / 255.0f);
				UiColorFieldEditor.this.fireValueChanged(event.getProperty(), oldColor3f, colorValue);			
			}
		});
		this.pack();
	}
	/** (inheritDoc)
	 * @see org.goko.common.preferences.fieldeditor.ui.UiFieldEditor#getFieldEditorBinding(org.eclipse.core.databinding.DataBindingContext, org.goko.common.bindings.AbstractModelObject)
	 */
	@Override
	protected Binding getFieldEditorBinding(DataBindingContext bindingContext, AbstractModelObject modelObject) throws GkException {
		//IObservableValue targetObserver = JFaceProperties.value(ColorSelector.class, "color3f", ColorSelector.PROP_COLORCHANGE).observe(colorSelector);
		IObservableValue fgTarget = new JFaceProperty(ColorSelector.PROP_COLORCHANGE, ColorSelector.PROP_COLORCHANGE, UiColorFieldEditor.class).observe(this);
	
		IObservableValue modelObserver  = BeanProperties.value(propertyName).observe(modelObject);
		
		UpdateValueStrategy targetToModelStrategy = new UpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE);		
		UpdateValueStrategy modelToTargetStrategy = new UpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE);
		
		return bindingContext.bindValue(fgTarget, modelObserver, targetToModelStrategy, modelToTargetStrategy);
	}

	/**
	 * @return the colorSelector
	 */
	public ColorSelector getColorSelector() {
		return colorSelector;
	}

	/**
	 * @param colorSelector the colorSelector to set
	 */
	public void setColorSelector(ColorSelector colorSelector) {
		this.colorSelector = colorSelector;
	}

	/**
	 * @return the colorValue
	 */
	public Color3f getColorValue() {
		return colorValue;
	}

	/**
	 * @param colorValue the colorValue to set
	 */
	public void setColorValue(Color3f colorValue) {
		this.colorValue = colorValue;
		colorSelector.setColorValue(new RGB( Math.round(colorValue.x * 255), Math.round(colorValue.y * 255), Math.round(colorValue.z * 255)));
	}

	/**
	 * Adds a property change listener to this <code>ColorSelector</code>.
	 * Events are fired when the color in the control changes via the user
	 * clicking an selecting a new one in the color dialog. No event is fired in
	 * the case where <code>setColorValue(RGB)</code> is invoked.
	 * 
	 * @param listener
	 *            a property change listener
	 * @since CHANGEME
	 */
	public void addPropertyChangeListener(IPropertyChangeListener listener) {
		setPropertyChangeListener(listener);
	}

	/**
	 * Removes the given listener from this <code>ColorSelector</code>. Has no
	 * effect if the listener is not registered.
	 * 
	 * @param listener
	 *            a property change listener
	 * @since CHANGEME
	 */
	public void removePropertyChangeListener(IPropertyChangeListener listener) {
		setPropertyChangeListener(listener);
	}
	
	/** (inheritDoc)
	 * @see org.goko.common.preferences.fieldeditor.ui.UiFieldEditor#setEnabled(boolean)
	 */
	@Override
	public void setEnabled(boolean enabled) {		
		super.setEnabled(enabled);
		if(!enabled){
			setColorValue(new Color3f(0.85f,0.85f,0.85f));
		}
	}
//	org.eclipse.jface.preference.ColorSelector.addPropertyChangeListener(java.beans.PropertyChangeListener)
//	cf https://bugs.eclipse.org/bugs/show_bug.cgi?id=374341
}

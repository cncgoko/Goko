package org.goko.common.preferences;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.goko.common.preferences.fieldeditor.preference.IPreferenceFieldEditor;
import org.goko.common.preferences.fieldeditor.preference.PreferenceFieldEditor;
import org.goko.core.common.exception.GkException;
import org.goko.core.log.GkLog;

public abstract class GkFieldEditorPreferencesPage extends GkPreferencesPage {
	private static final GkLog LOG = GkLog.getLogger(GkPreferencesPage.class);
	/** List of field editors*/
	private List<IPreferenceFieldEditor<?>> fields;
	/** The parent composite of all editors */
	private Composite fieldEditorParent;
	/** The invalid field editor*/
	private IPreferenceFieldEditor<?> invalidFieldEditor;
	/** Decorator for current invalid field*/
	private ControlDecoration invalidFieldDecorator;
	
	/** Default constructor */
	public GkFieldEditorPreferencesPage(){ }
	
	/** (inheritDoc)
	 * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control createContents(Composite parent) {
		fieldEditorParent = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
        layout.numColumns = 1;
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        fieldEditorParent.setLayout(layout);				
		try {
			createPreferencePage(fieldEditorParent);
			if(getPreferenceStore() == null){
				LOG.error("Preferences store is not set or null for page '"+getTitle()+"'");
			}
			initialize();
			postInitialize();
			validate();
		} catch (GkException e) {
			LOG.error(e);			
		}
		return fieldEditorParent;
	}
	
	protected void postInitialize() throws GkException{
		
	}
	
	protected abstract void createPreferencePage(Composite parent) throws GkException;
	/**
	 * Add the given field editor to this preference page
	 * @param field the field editor to add
	 */
	public void addField(IPreferenceFieldEditor<?> field){
		if(fields == null){
			fields = new ArrayList<IPreferenceFieldEditor<?>>();
		}
		fields.add(field);
		
	}
	
	/**
	 * Initialize the field editor 
	 * @throws GkException GkException 
	 */
	protected void initialize() throws GkException{
		if(CollectionUtils.isNotEmpty(fields)){
			for (IPreferenceFieldEditor<?> fieldEditor : fields) {
				fieldEditor.setPage(this);
				fieldEditor.setPropertyChangeListener(this);
				fieldEditor.setPreferenceStore(getPreferenceStore());
				fieldEditor.load();
			}
		}
	}
	
	/**
	 * Validates the page
	 */
	protected void validate(){
		boolean valid = true;
		if(CollectionUtils.isNotEmpty(fields)){
			for (IPreferenceFieldEditor<?> fieldEditor : fields) {
				valid = fieldEditor.isValid();
				if(!valid){
					break;
				}
			}
		}
		setValid(valid);
	}
	
	/** (inheritDoc)
	 * @see org.eclipse.jface.util.IPropertyChangeListener#propertyChange(org.eclipse.jface.util.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if(StringUtils.equals(event.getProperty(), PreferenceFieldEditor.IS_VALID)){			
            boolean newValue = ((Boolean) event.getNewValue()).booleanValue();
            // If the new value is true then we must check all field editors.
            // If it is false, then the page is invalid in any case.
            if (newValue) {
            	if(invalidFieldDecorator != null){
            		invalidFieldDecorator.hide();
            	}
            	validate();
            } else {
                invalidFieldEditor = (PreferenceFieldEditor<?>) event.getSource();    
                invalidFieldDecorator = new ControlDecoration(invalidFieldEditor.getControl(), SWT.TOP | SWT.RIGHT);
                Image image = FieldDecorationRegistry. getDefault(). getFieldDecoration(FieldDecorationRegistry.DEC_ERROR). getImage();
                invalidFieldDecorator.setDescriptionText(getErrorMessage());
                invalidFieldDecorator.setImage(image);
                invalidFieldDecorator.show();
                setValid(newValue);
            }		        
		}
	}	
	
	/** (inheritDoc)
	 * @see org.eclipse.jface.preference.PreferencePage#performDefaults()
	 */
	@Override
	protected void performDefaults() {
		try{
			for (IPreferenceFieldEditor<?> fieldEditor : fields) {
				fieldEditor.setDefault();
			}
		}catch(GkException e){
			e.printStackTrace();
		}
	}

	@Override
	public boolean performOk() {
		try{
			if(CollectionUtils.isNotEmpty(fields)){
				for (IPreferenceFieldEditor<?> fieldEditor : fields) {
					fieldEditor.store();
				}	
			}
			return true;
		}catch(GkException e){
			LOG.error(e);
			return false;
		}		
	}
}

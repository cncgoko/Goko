package org.goko.featuremanager.config;

import java.util.List;

import javax.inject.Inject;

import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.goko.common.elements.combo.GkCombo3;
import org.goko.common.preferences.GkPreferencesPage;
import org.goko.core.common.exception.GkException;
import org.goko.core.log.GkLog;
import org.goko.featuremanager.bean.Feature;
import org.goko.featuremanager.bean.FeatureBundle;
import org.goko.featuremanager.service.IFeatureManager;

public class FeatureConfigurator extends GkPreferencesPage {
	private static final GkLog LOG = GkLog.getLogger(FeatureConfigurator.class);
	@Inject
	private IFeatureManager featureManager;
	private GkCombo3<FeatureBundle> controllerCombo;
	
	public FeatureConfigurator() {
		super();
		setTitle("Hardware");		
	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control createContents(Composite parent) {		
		Composite content = new Composite(parent, SWT.NONE);
		try{
			/* TEST CODE */
//			Feature feature = new Feature();
//			feature.setId("org.goko.controller");
//			feature.setName("org.goko.controller");
//			feature.setDescription("org.goko.controller");		
//			featureManager.addFeature(feature);
//			FeatureBundle bundle = new FeatureBundle();
//			bundle.setName("TinyG");
//			bundle.setSymbolicName("org.goko.tinyg");			
//			List<String> providedFeatureIds = new ArrayList<String>();
//			providedFeatureIds.add("org.goko.controller");
//			bundle.setProvidedFeatureIds(providedFeatureIds);
//			
//			featureManager.addFeatureProvider(bundle);
//			
//			featureManager.bindFeatureProvider("org.goko.controller", "org.goko.tinyg");
			/* */
			content.setLayout(new GridLayout(2, false));
			
			Label lblController = new Label(content, SWT.NONE);
			lblController.setText("Controller :");
			
			controllerCombo = new GkCombo3<FeatureBundle>(content, SWT.READ_ONLY, "getName");
			Combo combo = controllerCombo.getCombo();
			combo.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
			
			Feature controllerFeature 		= featureManager.getFeature("org.goko.controller");
			List<FeatureBundle> lstBundle 	= featureManager.getFeatureBundle(controllerFeature);
			LOG.info(" **** Found "+lstBundle.size()+" bundles for org.goko.controller");
			controllerCombo.setInput(lstBundle);
			controllerCombo.setSelectedValue(featureManager.getInstalledBundleByFeature(controllerFeature.getId()));
		}catch(GkException e){
			LOG.error(e);
		}
		return content;		
	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.util.IPropertyChangeListener#propertyChange(org.eclipse.jface.util.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		// TODO Auto-generated method stub
		
	}
	
	/** (inheritDoc)
	 * @see org.eclipse.jface.preference.PreferencePage#performOk()
	 */
	@Override
	public boolean performOk() {
		if(controllerCombo != null){
			try {
				FeatureBundle controllerBundle = controllerCombo.getSelectedValue();
				featureManager.unbindFeatureProvider("org.goko.controller");
				featureManager.bindFeatureProvider("org.goko.controller", controllerBundle.getSymbolicName());
				featureManager.save();
			} catch (GkException e) {
				LOG.error(e);
			}
		}
		return super.performOk();
	}

//	/** (inheritDoc)
//	 * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
//	 */
//	@Override
//	protected Control createContents(Composite parent) {
//		Composite content = new Composite(parent, SWT.NONE);
//		try{
//			
//			content.setLayout(new GridLayout(2, false));
//			
//			Label lblNewLabel = new Label(content, SWT.NONE);
//			lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
//			lblNewLabel.setText("Controller :");
//			
//			GkCombo<LabeledValue<FeatureBundle>> comboViewer = new GkCombo<LabeledValue<FeatureBundle>>(content, SWT.NONE);
//			Combo combo = comboViewer.getCombo();
//			combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
//			
//			Feature controllerFeature 		= featureManager.getFeature("org.goko.controller");
//			List<FeatureBundle> lstBundle 	= featureManager.getFeatureBundle(controllerFeature);
//			
//			getController().addItemsBinding(comboViewer, "lstControllerBundle");
//			//combo.setItems();
//		}catch(GkException e){
//			e.printStackTrace();
//		}
//		return content;
//	}
//
//	/** (inheritDoc)
//	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
//	 */
//	@Override
//	public void init(IWorkbench workbench) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	protected boolean internPerformOk() throws GkException {
//		// TODO Auto-generated method stub
//		return false;
//	}
}


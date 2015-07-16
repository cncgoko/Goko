package org.goko.tinyg.configuration;

import java.math.BigDecimal;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.goko.core.common.exception.GkException;
import org.goko.core.log.GkLog;
import org.goko.tinyg.controller.configuration.TinyGConfiguration;

public class TinyGConfigurationDevicePage extends AbstractTinyGConfigurationPage{
	private static final GkLog LOG = GkLog.getLogger(TinyGConfigurationDevicePage.class);	
	private Label lblFirmwareVersion;
	private Label lblFirmwareBuild;
	private Label lblDeviceId;
	
	public TinyGConfigurationDevicePage(TinyGConfiguration configuration) {
		super(configuration);
		setTitle("Device");
	}

	@Override
	public void setVisible(boolean visible) {		
		super.setVisible(visible);
		if(visible){
			try {
				lblFirmwareVersion.setText(getConfiguration().getSetting(TinyGConfiguration.SYSTEM_SETTINGS, TinyGConfiguration.FIRMWARE_VERSION, BigDecimal.class).toPlainString());
				lblFirmwareBuild.setText(getConfiguration().getSetting(TinyGConfiguration.SYSTEM_SETTINGS, TinyGConfiguration.FIRMWARE_BUILD, BigDecimal.class).toPlainString());
				lblDeviceId.setText(getConfiguration().getSetting(TinyGConfiguration.SYSTEM_SETTINGS, TinyGConfiguration.UNIQUE_ID, String.class));
			} catch (GkException e) {
				LOG.error(e);
			}
		}
	}
	/** (inheritDoc)
	 * @see org.goko.common.preferences.GkFieldEditorPreferencesPage#createPreferencePage(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void createPreferencePage(Composite parent) throws GkException {
		GridLayout gridLayout = (GridLayout) parent.getLayout();
		gridLayout.horizontalSpacing = 10;
		gridLayout.verticalSpacing = 8;
		gridLayout.numColumns = 2;
		
		Label lbl1 = new Label(parent, SWT.NONE);
		lbl1.setText("Firmware version");
		
		lblFirmwareVersion = new Label(parent, SWT.NONE);
		lblFirmwareVersion.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		lblFirmwareVersion.setText("New Label");
		
		Label lblNewLabel = new Label(parent, SWT.NONE);
		lblNewLabel.setText("Firmware build");
		
		lblFirmwareBuild = new Label(parent, SWT.NONE);
		lblFirmwareBuild.setText("New Label");
		
		Label lblNewLabel_1 = new Label(parent, SWT.NONE);
		lblNewLabel_1.setText("Device ID");
		
		lblDeviceId = new Label(parent, SWT.NONE);
		lblDeviceId.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		lblDeviceId.setText("New Label");
	}
}

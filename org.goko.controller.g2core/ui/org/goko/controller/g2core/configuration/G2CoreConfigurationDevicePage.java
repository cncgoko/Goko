package org.goko.controller.g2core.configuration;

import java.math.BigDecimal;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.goko.controller.g2core.controller.G2Core;
import org.goko.core.common.exception.GkException;
import org.goko.core.log.GkLog;

public class G2CoreConfigurationDevicePage extends AbstractG2CoreConfigurationPage{
	private static final GkLog LOG = GkLog.getLogger(G2CoreConfigurationDevicePage.class);	
	private Label lblFirmwareVersion;
	private Label lblFirmwareBuild;
	private Label lblDeviceId;
	private Label lblBuildString;
	private Label lblBuildConfig;
	private Label lblHardwarePlatform;
	private Label lblHardwareVersion;
	
	public G2CoreConfigurationDevicePage(G2CoreConfiguration configuration) {
		super(configuration);
		setTitle("Device");
	}

	@Override
	public void setVisible(boolean visible) {		
		super.setVisible(visible);
		if(visible){
			try {
				lblFirmwareVersion.setText(getConfiguration().getSetting(G2Core.Configuration.Groups.SYSTEM, G2Core.Configuration.System.FIRMWARE_VERSION, BigDecimal.class).toPlainString());
				lblFirmwareBuild.setText(getConfiguration().getSetting(G2Core.Configuration.Groups.SYSTEM, G2Core.Configuration.System.FIRMWARE_BUILD, BigDecimal.class).toPlainString());
				lblDeviceId.setText(getConfiguration().getSetting(G2Core.Configuration.Groups.SYSTEM, G2Core.Configuration.System.BOARD_ID, String.class));
				lblBuildString.setText(getConfiguration().getSetting(G2Core.Configuration.Groups.SYSTEM, G2Core.Configuration.System.FIRMWARE_BUILD_STRING, String.class));
				lblBuildConfig.setText(getConfiguration().getSetting(G2Core.Configuration.Groups.SYSTEM, G2Core.Configuration.System.FIRMWARE_BUILD_CONFIG, String.class));
				lblHardwarePlatform.setText(getConfiguration().getSetting(G2Core.Configuration.Groups.SYSTEM, G2Core.Configuration.System.HARDWARE_PLATFORM, BigDecimal.class).toPlainString());
				lblHardwareVersion.setText(getConfiguration().getSetting(G2Core.Configuration.Groups.SYSTEM, G2Core.Configuration.System.HARDWARE_VERSION, BigDecimal.class).toPlainString());
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
		lblFirmwareBuild.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		lblFirmwareBuild.setText("New Label");
		
		Label lblNewLabel_2 = new Label(parent, SWT.NONE);
		lblNewLabel_2.setText("Firmware build string");
		
		lblBuildString = new Label(parent, SWT.NONE);
		lblBuildString.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		lblBuildString.setText("New Label");
		
		Label lblNewLabel_3 = new Label(parent, SWT.NONE);
		lblNewLabel_3.setText("Firmware build config");
		
		lblBuildConfig = new Label(parent, SWT.NONE);
		lblBuildConfig.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		lblBuildConfig.setText("New Label");
		
		Label lblNewLabel_4 = new Label(parent, SWT.NONE);
		lblNewLabel_4.setText("Hardware platform");
		
		lblHardwarePlatform = new Label(parent, SWT.NONE);
		lblHardwarePlatform.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		lblHardwarePlatform.setText("New Label");
		
		Label lblNewLabel_5 = new Label(parent, SWT.NONE);
		lblNewLabel_5.setText("Hardware version");
		
		lblHardwareVersion = new Label(parent, SWT.NONE);
		lblHardwareVersion.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		lblHardwareVersion.setText("New Label");
		
		Label lblNewLabel_1 = new Label(parent, SWT.NONE);
		lblNewLabel_1.setText("Device ID");
		
		lblDeviceId = new Label(parent, SWT.NONE);
		lblDeviceId.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		lblDeviceId.setText("New Label");
	}
}

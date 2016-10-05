package goko.handlers;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;
import org.goko.core.log.GkLog;

public class AboutDialog extends Dialog {
	private static final GkLog LOG = GkLog.getLogger(AboutDialog.class);
	protected Object result;
	protected Shell shlAboutGoko;

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public AboutDialog(Shell parent) {
		super(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
	}

	/**
	 * Open the dialog.
	 * 
	 * @return the result
	 */
	public Object open() {
		createContents();
		shlAboutGoko.open();
		shlAboutGoko.layout();
		Display display = getParent().getDisplay();
		while (!shlAboutGoko.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shlAboutGoko = new Shell(getParent(), getStyle());
		shlAboutGoko.setSize(376, 148);
		shlAboutGoko.setText("About Goko");
		shlAboutGoko.setLayout(new GridLayout(1, false));

		Composite composite_1 = new Composite(shlAboutGoko, SWT.NONE);
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		composite_1.setLayout(new GridLayout(1, false));

		Label lblGokoIsA = new Label(composite_1, SWT.WRAP);
		GridData gd_lblGokoIsA = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblGokoIsA.widthHint = 350;
		lblGokoIsA.setLayoutData(gd_lblGokoIsA);
		lblGokoIsA.setText("Goko is an open source desktop application for CNC control and operation");

		Composite composite_2 = new Composite(composite_1, SWT.NONE);
		composite_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		composite_2.setLayout(new GridLayout(2, false));

		Label lblAlphaVersion = new Label(composite_2, SWT.NONE);
		lblAlphaVersion.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblAlphaVersion.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.ITALIC));
		lblAlphaVersion.setText("Version");

		Label lblVersion = new Label(composite_2, SWT.NONE);
		lblVersion.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		new Label(composite_1, SWT.NONE);

		Label lblDate = new Label(composite_2, SWT.NONE);
		lblDate.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblDate.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.ITALIC));
		lblDate.setText("Build");
		
		Label lblBuild = new Label(composite_2, SWT.NONE);
		lblBuild.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
				
		Properties prop = new Properties();
		ClassLoader loader = Thread.currentThread().getContextClassLoader();           
		InputStream stream = loader.getResourceAsStream("/version.properties");
		try {
			prop.load(stream);
			String version = prop.getProperty("goko.version");
			String build = prop.getProperty("goko.build.timestamp");
			lblVersion.setText(version);
			lblBuild.setText(build);	
			
		} catch (IOException e) {
			LOG.error(e);
		}
		
		Composite composite = new Composite(composite_1, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));

		Label lblMoreInformationOn = new Label(composite, SWT.NONE);
		lblMoreInformationOn.setText("More information on");

		Link link = new Link(composite, SWT.NONE);
		link.setText("<a>http://www.goko.fr</a>");

	}
}

package goko.handlers;

import java.awt.Desktop;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
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
		shlAboutGoko.setSize(376, 248);
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
		GridData gd_lblMoreInformationOn = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblMoreInformationOn.widthHint = 60;
		lblMoreInformationOn.setLayoutData(gd_lblMoreInformationOn);
		lblMoreInformationOn.setText("Website :");

		Link link = new Link(composite, SWT.NONE);
		link.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent event) {
				if (event.button == 1) {   // Left button pressed & released
		            Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
		            if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
		                try {
		                    desktop.browse(URI.create("http://www.goko.fr"));
		                } catch (Exception e) {
		                    LOG.error(e);
		                }
		            }
		        }
			}
		});
		link.setText("<a>http://www.goko.fr</a>");
		
		Composite composite_3 = new Composite(composite_1, SWT.NONE);
		composite_3.setLayout(new GridLayout(2, false));
		
		Label lblForum = new Label(composite_3, SWT.NONE);
		GridData gd_lblForum = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblForum.widthHint = 60;
		lblForum.setLayoutData(gd_lblForum);
		lblForum.setText("Forum :");
		
		Link link_1 = new Link(composite_3, 0);
		link_1.setText("<a>http://discuss.goko.fr</a>");
		link_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent event) {
				if (event.button == 1) {   // Left button pressed & released
		            Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
		            if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
		                try {
		                    desktop.browse(URI.create("http://discuss.goko.fr"));
		                } catch (Exception e) {
		                    LOG.error(e);
		                }
		            }
		        }
			}
		});
		
		Composite composite_4 = new Composite(composite_1, SWT.NONE);
		composite_4.setLayout(new GridLayout(2, false));
		
		Label lblContact = new Label(composite_4, SWT.NONE);
		GridData gd_lblContact = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblContact.widthHint = 60;
		lblContact.setLayoutData(gd_lblContact);
		lblContact.setText("Contact :");
			    
		Link link_2 = new Link(composite_4, 0);
		link_2.setText("<a>"+toAscii("636f6e7461637440676f6b6f2e6672")+"</a>");

	}
	
	private String toAscii(String hex){
	    StringBuilder output = new StringBuilder();
	    for (int i = 0; i < hex.length(); i+=2) {
	        String str = hex.substring(i, i+2);
	        output.append((char)Integer.parseInt(str, 16));
	    }
	    return output.toString();
	}
}

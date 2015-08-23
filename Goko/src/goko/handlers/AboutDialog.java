package goko.handlers;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Shell;

public class AboutDialog extends Dialog {

	protected Object result;
	protected Shell shlAboutGoko;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public AboutDialog(Shell parent) {
		super(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);		
	}

	/**
	 * Open the dialog.
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
		
		Label lblAlphaVersion = new Label(composite_1, SWT.NONE);
		lblAlphaVersion.setText("Alpha version 0.0.2");
		new Label(composite_1, SWT.NONE);
		
		Composite composite = new Composite(composite_1, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		
		Label lblMoreInformationOn = new Label(composite, SWT.NONE);
		lblMoreInformationOn.setText("More information on");
		
		Link link = new Link(composite, SWT.NONE);
		link.setText("<a>http://www.goko.fr</a>");

	}
}

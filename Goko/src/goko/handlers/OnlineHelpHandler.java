/**
 * 
 */
package goko.handlers;

import java.awt.Desktop;
import java.net.URI;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.swt.widgets.Shell;

/**
 * Handler for opening online help
 * @author PsyKo
 * @date 1 févr. 2016
 */
public class OnlineHelpHandler {

	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_SHELL) Shell shell) {
		 Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
		    if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
		        try {
		            URI uri = new URI("http://docs.goko.fr/");
					desktop.browse(uri);
		        } catch (Exception e) {
		            e.printStackTrace();
		        }
		    }
	}
}

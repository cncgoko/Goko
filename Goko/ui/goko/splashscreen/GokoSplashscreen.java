/**
 * 
 */
package goko.splashscreen;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Psyko
 * @date 11 sept. 2016
 */
public class GokoSplashscreen{
	private Shell shell;
	
	public GokoSplashscreen() {
		
	}

	public void open(){
		shell = new Shell(SWT.TOOL | SWT.NO_TRIM);			
		Image background = createBackgroundImage(shell);
		shell.setBackgroundImage(background);
		shell.setBackgroundMode(SWT.INHERIT_DEFAULT);
		
		Rectangle imageBounds = background.getBounds();
		shell.setSize(imageBounds.width, imageBounds.height);
		shell.setLocation(getMonitorCenter(shell));
		
		shell.open();
	}
	
	public void close(){
		if(!shell.isDisposed()){
			shell.close();
			shell.dispose();
		}
	}
	
	private Point getMonitorCenter(Shell shell) {
		Monitor primary = shell.getDisplay().getPrimaryMonitor ();
		Rectangle bounds = primary.getBounds();
		Rectangle rect = shell.getBounds();
		int x = bounds.x + (bounds.width - rect.width) / 2;
		int y = bounds.y + (bounds.height - rect.height) / 2;
		return new Point(x, y);
	}
	
	private Image createBackgroundImage(Shell parent) {
		final Image splashImage = getImageDescriptor("Goko", "GokoSplash.png").createImage();
		parent.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				splashImage.dispose();
			}
		});
		return splashImage;
	}
	
	private ImageDescriptor getImageDescriptor(String pluginId, String path) {
		try {
			if (!path.startsWith("/")) {
				path = "/" + path;
			}
			URL url = new URL("platform:/plugin/" + pluginId + path);
			url = FileLocator.resolve(url);
			return ImageDescriptor.createFromURL(url);
		} catch (MalformedURLException e) {
			String msg = NLS.bind("The image path {0} in not a valid location the bundle {1}.", path, pluginId);
			throw new RuntimeException(msg, e);
		} catch (IOException e) {
			String msg = NLS.bind("The image {0} was not found in the bundle {1}.", path, pluginId);
			throw new RuntimeException(msg, e);
		}
	}
}

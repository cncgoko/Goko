/**
 * 
 */
package org.goko.common.addons;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.goko.core.common.exception.GkException;
import org.goko.core.feature.IFeatureSet;
import org.goko.core.feature.IFeatureSetManager;

/**
 * Addon use to trigger the loading of model fragment defined by {@link IFeatureSet}
 * @author PsyKo
 *
 */
public class ModelFragmentLoaderAddon {
		
	@Execute
	public void applicationStarted(IEclipseContext context, MApplication application, IFeatureSetManager featureSetManager) throws GkException {
		featureSetManager.loadFeatureSetFragment(context);
	}
}

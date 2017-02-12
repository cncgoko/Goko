/**
 * 
 */
package org.goko.controller.g2core.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.goko.controller.g2core.configuration.G2CoreConfiguration;
import org.goko.controller.g2core.controller.IG2CoreControllerService;
import org.goko.controller.g2core.handlers.watcher.JSonModeFix;
import org.goko.controller.g2core.handlers.watcher.JSonVerbosityFix;
import org.goko.controller.g2core.handlers.watcher.QueueReportVerbosityFix;
import org.goko.controller.tinyg.commons.configuration.ITinyGConfigurationListener;
import org.goko.controller.tinyg.commons.configuration.watcher.ITinyGConfigurationFix;
import org.goko.core.common.exception.GkException;
import org.goko.core.log.GkLog;

/**
 * Watches the configuration to make sure G2Core and Goko will work together
 * 
 * @author Psyko
 * @date 12 févr. 2017
 */
public class G2CoreConfigurationWatcherHandler implements ITinyGConfigurationListener<G2CoreConfiguration> {
	/** LOG */
	private static final GkLog LOG = GkLog.getLogger(G2CoreConfigurationWatcherHandler.class);
	
	/** The fixes to apply */
	private List<ITinyGConfigurationFix<G2CoreConfiguration>> lstConfigurationFix;
	/** The target service */
	private IG2CoreControllerService controllerService;
	/** UI Sync object */
	@Inject
	private UISynchronize uiSynchronize;
	/** Parent shell */
	/** Update in progress */
	private AtomicBoolean updateInProgress;
	
	/**
	 * Constructor 
	 */
	@Inject
	public G2CoreConfigurationWatcherHandler(IG2CoreControllerService service) {
		service.addConfigurationListener(this);
		this.controllerService = service;
		this.lstConfigurationFix = new ArrayList<ITinyGConfigurationFix<G2CoreConfiguration>>();
		this.lstConfigurationFix.add(new JSonModeFix());
		this.lstConfigurationFix.add(new JSonVerbosityFix());
		this.lstConfigurationFix.add(new QueueReportVerbosityFix());
		this.updateInProgress = new AtomicBoolean(false);
	}

	/** (inheritDoc)
	 * @see org.goko.controller.tinyg.controller.configuration.ITinyGConfigurationListener#onConfigurationChanged(org.goko.controller.tinyg.controller.configuration.TinyGConfiguration)
	 */
	@Override
	public void onConfigurationChanged(G2CoreConfiguration configuration) {	
		if(!updateInProgress.get() &&  configuration.isCompletelyLoaded()){			
			
			List<ITinyGConfigurationFix<G2CoreConfiguration>> lstFixToApply = new ArrayList<ITinyGConfigurationFix<G2CoreConfiguration>>();
			G2CoreConfiguration tinyGConfiguration = controllerService.getConfiguration();
			
			if(CollectionUtils.isNotEmpty(lstConfigurationFix)){
				for (ITinyGConfigurationFix<G2CoreConfiguration> fix : lstConfigurationFix) {
					if(fix.shouldApply(tinyGConfiguration)){
						lstFixToApply.add(fix);							
					}
				}
			}
							
			if(CollectionUtils.isNotEmpty(lstFixToApply)){
				suggestToApply(lstFixToApply);
			}
			
		}	
	}
	
	/**
	 * Opens the dialog describing changes that should be made, and asking for user feedback
	 * @param lstFixToApply the list of fix to apply
	 */
	private void suggestToApply(final List<ITinyGConfigurationFix<G2CoreConfiguration>> lstFixToApply){
		if(CollectionUtils.isNotEmpty(lstFixToApply)){
			uiSynchronize.asyncExec(new Runnable() {
				
				/** (inheritDoc)
				 * @see java.lang.Runnable#run()
				 */
				@Override
				public void run() {
					G2CoreGConfigurationFixDialog dialog = new G2CoreGConfigurationFixDialog(null, getDescription(lstFixToApply));
					int result = dialog.open();
					if(result == IDialogConstants.OK_ID){
						try {
							G2CoreConfiguration tinyGConfiguration = controllerService.getConfiguration();
							for (ITinyGConfigurationFix<G2CoreConfiguration> fix : lstConfigurationFix) {
								if(fix.shouldApply(tinyGConfiguration)){
									fix.apply(tinyGConfiguration);
								}
							}						
							controllerService.applyConfiguration(tinyGConfiguration);
						} catch (GkException e) {
							LOG.error(e);
						}
					}
				}
			});
		}
	}
	
	private String[] getDescription(List<ITinyGConfigurationFix<G2CoreConfiguration>> lstFixToApply){		
		if(CollectionUtils.isNotEmpty(lstFixToApply)){
			String[] arrDescriptions = new String[lstFixToApply.size()];
			int i = 0;
			for (ITinyGConfigurationFix<G2CoreConfiguration> fix : lstFixToApply) {				
				arrDescriptions[i] = fix.getDescription();
				i++;
			}
			return arrDescriptions;
		}
		return null;
	}
}

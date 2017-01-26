/**
 * 
 */
package org.goko.controller.tinyg.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.goko.controller.tinyg.commons.configuration.ITinyGConfigurationListener;
import org.goko.controller.tinyg.controller.ITinygControllerService;
import org.goko.controller.tinyg.controller.configuration.TinyGConfiguration;
import org.goko.controller.tinyg.handlers.watcher.ITinyGConfigurationFix;
import org.goko.controller.tinyg.handlers.watcher.JSonModeFix;
import org.goko.controller.tinyg.handlers.watcher.JSonVerbosityFix;
import org.goko.controller.tinyg.handlers.watcher.QueueReportVerbosityFix;
import org.goko.core.common.exception.GkException;
import org.goko.core.log.GkLog;

/**
 * Watches the configuration to make sure TinyG and Goko will work together
 * 
 * @author Psyko
 * @date 6 juin 2016
 */
public class TinyGConfigurationWatcherHandler implements ITinyGConfigurationListener<TinyGConfiguration> {
	/** LOG */
	private static final GkLog LOG = GkLog.getLogger(TinyGConfigurationWatcherHandler.class);
	
	/** The fixes to apply */
	private List<ITinyGConfigurationFix> lstConfigurationFix;
	/** The target service */
	private ITinygControllerService tinygControllerService;
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
	public TinyGConfigurationWatcherHandler(ITinygControllerService service) {
		service.addConfigurationListener(this);
		this.tinygControllerService = service;
		this.lstConfigurationFix = new ArrayList<ITinyGConfigurationFix>();
		this.lstConfigurationFix.add(new JSonModeFix());
		this.lstConfigurationFix.add(new JSonVerbosityFix());
		this.lstConfigurationFix.add(new QueueReportVerbosityFix());
		this.updateInProgress = new AtomicBoolean(false);
	}

	/** (inheritDoc)
	 * @see org.goko.controller.tinyg.controller.configuration.ITinyGConfigurationListener#onConfigurationChanged(org.goko.controller.tinyg.controller.configuration.TinyGConfiguration)
	 */
	@Override
	public void onConfigurationChanged(TinyGConfiguration configuration) {	
		if(!updateInProgress.get() &&  configuration.isCompletelyLoaded()){			
			
			List<ITinyGConfigurationFix> lstFixToApply = new ArrayList<ITinyGConfigurationFix>();
			TinyGConfiguration tinyGConfiguration = tinygControllerService.getConfiguration();
			
			if(CollectionUtils.isNotEmpty(lstConfigurationFix)){
				for (ITinyGConfigurationFix fix : lstConfigurationFix) {
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
	private void suggestToApply(final List<ITinyGConfigurationFix> lstFixToApply){
		if(CollectionUtils.isNotEmpty(lstFixToApply)){
			uiSynchronize.asyncExec(new Runnable() {
				
				/** (inheritDoc)
				 * @see java.lang.Runnable#run()
				 */
				@Override
				public void run() {
					TinyGConfigurationFixDialog dialog = new TinyGConfigurationFixDialog(null, getDescription(lstFixToApply));
					int result = dialog.open();
					if(result == IDialogConstants.OK_ID){
						try {
							TinyGConfiguration tinyGConfiguration = tinygControllerService.getConfiguration();
							for (ITinyGConfigurationFix fix : lstConfigurationFix) {
								if(fix.shouldApply(tinyGConfiguration)){
									fix.apply(tinyGConfiguration);
								}
							}						
							tinygControllerService.applyConfiguration(tinyGConfiguration);
						} catch (GkException e) {
							LOG.error(e);
						}
					}
				}
			});
		}
	}
	
	private String[] getDescription(List<ITinyGConfigurationFix> lstFixToApply){		
		if(CollectionUtils.isNotEmpty(lstFixToApply)){
			String[] arrDescriptions = new String[lstFixToApply.size()];
			int i = 0;
			for (ITinyGConfigurationFix fix : lstFixToApply) {				
				arrDescriptions[i] = fix.getDescription();
				i++;
			}
			return arrDescriptions;
		}
		return null;
	}
}

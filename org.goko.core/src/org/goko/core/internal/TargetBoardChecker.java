/**
 * 
 */
package org.goko.core.internal;

import java.util.List;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.di.extensions.Preference;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.internal.workbench.swt.PartRenderingEngine;
import org.eclipse.e4.ui.workbench.lifecycle.PostContextCreate;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.goko.core.common.exception.GkException;
import org.goko.core.config.GokoPreference;
import org.goko.core.feature.IFeatureSetManager;
import org.goko.core.feature.TargetBoard;
import org.goko.core.log.GkLog;

/**
 * @author PsyKo
 *
 */
public class TargetBoardChecker {
	private static final GkLog LOG = GkLog.getLogger(TargetBoardChecker.class);
	@Inject
	IFeatureSetManager featureSetManager;

	
	@PostContextCreate
	public void startup(IEclipseContext context) throws GkException {
		String targetBoard = GokoPreference.getInstance().getTargetBoard();
		if (StringUtils.isEmpty(targetBoard)
				|| !featureSetManager.existTargetBoard(targetBoard)) {
			List<TargetBoard> lstSupportedBoard = featureSetManager.getSupportedBoards();
			if(CollectionUtils.size(lstSupportedBoard) == 1){
				featureSetManager.setTargetBoard(lstSupportedBoard.get(0).getId());
				//GokoPreference.getInstance().setTargetBoard(lstSupportedBoard.get(0).getId());
			}else{
				openTargetBoardSelection(context);	
			}	
			featureSetManager.startFeatureSet();
		}		
	}

	private void openTargetBoardSelection(IEclipseContext context) throws GkException {
		final Shell shell = new Shell(SWT.INHERIT_NONE);

		final TargetBoardSelectionDialog dialog = new TargetBoardSelectionDialog(shell);
		dialog.setLstTargetBoard(featureSetManager.getSupportedBoards());
		dialog.create();
		
		PartRenderingEngine.initializeStyling(shell.getDisplay(), context);

		
		if (dialog.open() != Window.OK) {
			System.exit(0);
		}else{
			featureSetManager.setTargetBoard(dialog.getTargetBoard());			
			//GokoPreference.getInstance().setTargetBoard(dialog.getTargetBoard());
		}
	}
	
	/**
	 * Tracks the current target board 
	 * @param targetBoard the target board 
	 * @param context the IEclipseContext
	 */
	@Inject
	@Optional
	public void onTargetBoardChange(@Preference(nodePath = GokoPreference.NODE_ID, value = GokoPreference.KEY_TARGET_BOARD) String targetBoard, IEclipseContext context) {				    
		context.set("org.goko.targetBoard",  targetBoard);
	}	
}

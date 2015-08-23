/**
 * 
 */
package org.goko.core.internal;

import java.util.List;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.di.extensions.Preference;
import org.eclipse.jface.window.Window;
import org.goko.core.common.exception.GkException;
import org.goko.core.config.GokoPreference;
import org.goko.core.feature.IFeatureSetManager;
import org.goko.core.feature.TargetBoard;
import org.goko.core.log.GkLog;

/**
 * Tracks the changes in the defined target board
 * 
 * @author PsyKo
 *
 */
@Creatable
public class TargetBoardTracker {
	private static final GkLog LOG = GkLog.getLogger(TargetBoardTracker.class);
	@Inject
	IFeatureSetManager featureSetManager;

	
	/**
	 * Checks if a target board is defined at startup. If not, a Dialog opens and ask the user to chose a board
	 * @param context the context 
	 * @throws GkException GkException
	 */
	public void checkTargetBoardDefined(IEclipseContext context) throws GkException {
		LOG.info("Checking for target board...");
		String targetBoard = GokoPreference.getInstance().getTargetBoard();
		if (StringUtils.isEmpty(targetBoard)
				|| !featureSetManager.existTargetBoard(targetBoard)) {
			List<TargetBoard> lstSupportedBoard = featureSetManager.getSupportedBoards();
			if(CollectionUtils.size(lstSupportedBoard) == 1){
				featureSetManager.setTargetBoard(lstSupportedBoard.get(0).getId());
			}else{
				openTargetBoardSelection(context);	
			}	
			featureSetManager.startFeatureSet();
		}
		LOG.info("Active target board is ["+ GokoPreference.getInstance().getTargetBoard()+"]");
	}

	/**
	 * Opens the board selection dialog
	 * @param context the context 
	 * @throws GkException GkException
	 */
	private void openTargetBoardSelection(IEclipseContext context) throws GkException {
		final TargetBoardSelectionDialog dialog = new TargetBoardSelectionDialog();
		dialog.setLstTargetBoard(featureSetManager.getSupportedBoards());
		dialog.create();
				
		if (dialog.open() != Window.OK) {
			System.exit(0);
		}else{
			featureSetManager.setTargetBoard(dialog.getTargetBoard());		
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

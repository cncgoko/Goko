/**
 * 
 */
package org.goko.core.feature;

import java.util.List;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.goko.core.common.exception.GkException;

/**
 * Manager for feature set handling. A IFeatureSet represents features related to a specific controller
 * 
 * @author PsyKo
 *
 */
public interface IFeatureSetManager {
	public static final String TOPIC_LOAD_MODEL_FRAGMENTS = "topic/featureSet/loadModelFragments";
	public static final String TOPIC_UNLOAD_MODEL_FRAGMENTS = "topic/featureSet/unloadModelFragments";
	public static final String TOPIC_TARGET_BOARD_PROPERTY = "topic/featureSet/property/targetboard";
	/**
	 * Register the given {@link IFeatureSet}
	 * @param featureSet the feature set to register 
	 * @throws GkException GkException
	 */
	void addFeatureSet(IFeatureSet featureSet) throws GkException;
	
	/**
	 * Load the model fragment related to the current Target board 
	 * @param context the IEclipse context
	 * @throws GkException GkException
	 */
	void loadFeatureSetFragment(IEclipseContext context) throws GkException;
	
	/**
	 * Unload the model fragment related to the current Target board 
	 * @param context the IEclipse context
	 * @param oldTargetBoard the target board id to remove the fragments for
	 * @throws GkException GkException
	 */
	void unloadFeatureSetFragment(IEclipseContext context, String oldTargetBoard) throws GkException;
	
	/**
	 * Check if the given target board id is supported by a registered {@link IFeatureSet}
	 * @param targetBoardId the ID of the target board
	 * @return <code>true</code> if a {@link IFeatureSet} matches the board, <code>false</code> otherwise
	 */
	boolean existTargetBoard(String targetBoardId);

	/**
	 * Returns the list of supported board
	 * @return the list of supported boards
	 */
	List<TargetBoard> getSupportedBoards();
	
	/**
	 * Set the given id as the current target board 
	 * @param id the id of the target board to use 
	 * @throws GkException GkException
	 */
	void setTargetBoard(String id) throws GkException;
}

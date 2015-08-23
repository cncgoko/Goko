/**
 * 
 */
package org.goko.core.feature;

import java.util.List;

import org.goko.core.common.exception.GkException;

/**
 * Manager for feature set handling. A IFeatureSet represents features related to a specific controller
 * 
 * @author PsyKo
 *
 */
public interface IFeatureSetManager {
	public static final String TOPIC_TARGET_BOARD_PROPERTY = "topic/featureSet/property/targetboard";
	/**
	 * Register the given {@link IFeatureSet}
	 * @param featureSet the feature set to register 
	 * @throws GkException GkException
	 */
	void addFeatureSet(IFeatureSet featureSet) throws GkException;
	
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
	
	/**
	 * Starts the feature set registered for the current target board
	 * @throws GkException GkException
	 */
	void startFeatureSet() throws GkException;
}

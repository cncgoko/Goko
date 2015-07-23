package org.goko.core.feature;

import org.goko.core.common.exception.GkException;
import org.osgi.framework.BundleContext;

/**
 * Interface describing a set of feature for a specific controller/board 
 * 
 * @author PsyKo
 *
 */
public interface IFeatureSet {
	/**
	 * Returns the description of the board supported by this feature set
	 * @return {@link TargetBoard}
	 */
	TargetBoard getTargetBoard();
	
	/**
	 * Starts the feature set 	
	 * @param context the bundle context
	 * @throws GkException GkException
	 */
	void start(BundleContext context) throws GkException;
	
	/**
	 * Stops the feature set 
	 * @throws GkException GkException
	 */
	void stop() throws GkException;
}

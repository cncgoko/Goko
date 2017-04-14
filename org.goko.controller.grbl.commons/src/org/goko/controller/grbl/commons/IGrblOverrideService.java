/**
 * 
 */
package org.goko.controller.grbl.commons;

import org.goko.core.common.exception.GkException;

/**
 * @author Psyko
 * @date 10 avr. 2017
 */
public interface IGrblOverrideService {

	void resetSpindleSpeed() throws GkException;
	
	void increaseSpindleSpeed1() throws GkException;
	
	void increaseSpindleSpeed10() throws GkException;
	
	void decreaseSpindleSpeed1() throws GkException;
	
	void decreaseSpindleSpeed10() throws GkException;
	
	void resetFeedSpeed() throws GkException;
	
	void increaseFeedSpeed1() throws GkException;
	
	void increaseFeedSpeed10() throws GkException;
	
	void decreaseFeedSpeed1() throws GkException;
	
	void decreaseFeedSpeed10() throws GkException;
	
	void setRapidSpeed25() throws GkException;
	
	void setRapidSpeed50() throws GkException;
	
	void resetRapidSpeed() throws GkException;
}

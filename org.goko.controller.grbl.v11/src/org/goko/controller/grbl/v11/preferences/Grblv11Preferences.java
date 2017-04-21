/**
 * 
 */
package org.goko.controller.grbl.v11.preferences;

import org.goko.core.common.measure.quantity.Time;
import org.goko.core.common.measure.quantity.TimeUnit;
import org.goko.core.config.GkPreference;

/**
 * @author Psyko
 * @date 21 avr. 2017
 */
public class Grblv11Preferences extends GkPreference {
	public static final String NODE = "org.goko.controller.grbl.v11";
	
	public static final String POLLING_PERIOD_MS 	= "pollingPeriodMs";
	
	private static Grblv11Preferences instance;

	private Grblv11Preferences(){
		super(NODE);		
	}

	public static Grblv11Preferences getInstance() {
		if(instance == null){
			instance = new Grblv11Preferences();
			instance.initialiseValues();
		}
		return instance;
	}
	
	private void initialiseValues() {
		getPreferenceStore().setDefault(POLLING_PERIOD_MS, 100);
	}
	
	public void setPollingPeriod(Time period){
		getPreferenceStore().setValue(POLLING_PERIOD_MS, period.value(TimeUnit.MILLISECOND).intValue());
	}
	
	public Time getPollingPeriod(){
		return Time.valueOf(getPreferenceStore().getString(POLLING_PERIOD_MS), TimeUnit.MILLISECOND);
	}
}

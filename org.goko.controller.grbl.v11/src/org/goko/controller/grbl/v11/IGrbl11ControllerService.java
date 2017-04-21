/**
 * 
 */
package org.goko.controller.grbl.v11;

import org.goko.controller.grbl.commons.IGrblControllerService;
import org.goko.controller.grbl.commons.IGrblOverrideService;
import org.goko.controller.grbl.commons.configuration.IGrblConfigurationListener;
import org.goko.controller.grbl.v11.bean.GrblMachineState;
import org.goko.controller.grbl.v11.configuration.GrblConfiguration;

/**
 * @author Psyko
 * @date 18 avr. 2017
 */
public interface IGrbl11ControllerService extends IGrblControllerService<GrblConfiguration, GrblMachineState>,
													IGrblOverrideService, 
													IGrblConfigurationListener<GrblConfiguration>{

}

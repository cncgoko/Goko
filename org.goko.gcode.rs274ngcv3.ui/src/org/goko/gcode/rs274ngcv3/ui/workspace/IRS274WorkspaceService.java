/**
 * 
 */
package org.goko.gcode.rs274ngcv3.ui.workspace;

import java.util.List;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.service.IGokoService;
import org.goko.core.gcode.rs274ngcv3.element.GCodeProvider;
import org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.IModifierUiProvider;

/**
 * @author PsyKo
 * @date 31 oct. 2015
 */
public interface IRS274WorkspaceService extends IGokoService{

	List<IModifierUiProvider<GCodeProvider, ?>> getModifierBuilder() throws GkException;
}

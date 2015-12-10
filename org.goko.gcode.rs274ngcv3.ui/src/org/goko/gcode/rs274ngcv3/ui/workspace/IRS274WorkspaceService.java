/**
 *
 */
package org.goko.gcode.rs274ngcv3.ui.workspace;

import java.util.List;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.service.IGokoService;
import org.goko.core.gcode.rs274ngcv3.element.GCodeProvider;
import org.goko.core.workspace.service.IProjectSaveParticipant;
import org.goko.gcode.rs274ngcv3.ui.workspace.io.XmlRS274GContent;
import org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.IModifierUiProvider;

/**
 * @author PsyKo
 * @date 31 oct. 2015
 */
public interface IRS274WorkspaceService extends IGokoService, IProjectSaveParticipant<XmlRS274GContent>{

	List<IModifierUiProvider<GCodeProvider, ?>> getModifierBuilder() throws GkException;

	void addModifierBuilder(IModifierUiProvider<GCodeProvider, ?> modifierBuilder) throws GkException;
}

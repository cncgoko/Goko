/**
 *
 */
package org.goko.gcode.rs274ngcv3.ui.workspace.io.source;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.element.IGCodeProviderSource;

/**
 * @author PsyKo
 * @date 13 déc. 2015
 */
public abstract class XmlGCodeProviderSource {

	public abstract IGCodeProviderSource getSource() throws GkException;
}

package org.goko.core.workspace.tree.renderer;

import org.eclipse.jface.viewers.StyledString;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wb.swt.ResourceManager;
import org.goko.core.gcode.element.IGCodeProvider;
import org.goko.core.workspace.bean.NodeGCodeProviderContainer;
import org.goko.core.workspace.tree.GkProjectNodeLabelProvider;

public class GCodeProviderLabelProvider extends GkProjectNodeLabelProvider<IGCodeProvider> {

	public GCodeProviderLabelProvider() {
		super(NodeGCodeProviderContainer.NODE_TYPE);
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.workspace.tree.GkProjectNodeLabelProvider#getStyledTextForContent(org.goko.core.common.utils.IIdBean)
	 */
	@Override
	protected StyledString getStyledTextForContent(IGCodeProvider element) {		
		return new StyledString(element.getCode());
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.workspace.tree.GkProjectNodeLabelProvider#getImageForContent(org.goko.core.common.utils.IIdBean)
	 */
	@Override
	protected Image getImageForContent(IGCodeProvider element) {		
		return ResourceManager.getPluginImage("org.goko.core.workspace", "icons/node/gcodeprovider.png");
	}

}

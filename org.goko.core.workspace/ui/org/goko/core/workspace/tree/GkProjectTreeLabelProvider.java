package org.goko.core.workspace.tree;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.swt.graphics.Image;
import org.goko.core.workspace.bean.INodeType;
import org.goko.core.workspace.bean.IProjectNode;
import org.goko.core.workspace.bean.NodeGCodeProviderContainer;
import org.goko.core.workspace.tree.renderer.GCodeProviderLabelProvider;

public class GkProjectTreeLabelProvider extends LabelProvider implements IStyledLabelProvider {
	Map<INodeType<?>, GkProjectNodeLabelProvider<?>> mapRenderer;
	
	public GkProjectTreeLabelProvider() {
		mapRenderer = new HashMap<INodeType<?>, GkProjectNodeLabelProvider<?>>();
		// TEMPORARY CODE
		mapRenderer.put(NodeGCodeProviderContainer.NODE_TYPE, new GCodeProviderLabelProvider());
	}
	@Override
	public void addListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub
		
	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider#getStyledText(java.lang.Object)
	 */
	@Override
	public StyledString getStyledText(Object element) {
		if(element instanceof IProjectNode<?>){
			IProjectNode<?> node = (IProjectNode<?>) element;
			return mapRenderer.get(node.getType()).getStyledText(node);
		}
		return null;
	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider#getImage(java.lang.Object)
	 */
	@Override
	public Image getImage(Object element) {
		if(element instanceof IProjectNode<?>){
			IProjectNode<?> node = (IProjectNode<?>) element;
			GkProjectNodeLabelProvider<?> renderer = mapRenderer.get(node.getType());
			return renderer.getImage(element);
		}
		return null;
	}

}

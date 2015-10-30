package org.goko.core.workspace.tree;

import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;
import org.apache.commons.lang3.ObjectUtils;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.swt.graphics.Image;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.utils.IIdBean;
import org.goko.core.workspace.bean.INodeType;
import org.goko.core.workspace.bean.IProjectNode;

public class GkProjectNodeLabelProvider<T extends IIdBean> implements IStyledLabelProvider {
	private INodeType<T> type;
	
	public GkProjectNodeLabelProvider(INodeType<T> type) {
		super();
		this.type = type;
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
			try {
				return getStyledTextForContent((T) node.getContent());
			} catch (GkException e) {				
				e.printStackTrace();
			}
		}
		return null;
	}
	
	protected StyledString getStyledTextForContent(T element) {	
		StyledString styledString = new StyledString(ObjectUtils.toString(element));
		return styledString;
	}
	
	/** (inheritDoc)
	 * @see org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider#getImage(java.lang.Object)
	 */
	@Override
	public Image getImage(Object element) {
		if(element instanceof IProjectNode<?>){
			IProjectNode<?> node = (IProjectNode<?>) element;
			try {
				return getImageForContent((T) node.getContent());
			} catch (GkException e) {				
				e.printStackTrace();
			}
		}
		return null;		
	}

	protected Image getImageForContent(T element) {
		return null;
	}

	public INodeType<T> getType() {
		return type;
	}
}

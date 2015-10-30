package org.goko.core.workspace.bean;

import java.util.ArrayList;
import java.util.List;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.utils.IIdBean;

public class ProjectNode<T extends IIdBean> implements IProjectNode<T> {	
	/** The type of this node */
	private INodeType<T> type;
	private T content;
	private List<IProjectNode<? extends IIdBean>> children;
	
	public ProjectNode(INodeType<T> type) {
		super();
		this.type = type;
		this.children = new ArrayList<IProjectNode<? extends IIdBean>>();
	}
	
	
	/** (inheritDoc)
	 * @see org.goko.core.workspace.bean.IProjectNode#getType()
	 */
	@Override
	public INodeType<T> getType() {
		return type;
	}
	
	/**
	 * @param type
	 */
	public void setType(INodeType<T> type) {
		this.type = type;
	}
		
	/** (inheritDoc)
	 * @see org.goko.core.workspace.bean.IProjectNode#getChildren()
	 */
	@Override
	public List<IProjectNode<? extends IIdBean>> getChildren() throws GkException {		
		return children;
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.workspace.bean.IProjectNode#addChild(org.goko.core.workspace.bean.IProjectNode)
	 */
	@Override
	public void addChild(IProjectNode<? extends IIdBean> child) {
		children.add(child);
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.workspace.bean.IProjectNode#addChildren(java.util.List)
	 */
	@Override
	public void addChildren(List<IProjectNode<? extends IIdBean>> children) {
		children.addAll(children);
	}


	public T getContent() {
		return content;
	}


	public void setContent(T content) {
		this.content = content;
	}
	
}

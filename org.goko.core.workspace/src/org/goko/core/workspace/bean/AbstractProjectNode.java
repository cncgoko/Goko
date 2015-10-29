package org.goko.core.workspace.bean;

public abstract class AbstractProjectNode<T> implements IProjectNode<T> {	
	/** The type of this node */
	private INodeType<T> type;
	
	public AbstractProjectNode(INodeType<T> type) {
		super();
		this.type = type;
	}
	
	/**
	 * @return the type
	 */
	public INodeType<T> getType() {
		return type;
	}
	
	/**
	 * @param type the type to set
	 */
	public void setType(INodeType<T> type) {
		this.type = type;
	}
}

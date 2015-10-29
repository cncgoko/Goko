package org.goko.core.workspace.bean;

import org.apache.commons.lang3.StringUtils;

public class NodeType<T> implements INodeType<T> {
	private String type;

	
	public NodeType(String type) {
		super();
		this.type = type;
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.bean.INodeType#equals(org.goko.core.workspace.bean.INodeType)
	 */
	@Override
	public boolean equals(INodeType<?> otherNodeType) {
		if(otherNodeType instanceof NodeType){
			return StringUtils.equals(((NodeType<?>) otherNodeType).getType(), type);
		}
		return false;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	
	
}

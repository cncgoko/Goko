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

	/** (inheritDoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	/** (inheritDoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NodeType other = (NodeType) obj;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
	
	
}

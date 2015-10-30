package org.goko.core.workspace.bean;

import org.goko.core.gcode.element.IGCodeProvider;

public class NodeGCodeProviderContainer extends ProjectNode<IGCodeProvider>{
	/** Node type for this node */
	public static final INodeType<IGCodeProvider> NODE_TYPE = new NodeType<IGCodeProvider>("GCODEPROVIDER");
	
	public NodeGCodeProviderContainer() {
		super(NODE_TYPE);		
	}

}

package org.goko.core.workspace.bean;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.utils.CacheById;
import org.goko.core.gcode.element.IGCodeProvider;

public class NodeGCodeProviderContainer extends AbstractProjectNode<NodeGCodeProviderContainer>{
	/** Node type for this node */
	public static final INodeType<NodeGCodeProviderContainer> NODE_TYPE = new NodeType<NodeGCodeProviderContainer>("GCODEPROVIDER");
	/** Cache of GCode provider */
	private CacheById<IGCodeProvider> cacheProvider;
	
	public NodeGCodeProviderContainer() {
		super(NODE_TYPE);
		cacheProvider = new CacheById<>();
	}
	
	/**
	 * Return the {@link IGCodeProvider} with the given id
	 * @param idGCodeProvider id of the requested provider 
	 * @return IGCodeProvider
	 * @throws GkException if no IGCodeProvider with the given id is found
	 */
	public IGCodeProvider get(Integer idGCodeProvider) throws GkException{
		return cacheProvider.get(idGCodeProvider);
	}

	/**
	 * Add the given {@link IGCodeProvider} 
	 * @param gCodeProvider the {@link IGCodeProvider} to add 
	 * @throws GkException GkException
	 */
	public void add(IGCodeProvider gCodeProvider) throws GkException{
		cacheProvider.add(gCodeProvider);
	}
	
	/**
	 * Remove the given {@link IGCodeProvider} 
	 * @param idGCodeProvider the id of the {@link IGCodeProvider} to remove 
	 * @throws GkException GkException
	 */
	public void remove(Integer idGCodeProvider) throws GkException{
		cacheProvider.remove(idGCodeProvider);
	}
}

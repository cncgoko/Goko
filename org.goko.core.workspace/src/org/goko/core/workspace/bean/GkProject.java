/**
 * 
 */
package org.goko.core.workspace.bean;

import java.util.HashMap;
import java.util.Map;

import org.goko.core.common.exception.GkException;


/**
 * Describes a Goko project
 * 
 * @author PsyKo
 * @date 10 oct. 2015
 */
public class GkProject {
	/** The name of the project */
	private String name;
	/** The path to the project file */
	private String filepath;
	/** The list of loaded gcode file */
	private Map<INodeType<?>, IProjectNode<?>> projectNodes;
	
	public GkProject() {
		this.projectNodes = new HashMap<INodeType<?>, IProjectNode<?>>();
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the filepath
	 */
	public String getFilepath() {
		return filepath;
	}
	/**
	 * @param filepath the filepath to set
	 */
	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	public <T extends IProjectNode<?>> T getNode(INodeType<T> nodeType) throws GkException{
		return (T) projectNodes.get(nodeType);
	}
	
	public <T> void addNode(IProjectNode<T> node){
		projectNodes.put(node.getType(), node);		
	}
}

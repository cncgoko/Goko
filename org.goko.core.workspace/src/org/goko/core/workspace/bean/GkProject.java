/**
 * 
 */
package org.goko.core.workspace.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.utils.IIdBean;


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

	@SuppressWarnings("unchecked")
	public <T extends IIdBean> IProjectNode<T> getNode(INodeType<T> nodeType) throws GkException{
		return (IProjectNode<T>) projectNodes.get(nodeType);
	}
	
	public <T extends IIdBean> void addNode(IProjectNode<T> node){
		projectNodes.put(node.getType(), node);		
	}
	
	public List<IProjectNode<? extends IIdBean>> getNodes(){
		return new ArrayList<IProjectNode<? extends IIdBean>>(projectNodes.values());
	}
}

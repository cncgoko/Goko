package org.goko.core.workspace.bean;

import java.util.List;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.utils.IIdBean;

public interface IProjectNode<T extends IIdBean>{
	
	INodeType<T> getType();	
	
	T getContent() throws GkException;

	void addChild(IProjectNode<? extends IIdBean> child);
	
	void addChildren(List<IProjectNode<? extends IIdBean>> children);
	
	List<IProjectNode<? extends IIdBean>> getChildren() throws GkException;
	
	La node doit contenir tout ce qu'il faut pour :
	- s'afficher dans la vue du workspace (label provider et content provider)
	- s'exporter
	- se sauvegarder
	
	Exemple de node :
	
		RS274ToNodeBridge
}

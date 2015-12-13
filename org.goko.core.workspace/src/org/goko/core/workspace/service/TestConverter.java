/**
 * 
 */
package org.goko.core.workspace.service;

import org.goko.core.workspace.io.XmlNodeConverter;
import org.goko.core.workspace.io.XmlProjectContainer;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.convert.Converter;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.OutputNode;

/**
 * @author PsyKo
 * @date 11 déc. 2015
 */
public class TestConverter implements Converter<XmlProjectContainer> {
	private IWorkspaceService workspaceService;
	
	
	/**
	 * @param workspaceService
	 */
	public TestConverter(IWorkspaceService workspaceService) {
		super();
		this.workspaceService = workspaceService;
	}

	/** (inheritDoc)
	 * @see org.simpleframework.xml.convert.Converter#read(org.simpleframework.xml.stream.InputNode)
	 */
	@Override
	public XmlProjectContainer read(InputNode inputNode) throws Exception {
		InputNode type = inputNode.getAttribute("type");
		IProjectLoadParticipant<?> participant = workspaceService.findProjectLoadParticipantByType(type.getValue());		
		XmlNodeConverter<? extends Object> t = new XmlNodeConverter<>(participant.newContentInstance().getClass());		
		participant.load(inputNode.getNext());
		return new XmlProjectContainer(type.getValue(), null);
	}

	/** (inheritDoc)
	 * @see org.simpleframework.xml.convert.Converter#write(org.simpleframework.xml.stream.OutputNode, java.lang.Object)
	 */
	@Override
	public void write(OutputNode outputNode, XmlProjectContainer container) throws Exception {		
		Serializer s = new Persister();
		outputNode.setAttribute("type", container.getType());
		s.write(container.getContent(), outputNode);
	}

}

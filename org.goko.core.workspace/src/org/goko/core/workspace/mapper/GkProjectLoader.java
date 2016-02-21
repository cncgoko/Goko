/**
 * 
 */
package org.goko.core.workspace.mapper;

import org.apache.commons.collections.CollectionUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.workspace.element.GkProject;
import org.goko.core.workspace.io.XmlGkProject;
import org.goko.core.workspace.io.XmlProjectContainer;
import org.goko.core.workspace.service.ILoader;
import org.goko.core.workspace.service.IMapperService;

/**
 * @author PsyKo
 * @date 10 févr. 2016
 */
public class GkProjectLoader implements ILoader<XmlGkProject, GkProject> {

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.ILoader#load(java.lang.Object, org.goko.core.workspace.service.IMapperService)
	 */
	@Override
	public GkProject load(XmlGkProject input, IMapperService mapperService) throws GkException {
		GkProject project = new GkProject();
		project.setDirty(false);
		
		if(CollectionUtils.isNotEmpty(input.getProjectContainer())){
			for (XmlProjectContainer xmlProjectContainer : input.getProjectContainer()) {
				mapperService.load(xmlProjectContainer, XmlProjectContainer.class);
			}
		}
		return project;
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.ILoader#getOutputClass()
	 */
	@Override
	public Class<GkProject> getOutputClass() {
		return GkProject.class;
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.ILoader#getInputClass()
	 */
	@Override
	public Class<XmlGkProject> getInputClass() {
		return XmlGkProject.class;
	}

}

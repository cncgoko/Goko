/**
 * 
 */
package org.goko.core.workspace.bean;

import java.util.Comparator;

/**
 * @author Psyko
 * @date 3 avr. 2016
 */
public class ProjectContainerUiProviderComparator implements Comparator<ProjectContainerUiProvider>{

	/** (inheritDoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(ProjectContainerUiProvider o1, ProjectContainerUiProvider o2) {
		return o1.getOrder() - o2.getOrder();
	}

}

/**
 * 
 */
package org.goko.core.workspace.service;

import java.util.Comparator;

/**
 * @author PsyKo
 * @date 21 févr. 2016
 */
public class ProjectLoadParticipantComparator implements Comparator<IProjectLoadParticipant>{

	/** (inheritDoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(IProjectLoadParticipant o1, IProjectLoadParticipant o2) {
		return o1.getPriority() - o2.getPriority();
	}

}

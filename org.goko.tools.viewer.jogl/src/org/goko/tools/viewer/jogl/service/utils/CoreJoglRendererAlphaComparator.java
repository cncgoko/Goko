package org.goko.tools.viewer.jogl.service.utils;

import java.util.Comparator;

import org.goko.tools.viewer.jogl.service.ICoreJoglRenderer;

/**
 * Comparator used to sort {@link ICoreJoglRenderer} depending on their use of Alpha channel (transparent renderer are rendered last)
 * 
 * @author Psyko
 */
public class CoreJoglRendererAlphaComparator implements Comparator<ICoreJoglRenderer> {

	/** (inheritDoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(ICoreJoglRenderer r1, ICoreJoglRenderer r2) {
		if(r2.useAlpha() == r1.useAlpha()){
			return 0;
		}else if(!r1.useAlpha() && r2.useAlpha()){
			return -11;
		}
		return 1;
	}

}

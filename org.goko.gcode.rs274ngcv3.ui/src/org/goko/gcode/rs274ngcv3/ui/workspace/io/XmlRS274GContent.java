package org.goko.gcode.rs274ngcv3.ui.workspace.io;

import java.util.ArrayList;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name="rs274Content")
public class XmlRS274GContent {

	@ElementList
	ArrayList<XmlGCodeProvider> lstGCodeProvider;

	/**
	 * @return the lstGCodeProvider
	 */
	public ArrayList<XmlGCodeProvider> getLstGCodeProvider() {
		return lstGCodeProvider;
	}

	/**
	 * @param lstGCodeProvider the lstGCodeProvider to set
	 */
	public void setLstGCodeProvider(ArrayList<XmlGCodeProvider> lstGCodeProvider) {
		this.lstGCodeProvider = lstGCodeProvider;
	}

}

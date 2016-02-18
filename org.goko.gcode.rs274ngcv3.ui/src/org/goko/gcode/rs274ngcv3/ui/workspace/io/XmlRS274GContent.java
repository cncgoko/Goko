package org.goko.gcode.rs274ngcv3.ui.workspace.io;

import java.util.ArrayList;

import org.goko.core.workspace.io.XmlProjectContainer;
import org.simpleframework.xml.DerivedType;
import org.simpleframework.xml.ElementList;

@DerivedType(parent=XmlProjectContainer.class, name="content:rs274")
public class XmlRS274GContent extends XmlProjectContainer{

	@ElementList(name = "gcodeProviders")
	private ArrayList<XmlGCodeProvider> lstGCodeProvider;

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

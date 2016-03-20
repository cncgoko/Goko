package org.goko.gcode.rs274ngcv3.ui.workspace.io;

import java.util.ArrayList;

import org.goko.core.workspace.io.XmlProjectContainer;
import org.goko.gcode.rs274ngcv3.ui.workspace.io.bean.XmlGCodeProvider;
import org.simpleframework.xml.DerivedType;
import org.simpleframework.xml.ElementList;

@DerivedType(parent=XmlProjectContainer.class, name=XmlRS274GContent.CONTAINER_TYPE)
public class XmlRS274GContent extends XmlProjectContainer{
	public static final String CONTAINER_TYPE ="content:rs274";
	
	@ElementList(name = "gcodeProviders")
	private ArrayList<XmlGCodeProvider> lstGCodeProvider;

	/**
	 * Constructor
	 */
	public XmlRS274GContent() {
		super(CONTAINER_TYPE);
	}
	
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

package org.goko.core.gcode.rs274ngcv3.element;

import java.util.Date;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.element.IGCodeProvider;

public interface IStackableGCodeProvider extends IGCodeProvider{

	IStackableGCodeProvider getParent();

	void setParent(IStackableGCodeProvider parent);

	IStackableGCodeProvider getChild();

	void setChild(IStackableGCodeProvider parent);
	
	void update() throws GkException;

	Integer getIdModifier();

	Date getModificationDate();
	
}

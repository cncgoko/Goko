package org.goko.core.gcode.rs274ngcv3.element;

import java.util.Date;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.utils.IIdBean;
import org.goko.core.gcode.element.IGCodeProvider;

public interface IModifier<T extends GCodeProvider> extends IIdBean {

	Integer getIdGCodeProvider();

	String getModifierName();

	boolean isEnabled();

	boolean isConfigured();
	
	void setEnabled(boolean enabled);

	public void apply(IGCodeProvider source, T target) throws GkException;

	int getOrder();

	void setOrder(int order);

	Date getModificationDate();

	void setModificationDate(Date date);
}


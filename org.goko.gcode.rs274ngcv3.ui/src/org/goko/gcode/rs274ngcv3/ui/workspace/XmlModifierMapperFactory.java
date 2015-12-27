package org.goko.gcode.rs274ngcv3.ui.workspace;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.rs274ngcv3.element.GCodeProvider;
import org.goko.core.gcode.rs274ngcv3.element.IModifier;
import org.goko.gcode.rs274ngcv3.ui.workspace.io.XmlGCodeModifier;

public class XmlModifierMapperFactory {
//	private Map<Class, XmlModifierTransformer> modifierToXmlMapper;
//	private Map<Class, ModifierTransformer> xmlToModifierMapper;

	public XmlGCodeModifier getXmlModifier(IModifier<GCodeProvider> modifier) throws GkException{
		return null;
	}

	public IModifier<GCodeProvider> getXmlModifier(XmlGCodeModifier xmlModifier) throws GkException{
		return null;
	}
}

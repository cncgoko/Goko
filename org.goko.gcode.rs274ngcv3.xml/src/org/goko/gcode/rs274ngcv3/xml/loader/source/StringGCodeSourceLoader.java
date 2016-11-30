/**
 * 
 */
package org.goko.gcode.rs274ngcv3.xml.loader.source;

import org.apache.commons.lang3.StringUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.rs274ngcv3.element.source.StringGCodeSource;
import org.goko.core.workspace.service.ILoader;
import org.goko.core.workspace.service.IMapperService;
import org.goko.gcode.rs274ngcv3.xml.bean.source.XmlStringGCodeSource;

/**
 * @author Psyko
 * @date 16 oct. 2016
 */
public class StringGCodeSourceLoader implements ILoader<XmlStringGCodeSource, StringGCodeSource>{

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.ILoader#load(java.lang.Object, org.goko.core.workspace.service.IMapperService)
	 */
	@Override
	public StringGCodeSource load(XmlStringGCodeSource input, IMapperService mapperService) throws GkException {		
		return new StringGCodeSource(StringUtils.defaultString(input.getContent()));
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.ILoader#getOutputClass()
	 */
	@Override
	public Class<StringGCodeSource> getOutputClass() {
		return StringGCodeSource.class;
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.ILoader#getInputClass()
	 */
	@Override
	public Class<XmlStringGCodeSource> getInputClass() {
		return XmlStringGCodeSource.class;
	}

}

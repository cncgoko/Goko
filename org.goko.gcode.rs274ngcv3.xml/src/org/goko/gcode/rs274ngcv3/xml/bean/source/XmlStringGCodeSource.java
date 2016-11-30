package org.goko.gcode.rs274ngcv3.xml.bean.source;

import org.apache.commons.lang3.StringUtils;
import org.simpleframework.xml.DerivedType;
import org.simpleframework.xml.Text;


/**
 * XML description of a StringGCodeSource
 * @author PsyKo
 * @date 19 mars 2016
 */
@DerivedType(parent=XmlGCodeProviderSource.class, name="source:string")
public class XmlStringGCodeSource extends XmlGCodeProviderSource{
	/** The actual content */
	@Text(required=false, empty=StringUtils.EMPTY)
	private String content;

	
	/**
	 * Default constructor
	 */
	public XmlStringGCodeSource() {
		super();		
	}

	/**
	 * @param content
	 */
	public XmlStringGCodeSource(String content) {
		super();
		this.content = content;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}
	
	
	
}

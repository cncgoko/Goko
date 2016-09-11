package org.goko.core.common.io.xml.math;

import org.apache.commons.lang3.StringUtils;
import org.simpleframework.xml.transform.Transform;

/**
 * Transform for an XmlAngle object
 *
 * @author Psyko
 */
public class XmlBigDecimalTransform implements Transform<XmlBigDecimal>{

	/** (inheritDoc)
	 * @see org.simpleframework.xml.transform.Transform#read(java.lang.String)
	 */
	@Override
	public XmlBigDecimal read(String value) throws Exception {
		XmlBigDecimal result = null;
		if(StringUtils.isNotEmpty(value)){
			result = new XmlBigDecimal(value);
		}
		return result;
	}

	/** (inheritDoc)
	 * @see org.simpleframework.xml.transform.Transform#write(java.lang.Object)
	 */
	@Override
	public String write(XmlBigDecimal xmlBigDecimal) throws Exception {
		String result = null;
		if(xmlBigDecimal != null){					
			result = xmlBigDecimal.getValue().toPlainString();
		}
		return result;
	}

}

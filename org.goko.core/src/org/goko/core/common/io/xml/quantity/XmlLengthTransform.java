package org.goko.core.common.io.xml.quantity;

import org.apache.commons.lang3.StringUtils;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.units.Unit;
import org.simpleframework.xml.transform.Transform;

/**
 * Transform for an XmlLength object
 *
 * @author Psyko
 */
public class XmlLengthTransform implements Transform<XmlLength>{

	/** (inheritDoc)
	 * @see org.simpleframework.xml.transform.Transform#read(java.lang.String)
	 */
	@Override
	public XmlLength read(String value) throws Exception {
		XmlLength result = null;
		if(StringUtils.isNotEmpty(value)){
			result = new XmlLength(Length.parse(value));
		}
		return result;
	}

	/** (inheritDoc)
	 * @see org.simpleframework.xml.transform.Transform#write(java.lang.Object)
	 */
	@Override
	public String write(XmlLength value) throws Exception {
		String result = null;
		if(value != null){
			Unit<Length> unit = value.getQuantity().getUnit();
			result = String.valueOf(value.getQuantity().value(unit))+unit.getSymbol();
		}
		return result;
	}

}

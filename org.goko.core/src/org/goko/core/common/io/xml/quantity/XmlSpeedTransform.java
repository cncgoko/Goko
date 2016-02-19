package org.goko.core.common.io.xml.quantity;

import org.apache.commons.lang3.StringUtils;
import org.goko.core.common.measure.quantity.Speed;
import org.goko.core.common.measure.units.Unit;
import org.simpleframework.xml.transform.Transform;

/**
 * Transform for an XmlSpeed object
 *
 * @author Psyko
 */
public class XmlSpeedTransform implements Transform<XmlSpeed>{

	/** (inheritDoc)
	 * @see org.simpleframework.xml.transform.Transform#read(java.lang.String)
	 */
	@Override
	public XmlSpeed read(String value) throws Exception {
		XmlSpeed result = null;
		if(StringUtils.isNotEmpty(value)){
			result = new XmlSpeed(Speed.parse(value));
		}
		return result;
	}

	/** (inheritDoc)
	 * @see org.simpleframework.xml.transform.Transform#write(java.lang.Object)
	 */
	@Override
	public String write(XmlSpeed value) throws Exception {
		String result = null;
		if(value != null){
			Unit<Speed> unit = value.getQuantity().getUnit();
			result = String.valueOf(value.getQuantity().value(unit))+unit.getSymbol();
		}
		return result;
	}

}

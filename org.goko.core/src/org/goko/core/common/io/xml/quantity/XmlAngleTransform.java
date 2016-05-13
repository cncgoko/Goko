package org.goko.core.common.io.xml.quantity;

import org.apache.commons.lang3.StringUtils;
import org.goko.core.common.measure.quantity.Angle;
import org.goko.core.common.measure.units.Unit;
import org.simpleframework.xml.transform.Transform;

/**
 * Transform for an XmlAngle object
 *
 * @author Psyko
 */
public class XmlAngleTransform implements Transform<XmlAngle>{

	/** (inheritDoc)
	 * @see org.simpleframework.xml.transform.Transform#read(java.lang.String)
	 */
	@Override
	public XmlAngle read(String value) throws Exception {
		XmlAngle result = null;
		if(StringUtils.isNotEmpty(value)){
			result = new XmlAngle(Angle.parse(value));
		}
		return result;
	}

	/** (inheritDoc)
	 * @see org.simpleframework.xml.transform.Transform#write(java.lang.Object)
	 */
	@Override
	public String write(XmlAngle value) throws Exception {
		String result = null;
		if(value != null){
			Unit<Angle> unit = value.getQuantity().getUnit();			
			result = String.valueOf(value.getQuantity().value(unit))+unit.getSymbol();
		}
		return result;
	}

}

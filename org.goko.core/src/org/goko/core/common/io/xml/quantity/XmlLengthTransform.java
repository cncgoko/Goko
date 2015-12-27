package org.goko.core.common.io.xml.quantity;

import org.apache.commons.lang3.StringUtils;
import org.goko.core.common.measure.dimension.QuantityDimension;
import org.goko.core.common.measure.quantity.type.NumberQuantity;
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
			result = new XmlLength(NumberQuantity.of(value, QuantityDimension.LENGTH));
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
			result = String.valueOf(value.getQuantity().getValue())+value.getQuantity().getUnit().getSymbol();
		}
		return result;
	}

}

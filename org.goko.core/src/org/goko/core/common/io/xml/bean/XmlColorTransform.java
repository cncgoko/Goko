package org.goko.core.common.io.xml.bean;

import org.apache.commons.lang3.StringUtils;
import org.simpleframework.xml.transform.Transform;

/**
 * Transform for an XmlColor object
 *
 * @author Psyko
 */
public class XmlColorTransform implements Transform<XmlColor>{
		
	/** (inheritDoc)
	 * @see org.simpleframework.xml.transform.Transform#read(java.lang.String)
	 */
	@Override
	public XmlColor read(String value) throws Exception {
		XmlColor result = null;
		if(StringUtils.isNotEmpty(value)){			
			if(value.matches("rgb\\(([0-9]*),([0-9]*),([0-9]*)\\)")){
				String rgb = value.substring(4, value.length() - 1);
				String[] arr = rgb.split(",");
				int r = Integer.valueOf(arr[0]);
				int g = Integer.valueOf(arr[1]);
				int b = Integer.valueOf(arr[2]);
				result = new XmlColor(r, g, b);
				
			}
		}
		return result;
	}

	/** (inheritDoc)
	 * @see org.simpleframework.xml.transform.Transform#write(java.lang.Object)
	 */
	@Override
	public String write(XmlColor value) throws Exception {
		String result = null;
		if(value != null){
			result = "rgb("+value.getRed()+","+value.getGreen()+","+value.getBlue()+")";
		}
		return result;
	}

}

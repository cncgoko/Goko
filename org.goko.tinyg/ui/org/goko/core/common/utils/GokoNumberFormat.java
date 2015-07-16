package org.goko.core.common.utils;

import java.text.NumberFormat;
import java.util.Locale;

public class GokoNumberFormat {
	public static NumberFormat getNumberInstance(){
		NumberFormat instance = NumberFormat.getNumberInstance(Locale.US);
		instance.setGroupingUsed(false);		
		return instance;
	}	
}

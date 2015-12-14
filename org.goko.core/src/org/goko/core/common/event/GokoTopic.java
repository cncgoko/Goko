package org.goko.core.common.event;

public interface GokoTopic {
	
	interface File{		
		
		interface Open{
			public static final String TOPIC				= "topic/goko/file/open";
		}
		public static final String PROPERTY_FILEPATH			= "prop/goko/file/filepath";		
	}
}

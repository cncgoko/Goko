package org.goko.gcode.filesender;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.goko.core.common.event.GokoTopic;

public class OpenGCodeFileHandler {
	@Inject
	private IEventBroker broker;
	
	@Execute
	public void executeOpenFile(Shell shell){
		FileDialog dialog = new FileDialog(shell);
		dialog.setText("Open GCode file...");

		String filePath = dialog.open(); 
		if(StringUtils.isNotBlank(filePath)){
			Map<String, String> propertyMap = new HashMap<String, String>();
			propertyMap.put(GokoTopic.File.PROPERTY_FILEPATH, filePath);
			broker.post(GokoTopic.File.Open.TOPIC, propertyMap);
		}
	}
}

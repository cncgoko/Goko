package org.goko.core.viewer.renderer.gcode;

import java.lang.ref.WeakReference;

import org.goko.core.gcode.bean.GCodeCommand;
import org.goko.core.viewer.renderer.AbstractViewer3DRenderer;

public abstract class AbstractGCodeCommandRenderer<T extends GCodeCommand> extends AbstractViewer3DRenderer {
	private WeakReference<T> commandRef;

	public AbstractGCodeCommandRenderer(T command){
		commandRef = new WeakReference<T>(command);
	}

	protected T getGCodeCommand(){
		if(commandRef != null ){
			return commandRef.get();
		}
		return null;
	}

	public void setGCodeCommand(T command){
		this.commandRef = new WeakReference<T>(command);
	}
}

/**
 * 
 */
package org.goko.tools.camera.part;

import java.awt.Dimension;
import java.util.List;

import org.goko.common.bindings.AbstractModelObject;
import org.goko.common.elements.combo.LabeledValue;

import com.github.sarxos.webcam.Webcam;

/**
 * @author PsyKo
 * @date 6 nov. 2015
 */
public class CameraDisplayPartModel extends AbstractModelObject {
	/** Device name property */
	public static final String DEVICE = "device";
	/** Device list property */
	public static final String DEVICE_LIST = "deviceList";
	/** Supported resolution list property */
	public static final String RESOLUTION_LIST = "resolutionList";
	/** Selected resolution list property */
	public static final String RESOLUTION = "resolution";
	/** Display state */
	public static final String VIDEO_DISPLAY_STARTED = "videoDisplayStarted";	
	/** Name of the selected device */
	private LabeledValue<Webcam> deviceName;
	/** Available webcam */
	private List<LabeledValue<Webcam>> deviceList;
	/** Available webcam */
	private List<LabeledValue<Dimension>> resolutionList;
	/** Display state */
	private boolean videoDisplayStarted;
	/** Selected resolution */
	private LabeledValue<Dimension> resolution;
	
	/**
	 * @return the deviceName
	 */
	public LabeledValue<Webcam> getDevice() {
		return deviceName;
	}

	/**
	 * @param deviceName the deviceName to set
	 */
	public void setDevice(LabeledValue<Webcam> deviceName) {
		firePropertyChange(DEVICE,this.deviceName, this.deviceName = deviceName);
	}

	/**
	 * @return the lstWebcam
	 */
	public List<LabeledValue<Webcam>> getDeviceList() {
		return deviceList;
	}

	/**
	 * @param lstWebcam the lstWebcam to set
	 */
	public void setDeviceList(List<LabeledValue<Webcam>> lstWebcam) {
		firePropertyChange(DEVICE_LIST,this.deviceList, this.deviceList = lstWebcam);
	}

	
	/**
	 * @return the videoDisplayStarted
	 */
	public boolean isVideoDisplayStarted() {
		return videoDisplayStarted;
	}

	/**
	 * @param videoDisplayStarted the videoDisplayStarted to set
	 */
	public void setVideoDisplayStarted(boolean videoDisplayStarted) {
		firePropertyChange(VIDEO_DISPLAY_STARTED,this.videoDisplayStarted, this.videoDisplayStarted = videoDisplayStarted);
	}

	/**
	 * @return the resolutionList
	 */
	public List<LabeledValue<Dimension>> getResolutionList() {
		return resolutionList;
	}

	/**
	 * @param resolutionList the resolutionList to set
	 */
	public void setResolutionList(List<LabeledValue<Dimension>> resolutionList) {
		firePropertyChange(RESOLUTION_LIST,this.resolutionList, this.resolutionList = resolutionList);	
	}

	/**
	 * @return the resolution
	 */
	public LabeledValue<Dimension> getResolution() {
		return resolution;
	}

	/**
	 * @param resolution the resolution to set
	 */
	public void setResolution(LabeledValue<Dimension> resolution) {
		firePropertyChange(RESOLUTION,this.resolution, this.resolution = resolution);
	}

	
}

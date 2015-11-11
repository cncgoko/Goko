/**
 * 
 */
package org.goko.tools.camera.part;

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
	/** Display state */
	public static final String VIDEO_DISPLAY_STARTED = "videoDisplayStarted";
	/** Name of the selected device */
	private LabeledValue<Webcam> deviceName;
	/** Available webcam */
	private List<LabeledValue<Webcam>> deviceList;
	/** Display state */
	private boolean videoDisplayStarted;
	
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

	
	
}

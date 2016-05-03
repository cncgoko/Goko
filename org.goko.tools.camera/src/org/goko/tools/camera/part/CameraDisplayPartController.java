package org.goko.tools.camera.part;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.goko.common.GkUiUtils;
import org.goko.common.bindings.AbstractController;
import org.goko.common.elements.combo.LabeledValue;
import org.goko.core.common.exception.GkException;

import com.github.sarxos.webcam.Webcam;

/**
 * @author PsyKo
 * @date 6 nov. 2015
 */
public class CameraDisplayPartController extends AbstractController<CameraDisplayPartModel> {

	/**
	 * Constructor
	 */
	public CameraDisplayPartController() {
		super(new CameraDisplayPartModel());
	}

	/** (inheritDoc)
	 * @see org.goko.common.bindings.AbstractController#initialize()
	 */
	@Override
	public void initialize() throws GkException {
		updateDeviceCombo();
		
		List<LabeledValue<Dimension>> lstResolution = new ArrayList<LabeledValue<Dimension>>();
		lstResolution.add( new LabeledValue<Dimension>(new Dimension(320,240), "320x240"));
		lstResolution.add( new LabeledValue<Dimension>(new Dimension(640,480), "640x480"));
		lstResolution.add( new LabeledValue<Dimension>(new Dimension(1024,768), "1024x768"));
		lstResolution.add( new LabeledValue<Dimension>(new Dimension(1280,720), "1280x720"));
		getDataModel().setResolutionList(lstResolution);
		getDataModel().setResolution(GkUiUtils.getLabelledValueByKey(new Dimension(1280,720), lstResolution));
	}


	public void updateDeviceCombo(){
		List<Webcam> lstWebcams = Webcam.getWebcams();
		List<LabeledValue<Webcam>> lstValues = new ArrayList<LabeledValue<Webcam>>();
		
		if(CollectionUtils.isNotEmpty(lstWebcams)){
			for (Webcam webcam : lstWebcams) {
				lstValues.add(new LabeledValue<Webcam>(webcam, webcam.getName()));
			}
			getDataModel().setDeviceList(lstValues);
			if(getDataModel().getDevice() == null){
				getDataModel().setDevice(lstValues.get(0));
			}
		}else{
			getDataModel().setDeviceList(lstValues);
			getDataModel().setDevice(null);
		}
		
	}
}

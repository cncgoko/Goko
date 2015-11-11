package org.goko.tools.camera.part;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
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

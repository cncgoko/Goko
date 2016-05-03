package org.goko.tools.camera;

import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.wb.swt.ResourceManager;
import org.goko.common.GkUiComponent;
import org.goko.common.elements.combo.GkCombo;
import org.goko.common.elements.combo.LabeledValue;
import org.goko.core.common.exception.GkException;
import org.goko.core.log.GkLog;
import org.goko.tools.camera.part.CameraDisplayPartController;
import org.goko.tools.camera.part.CameraDisplayPartModel;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamDiscoveryEvent;
import com.github.sarxos.webcam.WebcamDiscoveryListener;
import com.github.sarxos.webcam.WebcamException;

public class CameraDisplayPart extends GkUiComponent<CameraDisplayPartController, CameraDisplayPartModel> implements WebcamDiscoveryListener, PropertyChangeListener {
	/** LOG */
	private static final GkLog LOG = GkLog.getLogger(CameraDisplayPart.class);	
	private WebcamComposite webcamComposite;
	private GkCombo<LabeledValue<Webcam>> comboDevice;
	private GkCombo<LabeledValue<Dimension>> comboResolution;
	
	@Inject
	public CameraDisplayPart(IEclipseContext context) {
		super(context, new CameraDisplayPartController());		
	}
	
	@PostConstruct
	public void postConstruct(Composite parent) throws IOException, WebcamException, TimeoutException, GkException {
		parent.setLayout(new GridLayout(1, false));
		
		Composite composite_1 = new Composite(parent, SWT.NONE);
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout gl_composite_1 = new GridLayout(1, false);
		gl_composite_1.verticalSpacing = 0;
		gl_composite_1.marginWidth = 0;
		gl_composite_1.marginHeight = 0;
		gl_composite_1.horizontalSpacing = 0;
		composite_1.setLayout(gl_composite_1);
		
		Composite ToolbarComposite = new Composite(composite_1, SWT.NONE);
		ToolbarComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		ToolbarComposite.setLayout(new GridLayout(4, false));
		
		comboDevice = new GkCombo(ToolbarComposite, SWT.READ_ONLY);
		Combo combo = comboDevice.getCombo();
		GridData gd_combo = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_combo.widthHint = 130;
		combo.setLayoutData(gd_combo);

		comboResolution = new GkCombo<LabeledValue<Dimension>>(ToolbarComposite, SWT.READ_ONLY);
		Combo comboResolution2 = comboResolution.getCombo();
		GridData gd_comboResolution = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_comboResolution.widthHint = 80;
		comboResolution2.setLayoutData(gd_comboResolution);
		
		Button btnStartVideo = new Button(ToolbarComposite, SWT.NONE);
		btnStartVideo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				getDataModel().setVideoDisplayStarted(true);
			}
		});
		
		Button btnStopVideo = new Button(ToolbarComposite, SWT.NONE);
		btnStopVideo.setImage(ResourceManager.getPluginImage("org.goko.tools.camera", "resources/icons/stop.gif"));
		btnStopVideo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				getDataModel().setVideoDisplayStarted(false);
			}
		});
		getController().addEnableBinding(btnStopVideo, CameraDisplayPartModel.VIDEO_DISPLAY_STARTED);
		
		btnStartVideo.setImage(ResourceManager.getPluginImage("org.goko.tools.camera", "resources/icons/control.png"));
		
		final Composite composite = new Composite(composite_1, SWT.BORDER | SWT.EMBEDDED);
		composite.setLayout(new FillLayout(SWT.HORIZONTAL));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
				
		webcamComposite = new WebcamComposite(composite, SWT.NONE);		
		webcamComposite.setFPS(20.0);

		webcamComposite.setFillArea(true);
		
		getController().addItemsBinding(comboDevice, CameraDisplayPartModel.DEVICE_LIST);		
		getController().addItemSelectionBinding(comboDevice, CameraDisplayPartModel.DEVICE);		
		getController().addEnableReverseBinding(comboDevice, CameraDisplayPartModel.VIDEO_DISPLAY_STARTED);
		
		getController().addItemsBinding(comboResolution, CameraDisplayPartModel.RESOLUTION_LIST);
		getController().addItemSelectionBinding(comboResolution, CameraDisplayPartModel.RESOLUTION);
		getController().addEnableReverseBinding(comboResolution, CameraDisplayPartModel.VIDEO_DISPLAY_STARTED);
		
		getController().addEnableReverseBinding(btnStartVideo, CameraDisplayPartModel.VIDEO_DISPLAY_STARTED);
				
		Webcam.addDiscoveryListener(this);
		
		getDataModel().addPropertyChangeListener(this);
		if(getDataModel().getDevice() != null){
			webcamComposite.setWebcam(getDataModel().getDevice().getValue());
		}
	}
		
	@PreDestroy
	public void preDestroy(){
		if(webcamComposite.isStarted()){
			webcamComposite.stop();
		}
	}

	/** (inheritDoc)
	 * @see com.github.sarxos.webcam.WebcamDiscoveryListener#webcamFound(com.github.sarxos.webcam.WebcamDiscoveryEvent)
	 */
	@Override
	public void webcamFound(WebcamDiscoveryEvent arg0) {
		getController().updateDeviceCombo();
	}

	/** (inheritDoc)
	 * @see com.github.sarxos.webcam.WebcamDiscoveryListener#webcamGone(com.github.sarxos.webcam.WebcamDiscoveryEvent)
	 */
	@Override
	public void webcamGone(WebcamDiscoveryEvent arg0) {
		getController().updateDeviceCombo();
	}

	/** (inheritDoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {

		if(CameraDisplayPartModel.VIDEO_DISPLAY_STARTED.equals(evt.getPropertyName())){
			if(getDataModel().isVideoDisplayStarted()){				
				Webcam webcam = webcamComposite.getWebcam();
				webcam.setCustomViewSizes(new Dimension[] { getDataModel().getResolution().getValue() }); // register custom resolution
				webcam.setViewSize(getDataModel().getResolution().getValue()); // set it
				webcamComposite.start();
			}else{
				webcamComposite.stop();				
			}
		}else if(CameraDisplayPartModel.DEVICE.equals(evt.getPropertyName())){
			if(webcamComposite.isStarted()){
				webcamComposite.stop();
			}			
			
			if(getDataModel().getDevice() != null){				
				webcamComposite.setWebcam(getDataModel().getDevice().getValue());
				
				if(getDataModel().isVideoDisplayStarted()){
					webcamComposite.start();	
				}				
			}
		}
	}
}
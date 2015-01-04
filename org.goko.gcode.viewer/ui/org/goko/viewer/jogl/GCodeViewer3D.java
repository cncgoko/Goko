/*******************************************************************************
 * 	This file is part of Goko.
 *
 *   Goko is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   Goko is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with Goko.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package org.goko.viewer.jogl;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.PersistState;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.opengl.GLData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.wb.swt.ResourceManager;
import org.goko.common.GkUiComponent;
import org.goko.core.common.exception.GkException;
import org.goko.core.controller.IControllerService;
import org.goko.core.gcode.service.IGCodeService;
import org.goko.viewer.jogl.model.GCodeViewer3DController;
import org.goko.viewer.jogl.model.GCodeViewer3DModel;
import org.goko.viewer.jogl.service.IJoglViewerService;

import com.jogamp.opengl.util.Animator;

public class GCodeViewer3D extends GkUiComponent<GCodeViewer3DController, GCodeViewer3DModel>{
	@Inject IGCodeService gcodeService;
	@Inject IJoglViewerService viewerService;
	@Inject IControllerService controllerService;

	 /** Widget that displays OpenGL content. */
    private GokoJoglCanvas glcanvas;

    private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Animator animator;
    private static final String VIEWER_ENABLED = "org.goko.gcode.viewer.enabled";
    private static final String VIEWER_GRID_ENABLED = "org.goko.gcode.viewer.gridEnabled";
    private static final String VIEWER_LOCK_CAMERA_ON_TOOL = "org.goko.gcode.viewer.lockCameraOnTool";
    private static final String VIEWER_COORDINATE_SYSTEM_ENABLED = "org.goko.gcode.viewer.coordinateSystemEnabled";

    /**
     * Part constructor
     * @param context IEclipseContext
     * @throws GkException
     */
    @Inject
	public GCodeViewer3D(IEclipseContext context) throws GkException {
    	super(new GCodeViewer3DController(new GCodeViewer3DModel()));
    	ContextInjectionFactory.inject(getController(), context);
    	getController().initialize();
	}

	/*@Inject
	@Optional
	private void getNotified(@UIEventTopic("gcodefile") IGCodeProvider file) throws GkException{
		getController().setGCodeFile(file);
	}*/

	@PostConstruct
    public void createPartControl( Composite superCompositeParent, IEclipseContext context,MPart part ) throws GkException {
		Composite compositeParent = new Composite(superCompositeParent, SWT.NONE);
		formToolkit.adapt(compositeParent);
		formToolkit.paintBordersFor(compositeParent);
		GLData gldata = new GLData();
        gldata.doubleBuffer = true;
        GridLayout gl_compositeParent = new GridLayout(1, false);
        gl_compositeParent.verticalSpacing = 0;
        gl_compositeParent.marginWidth = 0;
        gl_compositeParent.marginHeight = 0;
        compositeParent.setLayout(gl_compositeParent);

        Composite composite = new Composite(compositeParent, SWT.NONE);
        GridLayout gl_composite = new GridLayout(6, false);
        gl_composite.verticalSpacing = 3;
        gl_composite.marginHeight = 3;
        composite.setLayout(gl_composite);

        final Button viewEnableBtn = formToolkit.createButton(composite, "", SWT.TOGGLE);
        viewEnableBtn.setImage(ResourceManager.getPluginImage("org.goko.gcode.viewer", "icons/activated.png"));
        viewEnableBtn.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseUp(MouseEvent e) {
        		getController().setRenderEnabled(viewEnableBtn.getSelection());
        	}
        });

        final Button toolFollowBtn = formToolkit.createButton(composite, "", SWT.TOGGLE);
        toolFollowBtn.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseUp(MouseEvent e) {
        		try {
					getController().setLockCameraOnTool(toolFollowBtn.getSelection());
				} catch (GkException e1) {
					displayMessage(e1);
				}
        	}
        });

        toolFollowBtn.setImage(ResourceManager.getPluginImage("org.goko.gcode.viewer", "icons/follow-tool.png"));
        toolFollowBtn.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, true, 1, 1));



        final Button btnShowGrid = new Button(composite, SWT.TOGGLE);
        btnShowGrid.setImage(ResourceManager.getPluginImage("org.goko.gcode.viewer", "icons/grid.png"));
        btnShowGrid.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseUp(MouseEvent e) {
        		try {
					getController().setShowGrid(btnShowGrid.getSelection());
				} catch (GkException e1) {
					displayMessage(e1);
				}
        	}
        });
        final Combo combo = new Combo(composite, SWT.READ_ONLY);
        combo.setItems(new String[] {"Perspective", "Orthographic"});
        combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        formToolkit.adapt(combo);
        formToolkit.paintBordersFor(combo);
        combo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try{
					if(glcanvas != null){
						if(combo.getSelectionIndex() == 0){
							getController().setPerspectiveCamera();

						}else if(combo.getSelectionIndex() == 1){
							getController().setOrthographicCamera();
						}
				}
				}catch (GkException e2) {
					displayMessage(e2);
				}
			}
		});
        combo.select(0);

        glcanvas = viewerService.createCanvas(compositeParent);
        glcanvas.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

        animator = new Animator();

        animator.add(glcanvas);
        animator.start();

        ContextInjectionFactory.inject(glcanvas, context);

        final Button btnShowCoordinatesSystems = new Button(composite, SWT.TOGGLE);
        btnShowCoordinatesSystems.setToolTipText("Show coordinates systems");
        btnShowCoordinatesSystems.setImage(ResourceManager.getPluginImage("org.goko.gcode.viewer", "icons/show-cs.png"));
        formToolkit.adapt(btnShowCoordinatesSystems, true, true);

        btnShowCoordinatesSystems.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseUp(MouseEvent e) {
        		if(glcanvas != null){
        			try {
						getController().setShowCoordinateSystem(btnShowCoordinatesSystems.getSelection());
					} catch (GkException e1) {
						displayMessage(e1);
					}
        		}
        	}
        });

        // Binding for state saving
        getController().addSelectionBinding(viewEnableBtn, "enabled");
        getController().addSelectionBinding(btnShowGrid, "showGrid");
		getController().addSelectionBinding(toolFollowBtn, "followTool");
		getController().addSelectionBinding(btnShowCoordinatesSystems, "showCoordinateSystem");

		final Button btnKeyboardJog = new Button(composite, SWT.FLAT | SWT.TOGGLE);
		btnKeyboardJog.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				glcanvas.setKeyboardJogEnabled(btnKeyboardJog.getSelection());

			}
		});

		btnKeyboardJog.setImage(ResourceManager.getPluginImage("org.goko.gcode.viewer", "icons/keyboard--arrow.png"));
		formToolkit.adapt(btnKeyboardJog, true, true);

		//glcanvas.setMenu(initContextualMenu(composite));



		Map<String, String> state = part.getPersistedState();
		String viewerEnabledStr = state.get(VIEWER_ENABLED);
		if(StringUtils.isNotEmpty(viewerEnabledStr)){
			getDataModel().setEnabled(BooleanUtils.toBoolean(viewerEnabledStr));
			getController().setRenderEnabled(BooleanUtils.toBoolean(viewerEnabledStr));
		}
		String gridEnabledStr = state.get(VIEWER_GRID_ENABLED);
		if(StringUtils.isNotEmpty(gridEnabledStr)){
			getDataModel().setShowGrid(BooleanUtils.toBoolean(gridEnabledStr));
			getController().setShowGrid(BooleanUtils.toBoolean(gridEnabledStr));
		}
		String lockCameraToolStr = state.get(VIEWER_LOCK_CAMERA_ON_TOOL);
		if(StringUtils.isNotEmpty(lockCameraToolStr)){
			getDataModel().setFollowTool(BooleanUtils.toBoolean(lockCameraToolStr));
			getController().setLockCameraOnTool(BooleanUtils.toBoolean(lockCameraToolStr));
		}
		String csEnabledStr = state.get(VIEWER_COORDINATE_SYSTEM_ENABLED);
		if(StringUtils.isNotEmpty(csEnabledStr)){
			getDataModel().setShowCoordinateSystem(BooleanUtils.toBoolean(csEnabledStr));
			getController().setShowCoordinateSystem(BooleanUtils.toBoolean(csEnabledStr));
		}
    }

//	protected Menu initContextualMenu(Control composite) throws GkException{
//		final Menu menu = new Menu(composite);
//
//		MenuItem mntmNewSubmenu = new MenuItem(menu, SWT.CASCADE);
//		mntmNewSubmenu.setText("Camera");
//
//		Menu cameraSubmenu = new Menu(mntmNewSubmenu);
//		mntmNewSubmenu.setMenu(cameraSubmenu);
//		List<AbstractCamera> cameras = viewerService.getSupportedCamera();
//		for (final AbstractCamera abstractCamera : cameras) {
//			MenuItem cameraItem = new MenuItem(cameraSubmenu, SWT.NONE);
//			cameraItem.setText(abstractCamera.getLabel());
//			cameraItem.addSelectionListener(new SelectionAdapter() {
//				@Override
//				public void widgetSelected(SelectionEvent e) {
//					try {
//						viewerService.setActiveCamera(abstractCamera.getId());
//					} catch (GkException exception) {
//						displayMessage(exception);
//					}
//				}
//			});
//		}
//		return menu;
//	}

	@PreDestroy
	public void dispose(MPart part) {
		if(animator != null && animator.isStarted()){
			animator.stop();
		}
	}

	@PersistState
	public void persist(MPart part) {
		if(getDataModel() != null){
			part.getPersistedState().put(VIEWER_ENABLED, String.valueOf(getDataModel().isEnabled()));
			part.getPersistedState().put(VIEWER_GRID_ENABLED, String.valueOf(getDataModel().isShowGrid()));
			part.getPersistedState().put(VIEWER_LOCK_CAMERA_ON_TOOL, String.valueOf(getDataModel().isFollowTool()));
			part.getPersistedState().put(VIEWER_COORDINATE_SYSTEM_ENABLED, String.valueOf(getDataModel().isShowCoordinateSystem()));
		}
	}

	@Focus
	public void setFocus() {
		// TODO	Set the focus to control
	}
}

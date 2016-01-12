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
package org.goko.tools.viewer.jogl;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.PersistState;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.opengl.GLData;
import org.eclipse.swt.widgets.Composite;
import org.goko.common.GkUiComponent;
import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.rs274ngcv3.IRS274NGCService;
import org.goko.tools.viewer.jogl.model.GCodeViewer3DController;
import org.goko.tools.viewer.jogl.model.GCodeViewer3DModel;
import org.goko.tools.viewer.jogl.service.IJoglViewerService;
import org.osgi.service.event.EventHandler;

import com.jogamp.opengl.util.FPSAnimator;

public class GCodeViewer3D extends GkUiComponent<GCodeViewer3DController, GCodeViewer3DModel> implements EventHandler {
	@Inject
	IRS274NGCService gcodeService;
	@Inject
	IJoglViewerService viewerService;
	@Inject
	private IEventBroker broker;

	/** Widget that displays OpenGL content. */
	private GokoJoglCanvas glcanvas;
	private FPSAnimator animator;
	private static final String VIEWER_ENABLED = "org.goko.tools.viewer.jogl.enabled";
	private static final String VIEWER_GRID_ENABLED = "org.goko.tools.viewer.jogl.gridEnabled";
	private static final String VIEWER_BOUNDS_ENABLED = "org.goko.tools.viewer.jogl.boundsEnabled";
	private static final String VIEWER_LOCK_CAMERA_ON_TOOL = "org.goko.tools.viewer.jogl.lockCameraOnTool";
	private static final String VIEWER_COORDINATE_SYSTEM_ENABLED = "org.goko.tools.viewer.jogl.coordinateSystemEnabled";
	public  static final String TOPIC_ENABLE_KEYBOARD_JOG = "topic/org/goko/tools/viewer/jogl/enableKeyboardJog";

	/**
	 * Part constructor
	 *
	 * @param context
	 *            IEclipseContext
	 * @throws GkException
	 */
	@Inject
	public GCodeViewer3D(IEclipseContext context) throws GkException {
		super(new GCodeViewer3DController(new GCodeViewer3DModel()));
		ContextInjectionFactory.inject(getController(), context);
		getController().initialize();
		context.set(GCodeViewer3DController.class, getController());
	}

	@PostConstruct
	public void createPartControl(Composite superCompositeParent, IEclipseContext context, MPart part) throws GkException {
		Composite compositeParent = new Composite(superCompositeParent, SWT.NONE);
		GLData gldata = new GLData();
		gldata.doubleBuffer = true;
		GridLayout gl_compositeParent = new GridLayout(1, false);
		gl_compositeParent.verticalSpacing = 0;
		gl_compositeParent.marginWidth = 0;
		gl_compositeParent.marginHeight = 0;
		compositeParent.setLayout(gl_compositeParent);

		//final ToolBar toolBar = new ToolBar(compositeParent, SWT.FLAT | SWT.RIGHT);

		glcanvas = viewerService.createCanvas(compositeParent);
		context.getParent().set(GokoJoglCanvas.class, glcanvas);

//		final ToolItem btnEnableView = new ToolItem(toolBar, SWT.CHECK);
//		btnEnableView.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				getController().setRenderEnabled(btnEnableView.getSelection());
//			}
//		});
//		btnEnableView.setToolTipText("Enable/disable 3D view");
//		btnEnableView.setWidth(32);
//		btnEnableView.setHotImage(ResourceManager.getPluginImage("org.goko.tools.viewer.jogl", "resources/icons/activated.png"));
//		btnEnableView.setImage(ResourceManager.getPluginImage("org.goko.tools.viewer.jogl", "resources/icons/activated.png"));
//
//		final ToolItem btnEnableGrid = new ToolItem(toolBar, SWT.CHECK);
//		btnEnableGrid.addSelectionListener(new SelectionAdapter() {
//			@Override public void widgetSelected(SelectionEvent e) {
//				try {
//					getController().setShowGrid(btnEnableGrid.getSelection());
//				} catch (GkException ex) {
//					displayError(ex);
//				}
//			}
//		});
//		btnEnableGrid.setToolTipText("Show/hide grid");
//		btnEnableGrid.setImage(ResourceManager.getPluginImage("org.goko.tools.viewer.jogl", "resources/icons/grid.png"));
//		getController().addSelectionBinding(btnEnableGrid, "showGrid");
//
//		final ToolItem btnEnableBounds = new ToolItem(toolBar, SWT.CHECK);
//		btnEnableBounds.addSelectionListener(new SelectionAdapter() {
//			@Override public void widgetSelected(SelectionEvent e) {
//				try {
//					getController().setDisplayBounds(btnEnableBounds.getSelection());
//				} catch (GkException ex) {
//					displayError(ex);
//				}
//			}
//		});
//		btnEnableBounds.setToolTipText("Show/hide bounds");
//		btnEnableBounds.setImage(ResourceManager.getPluginImage("org.goko.tools.viewer.jogl", "resources/icons/show-bound.png"));
//		getController().addSelectionBinding(btnEnableBounds, "enabled");
//
//		getController().addSelectionBinding(btnEnableView, "enabled");

		 // register context menu on the table
	   // menuService.registerContextMenu(compositeParent, "org.goko.tools.viewer.jogl.popupmenu.0");

//		final ICoordinateSystemAdapter csAdapter = viewerService.getCoordinateSystemAdapter();
//		if(csAdapter != null){
//			final Menu coordinateSystemMenu = new Menu(toolBar.getShell(), SWT.POP_UP);
//			final MenuItem csItemAll = new MenuItem(coordinateSystemMenu, SWT.PUSH);
//			csItemAll.setText("Show all");
//			csItemAll.addSelectionListener(new SelectionAdapter() {
//			  @Override public void widgetSelected(SelectionEvent e) {
//				  try {
//					for (final EnumCoordinateSystem cs : csAdapter.getCoordinateSystem()) {
//						  viewerService.setCoordinateSystemEnabled(cs, true);
//						  getDataModel().getCoordinateSystemEnabled().put(cs.name(), true);
//					  }
//				} catch (GkException e1) {
//					displayError(e1);
//				}
//			  };
//			});
//			MenuItem csItemNone = new MenuItem(coordinateSystemMenu, SWT.PUSH);
//			csItemNone.setText("Hide all");
//			csItemNone.addSelectionListener(new SelectionAdapter() {
//				  @Override public void widgetSelected(SelectionEvent e) {
//					  try {
//						for (final EnumCoordinateSystem cs : csAdapter.getCoordinateSystem()) {
//							  viewerService.setCoordinateSystemEnabled(cs, false);
//							  getDataModel().getCoordinateSystemEnabled().put(cs.name(), false);
//						  }
//					} catch (GkException e1) {
//						displayError(e1);
//					}
//				  };
//				});
//			MenuItem csItemSeparator = new MenuItem(coordinateSystemMenu, SWT.SEPARATOR);
//			// Create the menu for CS display
//			for (final EnumCoordinateSystem cs : csAdapter.getCoordinateSystem()) {
//		      final MenuItem item = new MenuItem(coordinateSystemMenu, SWT.CHECK);
//		      item.setText(cs.name());
//		      viewerService.setCoordinateSystemEnabled(cs, true);
//    		  getDataModel().getCoordinateSystemEnabled().put(cs.name(), true);
//		      item.addSelectionListener(new SelectionAdapter() {
//		    	  @Override public void widgetSelected(SelectionEvent e) {
//		    		  viewerService.setCoordinateSystemEnabled(cs, item.getSelection());
//		    		  getDataModel().getCoordinateSystemEnabled().put(cs.name(), item.getSelection());
//		    	  };
//		      });
//		      {
//		    	  IObservableValue 	modelObservable 	= Observables.observeMapEntry(getDataModel().getCoordinateSystemEnabled(), cs.name());
//		    	  IObservableValue  controlObservable 	= WidgetProperties.selection().observe(item);
//
//		    	  UpdateValueStrategy policy = new UpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE);
//		    	  getController().getBindingContext().bindValue(controlObservable, modelObservable,null, policy);
//		      }
//		    }
//
//			final ToolItem tltmDropdownItem = new ToolItem(toolBar, SWT.DROP_DOWN);
//			tltmDropdownItem.setToolTipText("Show/hide coordinates systems");
//			tltmDropdownItem.setImage(ResourceManager.getPluginImage("org.goko.tools.viewer.jogl", "resources/icons/show-cs.png"));
//			tltmDropdownItem.addListener(SWT.Selection, new Listener() {
//				@Override
//				public void handleEvent(Event event) {
//					Rectangle rect = tltmDropdownItem.getBounds();
//					Point pt = new Point(rect.x, rect.y + rect.height);
//					pt = toolBar.toDisplay(pt);
//					coordinateSystemMenu.setLocation(pt.x, pt.y);
//					coordinateSystemMenu.setVisible(true);
//				}
//			});
//		}



//		ToolItem toolItem = new ToolItem(toolBar, SWT.SEPARATOR);
//
//		ToolItem btnZoomExtent = new ToolItem(toolBar, SWT.NONE);
//		btnZoomExtent.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				try {
//					getController().zoomToFit();
//				} catch (GkException ex) {
//					displayError(ex);
//				}
//			}
//		});
//		btnZoomExtent.setToolTipText("Zoom extents");
//		btnZoomExtent.setWidth(32);
//		btnZoomExtent.setImage(ResourceManager.getPluginImage("org.goko.tools.viewer.jogl", "resources/icons/layer-resize.png"));
//
//		final ToolItem btnView = new ToolItem(toolBar, SWT.DROP_DOWN);
//		btnView.setImage(ResourceManager.getPluginImage("org.goko.tools.viewer.jogl", "resources/icons/projection-screen.png"));
//		btnView.setToolTipText("Change view");
//		final Menu viewMenu = new Menu(toolBar.getShell(), SWT.POP_UP);
//
//		for (AbstractCamera camera: viewerService.getSupportedCamera()) {
//			MenuItem btnCamera = new MenuItem(viewMenu, SWT.PUSH);
//			btnCamera.setText(camera.getLabel());
//			final String cameraId = camera.getId();
//
//			btnCamera.addSelectionListener(new SelectionAdapter() {
//
//				@Override
//				public void widgetSelected(SelectionEvent e) {
//					try {
//						viewerService.setActiveCamera(cameraId);
//					} catch (GkException e1) {
//						displayError(e1);
//					}
//				}
//			});
//		}
//
//
//		btnView.addListener(SWT.Selection, new Listener() {
//			@Override
//			public void handleEvent(Event event) {
//				Rectangle rect = btnView.getBounds();
//				Point pt = new Point(rect.x, rect.y + rect.height);
//				pt = toolBar.toDisplay(pt);
//				viewMenu.setLocation(pt.x, pt.y);
//				viewMenu.setVisible(true);
//			}
//		});
//
//		ToolItem toolItem_1 = new ToolItem(toolBar, SWT.SEPARATOR);

//		btnKeyboardJog = new ToolItem(toolBar, SWT.CHECK);
//		btnKeyboardJog.addSelectionListener(new SelectionAdapter() {
//			@Override public void widgetSelected(SelectionEvent e) {
//				glcanvas.setFocus();
//				glcanvas.setKeyboardJogEnabled(btnKeyboardJog.getSelection());
//			}
//		});
		glcanvas.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
				glcanvas.setKeyboardJogEnabled(false);
			}

			@Override
			public void focusGained(FocusEvent e) {}
		});
//		btnKeyboardJog.setToolTipText("Enable/disable keyboard jogging (Ctrl + J)");
//		btnKeyboardJog.setImage(ResourceManager.getPluginImage("org.goko.tools.viewer.jogl", "resources/icons/keyboard--arrow.png"));



		glcanvas.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		animator = new FPSAnimator(30);

		animator.add(glcanvas);
		animator.start();

		ContextInjectionFactory.inject(glcanvas, context);

		// glcanvas.setMenu(initContextualMenu(composite));

		Map<String, String> state = part.getPersistedState();
		String viewerEnabledStr = state.get(VIEWER_ENABLED);
		if (StringUtils.isNotEmpty(viewerEnabledStr)) {
			getDataModel().setEnabled(BooleanUtils.toBoolean(viewerEnabledStr));
			getController().setRenderEnabled(BooleanUtils.toBoolean(viewerEnabledStr));
		}
		String gridEnabledStr = state.get(VIEWER_GRID_ENABLED);
		if (StringUtils.isNotEmpty(gridEnabledStr)) {
			getDataModel().setShowGrid(BooleanUtils.toBoolean(gridEnabledStr));
			getController().setShowGrid(BooleanUtils.toBoolean(gridEnabledStr));
		}
		String lockCameraToolStr = state.get(VIEWER_LOCK_CAMERA_ON_TOOL);
		if (StringUtils.isNotEmpty(lockCameraToolStr)) {
			getDataModel().setFollowTool(BooleanUtils.toBoolean(lockCameraToolStr));
			getController().setLockCameraOnTool(BooleanUtils.toBoolean(lockCameraToolStr));
		}
		String csEnabledStr = state.get(VIEWER_COORDINATE_SYSTEM_ENABLED);
		if (StringUtils.isNotEmpty(csEnabledStr)) {
			getDataModel().setShowCoordinateSystem(BooleanUtils.toBoolean(csEnabledStr));
			getController().setShowCoordinateSystem(BooleanUtils.toBoolean(csEnabledStr));
		}

		String boundsEnabledStr = state.get(VIEWER_BOUNDS_ENABLED);
		if (StringUtils.isNotEmpty(boundsEnabledStr)) {
			getDataModel().setShowBounds(BooleanUtils.toBoolean(boundsEnabledStr));
			getController().setDisplayBounds(BooleanUtils.toBoolean(boundsEnabledStr));
		}
		
		broker.subscribe(TOPIC_ENABLE_KEYBOARD_JOG, this);
	}

	@PreDestroy
	public void dispose(MPart part) {
		if (animator != null && animator.isStarted()) {
			animator.stop();
		}
	}

	@PersistState
	public void persist(MPart part) {
		if (getDataModel() != null) {
			part.getPersistedState().put(VIEWER_ENABLED, String.valueOf(getDataModel().isEnabled()));
			part.getPersistedState().put(VIEWER_GRID_ENABLED, String.valueOf(getDataModel().isShowGrid()));
			part.getPersistedState().put(VIEWER_LOCK_CAMERA_ON_TOOL, String.valueOf(getDataModel().isFollowTool()));
			part.getPersistedState().put(VIEWER_COORDINATE_SYSTEM_ENABLED, String.valueOf(getDataModel().isShowCoordinateSystem()));
			part.getPersistedState().put(VIEWER_BOUNDS_ENABLED, String.valueOf(getDataModel().isShowBounds()));
		}
	}

	@Focus
	public void setFocus() {
		// TODO Set the focus to control
	}

	/** (inheritDoc)
	 * @see org.osgi.service.event.EventHandler#handleEvent(org.osgi.service.event.Event)
	 */
	@Override
	public void handleEvent(org.osgi.service.event.Event event) {
		if(StringUtils.equals(event.getTopic(),TOPIC_ENABLE_KEYBOARD_JOG)){
			glcanvas.setKeyboardJogEnabled(!glcanvas.isKeyboardJogEnabled());
			glcanvas.setFocus();
		}
	}
}

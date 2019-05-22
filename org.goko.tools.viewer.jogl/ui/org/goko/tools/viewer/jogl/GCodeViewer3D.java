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
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
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

import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.AnimatorBase;

public class GCodeViewer3D extends GkUiComponent<GCodeViewer3DController, GCodeViewer3DModel> implements EventHandler {
	@Inject
	IRS274NGCService gcodeService;
	@Inject
	IJoglViewerService viewerService;
	@Inject
	private IEventBroker broker;

	/** Widget that displays OpenGL content. */
	private GokoJoglCanvas glcanvas;
	private AnimatorBase animator;
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
		super(context, new GCodeViewer3DController(new GCodeViewer3DModel()));		
		context.set(GCodeViewer3DController.class, getController());
	}

	@PostConstruct
	public void createPartControl(Composite superCompositeParent, IEclipseContext context, MPart part) throws GkException {
		Composite compositeParent = new Composite(superCompositeParent, SWT.NO_BACKGROUND);
		GLData gldata = new GLData();
		gldata.doubleBuffer = true;
		GridLayout gl_compositeParent = new GridLayout(1, false);
		gl_compositeParent.verticalSpacing = 0;
		gl_compositeParent.marginWidth = 0;
		gl_compositeParent.marginHeight = 0;
		compositeParent.setLayout(gl_compositeParent);
				
		glcanvas = viewerService.createCanvas(compositeParent);
		
		context.getParent().set(GokoJoglCanvas.class, glcanvas);
		glcanvas.addMouseListener(new MouseAdapter() {
			/** (inheritDoc)
			 * @see org.eclipse.swt.events.MouseAdapter#mouseDown(org.eclipse.swt.events.MouseEvent)
			 */
			@Override
			public void mouseDown(MouseEvent e) {
				// focus on right click				
				glcanvas.setFocus();
				glcanvas.forceFocus();				
			}
		});
		glcanvas.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				glcanvas.setKeyboardJogEnabled(false);				
			}

			@Override
			public void focusGained(FocusEvent e) {
				
			}
		});

		glcanvas.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		animator = new Animator();
		animator.setModeBits(false, AnimatorBase.MODE_EXPECT_AWT_RENDERING_THREAD);
		animator.add(glcanvas);		
		animator.start();

		ContextInjectionFactory.inject(glcanvas, context);

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

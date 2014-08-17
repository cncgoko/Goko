/*
 *
 *   Goko
 *   Copyright (C) 2013  PsyKo
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.goko.gcode.viewer;

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
import org.goko.core.gcode.service.IGCodeService;
import org.goko.gcode.viewer.controller.GCodeViewer3DController;
import org.goko.gcode.viewer.controller.GCodeViewer3DModel;

public class GCodeViewer3D extends GkUiComponent<GCodeViewer3DController, GCodeViewer3DModel>{
	@Inject IGCodeService gcodeService;
	 /** Widget that displays OpenGL content. */
    private GCode3DCanvas glcanvas;

    private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
    private static final String VIEWER_ENABLED = "org.goko.gcode.viewer.enabled";
    private static final String VIEWER_GRID_ENABLED = "org.goko.gcode.viewer.gridEnabled";

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
        GridLayout gl_composite = new GridLayout(3, false);
        gl_composite.verticalSpacing = 3;
        gl_composite.marginHeight = 3;
        composite.setLayout(gl_composite);

        Button btnShowGrid = new Button(composite, SWT.TOGGLE);
        btnShowGrid.setImage(ResourceManager.getPluginImage("org.goko.gcode.viewer", "icons/grid.png"));

        final Combo combo = new Combo(composite, SWT.NONE);
        combo.setItems(new String[] {"Perspective", "Orthographic"});
        combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        formToolkit.adapt(combo);
        formToolkit.paintBordersFor(combo);
        combo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(glcanvas != null){
					if(combo.getSelectionIndex() == 0){
						glcanvas.setPerspectiveCamera();
					}else if(combo.getSelectionIndex() == 1){
						glcanvas.setOrthographicCamera();
					}
				}
			}
		});
        combo.select(0);

        glcanvas = new GCode3DCanvas(compositeParent, SWT.NO_BACKGROUND, gldata);
        glcanvas.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        ContextInjectionFactory.inject(glcanvas, context);
      //  glcanvas.setCurrent();


        Button btnEnabledView = new Button(composite, SWT.CHECK);
        formToolkit.adapt(btnEnabledView, true, true);

        btnEnabledView.setText("Enable 3D view");

        getController().addSelectionBinding(btnShowGrid, "showGrid");
		getController().addPropertyBinding(glcanvas, "showGrid", "showGrid");
		getController().addSelectionBinding(btnEnabledView, "enabled");
		getController().addPropertyBinding(glcanvas, "renderEnabled", "enabled");
		getController().addPropertyBinding(glcanvas, "currentPosition", "currentPosition");
		getController().addPropertyBinding(glcanvas, "commandProvider", "commandProvider");

		Map<String, String> state = part.getPersistedState();
		String viewerEnabledStr = state.get(VIEWER_ENABLED);
		if(StringUtils.isNotEmpty(viewerEnabledStr)){
			getDataModel().setEnabled(BooleanUtils.toBoolean(viewerEnabledStr));
		}
		String gridEnabledStr = state.get(VIEWER_GRID_ENABLED);
		if(StringUtils.isNotEmpty(gridEnabledStr)){
			getDataModel().setShowGrid(BooleanUtils.toBoolean(gridEnabledStr));
		}
    }

	@PreDestroy
	public void dispose() {

	}

	@PersistState
	public void persist(MPart part) {
		if(getDataModel() != null){
			part.getPersistedState().put(VIEWER_ENABLED, String.valueOf(getDataModel().isEnabled()));
			part.getPersistedState().put(VIEWER_GRID_ENABLED, String.valueOf(getDataModel().isShowGrid()));
		}
	}

	@Focus
	public void setFocus() {
		// TODO	Set the focus to control
	}

}

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
package org.goko.tools.centerfinder;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.databinding.viewers.ObservableMapLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.wb.swt.ResourceManager;
import org.goko.common.GkUiComponent;
import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.bean.Tuple6b;
import org.goko.core.log.GkLog;
import org.goko.tools.centerfinder.model.CenterFinderController;
import org.goko.tools.centerfinder.model.CenterFinderModel;


public class CenterFinderPart extends GkUiComponent<CenterFinderController, CenterFinderModel> {
	private static final GkLog LOG = GkLog.getLogger(CenterFinderPart.class);
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Table table;
	private TableViewer tableViewer;
	private Button grabPoint;
	private Label centerXLabel;
	private Label centerYLabel;
	private Label centerZLabel;
	private Button goToCenterBtn;
	private Label radiusLabel;

	@Inject
	public CenterFinderPart(IEclipseContext context) {
		super(new CenterFinderController());
		ContextInjectionFactory.inject(getController(), context);
		try {
			getController().initialize();
		} catch (GkException e) {
			LOG.error(e);
		}
	}

	@PostConstruct
	public void createControls(final Composite parent, MPart part) throws GkException {
		parent.setLayout(new FillLayout(SWT.HORIZONTAL));

		Composite composite = new Composite(parent, SWT.NONE);
		formToolkit.adapt(composite);
		formToolkit.paintBordersFor(composite);
		composite.setLayout(new GridLayout(1, false));

		tableViewer = new TableViewer(composite, SWT.BORDER | SWT.FULL_SELECTION);
		table = tableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		GridData gd_table = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_table.heightHint = 85;
		table.setLayoutData(gd_table);
		formToolkit.paintBordersFor(table);

		TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnX = tableViewerColumn.getColumn();
		tblclmnX.setWidth(100);
		tblclmnX.setText("X");

		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnY = tableViewerColumn_1.getColumn();
		tblclmnY.setWidth(100);
		tblclmnY.setText("Y");

		TableViewerColumn tableViewerColumn_2 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnZ = tableViewerColumn_2.getColumn();
		tblclmnZ.setWidth(100);
		tblclmnZ.setText("Z");

		Composite composite_1 = new Composite(composite, SWT.NONE);
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.adapt(composite_1);
		formToolkit.paintBordersFor(composite_1);
		GridLayout gl_composite_1 = new GridLayout(3, false);
		gl_composite_1.marginWidth = 0;
		gl_composite_1.marginHeight = 0;
		composite_1.setLayout(gl_composite_1);

		grabPoint = formToolkit.createButton(composite_1, "", SWT.NONE);
		grabPoint.setToolTipText("Create point from position");
		grabPoint.setImage(ResourceManager.getPluginImage("org.goko.tools.centerfinder", "icons/grab-point.png"));
		grabPoint.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent event) {
				try{
					getController().grabPoint();
				}catch(GkException e){
					LOG.error(e);
				}
			}
		});

		Button btnNewButton_1 = formToolkit.createButton(composite_1, "", SWT.NONE);
		btnNewButton_1.setToolTipText("Remove selected point");
		btnNewButton_1.setImage(ResourceManager.getPluginImage("org.goko.tools.centerfinder", "icons/eraser.png"));

		goToCenterBtn = new Button(composite_1, SWT.NONE);
		goToCenterBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				getController().goToCalculatedCenter();
			}
		});
		goToCenterBtn.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		goToCenterBtn.setToolTipText("Go to calculated center");
		goToCenterBtn.setImage(ResourceManager.getPluginImage("org.goko.tools.centerfinder", "icons/arrow-step.png"));
		formToolkit.adapt(goToCenterBtn, true, true);
		btnNewButton_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent evt) {
				try {
					getController().clearSelectedSamplePoints();
				} catch (GkException e) {
					displayMessage(e);
				}
			}
		});

		Composite composite_2 = new Composite(composite, SWT.NONE);
		composite_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		formToolkit.adapt(composite_2);
		formToolkit.paintBordersFor(composite_2);
		composite_2.setLayout(new GridLayout(5, false));

		Label lblCenter = new Label(composite_2, SWT.NONE);
		formToolkit.adapt(lblCenter, true, true);
		lblCenter.setText("Center");

		Label lblX = new Label(composite_2, SWT.NONE);
		formToolkit.adapt(lblX, true, true);
		lblX.setText("X:");

		centerXLabel = new Label(composite_2, SWT.NONE);
		GridData gd_centerXLabel = new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1);
		gd_centerXLabel.widthHint = 80;
		centerXLabel.setLayoutData(gd_centerXLabel);
		formToolkit.adapt(centerXLabel, true, true);
		centerXLabel.setText("New Label");

		Label lblRadius = new Label(composite_2, SWT.NONE);
		lblRadius.setText("Radius :");
		formToolkit.adapt(lblRadius, true, true);

		radiusLabel = new Label(composite_2, SWT.NONE);
		GridData gd_radiusLabel = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_radiusLabel.widthHint = 80;
		radiusLabel.setLayoutData(gd_radiusLabel);
		radiusLabel.setText("New Label");
		formToolkit.adapt(radiusLabel, true, true);
		new Label(composite_2, SWT.NONE);

		Label lblY = new Label(composite_2, SWT.NONE);
		formToolkit.adapt(lblY, true, true);
		lblY.setText("Y:");

		centerYLabel = new Label(composite_2, SWT.NONE);
		centerYLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		formToolkit.adapt(centerYLabel, true, true);
		centerYLabel.setText("New Label");
		new Label(composite_2, SWT.NONE);
		new Label(composite_2, SWT.NONE);
		new Label(composite_2, SWT.NONE);

		Label lblZ = new Label(composite_2, SWT.NONE);
		formToolkit.adapt(lblZ, true, true);
		lblZ.setText("Z:");

		centerZLabel = new Label(composite_2, SWT.NONE);
		centerZLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		formToolkit.adapt(centerZLabel, true, true);
		centerZLabel.setText("New Label");
		new Label(composite_2, SWT.NONE);
		new Label(composite_2, SWT.NONE);
		initCustomDataBindings();
	}

	protected DataBindingContext initCustomDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		ObservableListContentProvider listContentProvider = new ObservableListContentProvider();
		IObservableMap[] observeMaps = PojoObservables.observeMaps(listContentProvider.getKnownElements(), Tuple6b.class, new String[]{"x", "y", "z"});
		tableViewer.setLabelProvider(new ObservableMapLabelProvider(observeMaps));
		tableViewer.setContentProvider(listContentProvider);
		//
		tableViewer.setInput(getDataModel().getSamplePoints());
		//
		try {
			getController().addTextDisplayBinding(centerXLabel, "centerXPosition");
			getController().addTextDisplayBinding(centerYLabel, "centerYPosition");
			getController().addTextDisplayBinding(centerZLabel, "centerZPosition");
			getController().addTextDisplayBinding(radiusLabel, "radius");
			getController().addTableSelectionBinding(tableViewer, "selectedPoint");
		} catch (GkException e) {
			LOG.error(e);
		}




		return bindingContext;
	}
}

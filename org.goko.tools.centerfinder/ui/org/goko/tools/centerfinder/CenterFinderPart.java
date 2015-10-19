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

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.wb.swt.ResourceManager;
import org.goko.common.GkUiComponent;
import org.goko.common.dialog.GkDialog;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkFunctionalException;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.quantity.Quantity;
import org.goko.core.config.GokoPreference;
import org.goko.core.log.GkLog;
import org.goko.core.math.Tuple6b;
import org.goko.tools.centerfinder.model.CenterFinderController;
import org.goko.tools.centerfinder.model.CenterFinderModel;


public class CenterFinderPart extends GkUiComponent<CenterFinderController, CenterFinderModel> {
	private static final GkLog LOG = GkLog.getLogger(CenterFinderPart.class);
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
		
		
		composite.setLayout(new GridLayout(1, false));

		tableViewer = new TableViewer(composite, SWT.BORDER | SWT.FULL_SELECTION);
		table = tableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		GridData gd_table = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_table.heightHint = 85;
		table.setLayoutData(gd_table);
		

		TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnX = tableViewerColumn.getColumn();
		tblclmnX.setWidth(100);
		tblclmnX.setText("X");
		tableViewerColumn.setLabelProvider(new QuantityLableProvider(0));
		
		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnY = tableViewerColumn_1.getColumn();
		tblclmnY.setWidth(100);
		tblclmnY.setText("Y");
		tableViewerColumn_1.setLabelProvider(new QuantityLableProvider(1));
		
		TableViewerColumn tableViewerColumn_2 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnZ = tableViewerColumn_2.getColumn();
		tblclmnZ.setWidth(100);
		tblclmnZ.setText("Z");
		tableViewerColumn_2.setLabelProvider(new QuantityLableProvider(2));
		
		Composite composite_1 = new Composite(composite, SWT.NONE);
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		
		GridLayout gl_composite_1 = new GridLayout(3, false);
		gl_composite_1.marginWidth = 0;
		gl_composite_1.marginHeight = 0;
		composite_1.setLayout(gl_composite_1);

		grabPoint = new Button(composite_1, SWT.NONE);
		grabPoint.setToolTipText("Create point from position");
		grabPoint.setImage(ResourceManager.getPluginImage("org.goko.tools.centerfinder", "icons/grab-point.png"));
		grabPoint.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent event) {
				try{
					getController().grabPoint();
				}catch(GkFunctionalException e){
					LOG.warn(e.getLocalizedMessage());
					GkDialog.openDialog(e);		
				}catch(GkException e){
					LOG.error(e.getLocalizedMessage());
					GkDialog.openDialog(e);
				}
			}
		});

		Button btnNewButton_1 = new Button(composite_1, SWT.NONE);
		btnNewButton_1.setToolTipText("Remove selected point");
		btnNewButton_1.setImage(ResourceManager.getPluginImage("org.goko.tools.centerfinder", "icons/eraser.png"));

//		goToCenterBtn = new Button(composite_1, SWT.NONE);
//		goToCenterBtn.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseUp(MouseEvent e) {
//				getController().goToCalculatedCenter();
//			}
//		});
//		goToCenterBtn.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
//		goToCenterBtn.setToolTipText("Go to calculated center");
//		goToCenterBtn.setImage(ResourceManager.getPluginImage("org.goko.tools.centerfinder", "icons/arrow-step.png"));
		
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
		
		
		composite_2.setLayout(new GridLayout(5, false));

		Label lblCenter = new Label(composite_2, SWT.NONE);
		
		lblCenter.setText("Center");

		Label lblX = new Label(composite_2, SWT.NONE);
		
		lblX.setText("X:");

		centerXLabel = new Label(composite_2, SWT.NONE);
		GridData gd_centerXLabel = new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1);
		gd_centerXLabel.widthHint = 80;
		centerXLabel.setLayoutData(gd_centerXLabel);
		
		centerXLabel.setText("New Label");

		Label lblRadius = new Label(composite_2, SWT.NONE);
		lblRadius.setText("Radius :");
		

		radiusLabel = new Label(composite_2, SWT.NONE);
		GridData gd_radiusLabel = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_radiusLabel.widthHint = 80;
		radiusLabel.setLayoutData(gd_radiusLabel);
		radiusLabel.setText("New Label");
		
		new Label(composite_2, SWT.NONE);

		Label lblY = new Label(composite_2, SWT.NONE);
		
		lblY.setText("Y:");

		centerYLabel = new Label(composite_2, SWT.NONE);
		centerYLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		
		centerYLabel.setText("New Label");
		new Label(composite_2, SWT.NONE);
		new Label(composite_2, SWT.NONE);
		new Label(composite_2, SWT.NONE);

		Label lblZ = new Label(composite_2, SWT.NONE);
		
		lblZ.setText("Z:");

		centerZLabel = new Label(composite_2, SWT.NONE);
		centerZLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		
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
		//A CORRIGER (les valeurs ne s'affichent pas bien suite au passage des tuples en Quantity)
		//tableViewer.setLabelProvider(new ObservableMapLabelProvider(observeMaps));
		//tableViewer.setLabelProvider(new QuantityLableProvider());
		
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
	
	class QuantityLableProvider extends CellLabelProvider{
		private int axis;
		
		/**
		 * @param axis
		 */
		public QuantityLableProvider(int axis) {
			super();
			this.axis = axis;
		}

		public String getText(Object element) {			
			if(element == null ){
				return StringUtils.EMPTY;
			}
			Tuple6b tuple = (Tuple6b) element;			
			Quantity<Length> quantity = tuple.getX();
			if(axis == 1){
				quantity = tuple.getY();
			}else if(axis == 2){
				quantity = tuple.getZ();
			}
			try {
				return GokoPreference.getInstance().format(quantity, true, true);
			} catch (GkException e) {
				LOG.error(e);
			}
			return StringUtils.EMPTY;
		}

		@Override
		public void update(ViewerCell cell) {
			cell.setText(getText(cell.getElement()));
		}
	}
}

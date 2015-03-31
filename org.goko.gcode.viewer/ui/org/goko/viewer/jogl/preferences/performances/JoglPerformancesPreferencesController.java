/*
 *	This file is part of Goko.
 *
 *  Goko is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Goko is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Goko.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.goko.viewer.jogl.preferences.performances;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.goko.common.GkUiUtils;
import org.goko.common.bindings.AbstractController;
import org.goko.common.elements.combo.LabeledValue;
import org.goko.core.common.exception.GkException;
import org.goko.core.config.GokoConfig;
import org.goko.core.log.GkLog;
import org.goko.viewer.jogl.service.JoglViewerSettings;

/**
 * Controller of performances settings
 * @author PsyKo
 *
 */
public class JoglPerformancesPreferencesController extends AbstractController<JoglPerformancesPreferencesModel> implements IPropertyChangeListener {
	private static final GkLog LOG = GkLog.getLogger(JoglPerformancesPreferencesController.class);

	/**
	 * Constructor
	 */
	public JoglPerformancesPreferencesController() {
		super(new JoglPerformancesPreferencesModel());
		try{
			initialize();
			GokoConfig.getInstance().getPreferences().addPropertyChangeListener(this);
		}catch(GkException e){
			LOG.error(e);
		}
	}

	/**
	 * @throws GkException
	 */
	@Override
	public void initialize() throws GkException {
		List<LabeledValue<Integer>> lstMultiSampling = new ArrayList<LabeledValue<Integer>>();
		lstMultiSampling.add( new LabeledValue<Integer>(1, "1x (Fastest)"));
		lstMultiSampling.add( new LabeledValue<Integer>(2, "2x"));
		lstMultiSampling.add( new LabeledValue<Integer>(4, "4x"));
		lstMultiSampling.add( new LabeledValue<Integer>(8, "8x (Nicest)"));
		getDataModel().setMultisamplingChoice(lstMultiSampling);

		Integer multiSampling  = JoglViewerSettings.getInstance().getMultisampling();
		getDataModel().setMultisampling(GkUiUtils.getLabelledValueByKey(multiSampling, lstMultiSampling));

		getDataModel().setUnits( GokoConfig.getInstance().getLengthUnit().getSymbol() );
		BigDecimal majorGridSpacing  = JoglViewerSettings.getInstance().getMajorGridSpacing();
		getDataModel().setMajorGridSpacing(majorGridSpacing);
		BigDecimal minorGridSpacing  = JoglViewerSettings.getInstance().getMinorGridSpacing();
		getDataModel().setMinorGridSpacing(minorGridSpacing);
	}


	public void savePreferences() {
		JoglViewerSettings.getInstance().setMultisampling(getDataModel().getMultisampling().getValue());
		JoglViewerSettings.getInstance().setMajorGridSpacing(getDataModel().getMajorGridSpacing());
		JoglViewerSettings.getInstance().setMinorGridSpacing(getDataModel().getMinorGridSpacing());
		JoglViewerSettings.getInstance().save();
	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.util.IPropertyChangeListener#propertyChange(org.eclipse.jface.util.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		try{
			if(StringUtils.equals(event.getProperty(), GokoConfig.KEY_DISTANCE_UNIT)){
				getDataModel().setUnits( GokoConfig.getInstance().getLengthUnit().getSymbol() );
				BigDecimal majorGridSpacing  = JoglViewerSettings.getInstance().getMajorGridSpacing();
				getDataModel().setMajorGridSpacing(majorGridSpacing);
				BigDecimal minorGridSpacing  = JoglViewerSettings.getInstance().getMinorGridSpacing();
				getDataModel().setMinorGridSpacing(minorGridSpacing);
			}
		}catch(GkException e){
			LOG.error(e);
		}
	}

}

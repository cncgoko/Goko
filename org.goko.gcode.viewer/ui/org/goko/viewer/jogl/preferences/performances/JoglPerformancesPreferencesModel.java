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
import java.util.List;

import org.goko.common.bindings.AbstractModelObject;
import org.goko.common.elements.combo.LabeledValue;

public class JoglPerformancesPreferencesModel extends AbstractModelObject {
	/** The avalailable multisampling values */
	private List<LabeledValue<Integer>> multisamplingChoice;
	/** The selected multisampling values */
	private LabeledValue<Integer> multisampling;
	private BigDecimal majorGridSpacing;
	private BigDecimal minorGridSpacing;
	private String units;

	/**
	 * @return the multisamplingChoice
	 */
	public List<LabeledValue<Integer>> getMultisamplingChoice() {
		return multisamplingChoice;
	}
	/**
	 * @param multisamplingChoice the multisamplingChoice to set
	 */
	public void setMultisamplingChoice(List<LabeledValue<Integer>> multisamplingChoice) {
		firePropertyChange("multisamplingChoice", this.multisamplingChoice, this.multisamplingChoice = multisamplingChoice);
	}
	/**
	 * @return the multisampling
	 */
	public LabeledValue<Integer> getMultisampling() {
		return multisampling;
	}
	/**
	 * @param multisampling the multisampling to set
	 */
	public void setMultisampling(LabeledValue<Integer> multisampling) {
		firePropertyChange("multisampling", this.multisampling, this.multisampling = multisampling);
	}
	/**
	 * @return the majorGridSpacing
	 */
	public BigDecimal getMajorGridSpacing() {
		return majorGridSpacing;
	}
	/**
	 * @param majorGridSpacing the majorGridSpacing to set
	 */
	public void setMajorGridSpacing(BigDecimal majorGridSpacing) {
		firePropertyChange("majorGridSpacing", this.majorGridSpacing, this.majorGridSpacing = majorGridSpacing);
	}
	/**
	 * @return the minorGridSpacing
	 */
	public BigDecimal getMinorGridSpacing() {
		return minorGridSpacing;
	}
	/**
	 * @param minorGridSpacing the minorGridSpacing to set
	 */
	public void setMinorGridSpacing(BigDecimal minorGridSpacing) {
		firePropertyChange("minorGridSpacing", this.minorGridSpacing, this.minorGridSpacing = minorGridSpacing);
	}
	/**
	 * @return the units
	 */
	public String getUnits() {
		return units;
	}
	/**
	 * @param units the units to set
	 */
	public void setUnits(String units) {
		firePropertyChange("units", this.units, this.units = units);
	}
}

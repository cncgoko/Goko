package org.goko.featuremanager.installer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GkInstallableUnitCategory {
	/** The name of the category */
	private String category;
	/** The units under the category */
	private List<GkInstallableUnit> lstGkInstallableUnit;
	
	/**
	 * Constructor
	 * @param category the category
	 * @param lstGkInstallableUnit the list of units
	 */
	public GkInstallableUnitCategory(String category, List<GkInstallableUnit> lstGkInstallableUnit) {
		super();
		this.category = category;
		this.lstGkInstallableUnit = lstGkInstallableUnit;
		for (GkInstallableUnit gkInstallableUnit : lstGkInstallableUnit) {
			gkInstallableUnit.setCategory(this);
		}
	}
	
	/**
	 * Constructor
	 * @param category the category
	 * @param lstGkInstallableUnit the list of units
	 */
	public GkInstallableUnitCategory(String category) {
		super();
		this.category = category;
		this.lstGkInstallableUnit = new ArrayList<GkInstallableUnit>();		
	}
	
	
	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}
	/**
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}
	/**
	 * @return the lstGkInstallableUnit
	 */
	public List<GkInstallableUnit> getLstGkInstallableUnit() {
		return lstGkInstallableUnit;
	}
	/**
	 * @param lstGkInstallableUnit the lstGkInstallableUnit to set
	 */
	public void setLstGkInstallableUnit(List<GkInstallableUnit> lstGkInstallableUnit) {
		this.lstGkInstallableUnit = lstGkInstallableUnit;
	}
	/** (inheritDoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((category == null) ? 0 : category.hashCode());
		return result;
	}
	/** (inheritDoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GkInstallableUnitCategory other = (GkInstallableUnitCategory) obj;
		if (category == null) {
			if (other.category != null)
				return false;
		} else if (!category.equals(other.category))
			return false;
		return true;
	}

	/**
	 * @param unit
	 * @return
	 * @see java.util.List#add(java.lang.Object)
	 */
	public boolean add(GkInstallableUnit unit) {
		return lstGkInstallableUnit.add(unit);
	}

	/**
	 * @param unit
	 * @return
	 * @see java.util.List#addAll(java.util.Collection)
	 */
	public boolean addAll(Collection<? extends GkInstallableUnit> unit) {
		return lstGkInstallableUnit.addAll(unit);
	}
}

package org.goko.core.gcode.rs274ngcv3.modifier;

import java.util.Date;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.utils.AbstractIdBean;
import org.goko.core.gcode.element.IGCodeProvider;
import org.goko.core.gcode.rs274ngcv3.IRS274NGCService;
import org.goko.core.gcode.rs274ngcv3.element.GCodeProvider;
import org.goko.core.gcode.rs274ngcv3.element.IModifier;

public abstract class AbstractModifier<T extends GCodeProvider> extends AbstractIdBean implements IModifier<T> {
	/** The provider id */
	private Integer idGCodeProvider;
	/** The modifier name */
	private String modifierName;
	/** The modifier state */
	private boolean enabled;
	/** The order of this modifier in the target modifier stack */
	private int order;
	/** The latest modification date */
	private Date modificationDate;
	/** The underlying GCode service */
	private IRS274NGCService rs274NGCService;

	/**
	 * Constructor	 
	 * @param modifierName the name of the modifier
	 */
	public AbstractModifier(String modifierName) {
		super();
		this.modifierName = modifierName;
		this.enabled = true;
		this.modificationDate = new Date();
	}
	
	/**
	 * Constructor
	 * @param idGCodeProvider the target provider
	 * @param modifierName the name of the modifier
	 */
	public AbstractModifier(Integer idGCodeProvider, String modifierName) {
		super();
		this.idGCodeProvider = idGCodeProvider;
		this.modifierName = modifierName;
		this.enabled = true;
		this.modificationDate = new Date();
	}

	/**
	 * @return the idGCodeProvider
	 */
	@Override
	public Integer getIdGCodeProvider() {
		return idGCodeProvider;
	}

	/**
	 * @param idGCodeProvider the idGCodeProvider to set
	 */
	public void setIdGCodeProvider(Integer idGCodeProvider) {
		this.idGCodeProvider = idGCodeProvider;
	}

	/**
	 * @return the modifierName
	 */
	@Override
	public String getModifierName() {
		return modifierName;
	}

	/**
	 * @param modifierName the modifierName to set
	 */
	public void setModifierName(String modifierName) {
		this.modifierName = modifierName;
	}

	/**
	 * @return the enabled
	 */
	@Override
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * @param enabled the enabled to set
	 */
	@Override
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.element.IModifier#apply(org.goko.core.gcode.rs274ngcv3.element.GCodeProvider, org.goko.core.gcode.rs274ngcv3.element.GCodeProvider)
	 */
	@Override
	public final void apply(IGCodeProvider source, T target) throws GkException {
		applyModifier(source, target);
	}

	protected abstract void applyModifier(IGCodeProvider source, T target) throws GkException;

	/**
	 * @return the order
	 */
	@Override
	public int getOrder() {
		return order;
	}

	/**
	 * @param order the order to set
	 */
	@Override
	public void setOrder(int order) {
		this.order = order;
	}
	/**
	 * Update the modification date to the current date
	 */
	protected void updateModificationDate(){
		this.modificationDate = new Date();
	}
	/**
	 * @return the modificationDate
	 */
	@Override
	public Date getModificationDate() {
		return modificationDate;
	}

	/**
	 * @param modificationDate the modificationDate to set
	 */
	@Override
	public void setModificationDate(Date modificationDate) {
		this.modificationDate = modificationDate;
	}

	/**
	 * @return the rsS274NGCService
	 */
	public IRS274NGCService getRS274NGCService() {
		return rs274NGCService;
	}

	/**
	 * @param rsS274NGCService the rsS274NGCService to set
	 */
	public void setRS274NGCService(IRS274NGCService rsS274NGCService) {
		this.rs274NGCService = rsS274NGCService;
	}

	/** (inheritDoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((idGCodeProvider == null) ? 0 : idGCodeProvider.hashCode());
		return result;
	}

	/** (inheritDoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractModifier other = (AbstractModifier) obj;
		if (idGCodeProvider == null) {
			if (other.idGCodeProvider != null)
				return false;
		} else if (!idGCodeProvider.equals(other.idGCodeProvider))
			return false;
		return true;
	}
	
	
}

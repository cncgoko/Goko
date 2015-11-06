package org.goko.core.gcode.rs274ngcv3.modifier;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.utils.AbstractIdBean;
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
	}

	/**
	 * @return the idGCodeProvider
	 */
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
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * @param enabled the enabled to set
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}	
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.element.IModifier#apply(org.goko.core.gcode.rs274ngcv3.element.GCodeProvider, org.goko.core.gcode.rs274ngcv3.element.GCodeProvider)
	 */
	@Override
	public final void apply(T source, T target) throws GkException {		
		applyModifier(source, target);		
	}
	
	protected abstract void applyModifier(T source, T target) throws GkException;

	/**
	 * @return the order
	 */
	public int getOrder() {
		return order;
	}

	/**
	 * @param order the order to set
	 */
	public void setOrder(int order) {
		this.order = order;
	}
}

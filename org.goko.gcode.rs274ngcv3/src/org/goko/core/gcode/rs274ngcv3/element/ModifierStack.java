package org.goko.core.gcode.rs274ngcv3.element;

import java.util.ArrayList;
import java.util.List;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.utils.AbstractIdBean;

public class ModifierStack extends AbstractIdBean{
	/** The id of the modifiers in the order of use */
	private List<Integer> modifiersSequence;

	/**
	 * Constructor
	 */
	public ModifierStack() {
		super();
		this.modifiersSequence = new ArrayList<Integer>();
	}

	/**
	 * @return the modifiersSequence
	 */
	public List<Integer> getModifiersSequence() {
		return modifiersSequence;
	}
	
	public void addModifier(Integer idModifier) throws GkException{
		modifiersSequence.add(idModifier);
	}
	
	public void removeModifier(Integer idModifier) throws GkException{
		modifiersSequence.remove(idModifier);
	}
}

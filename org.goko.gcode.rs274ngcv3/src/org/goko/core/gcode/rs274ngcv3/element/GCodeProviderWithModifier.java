package org.goko.core.gcode.rs274ngcv3.element;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.utils.CacheById;
import org.goko.core.common.utils.SequentialIdGenerator;
import org.goko.core.gcode.element.GCodeLine;
import org.goko.core.gcode.element.IGCodeProvider;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.internal.Activator;

public class GCodeProviderWithModifier implements IGCodeProvider{
	/** The original provider */
	private GCodeProvider baseProvider;
	/** The result provider */
	private GCodeProvider resultProvider;	
	/** The id of the modifiers in the order of use */
	private List<Integer> modifiersSequence;
	/** The code of this provider*/
	private String code;
	
	public GCodeProviderWithModifier() {	
		this.baseProvider = new GCodeProvider();
		this.modifiersSequence = new ArrayList<Integer>();
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.common.utils.IIdBean#getId()
	 */
	@Override
	public Integer getId() {
		return baseProvider.getId();
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.utils.IIdBean#setId(java.lang.Integer)
	 */
	@Override
	public void setId(Integer id) {
		baseProvider.setId(id);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.element.IGCodeProvider#getLines()
	 */
	@Override
	public List<GCodeLine> getLines() throws GkException {
		if(resultProvider == null){
			applyModifiers();
		}
		return resultProvider.getLines();
	}

	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.element.IGCodeProvider#getLine(java.lang.Integer)
	 */
	@Override
	public GCodeLine getLine(Integer idLine) throws GkException {
		if(resultProvider == null){
			applyModifiers();
		}
		return resultProvider.getLine(idLine);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.element.IGCodeProvider#getLineAtIndex(java.lang.Integer)
	 */
	@Override
	public GCodeLine getLineAtIndex(Integer indexLine) throws GkException {
		if(resultProvider == null){
			applyModifiers();
		}
		return resultProvider.getLineAtIndex(indexLine);
	}
	
	/**
	 * @return the baseProvider
	 */
	public GCodeProvider getBaseProvider() {
		return baseProvider;
	}

	/**
	 * @param baseProvider the baseProvider to set
	 */
	public void setBaseProvider(GCodeProvider baseProvider) {
		this.baseProvider = baseProvider;
	}

	/**
	 * @return the modifiersSequence
	 */
	public List<Integer> getModifiersSequence() {
		return modifiersSequence;
	}

	/**
	 * @param modifiersSequence the modifiersSequence to set
	 */
	public void setModifiersSequence(List<Integer> modifiersSequence) {
		this.modifiersSequence = modifiersSequence;
	}

	public void addModifier(IModifier modifier) throws GkException{
		this.cacheModifiers.add(modifier);
		this.modifiersSequence.add(modifier.getId());
	}
	
	public void deleteModifier(Integer idModifier) throws GkException{
		this.cacheModifiers.remove(idModifier);
		this.modifiersSequence.remove(idModifier);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}

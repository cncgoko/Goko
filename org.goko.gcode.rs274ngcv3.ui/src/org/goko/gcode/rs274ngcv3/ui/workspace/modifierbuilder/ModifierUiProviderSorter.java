package org.goko.gcode.rs274ngcv3.ui.workspace.modifierbuilder;

import java.util.Comparator;

import org.apache.commons.lang3.StringUtils;
import org.goko.core.gcode.rs274ngcv3.element.GCodeProvider;
import org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.IModifierUiProvider;

public class ModifierUiProviderSorter implements Comparator<IModifierUiProvider<GCodeProvider, ?>>{
	/** Sort methods*/
	public enum EnumModifierUiProviderSort{
		ALPHABETICAL,
		ALPHABETICAL_INVERSE;
	}
	/** The used sort */
	private EnumModifierUiProviderSort sortType;
	
	/**
	 * Constructor
	 * @param sortType the type of sort used 
	 */
	public ModifierUiProviderSorter(EnumModifierUiProviderSort sortType) {
		super();
		this.sortType = sortType;
	}

	/** (inheritDoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(IModifierUiProvider<GCodeProvider, ?> modifier1, IModifierUiProvider<GCodeProvider, ?> modifier2) {
		int result = 0;
		switch (sortType) {
		case ALPHABETICAL:	result = compareAlphabetical(modifier1, modifier2);		
			break;
		case ALPHABETICAL_INVERSE:	result = compareAlphabeticalInverse(modifier1, modifier2);
			break;
		default: result = compareAlphabetical(modifier1, modifier2);
		}
		return result;
	}

	/**
	 * Compare the modifiers name using alphabetical order
	 * @param modifier1 modifier 1
	 * @param modifier2 modifier 2
	 * @return int
	 */
	private int compareAlphabetical(IModifierUiProvider<GCodeProvider, ?> modifier1, IModifierUiProvider<GCodeProvider, ?> modifier2) {
		return StringUtils.defaultString(modifier1.getModifierName()).compareTo(modifier2.getModifierName());
	}
	
	/**
	 * Compare the modifiers name using inverse alphabetical order
	 * @param modifier1 modifier 1
	 * @param modifier2 modifier 2
	 * @return int
	 */
	private int compareAlphabeticalInverse(IModifierUiProvider<GCodeProvider, ?> modifier1, IModifierUiProvider<GCodeProvider, ?> modifier2) {
		return StringUtils.defaultString(modifier2.getModifierName()).compareTo(modifier1.getModifierName());
	}

}

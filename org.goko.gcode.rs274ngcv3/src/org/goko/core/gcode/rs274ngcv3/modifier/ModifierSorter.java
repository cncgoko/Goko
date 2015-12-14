package org.goko.core.gcode.rs274ngcv3.modifier;

import java.util.Comparator;

import org.goko.core.gcode.rs274ngcv3.element.IModifier;

public class ModifierSorter implements Comparator<IModifier<?>> {
	/** Type of sorting */
	public enum EnumModifierSortType{
		ORDER,
		ORDER_INVERSE
	}
	/** Type of sorting used in this sorter */
	private EnumModifierSortType sortType;
	
	
	public ModifierSorter(EnumModifierSortType sortType) {
		super();
		this.sortType = sortType;
	}


	/** (inheritDoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(IModifier<?> arg0, IModifier<?> arg1) {
		int result = 0;
		switch (sortType) {
		case ORDER:	result = compareByOrder(arg0, arg1);		
			break;	
		case ORDER_INVERSE:result = compareByOrderInverse(arg0, arg1);
			break;
		}
		return result;
	}

	private int compareByOrder(IModifier<?> arg0, IModifier<?> arg1) {
		int result = compareByIdGCodeProvider(arg0, arg1);
		if(result == 0){
			result = arg0.getOrder() - arg1.getOrder();
		}
		return result;
	}
	
	private int compareByOrderInverse(IModifier<?> arg0, IModifier<?> arg1) {
		int result = compareByIdGCodeProvider(arg0, arg1);
		if(result == 0){
			result = arg1.getOrder() - arg0.getOrder();
		}
		return result;
	}
	
	private int compareByIdGCodeProvider(IModifier<?> arg0, IModifier<?> arg1) {
		return arg0.getIdGCodeProvider().compareTo(arg1.getIdGCodeProvider());
	}
}

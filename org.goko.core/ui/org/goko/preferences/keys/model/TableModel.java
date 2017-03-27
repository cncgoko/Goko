/**
 * 
 */
package org.goko.preferences.keys.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.e4.ui.model.application.commands.MBindingTable;

/**
 * @author Psyko
 * @date 24 mars 2017
 */
public class TableModel extends CommonModel<TableElement>{

	private Map<String, TableElement> tableElementById; 
	/**
	 * @param controller
	 */
	public TableModel(KeyController controller) {
		super(controller);
	}
	
	
	public void init(List<MBindingTable> lstTable) {
		for (MBindingTable mBindingTable : lstTable) {
			TableElement te = new TableElement(getController());
			te.init(mBindingTable);
			getTableElementById().put(te.getId(), te);			
		}
	}
	
	public TableElement getTable(String id){
		return getTableElementById().get(id);
	}
	
	private Map<String, TableElement> getTableElementById(){
		if(tableElementById == null){
			tableElementById = new HashMap<>();
		}
		return tableElementById;
	}
}

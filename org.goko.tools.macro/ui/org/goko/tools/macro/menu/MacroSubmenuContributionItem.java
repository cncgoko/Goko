/**
 * 
 */
package org.goko.tools.macro.menu;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.goko.core.common.exception.GkException;
import org.goko.core.execution.monitor.uiprovider.menu.executionqueue.IExecutionQueueContributionItem;
import org.goko.core.gcode.service.IExecutionService;
import org.goko.core.log.GkLog;
import org.goko.tools.macro.bean.GCodeMacro;
import org.goko.tools.macro.service.IGCodeMacroService;
import org.goko.tools.macro.service.IGCodeMacroServiceListener;

/**
 * @author Psyko
 * @date 16 oct. 2016
 */
public class MacroSubmenuContributionItem extends MenuManager implements IExecutionQueueContributionItem, IGCodeMacroServiceListener {
	private static final GkLog LOG = GkLog.getLogger(MacroSubmenuContributionItem.class);
	/** The contributed menu */
	private IMenuManager item;
	/** Macro service */
	private IGCodeMacroService macroService;
	private IExecutionService executionService;	
	/**
	 * 
	 */
	public MacroSubmenuContributionItem() {
	}

	/**
	 * (inheritDoc)
	 * 
	 * @see org.goko.core.execution.monitor.uiprovider.menu.executionqueue.IExecutionQueueContributionItem#getItem()
	 */
	@Override
	public IContributionItem getItem() {
		if(item == null){
			item = new MenuManager("Add macro");
			buildMenu();
		}
		return item;
	}

	protected void buildMenu(){
		try {
			List<GCodeMacro> lstMacro = macroService.getGCodeMacro();
			if(CollectionUtils.isNotEmpty(lstMacro)){
				for (GCodeMacro macro : lstMacro) {
					AddMacroAction action = new AddMacroAction(executionService, macroService, macro.getId());
					action.setText(macro.getCode());
					item.add(action);
				}
			}else{
				item.add(new Action("No macro"){
					/** (inheritDoc)
					 * @see org.eclipse.jface.action.Action#isEnabled()
					 */
					@Override
					public boolean isEnabled() {						
						return false;
					}
				});
			}
		} catch (GkException e) {
			LOG.error(e);
		}
		
	}

	/**
	 * @param macroService the macroService to set
	 * @throws GkException GkException 
	 */
	public void setMacroService(IGCodeMacroService macroService) throws GkException {
		this.macroService = macroService;
		this.macroService.addListener(this);
	}

	/**
	 * @param executionService the executionService to set
	 */
	public void setExecutionService(IExecutionService executionService) {
		this.executionService = executionService;
	}

	/** (inheritDoc)
	 * @see org.goko.tools.macro.service.IGCodeMacroServiceListener#onGCodeMacroCreate(org.goko.tools.macro.bean.GCodeMacro)
	 */
	@Override
	public void onGCodeMacroCreate(GCodeMacro macro) {
		refreshMenu();
	}

	/** (inheritDoc)
	 * @see org.goko.tools.macro.service.IGCodeMacroServiceListener#onGCodeMacroUpdate(org.goko.tools.macro.bean.GCodeMacro)
	 */
	@Override
	public void onGCodeMacroUpdate(GCodeMacro macro) {
		refreshMenu();
	}

	/** (inheritDoc)
	 * @see org.goko.tools.macro.service.IGCodeMacroServiceListener#beforeGCodeMacroDelete(org.goko.tools.macro.bean.GCodeMacro)
	 */
	@Override
	public void beforeGCodeMacroDelete(GCodeMacro macro) {
		refreshMenu();
	}

	/** (inheritDoc)
	 * @see org.goko.tools.macro.service.IGCodeMacroServiceListener#afterGCodeMacroDelete(org.goko.tools.macro.bean.GCodeMacro)
	 */
	@Override
	public void afterGCodeMacroDelete(GCodeMacro macro) {
		refreshMenu();
	}

	private void refreshMenu(){
		if(item != null){
			item.dispose();
			item = null;
		}
	}
}

/**
 * 
 */
package org.goko.tools.macro.menu;

import org.eclipse.jface.action.Action;
import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.element.IGCodeProvider;
import org.goko.core.gcode.service.IExecutionService;
import org.goko.tools.macro.service.IGCodeMacroService;

/**
 * @author Psyko
 * @date 16 oct. 2016
 */
public class AddMacroAction extends Action{
	private IExecutionService executionService;	
	private IGCodeMacroService macroService;
	private Integer idMacro;
		
	/**
	 * @param executionService
	 * @param gcodeService
	 * @param macroService
	 * @param idMacro
	 */
	public AddMacroAction(IExecutionService executionService, IGCodeMacroService macroService, Integer idMacro) {
		super();
		this.executionService = executionService;
		this.macroService = macroService;
		this.idMacro = idMacro;		
	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.action.Action#run()
	 */
	@Override
	public void run() {
		try {
			IGCodeProvider provider = macroService.getGCodeProviderByMacro(idMacro);					
			executionService.addToExecutionQueue(provider);
		} catch (GkException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}

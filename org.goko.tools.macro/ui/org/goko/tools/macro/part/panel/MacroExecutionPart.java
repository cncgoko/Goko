/**
 * 
 */
package org.goko.tools.macro.part.panel;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.wb.swt.ResourceManager;
import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.execution.ExecutionQueueType;
import org.goko.core.gcode.service.IExecutionService;
import org.goko.core.log.GkLog;
import org.goko.tools.macro.bean.GCodeMacro;
import org.goko.tools.macro.service.DefaultGCodeMacroService;
import org.goko.tools.macro.service.IGCodeMacroServiceListener;

/**
 * @author Psyko
 * @date 26 oct. 2016
 */
public class MacroExecutionPart implements IGCodeMacroServiceListener{
	private static final GkLog LOG = GkLog.getLogger(MacroExecutionPart.class);
	private DefaultGCodeMacroService macroService;
	private IExecutionService executionService;
	private Composite parent;
	/**
	 * Constructor
	 */
	@Inject
	public MacroExecutionPart(DefaultGCodeMacroService macroService,IExecutionService executionService) throws GkException {
		super();
		this.macroService = macroService;
		this.macroService.addListener(this);
		this.executionService = executionService;
	}

	@PostConstruct
	public void postConstruct(Composite parent) throws GkException {
		this.parent = parent;
		updateButtons();
	}
	
	private void deleteAllButtons(){
		Control[] children = parent.getChildren();
		if(children != null){
			for (Control control : children) {
				control.dispose();
			}
		}
	}
	
	private void updateButtons() throws GkException{
		parent.setLayout(new FillLayout(SWT.HORIZONTAL));
		Composite composite = new Composite(parent, SWT.NONE);
		RowLayout rowLayout = new RowLayout(SWT.HORIZONTAL);
		rowLayout.fill = true;
		rowLayout.pack = true;
		rowLayout.spacing = 6;
		composite.setLayout(rowLayout);
		
		List<GCodeMacro> lstMacro = macroService.getGCodeMacro();
		for (GCodeMacro macro : lstMacro) {
			if(macro.isShowInMacroPanel()){
				Button btnMacro = new Button(composite, SWT.NONE);
				btnMacro.setText(macro.getCode());
			//	btnMacro.setLayoutData(new RowData(100, 35));
				if(macro.getButtonColor() != null){
					btnMacro.setBackground(ResourceManager.getColor(Math.round(macro.getButtonColor().x * 255),
																	Math.round(macro.getButtonColor().y * 255),
																	Math.round(macro.getButtonColor().z * 255)));
				}
				
				btnMacro.addMouseListener(new MouseAdapter() {
					/** (inheritDoc)
					 * @see org.eclipse.swt.events.MouseAdapter#mouseUp(org.eclipse.swt.events.MouseEvent)
					 */
					@Override
					public void mouseUp(MouseEvent e) {						
						super.mouseUp(e);
						try{
							boolean executionConfirmed = true;
							if(macro.isRequestConfirmBeforeExecution()){
								executionConfirmed = MessageDialog.openConfirm(parent.getShell(), "Confirm execution", "Do your really want to immediately execute the macro "+macro.getCode()+" ?");
							}
							if(executionConfirmed){
								if(executionService.isReadyForExecution()){
									executionService.clearExecutionQueue(ExecutionQueueType.SYSTEM);								
									executionService.addToExecutionQueue(ExecutionQueueType.SYSTEM, macroService.getGCodeProviderByMacro(macro.getId()));								
									executionService.beginQueueExecution(ExecutionQueueType.SYSTEM);
								}
							}
						}catch(GkException ex){
							LOG.error(ex);
						}
					}
				});
			}
		}
		parent.layout(true);
		parent.update();
		
	}

	/** (inheritDoc)
	 * @see org.goko.tools.macro.service.IGCodeMacroServiceListener#onGCodeMacroCreate(org.goko.tools.macro.bean.GCodeMacro)
	 */
	@Override
	public void onGCodeMacroCreate(GCodeMacro macro) {
		if(macro.isShowInMacroPanel()){
			deleteAllButtons();
			try {
				updateButtons();
			} catch (GkException e) {
				LOG.error(e);
			}
		}
	}

	/** (inheritDoc)
	 * @see org.goko.tools.macro.service.IGCodeMacroServiceListener#onGCodeMacroUpdate(org.goko.tools.macro.bean.GCodeMacro)
	 */
	@Override
	public void onGCodeMacroUpdate(GCodeMacro macro) {
		if(macro.isShowInMacroPanel()){
			deleteAllButtons();
			try {
				updateButtons();
			} catch (GkException e) {
				LOG.error(e);
			}
		}
	}

	/** (inheritDoc)
	 * @see org.goko.tools.macro.service.IGCodeMacroServiceListener#beforeGCodeMacroDelete(org.goko.tools.macro.bean.GCodeMacro)
	 */
	@Override
	public void beforeGCodeMacroDelete(GCodeMacro macro) {
		if(macro.isShowInMacroPanel()){
			deleteAllButtons();
			try {
				updateButtons();
			} catch (GkException e) {
				LOG.error(e);
			}
		}
	}

	/** (inheritDoc)
	 * @see org.goko.tools.macro.service.IGCodeMacroServiceListener#afterGCodeMacroDelete(org.goko.tools.macro.bean.GCodeMacro)
	 */
	@Override
	public void afterGCodeMacroDelete(GCodeMacro macro) {
		// TODO Auto-generated method stub
		
	}
}

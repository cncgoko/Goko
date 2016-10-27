/**
 * 
 */
package org.goko.tools.macro.part.management;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.goko.common.bindings.AbstractController;
import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.rs274ngcv3.element.source.StringGCodeSource;
import org.goko.core.log.GkLog;
import org.goko.tools.editor.GCodeEditorTopic;
import org.goko.tools.macro.bean.GCodeMacro;
import org.goko.tools.macro.document.GCodeMacroDocument;
import org.goko.tools.macro.service.DefaultGCodeMacroService;
import org.goko.tools.macro.service.IGCodeMacroServiceListener;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;

/**
 * @author Psyko
 * @date 17 oct. 2016
 */
public class MacroManagementController extends AbstractController<MacroManagementModel> implements IGCodeMacroServiceListener{
	private static final GkLog LOG = GkLog.getLogger(MacroManagementController.class);
	@Inject
	private DefaultGCodeMacroService macroService;
	/** Event admin */
	@Inject	
	private EventAdmin eventAdmin;
	/**
	 * @param binding
	 */
	public MacroManagementController() {
		super(new MacroManagementModel());
	}

	/** (inheritDoc)
	 * @see org.goko.common.bindings.AbstractController#initialize()
	 */
	@Override
	public void initialize() throws GkException {
		macroService.addListener(this);
		updateMacroList();		
	}
	
	private void updateMacroList() throws GkException{
		List<GCodeMacro> macros = macroService.getGCodeMacro();
		getDataModel().setAvailableMacro(new WritableList(macros, GCodeMacro.class));
		// Handle last selection
		if(CollectionUtils.isNotEmpty(macros)){
			if(getDataModel().getSelectedMacro() == null){			
				selectMacro(macros.get(0));			
			}else{
				GCodeMacro selectedMacro = macroService.getGCodeMacro(getDataModel().getSelectedMacro().getId());
				selectMacro(selectedMacro);
			}
		}
	}
	
	public void createNewMacro() throws GkException{
		GCodeMacro macro = new GCodeMacro();
		macro.setCode("New macro"); 
		macro.setRequestConfirmBeforeExecution(true);
		macro.setContent(new StringGCodeSource(StringUtils.EMPTY));
		macroService.addGCodeMacro(macro);
		selectMacro(macro);
	}

	/**
	 * @param firstElement
	 */
	public void selectMacro(GCodeMacro selectedMacro) {		
		getDataModel().setSelectedMacro(selectedMacro);
		getDataModel().setRequestConfirmation(selectedMacro.isRequestConfirmBeforeExecution());
		getDataModel().setMacroName(selectedMacro.getCode());
		getDataModel().setDisplayMacroButton(selectedMacro.isShowInMacroPanel());
		getDataModel().setOverrideButtonColor(selectedMacro.getButtonColor() != null);
		getDataModel().setButtonColor(selectedMacro.getButtonColor());		
		getDataModel().setDirty(false);
	}
	
	public void unselectMacro() {		
		getDataModel().setSelectedMacro(null);
		getDataModel().setRequestConfirmation(false);
		getDataModel().setMacroName(StringUtils.EMPTY);
		getDataModel().setDisplayMacroButton(false);
		getDataModel().setOverrideButtonColor(false);
		getDataModel().setButtonColor(null);		
		getDataModel().setDirty(false);
	}

	
	public void applyChangesToGCodeMacro() throws GkException{
		if(getDataModel().getSelectedMacro() != null){
			GCodeMacro sourceMacro = getDataModel().getSelectedMacro();
			GCodeMacro macro = new GCodeMacro();
			macro.setId(sourceMacro.getId());
			macro.setCode( getDataModel().getMacroName() );
			macro.setRequestConfirmBeforeExecution( getDataModel().isRequestConfirmation() );
			macro.setShowInMacroPanel( getDataModel().isDisplayMacroButton() );
			macro.setContent(sourceMacro.getContent()); // Don't change the content as it's managed by the editor directly
			macro.setButtonColor(getDataModel().getButtonColor());
			macroService.updateGCodeMacro(macro);
			getDataModel().setSelectedMacro(macroService.getGCodeMacro(macro.getId()));
			getDataModel().setDirty(false);
		}
	}
	
	public void cancelChangesToGCodeMacro(){
		if(getDataModel().getSelectedMacro() != null){
			selectMacro(getDataModel().getSelectedMacro());
			getDataModel().setDirty(false);
		}
	}
	
	public void openMacroInEditor() throws GkException{
		if(getDataModel().getSelectedMacro() != null){
			GCodeMacro macro = getDataModel().getSelectedMacro();
			Map<String, Object> map = new HashMap<String, Object>();
			GCodeMacroDocument documentProvider = new GCodeMacroDocument(macroService, macro, macroService.getGCodeProviderByMacro(macro.getId()));
			map.put(IEventBroker.DATA, documentProvider);
			eventAdmin.sendEvent(new Event(GCodeEditorTopic.TOPIC_OPEN_EDITOR, map));
		}
	}
	
	protected void deleteSelectedMacro() throws GkException{
		if(getDataModel().getSelectedMacro() != null){
			GCodeMacro macro = getDataModel().getSelectedMacro();
			unselectMacro();
			macroService.deleteGCodeMacro(macro);			
		}
	}
	

	/** (inheritDoc)
	 * @see org.goko.tools.macro.service.IGCodeMacroServiceListener#onGCodeMacroCreate(org.goko.tools.macro.bean.GCodeMacro)
	 */
	@Override
	public void onGCodeMacroCreate(GCodeMacro macro) {
		try {
			updateMacroList();
		} catch (GkException e) {
			LOG.error(e);
		}
	}

	/** (inheritDoc)
	 * @see org.goko.tools.macro.service.IGCodeMacroServiceListener#onGCodeMacroUpdate(org.goko.tools.macro.bean.GCodeMacro)
	 */
	@Override
	public void onGCodeMacroUpdate(GCodeMacro macro) {
		try {
			updateMacroList();
		} catch (GkException e) {
			LOG.error(e);
		}
	}

	/** (inheritDoc)
	 * @see org.goko.tools.macro.service.IGCodeMacroServiceListener#beforeGCodeMacroDelete(org.goko.tools.macro.bean.GCodeMacro)
	 */
	@Override
	public void beforeGCodeMacroDelete(GCodeMacro macro) {
		
	}

	/** (inheritDoc)
	 * @see org.goko.tools.macro.service.IGCodeMacroServiceListener#afterGCodeMacroDelete(org.goko.tools.macro.bean.GCodeMacro)
	 */
	@Override
	public void afterGCodeMacroDelete(GCodeMacro macro) {
		try {
			updateMacroList();
		} catch (GkException e) {
			LOG.error(e);
		}
	}
}

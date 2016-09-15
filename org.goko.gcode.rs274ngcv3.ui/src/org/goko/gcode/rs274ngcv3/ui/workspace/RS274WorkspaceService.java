/**
 *
 */
package org.goko.gcode.rs274ngcv3.ui.workspace;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.service.AbstractGokoService;
import org.goko.core.gcode.element.IGCodeProvider;
import org.goko.core.gcode.rs274ngcv3.IRS274NGCService;
import org.goko.core.gcode.rs274ngcv3.RenderingFormat;
import org.goko.core.gcode.rs274ngcv3.element.GCodeProvider;
import org.goko.core.gcode.rs274ngcv3.element.IModifier;
import org.goko.core.gcode.rs274ngcv3.modifier.IModifierListener;
import org.goko.core.gcode.service.IExecutionService;
import org.goko.core.gcode.service.IGCodeProviderRepositoryListener;
import org.goko.core.log.GkLog;
import org.goko.core.workspace.service.IWorkspaceService;
import org.goko.core.workspace.service.IWorkspaceUIService;
import org.goko.gcode.rs274ngcv3.ui.workspace.modifierbuilder.TranslateModifierBuilder;
import org.goko.gcode.rs274ngcv3.ui.workspace.modifierbuilder.array.ArrayModifierBuilder;
import org.goko.gcode.rs274ngcv3.ui.workspace.modifierbuilder.rotate.RotateModifierBuilder;
import org.goko.gcode.rs274ngcv3.ui.workspace.modifierbuilder.scale.ScaleModifierBuilder;
import org.goko.gcode.rs274ngcv3.ui.workspace.modifierbuilder.segmentize.SegmentizeModifierBuilder;
import org.goko.gcode.rs274ngcv3.ui.workspace.modifierbuilder.wrap.WrapModifierBuilder;
import org.goko.gcode.rs274ngcv3.ui.workspace.preferences.renderingformat.RenderingFormatPreference;
import org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.IModifierUiProvider;

/**
 * @author PsyKo
 * @date 31 oct. 2015
 */
/**
 * @author Psyko
 *
 */
public class RS274WorkspaceService extends AbstractGokoService implements IRS274WorkspaceService, IGCodeProviderRepositoryListener, IModifierListener, IPropertyChangeListener{
	/** LOG */
	private static final GkLog LOG = GkLog.getLogger(RS274WorkspaceService.class);
	/** Service ID */
	private static final String SERVICE_ID = "org.goko.gcode.rs274ngcv3.ui.workspace.RS274WorkspaceService";
	/** Workspace UI service */
	private IWorkspaceUIService workspaceUIService;
	/** Workspace service */
	private IWorkspaceService workspaceService;
	/** Workspace UI service */
	private IExecutionService<?,?> executionService;
	/** Workspace UI service */
	private IRS274NGCService gcodeService;
	/** The list of existing IModifierUiProvider*/
	private List<IModifierUiProvider<?>> lstModifierUiProvider;

	/**
	 * Constructor
	 */
	public RS274WorkspaceService() {
		lstModifierUiProvider = new ArrayList<IModifierUiProvider<?>>();
	}
	/** (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#getServiceId()
	 */
	@Override
	public String getServiceId() throws GkException {
		return SERVICE_ID;
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#start()
	 */
	@Override
	public void startService() throws GkException {		
		// Create the RS274 project container
		//getWorkspaceUIService().addProjectContainerUiProvider(new GCodeContainerUiProvider(getGcodeService(), this, executionService, workspaceService));
		getGcodeService().addListener(this);		
		getGcodeService().addModifierListener(this);	
		RenderingFormatPreference.getInstance().addPropertyChangeListener(this);
		initModifierUiProvider();		
		updateRenderingFormat();
	}

	/**
	 * Initialization of the default modifiers
	 * @throws GkException GkException
	 */
	private void initModifierUiProvider() throws GkException {
		//addModifierBuilder(new TestModifierBuilder());
		addModifierBuilder(new TranslateModifierBuilder());
		addModifierBuilder(new SegmentizeModifierBuilder());
		addModifierBuilder(new ScaleModifierBuilder());
		addModifierBuilder(new WrapModifierBuilder());
		addModifierBuilder(new RotateModifierBuilder());
		addModifierBuilder(new ArrayModifierBuilder());
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#stop()
	 */
	@Override
	public void stopService() throws GkException {

	}

	/**
	 * @return the workspaceUiService
	 */
	public IWorkspaceUIService getWorkspaceUIService() {
		return workspaceUIService;
	}

	/**
	 * @param workspaceUiService the workspaceUiService to set
	 */
	public void setWorkspaceUIService(IWorkspaceUIService workspaceUiService) {
		this.workspaceUIService = workspaceUiService;
	}

	/**
	 * @return the gcodeService
	 */
	public IRS274NGCService getGcodeService() {
		return gcodeService;
	}

	/**
	 * @param gcodeService the gcodeService to set
	 */
	public void setGcodeService(IRS274NGCService gcodeService) {
		this.gcodeService = gcodeService;
	}

	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.ui.workspace.IRS274WorkspaceService#getModifierBuilder()
	 */
	@Override
	public List<IModifierUiProvider<?>> getModifierBuilder() throws GkException{
		return lstModifierUiProvider;
	}

	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.ui.workspace.IRS274WorkspaceService#addModifierBuilder(org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.IModifierUiProvider)
	 */
	@Override
	public void addModifierBuilder(IModifierUiProvider<?> modifierBuilder) throws GkException {
		lstModifierUiProvider.add(modifierBuilder);
	}
	/**
	 * @return the executionService
	 */
	public IExecutionService<?, ?> getExecutionService() {
		return executionService;
	}

	/**
	 * @param executionService the executionService to set
	 */
	public void setExecutionService(IExecutionService<?, ?> executionService) {
		this.executionService = executionService;
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeProviderRepositoryListener#onGCodeProviderCreate(org.goko.core.gcode.element.IGCodeProvider)
	 */
	@Override
	public void onGCodeProviderCreate(IGCodeProvider provider) throws GkException {
		workspaceUIService.refreshWorkspaceUi();
		workspaceUIService.select(provider);
		markProjectDirty();
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeProviderRepositoryListener#onGCodeProviderUpdate(org.goko.core.gcode.element.IGCodeProvider)
	 */
	@Override
	public void onGCodeProviderUpdate(IGCodeProvider provider) throws GkException {
		workspaceUIService.refreshWorkspaceUi();
		markProjectDirty();
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeProviderRepositoryListener#beforeGCodeProviderDelete(org.goko.core.gcode.element.IGCodeProvider)
	 */
	@Override
	public void beforeGCodeProviderDelete(IGCodeProvider provider) throws GkException {
		// Nothing yet
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeProviderRepositoryListener#afterGCodeProviderDelete(org.goko.core.gcode.element.IGCodeProvider)
	 */
	@Override
	public void afterGCodeProviderDelete(IGCodeProvider provider) throws GkException {
		workspaceUIService.refreshWorkspaceUi();
		markProjectDirty();
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeProviderRepositoryListener#onGCodeProviderLocked(org.goko.core.gcode.element.IGCodeProvider)
	 */
	@Override
	public void onGCodeProviderLocked(IGCodeProvider provider) throws GkException {
		workspaceUIService.refreshWorkspaceUi();
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeProviderRepositoryListener#onGCodeProviderUnlocked(org.goko.core.gcode.element.IGCodeProvider)
	 */
	@Override
	public void onGCodeProviderUnlocked(IGCodeProvider provider) throws GkException {
		workspaceUIService.refreshWorkspaceUi();
	}

	protected void markProjectDirty() throws GkException{
		workspaceService.markProjectDirty();
	}
	/**
	 * @return the workspaceService
	 */
	public IWorkspaceService getWorkspaceService() {
		return workspaceService;
	}

	/**
	 * @param workspaceService the workspaceService to set
	 */
	public void setWorkspaceService(IWorkspaceService workspaceService) {
		this.workspaceService = workspaceService;
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.modifier.IModifierListener#onModifierCreate(java.lang.Integer)
	 */
	@Override
	public void onModifierCreate(Integer idModifier) throws GkException {
		workspaceUIService.refreshWorkspaceUi();
		workspaceUIService.select(gcodeService.getModifier(idModifier));
		markProjectDirty();
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.modifier.IModifierListener#onModifierUpdate(java.lang.Integer)
	 */
	@Override
	public void onModifierUpdate(Integer idModifier) throws GkException {
		workspaceUIService.refreshWorkspaceUi();
		workspaceUIService.select(gcodeService.getModifier(idModifier));
		markProjectDirty();
	}
	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.modifier.IModifierListener#onModifierDelete(java.lang.Integer)
	 */
	@Override
	public void onModifierDelete(IModifier<?> modifier) throws GkException {
		workspaceUIService.refreshWorkspaceUi();
		List<IModifier<GCodeProvider>> modifiers = gcodeService.getModifierByGCodeProvider(modifier.getIdGCodeProvider());
		if(CollectionUtils.isNotEmpty(modifiers)){
			workspaceUIService.select(modifiers.get(0));			
		}else{
			IGCodeProvider provider = gcodeService.getGCodeProvider(modifier.getIdGCodeProvider());
			workspaceUIService.select(provider);
		}		
		markProjectDirty();
	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.util.IPropertyChangeListener#propertyChange(org.eclipse.jface.util.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		updateRenderingFormat();		
	}

	/**
	 * Update the rendering format using preferences
	 */
	private void updateRenderingFormat(){
		RenderingFormat format = new RenderingFormat( RenderingFormatPreference.getInstance().isSkipComment(),
				RenderingFormatPreference.getInstance().isSkipLineNumber(),
				RenderingFormatPreference.getInstance().isTruncateDecimal(), 
				RenderingFormatPreference.getInstance().getDecimalDigitCount());
		gcodeService.setRenderingFormat(format);
	}
}

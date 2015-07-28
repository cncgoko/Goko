/**
 * 
 */
package org.goko.core.feature;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.preferences.IPreferencesService;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.EclipseContextFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.service.IGokoService;
import org.goko.core.config.GokoPreference;
import org.goko.core.log.GkLog;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;

/**
 * @author PsyKo
 *
 */
public class FeatureSetManagerImpl implements IGokoService, IFeatureSetManager, IPropertyChangeListener {
	/** Service ID */
	private static final String SERVICE_ID = "org.goko.core.feature.GokoFeatureManager";
	/** LOG */
	private static final GkLog LOG = GkLog.getLogger(FeatureSetManagerImpl.class);
	/** The list of registered features set */
	private List<IFeatureSet> lstFeatureSet;
	/** Current running feature set */
	private List<IFeatureSet> lstActiveFeatureSet;	
	/** Extension registry for fragment search */
	private IExtensionRegistry extensionRegistry;
	/** Preferences service */ // FIXME : check why the GokoPreference.getInstance.getTarget board throw a NPE
	private IPreferencesService preferencesService;
	/** The current active target board*/
	private String targetBoard;
	/** Event admin for sync GUI model handling */
	private EventAdmin eventAdmin;
	
	/**
	 * Constructor
	 */
	public FeatureSetManagerImpl() {
		this.lstFeatureSet = new ArrayList<IFeatureSet>();
		this.lstActiveFeatureSet = new ArrayList<IFeatureSet>();
		GokoPreference.getInstance().addPropertyChangeListener(this);
	}

	/**
	 * Add the given FeatureSet
	 * 
	 * @param featureSet
	 *            the feature set to add
	 * @throws GkException
	 *             GkException
	 */
	public void addFeatureSet(IFeatureSet featureSet) throws GkException {
		lstFeatureSet.add(featureSet);		
		startIfRequired(featureSet);
	}

	
	
	private void deactivateTargetBoardSupport(String targetBoard) throws GkException{
		// Let's unload any started set
		if (StringUtils.isNotBlank(targetBoard) && CollectionUtils.isNotEmpty(lstActiveFeatureSet)) {			
			for (IFeatureSet iFeatureSet : lstActiveFeatureSet) {				
				iFeatureSet.stop();
			}
			Map<String, String> properties = new HashMap<String, String>();
			properties.put(IEventBroker.DATA, targetBoard);
			Event event = new Event(TOPIC_UNLOAD_MODEL_FRAGMENTS, properties);
			eventAdmin.postEvent(event);
		}
	}
	private void activateTargetBoardSupport() throws GkException{		
		// Now enable new feature set
		if(StringUtils.isNotBlank(getTargetBoard())){			
			if (CollectionUtils.isNotEmpty(lstFeatureSet)) {
				for (IFeatureSet iFeatureSet : lstFeatureSet) {
					startIfRequired(iFeatureSet);				
				}
				// Notify GUI
				Map<String, String> properties = new HashMap<String, String>();
				properties.put(IEventBroker.DATA, targetBoard);
				Event event = new Event(TOPIC_LOAD_MODEL_FRAGMENTS, properties);
				eventAdmin.postEvent(event);
			}	
		}
//		else{
//			throw new GkTechnicalException("No targetboard in preferences...");
//		}
	}
	
	private void startIfRequired(IFeatureSet featureSet) throws GkException {
		if (StringUtils.isNotBlank(getTargetBoard()) &&
			StringUtils.equals(featureSet.getTargetBoard().getId(), getTargetBoard())) {
			lstActiveFeatureSet.add(featureSet);
			BundleContext bundleContext = FrameworkUtil.getBundle(featureSet.getClass()).getBundleContext();
			ContextInjectionFactory.inject(featureSet, EclipseContextFactory.getServiceContext(bundleContext));
			featureSet.start(bundleContext);
		}
	}
	/**
	 * @return the configured target board
	 */
	private String getTargetBoard() {
		if(targetBoard == null && preferencesService != null){
			targetBoard = preferencesService.getString(GokoPreference.NODE_ID,GokoPreference.KEY_TARGET_BOARD, StringUtils.EMPTY, null); 
		}
		return targetBoard;		
	}

	/**
	 * (inheritDoc)
	 * 
	 * @see org.goko.core.feature.IFeatureSetManager#setTargetBoard(java.lang.String)
	 */
	@Override
	public void setTargetBoard(String id) throws GkException {
		String currentTargetBoard = getTargetBoard();
		if(StringUtils.equals(currentTargetBoard, id)){
			return; // Nothing to chang			
		}
		GokoPreference.getInstance().setTargetBoard(id);
		GokoPreference.getInstance().save();
	}

	/**
	 * (inheritDoc)
	 * 
	 * @see org.goko.core.feature.IFeatureSetManager#loadFeatureSetFragment()
	 */
	@Override
	public void loadFeatureSetFragment(IEclipseContext context) throws GkException {
//		FeatureSetModelAssembler featureModelAssembler = new FeatureSetModelAssembler();
//		ContextInjectionFactory.inject(featureModelAssembler, context);
//		featureModelAssembler.processModel(getTargetBoard(), true);
	}

	/** (inheritDoc)
	 * @see org.goko.core.feature.IFeatureSetManager#unloadFeatureSetFragment(org.eclipse.e4.core.contexts.IEclipseContext, java.lang.String)
	 */
	@Override
	public void unloadFeatureSetFragment(IEclipseContext context, String oldTargetBoard) throws GkException {		
//		FeatureSetModelDisassembler disassembler = new FeatureSetModelDisassembler();
//		ContextInjectionFactory.inject(disassembler, context);
//		disassembler.removeModel(oldTargetBoard);
	}
	
	/**
	 * (inheritDoc)
	 * 
	 * @see org.goko.core.feature.IFeatureSetManager#existTargetBoard(java.lang.String)
	 */
	@Override
	public boolean existTargetBoard(String targetBoardId) {
		List<TargetBoard> lstBoards = getSupportedBoards();
		for (TargetBoard targetBoard : lstBoards) {
			if (StringUtils.equals(targetBoard.getId(), targetBoardId)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * (inheritDoc)
	 * 
	 * @see org.goko.core.common.service.IGokoService#getServiceId()
	 */
	@Override
	public String getServiceId() throws GkException {
		return SERVICE_ID;
	}

	/**
	 * (inheritDoc)
	 * 
	 * @see org.goko.core.common.service.IGokoService#start()
	 */
	@Override
	public void start() throws GkException {		
		LOG.info("Starting " + SERVICE_ID);	
		activateTargetBoardSupport();
	}

	/**
	 * @throws GkException 
	 * 
	 */
	public void setPreferencesService(IPreferencesService prefs) throws GkException {
		this.preferencesService = prefs;	
		activateTargetBoardSupport();
		//on ne detecte pas le changement de preference (le feature set manager n'est pas ajouté en listener sur les prefs)
	}
	/**
	 * (inheritDoc)
	 * 
	 * @see org.goko.core.common.service.IGokoService#stop()
	 */
	@Override
	public void stop() throws GkException {
		// TODO Auto-generated method stub

	}

	/**
	 * (inheritDoc)
	 * 
	 * @see org.goko.core.feature.IFeatureSetManager#getSupportedBoards()
	 */
	@Override
	public List<TargetBoard> getSupportedBoards() {
		List<TargetBoard> lstBoards = new ArrayList<TargetBoard>();
		for (IFeatureSet iFeatureSet : lstFeatureSet) {
			if(!lstBoards.contains(iFeatureSet.getTargetBoard())){
				lstBoards.add(iFeatureSet.getTargetBoard());
			}
		}
		return lstBoards;
	}

	/**
	 * (inheritDoc)
	 * 
	 * @see org.eclipse.jface.util.IPropertyChangeListener#propertyChange(org.eclipse.jface.util.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent event) {		
//		try {
//		//	deactivateTargetBoardSupport(String.valueOf(event.getOldValue()));
//		//	activateTargetBoardSupport();
//		} catch (GkException e) {
//			LOG.error(e);
//		}
		
		//a vertifier les cas à la con
	}

	/**
	 * @return the extensionRegistry
	 */
	protected IExtensionRegistry getExtensionRegistry() {
		return extensionRegistry;
	}

	/**
	 * @param extensionRegistry the extensionRegistry to set
	 */
	public void setExtensionRegistry(IExtensionRegistry extensionRegistry) {
		this.extensionRegistry = extensionRegistry;
	}

	/**
	 * @param eventAdmin the eventAdmin to set
	 */
	public void setEventAdmin(EventAdmin eventAdmin) {
		this.eventAdmin = eventAdmin;
	}



}

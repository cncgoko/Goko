/**
 * 
 */
package org.goko.core.feature;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.EclipseContextFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.service.IGokoService;
import org.goko.core.config.GokoPreference;
import org.goko.core.log.GkLog;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

/**
 * @author PsyKo
 *
 */
public class FeatureSetManagerImpl implements IGokoService, IFeatureSetManager {
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
	
	/**
	 * Constructor
	 */
	public FeatureSetManagerImpl() {
		this.lstFeatureSet = new ArrayList<IFeatureSet>();		
		this.lstActiveFeatureSet = new ArrayList<IFeatureSet>();		
	}
	
	/**
	 * Add the given FeatureSet 
	 * @param featureSet the feature set to add
	 * @throws GkException GkException 
	 */
	public void addFeatureSet(IFeatureSet featureSet) throws GkException{
		lstFeatureSet.add(featureSet);
		startIfRequired(featureSet);			
	}

	private void startIfRequired(IFeatureSet featureSet) throws GkException{			
		if(/*StringUtils.isEmpty(getTargetBoard()) 
		|| */StringUtils.equals(featureSet.getTargetBoard().getId(), getTargetBoard())){
			startFeatureSet(featureSet);	
			if(StringUtils.isEmpty(getTargetBoard())){ // Target board is not set, let's start the first one by default
				setTargetBoard(featureSet.getTargetBoard());
			}
		}		
	}
		
	/**
	 * @param targetBoard
	 */
	private void setTargetBoard(TargetBoard targetBoard) {
		 GokoPreference.getInstance().setTargetBoard(targetBoard.getId());
	}

	/**
	 * @return the configured target board
	 */
	private String getTargetBoard() {
		return GokoPreference.getInstance().getTargetBoard();
	}
	
	private void startFeatureSet(IFeatureSet featureSet) throws GkException{
		lstActiveFeatureSet.add(featureSet);
		BundleContext bundleContext = FrameworkUtil.getBundle(featureSet.getClass()).getBundleContext();		
		ContextInjectionFactory.inject(featureSet, EclipseContextFactory.getServiceContext(bundleContext));
		featureSet.start(bundleContext);
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.feature.IFeatureSetManager#loadFeatureSetFragment()
	 */
	@Override
	public void loadFeatureSetFragment(IEclipseContext context) throws GkException{		
		FeatureSetModelAssembler featureModelAssembler = new FeatureSetModelAssembler();
		ContextInjectionFactory.inject(featureModelAssembler, context);	
		featureModelAssembler.processModel(getTargetBoard(), true);
	}

	/** (inheritDoc)
	 * @see org.goko.core.feature.IFeatureSetManager#existTargetBoard(java.lang.String)
	 */
	@Override
	public boolean existTargetBoard(String targetBoardId) {
		List<TargetBoard> lstBoards = getSupportedBoards();
		for (TargetBoard targetBoard : lstBoards) {
			if(StringUtils.equals(targetBoard.getId(), targetBoardId)){
				return true;
			}
		}
		return false;
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
	public void start() throws GkException {
		LOG.info("Starting "+SERVICE_ID);		
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#stop()
	 */
	@Override
	public void stop() throws GkException {
		// TODO Auto-generated method stub
		
	}

	/** (inheritDoc)
	 * @see org.goko.core.feature.IFeatureSetManager#getSupportedBoards()
	 */
	@Override
	public List<TargetBoard> getSupportedBoards() {		
		List<TargetBoard> lstBoards = new ArrayList<TargetBoard>();
		for (IFeatureSet iFeatureSet : lstFeatureSet) {
			lstBoards.add(iFeatureSet.getTargetBoard());
		}
		return lstBoards;
	}

	/**
	 * @return the extensionRegistry
	 */
	public IExtensionRegistry getExtensionRegistry() {
		return extensionRegistry;
	}

	/**
	 * @param extensionRegistry the extensionRegistry to set
	 */
	public void setExtensionRegistry(IExtensionRegistry extensionRegistry) {
		this.extensionRegistry = extensionRegistry;
	}
	
	
}

/**
 * 
 */
package org.goko.core.feature;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.EclipseContextFactory;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkTechnicalException;
import org.goko.core.common.service.AbstractGokoService;
import org.goko.core.common.service.IGokoService;
import org.goko.core.common.utils.CacheByCode;
import org.goko.core.config.GokoPreference;
import org.goko.core.log.GkLog;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

/**
 * @author PsyKo
 *
 */
public class FeatureSetManagerImpl extends AbstractGokoService implements IGokoService, IFeatureSetManager {
	/** Service ID */
	private static final String SERVICE_ID = "org.goko.core.feature.GokoFeatureManager";
	/** LOG */
	private static final GkLog LOG = GkLog.getLogger(FeatureSetManagerImpl.class);
	/** The list of target boards */
	private CacheByCode<TargetBoard> cacheTargetBoard;
	/** The list of registered features set */
	private List<IFeatureSet> lstFeatureSet;
	/** Current running feature set */
	private List<IFeatureSet> lstActiveFeatureSet;	
	
	/**
	 * Constructor
	 */
	public FeatureSetManagerImpl() {
		this.lstFeatureSet = new ArrayList<IFeatureSet>();
		this.lstActiveFeatureSet = new ArrayList<IFeatureSet>();
		this.cacheTargetBoard = new CacheByCode<TargetBoard>();
	}

	/**
	 * Add the given FeatureSet 
	 * @param featureSet the feature set to add
	 * @throws GkException GkException
	 */
	public void addFeatureSet(IFeatureSet featureSet) throws GkException {
		if(!lstFeatureSet.contains(featureSet)){
			LOG.info("Registering feature set for "+featureSet.getTargetBoard()+" using ["+featureSet+"]");
			lstFeatureSet.add(featureSet);		
			if(!cacheTargetBoard.exist(featureSet.getTargetBoard().getCode())){
				cacheTargetBoard.add(featureSet.getTargetBoard());
			}
			startIfRequired(featureSet);
		}
	}	
//	
//	private void deactivateTargetBoardSupport(String targetBoard) throws GkException{
//		// Let's unload any started set
//		if (StringUtils.isNotBlank(targetBoard) && CollectionUtils.isNotEmpty(lstActiveFeatureSet)) {			
//			for (IFeatureSet iFeatureSet : lstActiveFeatureSet) {				
//				iFeatureSet.stop();
//			}			
//		}
//	}
	
	private void startIfRequired(IFeatureSet featureSet) throws GkException {
		if (StringUtils.isNotBlank(getTargetBoard()) &&
			StringUtils.equals(featureSet.getTargetBoard().getId(), getTargetBoard())) {
			// Starts only if not already active
			if(!lstActiveFeatureSet.contains(featureSet)){
				lstActiveFeatureSet.add(featureSet);			
				BundleContext bundleContext = FrameworkUtil.getBundle(featureSet.getClass()).getBundleContext();
				ContextInjectionFactory.inject(featureSet, EclipseContextFactory.getServiceContext(bundleContext));
				featureSet.start(bundleContext);
			}
		}
	}
	/**
	 * @return the configured target board
	 */
	private String getTargetBoard() {		
		return GokoPreference.getInstance().getTargetBoard();	
	}

	/** (inheritDoc)
	 * @see org.goko.core.feature.IFeatureSetManager#getCurrentTargetBoard()
	 */
	@Override
	public TargetBoard getCurrentTargetBoard() throws GkException {		
		return cacheTargetBoard.get(getTargetBoard());
	}
	/**
	 * (inheritDoc)
	 * @see org.goko.core.feature.IFeatureSetManager#setTargetBoard(java.lang.String)
	 */
	@Override
	public void setTargetBoard(String id) throws GkException {
		String currentTargetBoard = getTargetBoard();
		if(StringUtils.equals(currentTargetBoard, id)){
			return; // Nothing to change		
		}
		GokoPreference.getInstance().setTargetBoard(id);
		try {
			GokoPreference.getInstance().save();
		} catch (IOException e) {
			throw new GkTechnicalException(e);
		}
	}
	
	/**
	 * (inheritDoc)
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
	 * @see org.goko.core.common.service.IGokoService#getServiceId()
	 */
	@Override
	public String getServiceId() throws GkException {
		return SERVICE_ID;
	}

	/**
	 * (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#start()
	 */
	@Override
	public void startService() throws GkException {
		startFeatureSet();
	}

	/**
	 * (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#stop()
	 */
	@Override
	public void stopService() throws GkException {

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

	/** (inheritDoc)
	 * @see org.goko.core.feature.IFeatureSetManager#startFeatureSet()
	 */
	@Override
	public void startFeatureSet() throws GkException {
		// Now enable new feature set
		if(StringUtils.isNotBlank(getTargetBoard())){			
			if (CollectionUtils.isNotEmpty(lstFeatureSet)) {
				for (IFeatureSet iFeatureSet : lstFeatureSet) {
					startIfRequired(iFeatureSet);				
				}				
			}	
		}
	}


}

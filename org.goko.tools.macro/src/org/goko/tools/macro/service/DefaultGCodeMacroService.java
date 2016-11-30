/**
 * 
 */
package org.goko.tools.macro.service;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.collections.CollectionUtils;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkTechnicalException;
import org.goko.core.common.utils.CacheByCode;
import org.goko.core.common.utils.CacheById;
import org.goko.core.common.utils.CacheByKey;
import org.goko.core.common.utils.SequentialIdGenerator;
import org.goko.core.common.utils.UniqueCacheByCode;
import org.goko.core.gcode.element.IGCodeProvider;
import org.goko.core.gcode.service.GCodeProviderDeleteEvent;
import org.goko.core.gcode.service.IGCodeProviderDeleteVetoableListener;
import org.goko.core.gcode.service.IGCodeProviderRepositoryListener;
import org.goko.core.gcode.service.IGCodeService;
import org.goko.tools.macro.bean.GCodeMacro;
import org.goko.tools.macro.bean.GCodeMacroReferenceById;

/**
 * Default GCodeMacro services. Stores macro in a folder inside the workspace 
 * @author Psyko
 * @date 15 oct. 2016
 */
public class DefaultGCodeMacroService implements IGCodeMacroService {
	/** Service ID */
	private static final String SERVICE_ID ="org.goko.tools.macro.servicee.DefaultGCodeMacroService";
	/** The cache of providers */
	private CacheById<IGCodeProvider> cacheProviders;
	/** The cache of macros */
	private CacheById<GCodeMacro> cacheMacros;
	/** The cache of macros */
	private CacheByCode<GCodeMacro> cacheMacrosByCode;
	/** The cache of provider by macro */
	private CacheByKey<Integer, Integer> cacheProviderByMacro; 
	/** The GCode service used to transform the macros */
	private IGCodeService gcodeService;
	/** The list of modifier listener */
	private List<IGCodeMacroServiceListener> listenerList;
	/** The list of repository listener */
	private List<IGCodeProviderRepositoryListener> gcodeListenerList;
	/** The list of modifier listener */
	private List<IGCodeProviderDeleteVetoableListener> gcodeProviderDeleteListenerList;
	
	/**
	 * 
	 */
	public DefaultGCodeMacroService() {
		this.listenerList = new CopyOnWriteArrayList<IGCodeMacroServiceListener>();
		this.gcodeListenerList = new CopyOnWriteArrayList<IGCodeProviderRepositoryListener>();
		this.gcodeProviderDeleteListenerList = new CopyOnWriteArrayList<IGCodeProviderDeleteVetoableListener>();
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
		this.cacheProviders = new CacheById<IGCodeProvider>(new SequentialIdGenerator());
		this.cacheMacros = new CacheById<GCodeMacro>(new SequentialIdGenerator());
		this.cacheMacrosByCode = new UniqueCacheByCode<GCodeMacro>();
		this.cacheProviderByMacro = new CacheByKey<Integer, Integer>();
	}

	protected void initFromMacroFolder(){
		
	}
	/** (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#stop()
	 */
	@Override
	public void stop() throws GkException {
		
	}
	
	/** (inheritDoc)
	 * @see org.goko.tools.macro.service.IGCodeMacroService#addGCodeMacro(org.goko.tools.macro.bean.GCodeMacro)
	 */
	@Override
	public void addGCodeMacro(GCodeMacro gcodeMacro) throws GkException {
		this.cacheMacros.add(gcodeMacro);
		this.cacheMacrosByCode.add(gcodeMacro);
		notifyGCodeMacroCreate(gcodeMacro);
	}

	/** (inheritDoc)
	 * @see org.goko.tools.macro.service.IGCodeMacroService#getGCodeMacro(java.lang.Integer)
	 */
	@Override
	public GCodeMacro getGCodeMacro(Integer id) throws GkException {		
		return cacheMacros.get(id);
	}
	
	/** (inheritDoc)
	 * @see org.goko.tools.macro.service.IGCodeMacroService#getGCodeMacro(java.lang.String)
	 */
	@Override
	public GCodeMacro getGCodeMacro(String code) throws GkException {
		return cacheMacrosByCode.get(code);
	}
	
	/** (inheritDoc)
	 * @see org.goko.tools.macro.service.IGCodeMacroService#findGCodeMacro(java.lang.String)
	 */
	@Override
	public GCodeMacro findGCodeMacro(String code) throws GkException {
		return cacheMacrosByCode.find(code);
	}
	
	/** (inheritDoc)
	 * @see org.goko.tools.macro.service.IGCodeMacroService#findGCodeMacro(java.lang.Integer)
	 */
	@Override
	public GCodeMacro findGCodeMacro(Integer id) throws GkException {
		return cacheMacros.find(id);
	}

	/** (inheritDoc)
	 * @see org.goko.tools.macro.service.IGCodeMacroService#getGCodeMacro()
	 */
	@Override
	public List<GCodeMacro> getGCodeMacro() throws GkException {
		return cacheMacros.get();
	}

	/** (inheritDoc)
	 * @see org.goko.tools.macro.service.IGCodeMacroService#updateGCodeMacro(org.goko.tools.macro.bean.GCodeMacro)
	 */
	@Override
	public void updateGCodeMacro(GCodeMacro macro) throws GkException {
		cacheMacros.remove(macro.getId());
		cacheMacros.add(macro);
		// Force update the GCode provider
		updateGCodeProvider(macro);
		IGCodeProvider provider = getGCodeProviderByMacro(macro.getId());
		
		notifyGCodeMacroUpdate(macro);
		notifyGCodeProviderUpdate(provider);
	}
	/** (inheritDoc)
	 * @see org.goko.tools.macro.service.IGCodeMacroService#deleteGCodeMacro(org.goko.tools.macro.bean.GCodeMacro)
	 */
	@Override
	public void deleteGCodeMacro(GCodeMacro macro) throws GkException {		
		IGCodeProvider provider = getGCodeProviderByMacro(macro.getId());
		if(provider.isLocked()){
			throw new GkTechnicalException("Cannot delete the selected macro as it is currently in use.");
		}
		
		if(checkDeleteGCodeProvider(provider.getId())){		
			notifyBeforeGCodeMacroDelete(macro);
			notifyBeforeGCodeProviderDelete(provider);
			
			cacheMacros.remove(macro);
			cacheMacrosByCode.remove(macro);
			
			notifyAfterGCodeProviderDelete(provider);
			notifyAfterGCodeMacroDelete(macro);
		}
	}

	/** (inheritDoc)
	 * @see org.goko.tools.macro.service.IGCodeMacroService#deleteGCodeMacro(java.lang.String)
	 */
	@Override
	public void deleteGCodeMacro(String code) throws GkException {
		GCodeMacro macro = getGCodeMacro(code);
		deleteGCodeMacro(macro);
	}

	/** (inheritDoc)
	 * @see org.goko.tools.macro.service.IGCodeMacroService#deleteGCodeMacro(java.lang.Integer)
	 */
	@Override
	public void deleteGCodeMacro(Integer id) throws GkException {
		GCodeMacro macro = getGCodeMacro(id);
		deleteGCodeMacro(macro);
	}
	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeProviderRepository#getGCodeProvider()
	 */
	@Override
	public List<IGCodeProvider> getGCodeProvider() throws GkException {		
		return cacheProviders.get();
	}

	/** (inheritDoc)
	 * @see org.goko.tools.macro.service.IGCodeMacroService#getGCodeProviderByMacro(java.lang.Integer)
	 */
	@Override
	public IGCodeProvider getGCodeProviderByMacro(Integer idMacro) throws GkException {
		GCodeMacro macro = getGCodeMacro(idMacro);
		if(!cacheProviderByMacro.exist(idMacro)){			
			updateGCodeProvider(macro);
		}
		return getGCodeProvider(cacheProviderByMacro.get(idMacro));
	}
	
	/** (inheritDoc)
	 * @see org.goko.tools.macro.service.IGCodeMacroService#internalGetGCodeProviderByMacro(java.lang.Integer)
	 */
	@Override
	public IGCodeProvider internalGetGCodeProviderByMacro(Integer idMacro) throws GkException {
		GCodeMacro macro = getGCodeMacro(idMacro);
		if(!cacheProviderByMacro.exist(idMacro)){			
			updateGCodeProvider(macro);
		}
		return cacheProviders.get(cacheProviderByMacro.get(idMacro));
	}
	
	protected void updateGCodeProvider(GCodeMacro macro) throws GkException{
		IGCodeProvider provider = gcodeService.parse(macro.getContent(), new NullProgressMonitor());
		provider.setCode(macro.getCode());
		
		if(cacheProviderByMacro.exist(macro.getId())){
			Integer previousProviderId = cacheProviderByMacro.get(macro.getId());
			provider.setId(previousProviderId);			
			cacheProviderByMacro.remove(macro.getId());
			cacheProviders.remove(previousProviderId);
		}
		cacheProviders.add(provider);
		cacheProviderByMacro.add(macro.getId(), provider.getId());
		notifyGCodeProviderUpdate(getGCodeProvider(provider.getId())); 
	}
	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeProviderRepository#getGCodeProvider(java.lang.Integer)
	 */
	@Override
	public IGCodeProvider getGCodeProvider(Integer id) throws GkException {
		return new GCodeMacroReferenceById(this, id);
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeProviderRepository#internalGetGCodeProvider(java.lang.Integer)
	 */
	@Override
	public IGCodeProvider internalGetGCodeProvider(Integer id) throws GkException {
		return cacheProviders.get(id);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeProviderRepository#findGCodeProvider(java.lang.Integer)
	 */
	@Override
	public IGCodeProvider findGCodeProvider(Integer id) throws GkException {
		return cacheProviders.find(id);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeProviderRepository#getGCodeProvider(java.lang.String)
	 */
	@Override
	public IGCodeProvider getGCodeProvider(String code) throws GkException {
		return null;
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeProviderRepository#lockGCodeProvider(java.lang.Integer)
	 */
	@Override
	public void lockGCodeProvider(Integer idGcodeProvider) throws GkException {
		// Lock state not handled in macro		
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeProviderRepository#unlockGCodeProvider(java.lang.Integer)
	 */
	@Override
	public void unlockGCodeProvider(Integer idGcodeProvider) throws GkException {
		// Lock state not handled in macro
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeProviderRepository#addGCodeProvider(org.goko.core.gcode.element.IGCodeProvider)
	 */
	@Override
	public void addGCodeProvider(IGCodeProvider provider) throws GkException {
		cacheProviders.add(provider);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeProviderRepository#deleteGCodeProvider(java.lang.Integer)
	 */
	@Override
	public void deleteGCodeProvider(Integer id) throws GkException {
		GCodeMacro macro = getGCodeMacro(id);
		deleteGCodeMacro(macro);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeProviderRepository#addListener(org.goko.core.gcode.service.IGCodeProviderRepositoryListener)
	 */
	@Override
	public void addListener(IGCodeProviderRepositoryListener listener) throws GkException {
		gcodeListenerList.add(listener);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeProviderRepository#removeListener(org.goko.core.gcode.service.IGCodeProviderRepositoryListener)
	 */
	@Override
	public void removeListener(IGCodeProviderRepositoryListener listener) throws GkException {
		gcodeListenerList.remove(listener);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeProviderRepository#clearAll()
	 */
	@Override
	public void clearAll() throws GkException {
		cacheProviders.removeAll();
		cacheMacros.removeAll();
		cacheMacrosByCode.removeAll();
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeProviderRepository#addDeleteVetoableListener(org.goko.core.gcode.service.IGCodeProviderDeleteVetoableListener)
	 */
	@Override
	public void addDeleteVetoableListener(IGCodeProviderDeleteVetoableListener listener) throws GkException {
		if(!gcodeProviderDeleteListenerList.contains(listener)){
			gcodeProviderDeleteListenerList.add(listener);
		}	
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeProviderRepository#removeDeleteVetoableListener(org.goko.core.gcode.service.IGCodeProviderDeleteVetoableListener)
	 */
	@Override
	public void removeDeleteVetoableListener(IGCodeProviderDeleteVetoableListener listener) throws GkException {
		if(gcodeProviderDeleteListenerList.contains(listener)){
			gcodeProviderDeleteListenerList.remove(listener);
		}
	}

	/**
	 * @param gcodeService the gcodeService to set
	 */
	public void setGCodeService(IGCodeService gcodeService) {
		this.gcodeService = gcodeService;
	}
	
	/** (inheritDoc)
	 * @see org.goko.tools.macro.service.IGCodeMacroService#addListener(org.goko.tools.macro.service.IGCodeMacroServiceListener)
	 */
	@Override
	public void addListener(IGCodeMacroServiceListener listener) throws GkException {
		listenerList.add(listener);
	}
	
	/** (inheritDoc)
	 * @see org.goko.tools.macro.service.IGCodeMacroService#removeListener(org.goko.tools.macro.service.IGCodeMacroServiceListener)
	 */
	@Override
	public void removeListener(IGCodeMacroServiceListener listener) throws GkException {
		listenerList.remove(listener);		
	}
	
	/**
	 * Notify the listener that the given GCodeMacro was created
	 * @param macro the target GCodeMacro
	 * @throws GkException GkException
	 */
	protected void notifyGCodeMacroCreate(GCodeMacro macro) throws GkException {
		if (CollectionUtils.isNotEmpty(listenerList)) {
			for (IGCodeMacroServiceListener listener : listenerList) {
				listener.onGCodeMacroCreate(macro);
			}
		}
	}
	
	/**
	 * Notify the listener that the given GCodeMacro was updated
	 * @param macro the target GCodeMacro
	 * @throws GkException GkException
	 */
	protected void notifyGCodeMacroUpdate(GCodeMacro macro) throws GkException {
		if (CollectionUtils.isNotEmpty(listenerList)) {
			for (IGCodeMacroServiceListener listener : listenerList) {
				listener.onGCodeMacroUpdate(macro);
			}
		}
	}

	/**
	 * Notify the listener that the given GCodeMacro is about to be deleted
	 * @param macro the target GCodeMacro
	 * @throws GkException GkException
	 */
	protected void notifyBeforeGCodeMacroDelete(GCodeMacro macro) throws GkException {
		if (CollectionUtils.isNotEmpty(listenerList)) {
			for (IGCodeMacroServiceListener listener : listenerList) {
				listener.beforeGCodeMacroDelete(macro);
			}
		}
	}
	
	/**
	 * Notify the listener that the given GCodeMacro was deleted
	 * @param macro the target GCodeMacro
	 * @throws GkException GkException
	 */
	protected void notifyAfterGCodeMacroDelete(GCodeMacro macro) throws GkException {
		if (CollectionUtils.isNotEmpty(listenerList)) {
			for (IGCodeMacroServiceListener listener : listenerList) {
				listener.afterGCodeMacroDelete(macro);
			}
		}
	}
	
	/**
	 * Notify the listener that the given GCodeProvider was updated
	 * @param provider the target provider
	 * @throws GkException GkException
	 */
	protected void notifyGCodeProviderUpdate(IGCodeProvider provider) throws GkException {
		if (CollectionUtils.isNotEmpty(listenerList)) {
			for (IGCodeProviderRepositoryListener listener : gcodeListenerList) {
				listener.onGCodeProviderUpdate(provider);
			}
		}
	}

	/**
	 * Notify the listener that the given GCodeProvider is about to be deleted
	 * @param provider the target provider
	 * @throws GkException GkException
	 */
	protected void notifyBeforeGCodeProviderDelete(IGCodeProvider provider) throws GkException {
		if (CollectionUtils.isNotEmpty(listenerList)) {
			for (IGCodeProviderRepositoryListener listener : gcodeListenerList) {
				listener.beforeGCodeProviderDelete(provider);
			}
		}
	}
	
	/**
	 * Notify the listener that the given GCodeProvider was deleted
	 * @param provider the target provider
	 * @throws GkException GkException
	 */
	protected void notifyAfterGCodeProviderDelete(IGCodeProvider provider) throws GkException {
		if (CollectionUtils.isNotEmpty(listenerList)) {
			for (IGCodeProviderRepositoryListener listener : gcodeListenerList) {
				listener.afterGCodeProviderDelete(provider);
			}
		}
	}
	
	/**
	 * Calls every delete listener to make sure the GCodeProvider can be deleted
	 * @param idGCodeProvider the id of the provider to delete
	 * @return <code>true</code> if it can be deleted, <code>false</code> otherwise
	 */
	protected boolean checkDeleteGCodeProvider(Integer idGCodeProvider){
		GCodeProviderDeleteEvent event = new GCodeProviderDeleteEvent(idGCodeProvider);
		for (IGCodeProviderDeleteVetoableListener deleteListener : gcodeProviderDeleteListenerList) {
			deleteListener.beforeDelete(event);
			if(!event.isDoIt()){
				break;
			}
		}
		return event.isDoIt();
	}
}

package org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.panel;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.inject.Inject;

import org.eclipse.swt.widgets.Display;
import org.goko.common.bindings.AbstractController;
import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.rs274ngcv3.IRS274NGCService;
import org.goko.core.gcode.rs274ngcv3.element.GCodeProvider;
import org.goko.core.gcode.rs274ngcv3.element.IModifier;
import org.goko.core.log.GkLog;


public abstract class AbstractModifierPanelController<T extends AbstractModifierModelObject, M extends IModifier<GCodeProvider>> extends AbstractController<T> implements PropertyChangeListener {
	private static final GkLog LOG = GkLog.getLogger(AbstractModifierPanelController.class);
	@Inject
	private IRS274NGCService rs274NGCService ;
	private M modifier;
	private boolean lockModifierUpdateOnPropertyChange;
	
	/**
	 * Constructor
	 * @param binding
	 */
	public AbstractModifierPanelController(T binding) {
		super(binding);
		binding.addPropertyChangeListener(this);
	}

	/** (inheritDoc)
	 * @see org.goko.common.bindings.AbstractController#initialize()
	 */
	@Override
	public final void initialize() throws GkException {}
	
	/**
	 * Initialize the content of the data model with data from the modifier
	 * @throws GkException GkException
	 */
	public abstract void initializeFromModifier() throws GkException;
	
	/**
	 * @return the rs274NGCService
	 */
	public IRS274NGCService getRS274NGCService() {
		return rs274NGCService;
	}

	/**
	 * @param rs274ngcService the rs274NGCService to set
	 */
	public void setRS274NGCService(IRS274NGCService rs274ngcService) {
		rs274NGCService = rs274ngcService;
	}
	
	protected abstract M updateModifier() throws GkException;
	
	/**
	 * Performs the update in the RS274 service
	 * @throws GkException GkException
	 */
	public void performUpdateModifier() throws GkException{	
		if(getDataModel().isDirty()){			
			getDataModel().setDirty(false);
			Display.getDefault().asyncExec(new Runnable() {
				
				@Override
				public void run() {
					try {
						if(getRS274NGCService().findModifier(getModifier().getId()) != null){
							getRS274NGCService().updateModifier(updateModifier());
						}
					} catch (GkException e) {
						LOG.error(e);
					}
				}
			});			
		}
	}
	
	/** (inheritDoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		try {
			if(!isLockModifierUpdateOnPropertyChange() && getDataModel().isDirty()){
				performUpdateModifier();
			}
		} catch (GkException e) {
			LOG.error(e);
		}
	
	}
	
	/**
	 * @return the modifier
	 */
	public M getModifier() {
		return modifier;
	}

	/**
	 * @param modifier the modifier to set
	 */
	public void setModifier(M modifier) {
		this.modifier = modifier;
	}

	/**
	 * @return the lockModifierUpdateOnPropertyChange
	 */
	public boolean isLockModifierUpdateOnPropertyChange() {
		return lockModifierUpdateOnPropertyChange;
	}

	/**
	 * @param lockModifierUpdateOnPropertyChange the lockModifierUpdateOnPropertyChange to set
	 */
	public void setLockModifierUpdateOnPropertyChange(boolean lockModifierUpdateOnPropertyChange) {
		this.lockModifierUpdateOnPropertyChange = lockModifierUpdateOnPropertyChange;
	}

}

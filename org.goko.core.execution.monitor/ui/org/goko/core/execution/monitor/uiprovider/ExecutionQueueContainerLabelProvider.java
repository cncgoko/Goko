package org.goko.core.execution.monitor.uiprovider;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.DecorationOverlayIcon;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wb.swt.ResourceManager;
import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.element.validation.ValidationResult;
import org.goko.core.gcode.execution.ExecutionState;
import org.goko.core.gcode.execution.ExecutionToken;
import org.goko.core.gcode.service.IGCodeValidationService;
import org.goko.core.log.GkLog;

public class ExecutionQueueContainerLabelProvider extends LabelProvider implements IStyledLabelProvider {
	private static final GkLog LOG = GkLog.getLogger(ExecutionQueueContainerLabelProvider.class);	
	private final ImageDescriptor warningImageDescriptor;
	private final ImageDescriptor errorImageDescriptor;
	/** Validation service */
	private IGCodeValidationService<?, ?, ?> gcodeValidationService;
	
	/**
	 * Constructor
	 */
	public ExecutionQueueContainerLabelProvider() {
		super();
		warningImageDescriptor = ResourceManager.getPluginImageDescriptor("org.goko.gcode.rs274ngcv3.ui", "resources/icons/warn_ovr.png");
		errorImageDescriptor   = ResourceManager.getPluginImageDescriptor("org.goko.core.execution.monitor", "resources/icons/error_ovr.png");
	}
	
	/** (inheritDoc)
	 * @see org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider#getStyledText(java.lang.Object)
	 */
	@Override
	public StyledString getStyledText(Object element) {
		if(element instanceof ExecutionQueueContainerUiProvider){
			StyledString styleString = new StyledString();
			styleString.append("Execution queue ");
			return styleString;
		}else if(element instanceof ExecutionToken){
			ExecutionToken<?> executionToken = (ExecutionToken<?>) element;
			StyledString styleString = new StyledString();
			try {
				styleString.append(executionToken.getGCodeProvider().getCode());
			} catch (GkException e) {
				styleString.append("ERROR");
				LOG.error(e);
			}
			return styleString;
		}
		return null;
	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.viewers.LabelProvider#getImage(java.lang.Object)
	 */
	@Override
	public Image getImage(Object element) {
		if(element instanceof ExecutionQueueContainerUiProvider){
			return ResourceManager.getPluginImage("org.goko.core.execution.monitor", "resources/icons/blue-documents-stack.png");
		}else if(element instanceof ExecutionToken){
			ExecutionToken<?> token = (ExecutionToken<?>) element;
			Image image = null;
			boolean errors = false;
			ValidationResult result = null;
			try{
				if(gcodeValidationService != null){
					result = gcodeValidationService.getValidationResult(token.getGCodeProvider().getId());
					errors = result.hasErrors();
				}
			}catch(GkException e){
				LOG.error(e);
			}
			if(errors){
				image =  ResourceManager.getPluginImage("org.goko.core.execution.monitor", "resources/icons/error_ovr.png");
			}else if(token.getState() == ExecutionState.COMPLETE){
				image = ResourceManager.getPluginImage("org.goko.core.execution.monitor", "resources/icons/tick.png");
			}else if(token.getState() == ExecutionState.RUNNING){
				image = ResourceManager.getPluginImage("org.goko.core.execution.monitor", "resources/icons/control-running.png");
			}else if(token.getState() == ExecutionState.PAUSED){
				image = ResourceManager.getPluginImage("org.goko.core.execution.monitor", "resources/icons/pause.gif");
			}else if(token.getState() == ExecutionState.ERROR){
				image = ResourceManager.getPluginImage("org.goko.core.execution.monitor", "resources/icons/cross.png");
			}else if(token.getState() == ExecutionState.STOPPED){
				image = ResourceManager.getPluginImage("org.goko.core.execution.monitor", "resources/icons/stop.gif");
			}
			image = decorateValidationTarget(image, result);
			return image;
		}
		return null;
	}
	
	protected Image decorateValidationTarget(Image image, ValidationResult result){
		if(image != null && result != null){
			if(result.hasErrors()){
				return new DecorationOverlayIcon(image, errorImageDescriptor, IDecoration.BOTTOM_RIGHT).createImage();
			}else if(result.hasWarnings()){
				return new DecorationOverlayIcon(image, warningImageDescriptor, IDecoration.BOTTOM_RIGHT).createImage();
			}
		}
		return image;
	}

	/**
	 * @return the gcodeValidationService
	 */
	public IGCodeValidationService<?, ?, ?> getGcodeValidationService() {
		return gcodeValidationService;
	}

	/**
	 * @param gcodeValidationService the gcodeValidationService to set
	 */
	public void setGcodeValidationService(IGCodeValidationService<?, ?, ?> gcodeValidationService) {
		this.gcodeValidationService = gcodeValidationService;
	}
}

/**
 *
 */
package org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.DecorationOverlayIcon;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wb.swt.ResourceManager;
import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.element.IGCodeProvider;
import org.goko.core.gcode.element.validation.ValidationResult;
import org.goko.core.gcode.rs274ngcv3.element.IModifier;
import org.goko.core.gcode.service.IGCodeValidationService;
import org.goko.core.log.GkLog;


/**
 * @author PsyKo
 * @date 31 oct. 2015
 */
public class GCodeContainerLabelProvider extends LabelProvider implements IStyledLabelProvider {
	private static final GkLog LOG = GkLog.getLogger(GCodeContainerLabelProvider.class);
	private final ImageDescriptor warningImageDescriptor;
	private final ImageDescriptor errorImageDescriptor;
	/** Validation service */
	private IGCodeValidationService<?,?,?> gcodeValidationService;
	/**
	 * 
	 */
	public GCodeContainerLabelProvider() {
		super();
		warningImageDescriptor = ResourceManager.getPluginImageDescriptor("org.goko.gcode.rs274ngcv3.ui", "resources/icons/warn_ovr.png");
		errorImageDescriptor   = ResourceManager.getPluginImageDescriptor("org.goko.gcode.rs274ngcv3.ui", "resources/icons/error_ovr.png");
	}


	/** (inheritDoc)
	 * @see org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider#getStyledText(java.lang.Object)
	 */
	@Override
	public StyledString getStyledText(Object element) {
		if(element instanceof IGCodeProvider){
			return new StyledString(((IGCodeProvider) element).getCode());
		}else if(element instanceof GCodeContainerUiProvider){
			StyledString styleString = new StyledString();
			styleString.append("GCode ");
			return styleString;
		}else if(element instanceof IModifier){
			StyledString styleString = new StyledString();
			styleString.append(((IModifier<?>) element).getModifierName());
			return styleString;
		}
		return null;
	}


	/** (inheritDoc)
	 * @see org.eclipse.jface.viewers.LabelProvider#getImage(java.lang.Object)
	 */
	@Override
	public Image getImage(Object element) {
		if(element instanceof GCodeContainerUiProvider){
			return ResourceManager.getPluginImage("org.goko.gcode.rs274ngcv3.ui", "resources/icons/resource_persp.gif");
		}else if(element instanceof IGCodeProvider){
			Image image = ResourceManager.getPluginImage("org.goko.gcode.rs274ngcv3.ui", "resources/icons/document-attribute-g.png");
			IGCodeProvider provider = (IGCodeProvider) element;			
			if(provider.isLocked()){
				return ResourceManager.getPluginImage("org.goko.gcode.rs274ngcv3.ui", "resources/icons/lock.png");
			}
			try {
				image = decorateValidationTarget(image, provider);
			} catch (GkException e) {
				LOG.error(e);
			}
			return image;
		}else if(element instanceof IModifier){
			if(((IModifier<?>) element).isEnabled()){
				return ResourceManager.getPluginImage("org.goko.gcode.rs274ngcv3.ui", "resources/icons/modifier-on.png");
			}else{
				return ResourceManager.getPluginImage("org.goko.gcode.rs274ngcv3.ui", "resources/icons/modifier-off.png");
			}
		}
		return super.getImage(element);
	}
	
	protected Image decorateValidationTarget(Image image, IGCodeProvider target) throws GkException{
		if(gcodeValidationService != null){
			ValidationResult result = gcodeValidationService.getValidationResult(target.getId());
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
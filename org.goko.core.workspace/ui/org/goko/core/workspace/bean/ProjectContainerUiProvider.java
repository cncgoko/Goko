/**
 * 
 */
package org.goko.core.workspace.bean;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.goko.core.common.exception.GkException;

/**
 * @author PsyKo
 * @date 31 oct. 2015
 */
public abstract class ProjectContainerUiProvider {
	/** The type of supported container */
	private String type;
	/** The display order */
	private int order;
	
	/**
	 * @param type
	 */
	public ProjectContainerUiProvider(String type, int order) {
		super();
		this.type = type;
		this.order = order;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	public abstract boolean providesLabelFor(Object content) throws GkException;
	
	/**
	 * Returns the styled text label for the given element
	 *
	 * @param element
	 *            the element to evaluate the styled string for
	 *
	 * @return the styled string.
	 */
	public abstract StyledString getStyledText(Object element);

	/**
	 * Returns the image for the label of the given element. The image is
	 * owned by the label provider and must not be disposed directly.
	 * Instead, dispose the label provider when no longer needed.
	 *
	 * @param element
	 *            the element for which to provide the label image
	 * @return the image used to label the element, or <code>null</code>
	 *         if there is no image for the given object
	 */
	public abstract Image getImage(Object element);

	public abstract boolean providesConfigurationPanelFor(ISelection content) throws GkException;
	
	public abstract IPropertiesPanel createConfigurationPanelFor(Composite parent, ISelection content) throws GkException;
	
	public abstract boolean providesContentFor(Object content) throws GkException;
	
	public abstract boolean hasChildren(Object content) throws GkException;

	public abstract Object[] getChildren(Object content) throws GkException;

	public abstract Object getParent(Object content) throws GkException;
	
	public abstract boolean providesMenuFor(ISelection selection) throws GkException;
	
	public abstract void createMenuFor(IMenuManager contextMenu, ISelection selection) throws GkException;

	/**
	 * @return the order
	 */
	public int getOrder() {
		return order;
	}

	/**
	 * @param order the order to set
	 */
	public void setOrder(int order) {
		this.order = order;
	}
}

package org.goko.core.viewer.renderer;

import org.goko.core.common.exception.GkException;

/**
 * Defines a renderer
 *
 * @author Psyko
 *
 */
public interface IViewer3DRenderer {
	String getId();

	void render(IRendererProxy proxy) throws GkException;

	boolean isEnabled();

	void setEnabled(boolean enabled);
}

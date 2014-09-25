package org.goko.core.viewer.renderer;


public abstract class AbstractViewer3DRenderer implements IViewer3DRenderer {
	/** Enabled state of the renderer */
	private boolean disabled;

	public AbstractViewer3DRenderer() {
		disabled = false;
	}

	/** (inheritDoc)
	 * @see org.goko.core.viewer.renderer.IViewer3DRenderer#isEnabled()
	 */
	@Override
	public boolean isEnabled() {
		return !disabled;
	}

	/** (inheritDoc)
	 * @see org.goko.core.viewer.renderer.IViewer3DRenderer#setEnabled(boolean)
	 */
	@Override
	public void setEnabled(boolean enabled) {
		this.disabled = !enabled;
	}
}

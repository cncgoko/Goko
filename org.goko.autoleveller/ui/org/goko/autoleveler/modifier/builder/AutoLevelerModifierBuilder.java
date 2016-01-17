package org.goko.autoleveler.modifier.builder;

import org.eclipse.swt.widgets.Composite;
import org.goko.autoleveler.modifier.AutoLevelerModifier;
import org.goko.autoleveler.modifier.ui.AutoLevelerModifierConfigurationPanel;
import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.rs274ngcv3.IRS274NGCService;
import org.goko.core.gcode.rs274ngcv3.element.IModifier;
import org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.AbstractModifierUiProvider;
import org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.IModifierUiProvider;
import org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.panel.IModifierPropertiesPanel;

public class AutoLevelerModifierBuilder extends AbstractModifierUiProvider<AutoLevelerModifier> implements IModifierUiProvider<AutoLevelerModifier>{
	/** GCode service required by modifier */
	private IRS274NGCService rs274ngcService;
	
	/**
	 * Constructor
	 */
	public AutoLevelerModifierBuilder() {
		super(AutoLevelerModifier.class);
	}
	
	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.IModifierUiProvider#createDefaultModifier(java.lang.Integer)
	 */
	@Override
	public AutoLevelerModifier createDefaultModifier(Integer idTargetGCodeProvider) throws GkException {
		AutoLevelerModifier modifier = new AutoLevelerModifier(idTargetGCodeProvider);
		modifier.setRS274NGCService(rs274ngcService);
		return modifier;
	}

	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.IModifierUiProvider#providesConfigurationPanelFor(org.goko.core.gcode.rs274ngcv3.element.IModifier)
	 */
	@Override
	public boolean providesConfigurationPanelFor(IModifier<?> modifier) throws GkException {
		return modifier instanceof AutoLevelerModifier;
	}

	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.IModifierUiProvider#getModifierName()
	 */
	@Override
	public String getModifierName() {
		return "Auto leveler";
	}

	/**
	 * @return the rs274ngcService
	 */
	public IRS274NGCService getRS274NGCService() {
		return rs274ngcService;
	}

	/**
	 * @param rs274ngcService the rs274ngcService to set
	 */
	public void setRS274NGCService(IRS274NGCService rs274ngcService) {
		this.rs274ngcService = rs274ngcService;
	}

	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.AbstractModifierUiProvider#createPropertiesPanelForModifier(org.eclipse.swt.widgets.Composite, org.goko.core.gcode.rs274ngcv3.element.IModifier)
	 */
	@Override
	protected IModifierPropertiesPanel<AutoLevelerModifier> createPropertiesPanelForModifier(Composite parent, AutoLevelerModifier modifier) throws GkException {
		AutoLevelerModifierConfigurationPanel panel = new AutoLevelerModifierConfigurationPanel(getContext());
		panel.createContent(parent, modifier);
		parent.layout();
		return panel;
	}

}

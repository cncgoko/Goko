package org.goko.tools.autoleveler.modifier.builder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.widgets.Composite;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.quantity.LengthUnit;
import org.goko.core.common.measure.quantity.Speed;
import org.goko.core.common.measure.quantity.SpeedUnit;
import org.goko.core.controller.IProbingService;
import org.goko.core.gcode.element.IGCodeProvider;
import org.goko.core.gcode.rs274ngcv3.IRS274NGCService;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.GCodeProvider;
import org.goko.core.gcode.rs274ngcv3.element.IModifier;
import org.goko.core.gcode.rs274ngcv3.modifier.IModifierListener;
import org.goko.core.math.BoundingTuple6b;
import org.goko.core.math.Tuple6b;
import org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.AbstractModifierUiProvider;
import org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.IModifierUiProvider;
import org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.panel.IModifierPropertiesPanel;
import org.goko.tools.autoleveler.bean.grid.GridHeightMap;
import org.goko.tools.autoleveler.modifier.AutoLevelerModifier;
import org.goko.tools.autoleveler.modifier.GridAutoLevelerModifier;
import org.goko.tools.autoleveler.modifier.renderer.GridAutoLevelerRenderer;
import org.goko.tools.autoleveler.modifier.ui.AutoLevelerModifierConfigurationPanel;
import org.goko.tools.viewer.jogl.service.IJoglViewerService;
import org.goko.tools.viewer.jogl.utils.render.internal.AbstractVboJoglRenderer;

public class AutoLevelerModifierBuilder extends AbstractModifierUiProvider<GridAutoLevelerModifier> implements IModifierUiProvider<GridAutoLevelerModifier>, IModifierListener{
	/** GCode service required by modifier */
	private IRS274NGCService rs274ngcService;
	/** The jogl viewer service */
	private IJoglViewerService joglViewerService;
	private IProbingService probingService;
	/** Create the renderer for the modifier */
	private Map<Integer, AbstractVboJoglRenderer> renderers;
	/**
	 * Constructor
	 */
	public AutoLevelerModifierBuilder() {
		super(GridAutoLevelerModifier.class);
		this.renderers = new HashMap<Integer, AbstractVboJoglRenderer>();
	}
	
	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.IModifierUiProvider#createDefaultModifier(java.lang.Integer)
	 */
	@Override
	public GridAutoLevelerModifier createDefaultModifier(Integer idTargetGCodeProvider) throws GkException {
		GridAutoLevelerModifier modifier = new GridAutoLevelerModifier();
		modifier.setIdGCodeProvider(idTargetGCodeProvider);
		IGCodeProvider provider = getRS274NGCService().getGCodeProvider(idTargetGCodeProvider);
		BoundingTuple6b bounds = getRS274NGCService().getBounds(new GCodeContext(), getRS274NGCService().getInstructions(new GCodeContext(), provider));
		List<Tuple6b> offsets = new ArrayList<Tuple6b>();
		offsets.add(new Tuple6b( bounds.getMin().getX(), bounds.getMin().getY(), Length.ZERO));
		offsets.add(new Tuple6b( bounds.getMin().getX(), bounds.getMax().getY(), Length.ZERO));
		offsets.add(new Tuple6b( bounds.getMax().getX(), bounds.getMin().getY(), Length.ZERO));
		offsets.add(new Tuple6b( bounds.getMax().getX(), bounds.getMax().getY(), Length.ZERO));
		GridHeightMap defaultMap = new GridHeightMap(new int[][]{{0,1},{2,3}}, offsets);
		defaultMap.setClearanceHeight(Length.valueOf(3, LengthUnit.MILLIMETRE));
		defaultMap.setStart(bounds.getMin());
		defaultMap.setEnd(bounds.getMax());
		defaultMap.setProbeFeedrate(Speed.valueOf(30, SpeedUnit.MILLIMETRE_PER_MINUTE));
		defaultMap.setProbeStartHeight(Length.valueOf(1, LengthUnit.MILLIMETRE));
		defaultMap.setProbeLowerHeight(Length.valueOf(-1, LengthUnit.MILLIMETRE));
		modifier.setRS274NGCService(rs274ngcService);
		modifier.setHeightMap(defaultMap);
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
		this.rs274ngcService.addModifierListener(this);
	}

	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.AbstractModifierUiProvider#createPropertiesPanelForModifier(org.eclipse.swt.widgets.Composite, org.goko.core.gcode.rs274ngcv3.element.IModifier)
	 */
	@Override
	protected IModifierPropertiesPanel<GridAutoLevelerModifier> createPropertiesPanelForModifier(Composite parent, GridAutoLevelerModifier modifier) throws GkException {		
		AutoLevelerModifierConfigurationPanel panel = new AutoLevelerModifierConfigurationPanel(getContext());
		panel.createContent(parent, modifier);
		panel.getController().setModifier(modifier);
		parent.layout();
		return panel;
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.modifier.IModifierListener#onModifierCreate(java.lang.Integer)
	 */
	@Override
	public void onModifierCreate(Integer idModifier) throws GkException {
		IModifier<GCodeProvider> modifier = getRS274NGCService().getModifier(idModifier);
		if(modifier instanceof GridAutoLevelerModifier){
			GridAutoLevelerModifier autoleveler = (GridAutoLevelerModifier) modifier;
			GridAutoLevelerRenderer renderer = new GridAutoLevelerRenderer(autoleveler);
			renderers.put(autoleveler.getId(), renderer);
			joglViewerService.addRenderer(renderer);
		}
		
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.modifier.IModifierListener#onModifierUpdate(java.lang.Integer)
	 */
	@Override
	public void onModifierUpdate(Integer idModifier) throws GkException {
		IModifier<GCodeProvider> modifier = getRS274NGCService().getModifier(idModifier);
		if(modifier instanceof GridAutoLevelerModifier){			
			GridAutoLevelerModifier autoleveler = (GridAutoLevelerModifier) modifier;
			renderers.get(autoleveler.getId()).update();
			
		}
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.modifier.IModifierListener#onModifierDelete(java.lang.Integer)
	 */
	@Override
	public void onModifierDelete(Integer idModifier) throws GkException {
		if(renderers.containsKey(idModifier)){			
			AbstractVboJoglRenderer renderer = renderers.get(idModifier);						
			renderers.remove(idModifier);
			renderer.destroy();
		}
	}

	/**
	 * @return the joglViewerService
	 */
	public IJoglViewerService getJoglViewerService() {
		return joglViewerService;
	}

	/**
	 * @param joglViewerService the joglViewerService to set
	 */
	public void setJoglViewerService(IJoglViewerService joglViewerService) {
		this.joglViewerService = joglViewerService;
	}

	/**
	 * @return the probingService
	 */
	public IProbingService getProbingService() {
		return probingService;
	}

	/**
	 * @param probingService the probingService to set
	 */
	public void setProbingService(IProbingService probingService) {
		this.probingService = probingService;
	}

}

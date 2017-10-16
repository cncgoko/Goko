/**
 * 
 */
package org.goko.core.gcode.rs274ngcv3.jogl.renderer.colorizer;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import javax.vecmath.Color4f;

import org.apache.commons.collections.CollectionUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.element.IInstructionSetIterator;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.InstructionProvider;
import org.goko.core.gcode.rs274ngcv3.element.InstructionType;
import org.goko.core.gcode.rs274ngcv3.instruction.AbstractInstruction;
import org.goko.core.gcode.rs274ngcv3.instruction.SetSpindleSpeedInstruction;
import org.goko.core.gcode.rs274ngcv3.jogl.internal.Activator;
import org.goko.core.gcode.rs274ngcv3.jogl.renderer.colorizer.overlay.SpindleSpeedColorizerOverlay;

/**
 * @author Psyko
 * @date 31 août 2017
 */
public class SpindleSpeedColorizer extends AbstractInstructionColorizer {
	private static final Color4f DEFAULT_COLOR 	 = new Color4f(1f,1f, 1f, 1f);
	private static final Color4f MIN_COLOR 	 = new Color4f(0.125f,0.126f, 0.592f, 1f);
	private static final Color4f MED_COLOR_3 = new Color4f(1f, 1f,0f, 1f);
	private static final Color4f MED_COLOR_2 = new Color4f(0.04f,1,0.125f, 1f);
	private static final Color4f MED_COLOR_1 = new Color4f(0,0.98f,1f, 1f);
	private static final Color4f MAX_COLOR 	 = new Color4f(1f,0.125f,0, 1f);
	
	private BigDecimal minSpindleSpeed;
	private BigDecimal maxSpindleSpeed; 
	private Map<BigDecimal, Color4f> mapColorByFeed;
	/**
	 * @param overlay
	 */
	public SpindleSpeedColorizer() {
		super(new SpindleSpeedColorizerOverlay("Spindle speed"));
		minSpindleSpeed = new BigDecimal(100000);
		maxSpindleSpeed = BigDecimal.ZERO;
		mapColorByFeed = new HashMap<>();
		
	}

	public void initialize(GCodeContext context, InstructionProvider instructionSet) throws GkException {
		
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.jogl.renderer.colorizer.AbstractInstructionColorizer#initialize(org.goko.core.gcode.rs274ngcv3.context.GCodeContext, java.util.List)
	 */
	@Override
	public void initialize(GCodeContext context, List<Supplier<InstructionProvider>> instructionSet) throws GkException {
		mapColorByFeed = new HashMap<>();
		minSpindleSpeed = new BigDecimal(100000);
		maxSpindleSpeed = BigDecimal.ZERO;
		
		for (Supplier<InstructionProvider> supplier : instructionSet) {
			detectMinMaxValues(context, supplier.get());
		}
		buildGradient();
		
		getOverlay().setMinSpeed(minSpindleSpeed);
		getOverlay().setMaxSpeed(maxSpindleSpeed);
	}
	
	private void detectMinMaxValues(GCodeContext context, InstructionProvider instructionSet) throws GkException{
		IInstructionSetIterator<GCodeContext, AbstractInstruction> iterator = Activator.getRS274NGCService().getIterator(instructionSet, context);
		// Init from context
		if(minSpindleSpeed.compareTo(context.getSpindleSpeed()) > 0){
			minSpindleSpeed = context.getSpindleSpeed();
		}
		
		if(maxSpindleSpeed.compareTo(context.getSpindleSpeed())  < 0){
			maxSpindleSpeed = context.getSpindleSpeed();
		}
		
		
		while(iterator.hasNext()){
			GCodeContext preContext = new GCodeContext(iterator.getContext());
			AbstractInstruction instruction = iterator.next();			
			if(instruction.getType() == InstructionType.SET_SPINDLE_SPEED){
				SetSpindleSpeedInstruction instr = (SetSpindleSpeedInstruction) instruction;
				if(!mapColorByFeed.containsKey(instr.getSpindleSpeed())){
					mapColorByFeed.put(instr.getSpindleSpeed(), new Color4f());
				}
				if(minSpindleSpeed.compareTo(instr.getSpindleSpeed()) > 0){
					minSpindleSpeed = instr.getSpindleSpeed();
				}
				
				if(maxSpindleSpeed.compareTo(instr.getSpindleSpeed()) < 0){
					maxSpindleSpeed = instr.getSpindleSpeed();
				}
			}
		}
		// Fix in case there is only 1 feedrate
		if(minSpindleSpeed.equals(maxSpindleSpeed)){
			minSpindleSpeed = BigDecimal.ZERO;
		}		
	}
	
	private void buildGradient(){
		// Build chunks
		List<GradientChunk> gradientChunks = new ArrayList<>();
		BigDecimal dFeed = maxSpindleSpeed.subtract(minSpindleSpeed);
		gradientChunks.add(new GradientChunk(minSpindleSpeed, MIN_COLOR, minSpindleSpeed.add( dFeed.multiply(new BigDecimal("0.25"))), MED_COLOR_1));
		gradientChunks.add(new GradientChunk(minSpindleSpeed.add( dFeed.multiply(new BigDecimal("0.25"))), MED_COLOR_1, minSpindleSpeed.add( dFeed.multiply(new BigDecimal("0.50"))), MED_COLOR_2));
		gradientChunks.add(new GradientChunk(minSpindleSpeed.add( dFeed.multiply(new BigDecimal("0.50"))), MED_COLOR_2, minSpindleSpeed.add( dFeed.multiply(new BigDecimal("0.75"))), MED_COLOR_3));
		gradientChunks.add(new GradientChunk(minSpindleSpeed.add( dFeed.multiply(new BigDecimal("0.75"))), MED_COLOR_3, maxSpindleSpeed , MAX_COLOR));
		// Sort
		List<BigDecimal> knownFeed = new ArrayList<>(mapColorByFeed.keySet());
		if(CollectionUtils.isNotEmpty(knownFeed)){
			Collections.sort(knownFeed);
			Color4f dt = new Color4f();
			dt.sub(MAX_COLOR, MIN_COLOR);
			dt.scale( (float) (1.0 / knownFeed.size()));
			Color4f currentDt = new Color4f();
			for (BigDecimal speed : knownFeed) {
				Color4f newColor = new Color4f(DEFAULT_COLOR);
				for (GradientChunk chunk : gradientChunks) {
					if(chunk.contains(speed)){
						newColor = chunk.getColor(speed);
						break;
					}
				}
				//newColor.add(currentDt);
				mapColorByFeed.get(speed).set(newColor);
				currentDt.add(dt);
			}
		}
	}
	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.jogl.renderer.colorizer.AbstractInstructionColorizer#getOverlay()
	 */
	@Override
	public SpindleSpeedColorizerOverlay getOverlay() {		
		return (SpindleSpeedColorizerOverlay) super.getOverlay();
	}
	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.jogl.renderer.colorizer.IInstructionColorizer#getColor(org.goko.core.gcode.element.IGCodeContext, org.goko.core.gcode.element.IInstruction)
	 */
	@Override
	public Color4f getColor(GCodeContext context, AbstractInstruction instruction) throws GkException {
		if(mapColorByFeed.containsKey(context.getSpindleSpeed())){
			return mapColorByFeed.get(context.getSpindleSpeed());
		}
		return DEFAULT_COLOR;
	}
	
	class GradientChunk{
		private BigDecimal minSpeed;
		private Color4f minColor;
		private BigDecimal maxSpeed;
		private Color4f maxColor;
		
		/**
		 * @param minSpeed
		 * @param minColor
		 * @param maxSpeed
		 * @param maxColor
		 */
		public GradientChunk(BigDecimal minSpeed, Color4f minColor, BigDecimal maxSpeed, Color4f maxColor) {
			super();
			this.minSpeed = minSpeed;
			this.minColor = minColor;
			this.maxSpeed = maxSpeed;
			this.maxColor = maxColor;
		}

		protected boolean contains(BigDecimal speed){
			return minSpeed.compareTo(speed) <=0 && maxSpeed.compareTo(speed) >= 0;
		}
		
		protected Color4f getColor(BigDecimal speed){
			double delta = speed.subtract(minSpeed).divide(maxSpeed.subtract(minSpeed), 6, RoundingMode.HALF_UP).doubleValue();
			return new Color4f( (float)(minColor.x + delta * (maxColor.x - minColor.x)),
								(float)(minColor.y + delta * (maxColor.y - minColor.y)),
								(float)(minColor.z + delta * (maxColor.z - minColor.z)),
								1f);
		}
		
	}

}

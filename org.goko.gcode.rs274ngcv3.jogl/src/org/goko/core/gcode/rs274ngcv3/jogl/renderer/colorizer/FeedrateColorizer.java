/**
 * 
 */
package org.goko.core.gcode.rs274ngcv3.jogl.renderer.colorizer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.vecmath.Color4f;

import org.apache.commons.collections.CollectionUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.QuantityComparator;
import org.goko.core.common.measure.quantity.Speed;
import org.goko.core.common.measure.quantity.SpeedUnit;
import org.goko.core.gcode.element.IInstructionSetIterator;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.InstructionProvider;
import org.goko.core.gcode.rs274ngcv3.element.InstructionType;
import org.goko.core.gcode.rs274ngcv3.instruction.AbstractInstruction;
import org.goko.core.gcode.rs274ngcv3.instruction.SetFeedRateInstruction;
import org.goko.core.gcode.rs274ngcv3.jogl.internal.Activator;
import org.goko.core.gcode.rs274ngcv3.jogl.renderer.colorizer.overlay.FeedrateColorizerOverlay;

/**
 * @author Psyko
 * @date 23 juil. 2017
 */
public class FeedrateColorizer extends AbstractInstructionColorizer {
	private static final Color4f DEFAULT_COLOR 	 = new Color4f(1f,1f, 1f, 1f);
	private static final Color4f MIN_COLOR 	 = new Color4f(0.125f,0.126f, 0.592f, 1f);
	private static final Color4f MED_COLOR_3 = new Color4f(1f, 1f,0f, 1f);
	private static final Color4f MED_COLOR_2 = new Color4f(0.04f,1,0.125f, 1f);
	private static final Color4f MED_COLOR_1 = new Color4f(0,0.98f,1f, 1f);
	private static final Color4f MAX_COLOR 	 = new Color4f(1f,0.125f,0, 1f);
	
	private Speed minFeedrate;
	private Speed maxFeedrate; 
	private Map<Speed, Color4f> mapColorByFeed;
	/**
	 * @param overlay
	 */
	public FeedrateColorizer() {
		super(new FeedrateColorizerOverlay("Feedrate"));
		minFeedrate = Speed.valueOf(100000, SpeedUnit.MILLIMETRE_PER_MINUTE);
		maxFeedrate = Speed.ZERO;
		mapColorByFeed = new HashMap<>();
		
	}
 
	public void initialize(GCodeContext context, InstructionProvider instructionSet) throws GkException {
		minFeedrate = Speed.valueOf(100000, SpeedUnit.MILLIMETRE_PER_MINUTE);
		maxFeedrate = Speed.ZERO;
		IInstructionSetIterator<GCodeContext, AbstractInstruction> iterator = Activator.getRS274NGCService().getIterator(instructionSet, context);
		// Init from context
		if(minFeedrate.greaterThan(context.getFeedrate())){
			minFeedrate = context.getFeedrate();
		}
		
		if(maxFeedrate.lowerThan(context.getFeedrate())){
			maxFeedrate = context.getFeedrate();
		}
		
		while(iterator.hasNext()){
			GCodeContext preContext = new GCodeContext(iterator.getContext());
			AbstractInstruction instruction = iterator.next();			
			if(instruction.getType() == InstructionType.SET_FEED_RATE){
				SetFeedRateInstruction instr = (SetFeedRateInstruction) instruction;
				if(!mapColorByFeed.containsKey(instr.getFeedrate())){
					mapColorByFeed.put(instr.getFeedrate(), new Color4f());
				}
				if(minFeedrate.greaterThan(instr.getFeedrate())){
					minFeedrate = instr.getFeedrate();
				}
				
				if(maxFeedrate.lowerThan(instr.getFeedrate())){
					maxFeedrate = instr.getFeedrate();
				}
			}
		}
		// Fix in case there is only 1 feedrate
		if(minFeedrate.equals(maxFeedrate)){
			minFeedrate = Speed.ZERO;
		}
		// Build chunks
		List<GradientChunk> gradientChunks = new ArrayList<>();
		Speed dFeed = maxFeedrate.subtract(minFeedrate);
		gradientChunks.add(new GradientChunk(minFeedrate, MIN_COLOR, minFeedrate.add( dFeed.multiply(new BigDecimal("0.25"))), MED_COLOR_1));
		gradientChunks.add(new GradientChunk(minFeedrate.add( dFeed.multiply(new BigDecimal("0.25"))), MED_COLOR_1, minFeedrate.add( dFeed.multiply(new BigDecimal("0.50"))), MED_COLOR_2));
		gradientChunks.add(new GradientChunk(minFeedrate.add( dFeed.multiply(new BigDecimal("0.50"))), MED_COLOR_2, minFeedrate.add( dFeed.multiply(new BigDecimal("0.75"))), MED_COLOR_3));
		gradientChunks.add(new GradientChunk(minFeedrate.add( dFeed.multiply(new BigDecimal("0.75"))), MED_COLOR_3, maxFeedrate , MAX_COLOR));
		// Sort
		List<Speed> knownFeed = new ArrayList<>(mapColorByFeed.keySet());
		if(CollectionUtils.isNotEmpty(knownFeed)){
			Collections.sort(knownFeed, new QuantityComparator<Speed>());
			Color4f dt = new Color4f();
			dt.sub(MAX_COLOR, MIN_COLOR);
			dt.scale( (float) (1.0 / knownFeed.size()));
			Color4f currentDt = new Color4f();
			for (Speed speed : knownFeed) {
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
		getOverlay().setMinFeedrate(minFeedrate);
		getOverlay().setMaxFeedrate(maxFeedrate);
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.jogl.renderer.colorizer.AbstractInstructionColorizer#getOverlay()
	 */
	@Override
	public FeedrateColorizerOverlay getOverlay() {		
		return (FeedrateColorizerOverlay) super.getOverlay();
	}
	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.jogl.renderer.colorizer.IInstructionColorizer#getColor(org.goko.core.gcode.element.IGCodeContext, org.goko.core.gcode.element.IInstruction)
	 */
	@Override
	public Color4f getColor(GCodeContext context, AbstractInstruction instruction) throws GkException {
		if(mapColorByFeed.containsKey(context.getFeedrate())){
			return mapColorByFeed.get(context.getFeedrate());
		}
		return DEFAULT_COLOR;
	}
	
	class GradientChunk{
		private Speed minFeedrate;
		private Color4f minColor;
		private Speed maxFeedrate;
		private Color4f maxColor;
		
		/**
		 * @param minFeedrate
		 * @param minColor
		 * @param maxFeedrate
		 * @param maxColor
		 */
		public GradientChunk(Speed minFeedrate, Color4f minColor, Speed maxFeedrate, Color4f maxColor) {
			super();
			this.minFeedrate = minFeedrate;
			this.minColor = minColor;
			this.maxFeedrate = maxFeedrate;
			this.maxColor = maxColor;
		}

		protected boolean contains(Speed feed){
			return minFeedrate.lowerThanOrEqualTo(feed) && maxFeedrate.greaterThanOrEqualTo(feed);
		}
		
		protected Color4f getColor(Speed feed){
			double delta = feed.subtract(minFeedrate).divide(maxFeedrate.subtract(minFeedrate)).doubleValue();
			return new Color4f( (float)(minColor.x + delta * (maxColor.x - minColor.x)),
								(float)(minColor.y + delta * (maxColor.y - minColor.y)),
								(float)(minColor.z + delta * (maxColor.z - minColor.z)),
								1f);
		}
		
	}

}

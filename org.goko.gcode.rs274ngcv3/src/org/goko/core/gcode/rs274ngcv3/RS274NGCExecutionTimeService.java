/**
 * 
 */
package org.goko.core.gcode.rs274ngcv3;

import java.util.HashMap;
import java.util.Map;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Time;
import org.goko.core.execution.ExecutionConstraint;
import org.goko.core.execution.IGCodeExecutionTimeService;
import org.goko.core.gcode.element.IGCodeProvider;
import org.goko.core.gcode.element.IInstructionSetIterator;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.InstructionProvider;
import org.goko.core.gcode.rs274ngcv3.element.InstructionType;
import org.goko.core.gcode.rs274ngcv3.instruction.AbstractInstruction;
import org.goko.core.gcode.rs274ngcv3.instruction.executiontime.ArcFeedTimeCalculator;
import org.goko.core.gcode.rs274ngcv3.instruction.executiontime.DwellTimeCalculator;
import org.goko.core.gcode.rs274ngcv3.instruction.executiontime.IInstructionTimeCalculator;
import org.goko.core.gcode.rs274ngcv3.instruction.executiontime.StraightFeedTimeCalculator;
import org.goko.core.gcode.rs274ngcv3.instruction.executiontime.StraightProbeTimeCalculator;
import org.goko.core.gcode.rs274ngcv3.instruction.executiontime.StraightTraverseTimeCalculator;

/**
 * @author Psyko
 * @date 11 avr. 2017
 */
public class RS274NGCExecutionTimeService implements IGCodeExecutionTimeService {
	/** SERVICE ID */
	private static final String SERVICE_ID = "org.goko.core.gcode.rs274ngcv3.RS274NGCExecutionTimeService";
	/** The map of registered calculators */
	private Map<InstructionType, IInstructionTimeCalculator> mapCalculators;
	/** The gcode service */
	private IRS274NGCService rs274NGCService;
	/** Exectuion constraints */
	private ExecutionConstraint executionConstraint;
	
	/**
	 * Constructor
	 */
	public RS274NGCExecutionTimeService() {
		this.mapCalculators = new HashMap<>();
		this.executionConstraint = new ExecutionConstraint();
		// Add defaults
		addTimeCalculator(new StraightFeedTimeCalculator());
		addTimeCalculator(new ArcFeedTimeCalculator());
		addTimeCalculator(new DwellTimeCalculator());
		addTimeCalculator(new StraightProbeTimeCalculator());
		addTimeCalculator(new StraightTraverseTimeCalculator());
	}
	/** (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#getServiceId()
	 */
	@Override
	public String getServiceId() throws GkException {
		return SERVICE_ID;
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#start()
	 */
	@Override
	public void start() throws GkException {
		
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#stop()
	 */
	@Override
	public void stop() throws GkException {

	}

	/** (inheritDoc)
	 * @see org.goko.core.execution.IGCodeExecutionTimeService#evaluateExecutionTime(org.goko.core.gcode.element.IGCodeProvider)
	 */
	@Override
	public Time evaluateExecutionTime(IGCodeProvider provider) throws GkException {
		Time result = Time.ZERO;
		
		GCodeContext baseContext = new GCodeContext();
		InstructionProvider instructions = rs274NGCService.getInstructions(baseContext, provider);

		IInstructionSetIterator<GCodeContext, AbstractInstruction> iterator = rs274NGCService.getIterator(instructions, baseContext);

		GCodeContext preContext = null;
		IInstructionTimeCalculator calculator = null;
		while(iterator.hasNext()){
			preContext = new GCodeContext(iterator.getContext());
			AbstractInstruction instr = iterator.next();
			calculator = getTimeCalculator(instr.getType());
			if(calculator != null){
				result = result.add( calculator.calculateExecutionTime(preContext, instr, executionConstraint) );
			}
		}		
		return result;
	}

	public void addTimeCalculator(IInstructionTimeCalculator calculator){
		mapCalculators.put(calculator.getInstructionType(), calculator);
	}
	
	public IInstructionTimeCalculator getTimeCalculator(InstructionType type){
		return mapCalculators.get(type);
	}
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
	/**
	 * @return the executionConstraint
	 */
	public ExecutionConstraint getExecutionConstraint() {
		return executionConstraint;
	}
	/**
	 * @param executionConstraint the executionConstraint to set
	 */
	public void setExecutionConstraint(ExecutionConstraint executionConstraint) {
		this.executionConstraint = executionConstraint;
	}
}

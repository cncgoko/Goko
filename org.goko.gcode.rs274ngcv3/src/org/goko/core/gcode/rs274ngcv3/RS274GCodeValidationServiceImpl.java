/**
 * 
 */
package org.goko.core.gcode.rs274ngcv3;

import java.util.HashMap;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.element.IGCodeProvider;
import org.goko.core.gcode.element.validation.ValidationResult;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.InstructionIterator;
import org.goko.core.gcode.rs274ngcv3.element.InstructionProvider;
import org.goko.core.gcode.rs274ngcv3.instruction.AbstractInstruction;
import org.goko.core.gcode.rs274ngcv3.instruction.validation.RS274InstructionValidator;
import org.goko.core.gcode.service.IGCodeProviderRepositoryListener;
import org.goko.core.log.GkLog;

/**
 * @author Psyko
 * @date 2 juil. 2017
 */
public class RS274GCodeValidationServiceImpl implements IRS274GCodeValidationService, IGCodeProviderRepositoryListener{
	private static final GkLog LOG = GkLog.getLogger(RS274GCodeValidationServiceImpl.class);
	private static final String SERVICE_ID = "org.goko.core.gcode.rs274ngcv3.RS274GCodeValidationServiceImpl";
	private IRS274NGCService gcodeService;
	private HashMap<Integer, ValidationResult> validationResultByIdGCodeProvider;
		
	/**
	 * 
	 */
	public RS274GCodeValidationServiceImpl() {
		validationResultByIdGCodeProvider = new HashMap<>();
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
	 * @see org.goko.core.gcode.service.IGCodeValidationService#validate()
	 */
	@Override
	public ValidationResult validate(Integer idGCodeProvider) throws GkException {		
		IGCodeProvider 				provider 	= gcodeService.getGCodeProvider(idGCodeProvider);
		GCodeContext 				context 	= new GCodeContext();
		RS274InstructionValidator 	validator 	= new RS274InstructionValidator(provider);
		InstructionProvider 		instruction = gcodeService.getInstructions(context, provider);
		InstructionIterator 		iterator 	= new InstructionIterator(instruction, context, gcodeService);
		
		while (iterator.hasNext()) {
			context = new GCodeContext(iterator.getContext());
			AbstractInstruction instr = iterator.next();
			
			instr.accept(context, validator);
		}
		validationResultByIdGCodeProvider.put(idGCodeProvider, validator.getResult());
		return validator.getResult();
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeValidationService#getValidationResult(java.lang.Integer)
	 */
	@Override
	public ValidationResult getValidationResult(Integer idGCodeProvider){
		try{
			if(!validationResultByIdGCodeProvider.containsKey(idGCodeProvider)){
				validate(idGCodeProvider);			
			}
			return validationResultByIdGCodeProvider.get(idGCodeProvider);
		}catch(GkException e){
			LOG.error(e);			
			return ValidationResult.error(e);
		}
		
	}
	/**
	 * @return the gcodeService
	 */
	public IRS274NGCService getGcodeService() {
		return gcodeService;
	}
	/**
	 * @param gcodeService the gcodeService to set
	 * @throws GkException 
	 */
	public void setGcodeService(IRS274NGCService gcodeService) throws GkException {
		this.gcodeService = gcodeService;
		gcodeService.addListener(this);
	}
	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeProviderRepositoryListener#onGCodeProviderCreate(org.goko.core.gcode.element.IGCodeProvider)
	 */
	@Override
	public void onGCodeProviderCreate(IGCodeProvider provider) throws GkException {
		validate(provider.getId());
	}
	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeProviderRepositoryListener#onGCodeProviderUpdate(org.goko.core.gcode.element.IGCodeProvider)
	 */
	@Override
	public void onGCodeProviderUpdate(IGCodeProvider provider) throws GkException {
		validate(provider.getId());
	}
	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeProviderRepositoryListener#beforeGCodeProviderDelete(org.goko.core.gcode.element.IGCodeProvider)
	 */
	@Override
	public void beforeGCodeProviderDelete(IGCodeProvider provider) throws GkException {
		validationResultByIdGCodeProvider.remove(provider.getId());
	}
	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeProviderRepositoryListener#afterGCodeProviderDelete(org.goko.core.gcode.element.IGCodeProvider)
	 */
	@Override
	public void afterGCodeProviderDelete(IGCodeProvider provider) throws GkException {
		// TODO Auto-generated method stub
		
	}
	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeProviderRepositoryListener#onGCodeProviderLocked(org.goko.core.gcode.element.IGCodeProvider)
	 */
	@Override
	public void onGCodeProviderLocked(IGCodeProvider provider) throws GkException {
		// TODO Auto-generated method stub
		
	}
	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeProviderRepositoryListener#onGCodeProviderUnlocked(org.goko.core.gcode.element.IGCodeProvider)
	 */
	@Override
	public void onGCodeProviderUnlocked(IGCodeProvider provider) throws GkException {
		// TODO Auto-generated method stub
		
	}

}

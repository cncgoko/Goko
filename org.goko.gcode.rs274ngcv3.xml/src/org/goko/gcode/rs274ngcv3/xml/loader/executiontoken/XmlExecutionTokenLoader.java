/**
 * 
 */
package org.goko.gcode.rs274ngcv3.xml.loader.executiontoken;

import org.apache.commons.lang3.StringUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.execution.monitor.io.bean.XmlExecutionToken;
import org.goko.core.gcode.element.GCodeProviderReference;
import org.goko.core.gcode.element.IGCodeProvider;
import org.goko.core.gcode.execution.ExecutionToken;
import org.goko.core.gcode.execution.ExecutionTokenState;
import org.goko.core.gcode.rs274ngcv3.IRS274NGCService;
import org.goko.core.workspace.service.ILoader;
import org.goko.core.workspace.service.IMapperService;

/**
 * @author Psyko
 * @date 23 nov. 2016
 */
public class XmlExecutionTokenLoader implements ILoader<XmlExecutionToken, ExecutionToken>{
	/** The RS274 GCode service*/
	private IRS274NGCService rs274service;
	
	/**
	 * @param rs274service
	 */
	public XmlExecutionTokenLoader(IRS274NGCService rs274service) {
		super();
		this.rs274service = rs274service;
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.ILoader#load(java.lang.Object, org.goko.core.workspace.service.IMapperService)
	 */
	@Override
	public ExecutionToken load(XmlExecutionToken input, IMapperService mapperService) throws GkException {
		IGCodeProvider provider = null;
		if(input.getProviderReference() != null){
			GCodeProviderReference reference = mapperService.load(input.getProviderReference(), GCodeProviderReference.class);
			if(reference.isValid()){
				provider = reference;
			}
		}else if(StringUtils.isNotBlank(input.getGcodeProvider())){ // Backward compatibility
			provider = rs274service.getGCodeProvider(input.getGcodeProvider());
			
		}
		ExecutionToken token = null;
		if(provider != null){
			token = new ExecutionToken<ExecutionTokenState>(provider, ExecutionTokenState.NONE);
			token.setExecutionOrder(input.getExecutionOrder());
		}
		return token;
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.ILoader#getOutputClass()
	 */
	@Override
	public Class<ExecutionToken> getOutputClass() {
		return ExecutionToken.class;
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.ILoader#getInputClass()
	 */
	@Override
	public Class<XmlExecutionToken> getInputClass() {
		return XmlExecutionToken.class;
	}

}

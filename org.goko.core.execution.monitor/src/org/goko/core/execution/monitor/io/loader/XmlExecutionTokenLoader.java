/**
 * 
 */
package org.goko.core.execution.monitor.io.loader;

import org.goko.core.common.exception.GkException;
import org.goko.core.execution.monitor.io.bean.XmlExecutionToken;
import org.goko.core.gcode.element.GCodeProviderReference;
import org.goko.core.gcode.execution.ExecutionToken;
import org.goko.core.gcode.execution.ExecutionTokenState;
import org.goko.core.workspace.service.ILoader;
import org.goko.core.workspace.service.IMapperService;

/**
 * @author Psyko
 * @date 23 nov. 2016
 */
public class XmlExecutionTokenLoader implements ILoader<XmlExecutionToken, ExecutionToken>{

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.ILoader#load(java.lang.Object, org.goko.core.workspace.service.IMapperService)
	 */
	@Override
	public ExecutionToken load(XmlExecutionToken input, IMapperService mapperService) throws GkException {
		GCodeProviderReference providerReference = mapperService.load(input.getProviderReference(), GCodeProviderReference.class);
		ExecutionToken token = null;
		if(providerReference.isValid()){
			token = new ExecutionToken<ExecutionTokenState>(providerReference, ExecutionTokenState.NONE);
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

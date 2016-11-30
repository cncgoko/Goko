/**
 * 
 */
package org.goko.core.execution.monitor.io.exporter;

import org.goko.core.common.exception.GkException;
import org.goko.core.execution.monitor.io.bean.XmlExecutionToken;
import org.goko.core.gcode.execution.ExecutionToken;
import org.goko.core.gcode.io.XmlGCodeProviderReference;
import org.goko.core.workspace.service.IExporter;
import org.goko.core.workspace.service.IMapperService;

/**
 * @author Psyko
 * @date 23 nov. 2016
 */
public class XmlExecutionTokenExporter implements IExporter<ExecutionToken, XmlExecutionToken> {

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IExporter#export(java.lang.Object, org.goko.core.workspace.service.IMapperService)
	 */
	@Override
	public XmlExecutionToken export(ExecutionToken input, IMapperService mapperService) throws GkException {
		XmlExecutionToken token = new XmlExecutionToken();
		token.setExecutionOrder(input.getExecutionOrder());
		token.setProviderReference(mapperService.export(input.getGCodeProvider(), XmlGCodeProviderReference.class));
		return token;
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IExporter#getOutputClass()
	 */
	@Override
	public Class<XmlExecutionToken> getOutputClass() {
		return XmlExecutionToken.class;
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IExporter#getInputClass()
	 */
	@Override
	public Class<ExecutionToken> getInputClass() {
		return ExecutionToken.class;
	}

}

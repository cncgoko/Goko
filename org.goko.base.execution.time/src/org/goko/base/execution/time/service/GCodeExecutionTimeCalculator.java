/*
 *
 *   Goko
 *   Copyright (C) 2013  PsyKo
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.goko.base.execution.time.service;

import org.apache.commons.collections.CollectionUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.execution.IGCodeCommandExecutionTimeCalculator;
import org.goko.core.execution.IGCodeExecutionTimeService;
import org.goko.core.gcode.bean.GCodeCommand;
import org.goko.core.gcode.bean.GCodeContext;
import org.goko.core.gcode.bean.IGCodeProvider;
import org.goko.core.gcode.service.IGCodeService;

/**
 * Implementation of the time calculator
 *
 * @author PsyKo
 *
 */
public class GCodeExecutionTimeCalculator implements IGCodeExecutionTimeService {
	public static final String SERVICE_ID = "org.goko.base.execution.time.service.GCodeEecutionTimeCalculator";
	private TimeCalculatorFactory factory;

	private IGCodeService gcodeService;


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
		// TODO Auto-generated method stub

	}

	/** (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#stop()
	 */
	@Override
	public void stop() throws GkException {
		// TODO Auto-generated method stub

	}

	/** (inheritDoc)
	 * @see org.goko.core.execution.IGCodeExecutionTimeService#evaluateExecutionTime(org.goko.core.gcode.bean.IGCodeProvider)
	 */
	@Override
	public double evaluateExecutionTime(IGCodeProvider provider) throws GkException {
		double result = 0;
		if(factory == null){
			factory = new TimeCalculatorFactory();
		}
		if(CollectionUtils.isNotEmpty(provider.getGCodeCommands())){
			GCodeContext context = new GCodeContext();
			for (GCodeCommand command : provider.getGCodeCommands()) {
				//GCodeContext postContext = new GCodeContext(context);
				//gcodeService.update(postContext, command);

				result += evaluateExecutionTime(command, context);
				//context = postContext;
			}
		}
		return result;
	}

	/** (inheritDoc)
	 * @see org.goko.core.execution.IGCodeExecutionTimeService#evaluateExecutionTime(org.goko.core.gcode.bean.GCodeCommand)
	 */
	@Override
	public double evaluateExecutionTime(GCodeCommand command, GCodeContext context) throws GkException {


		IGCodeCommandExecutionTimeCalculator<GCodeCommand> calculator = factory.getCalculator(context, command);


		if(calculator != null){
			return calculator.evaluateExecutionTime(command, context);
		}
		return 0;
	}

	/**
	 * @return the gcodeService
	 */
	protected IGCodeService getGcodeService() {
		return gcodeService;
	}

	/**
	 * @param gcodeService the gcodeService to set
	 */
	protected void setGcodeService(IGCodeService gcodeService) {
		this.gcodeService = gcodeService;
	}

}

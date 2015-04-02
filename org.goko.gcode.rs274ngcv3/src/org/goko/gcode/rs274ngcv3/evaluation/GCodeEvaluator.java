/*
 *	This file is part of Goko.
 *
 *  Goko is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Goko is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Goko.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.goko.gcode.rs274ngcv3.evaluation;

import java.util.ArrayList;
import java.util.List;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.bean.GCodeCommand;
import org.goko.core.gcode.bean.GCodeContext;
import org.goko.core.gcode.bean.IGCodeCommandVisitor;
import org.goko.core.gcode.bean.IGCodeProvider;
import org.goko.core.gcode.bean.Tuple6b;
import org.goko.core.gcode.bean.commands.ArcMotionCommand;
import org.goko.core.gcode.bean.commands.CommentCommand;
import org.goko.core.gcode.bean.commands.FunctionCommand;
import org.goko.core.gcode.bean.commands.LinearMotionCommand;
import org.goko.core.gcode.bean.commands.MotionCommand;
import org.goko.core.gcode.bean.commands.RawCommand;
import org.goko.core.gcode.bean.commands.SettingCommand;

public class GCodeEvaluator implements IGCodeCommandVisitor{
	private List<GCodeCommandEvaluation> lstEvaluation;
	private GCodeCommandEvaluation current;
	private GCodeContext context;
	private GCodeContext initialContext;
	private Tuple6b position;

	public void evaluate(IGCodeProvider provider, GCodeContext initialContext) throws GkException{
		List<GCodeCommand> cmds = provider.getGCodeCommands();
		this.lstEvaluation = new ArrayList<GCodeCommandEvaluation>();
		this.initialContext = initialContext;
		this.context = new GCodeContext(initialContext);
		this.position = new Tuple6b();

		for (GCodeCommand gCodeCommand : cmds) {
			gCodeCommand.updateContext(context);
			this.current = new GCodeCommandEvaluation(gCodeCommand);
			gCodeCommand.accept(this);
			this.lstEvaluation.add(current);
		}
	}

	public void updateInitialContext(IGCodeProvider provider, GCodeContext updateContext) throws GkException{
		GCodeContextUpdater updater = new GCodeContextUpdater(initialContext, updateContext);
		this.initialContext = updateContext;
		this.context = new GCodeContext(initialContext);
		this.position = new Tuple6b();

		for (GCodeCommandEvaluation evaluation : lstEvaluation) {
			if(!updater.hasMoreUpdate()){
				break;
			}
			GCodeCommand gCodeCommand = evaluation.getGCodeCommand();
			current = evaluation;
			gCodeCommand.accept(this);
		}
	}

	private void updateGCodeContext(GCodeContextUpdater updater, GCodeCommand gCodeCommand){
//		if(gCodeCommand.isExplicitCoordinateSystem()){
//			setCoordinateSystem(null);
//		}
//		if(initial.getDistanceMode() == updated.getDistanceMode()){
//			setDistanceMode(null);
//		}
//		if(initial.getFeedrate() == updated.getFeedrate()){
//			setFeedrate(null);
//		}
//		if(initial.getMotionMode() == updated.getMotionMode()){
//			setMotionMode(null);
//		}
//		if(initial.getMotionType() == updated.getMotionType()){
//			setMotionType(null);
//		}
//		if(initial.getPlane() == updated.getPlane()){
//			setPlane(null);
//		}
//		if(initial.getToolNumber() == updated.getToolNumber()){
//			setToolNumber(null);
//		}
//		if(initial.getUnit() == updated.getUnit()){
//			setUnit(null);
//		}
	}
	@Override
	public void visit(RawCommand command) throws GkException {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(CommentCommand command) throws GkException {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(SettingCommand command) throws GkException {
		current.setDistanceMode(context.getDistanceMode());
		current.setFeedrate(context.getFeedrate());
		current.setMotionMode(context.getMotionMode());
		current.setMotionType(context.getMotionType());
		current.setUnit(context.getUnit());
		current.setPlane(context.getPlane());
		current.setCoordinateSystem(context.getCoordinateSystem());
	}

	@Override
	public void visit(MotionCommand command) throws GkException {
		visit((SettingCommand)command);
		current.setStart(new Tuple6b(position));
		current.setEnd(new Tuple6b(position));
		current.getEnd().add(command.getCoordinates());

		position = current.getEnd();
	}

	@Override
	public void visit(LinearMotionCommand command) throws GkException {
		visit((MotionCommand)command);

	}

	@Override
	public void visit(ArcMotionCommand command) throws GkException {
		visit((MotionCommand)command);

	}

	@Override
	public void visit(FunctionCommand command) throws GkException {
		// TODO Auto-generated method stub

	}
}

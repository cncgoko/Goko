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
package org.goko.log.part.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Display;

public class ProblemTreeContentProvider implements ITreeContentProvider, PropertyChangeListener {
	Viewer viewer;

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		this.viewer = viewer;
		if(newInput instanceof ProblemTreeContent){
			((ProblemTreeContent) newInput).addPropertyChangeListener(this);
		}
	}

	@Override
	public Object[] getElements(Object inputElement) {
		List<LogLevelNode> lstEvent = new ArrayList<LogLevelNode>();
		if(inputElement instanceof ProblemTreeContent){
			addIfNonEmpty(((ProblemTreeContent) inputElement).getErrors(),lstEvent);
			addIfNonEmpty(((ProblemTreeContent) inputElement).getWarnings(),lstEvent);
		}
		return lstEvent.toArray();
	}

	protected void addIfNonEmpty(LogLevelNode node, List<LogLevelNode> lstNode){
		if(CollectionUtils.isNotEmpty(node.getMessages())){
			lstNode.add(node);
		}
	}
	@Override
	public Object[] getChildren(Object parentElement) {
		if(parentElement instanceof LogLevelNode){
			return ((LogLevelNode) parentElement).getMessages().toArray();
		}
		return null;
	}

	@Override
	public Object getParent(Object element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		if(element instanceof LogLevelNode){
			return CollectionUtils.isNotEmpty(((LogLevelNode) element).getMessages());
		}
		return false;
	}

	/** (inheritDoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
		if(!viewer.getControl().isDisposed()){
			Display.getDefault().asyncExec(new Runnable() {

				@Override
				public void run() {
					viewer.refresh();
				}
			});
		}
	}

}

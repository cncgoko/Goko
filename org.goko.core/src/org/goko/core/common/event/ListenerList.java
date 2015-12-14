/*******************************************************************************
 * 	This file is part of Goko.
 *
 *   Goko is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   Goko is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with Goko.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package org.goko.core.common.event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListenerList<L> {
	private List<L> listeners;

	public ListenerList() {
		listeners = Collections.synchronizedList(new ArrayList<L>());
	}

	public void addListener(L listener){
		listeners.add(listener);
	}

	public void removeListener(L listener){
		listeners.remove(listener);
	}

	public List<L> getListeners(){
		return new ArrayList<L>(listeners);
	}
}

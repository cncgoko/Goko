package org.goko.gcode.filesender;

import java.util.List;

import org.eclipse.jface.viewers.ILazyContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.goko.core.gcode.bean.GCodeCommand;

public class MyLazyContentProvider implements ILazyContentProvider {
  private TableViewer viewer;
  private List<GCodeCommand> elements;

  public MyLazyContentProvider(TableViewer viewer) {
    this.viewer = viewer;
  }

  @Override
public void dispose() {
  }

  @Override
public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
    this.elements = (List<GCodeCommand> ) newInput;
  }

  @Override
public void updateElement(int index) {
    viewer.replace(elements.get(index), index);
  }
}
package org.goko.featuremanager.installer;

import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.StyledString.Styler;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.TextStyle;
import org.eclipse.swt.widgets.Display;

public class InstallableUnitLabelProvider extends StyledCellLabelProvider {
	int columnIndex;
	
	public InstallableUnitLabelProvider(int columnIndex) {
		super();
		this.columnIndex = columnIndex;
	}

	@Override
	public void update(ViewerCell cell) {
		StyledString string = new StyledString();		
		if(cell.getElement() instanceof GkInstallableUnit){
			GkInstallableUnit unit = (GkInstallableUnit) cell.getElement();					
			// Name column
			if(columnIndex == 0){				
				// Installed
				if(unit.isInstalled()){					
					string.append(unit.getName()+ " (Installed)",new Styler() {
			            @Override
			            public void applyStyles(TextStyle textStyle) {			            	
			                textStyle.foreground = Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GRAY);			                
			            }
			        });
				}else{
					string.append(unit.getName());
				}
			}else if(columnIndex == 1){
				if(unit.isInstalled()){		
					string.append(unit.getVersion().toString(),new Styler() {
			            @Override
			            public void applyStyles(TextStyle textStyle) {			            	
			                textStyle.foreground = Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GRAY);			                
			            }
			        });				
				}
			}
			
		}else if(cell.getElement() instanceof GkInstallableUnitCategory){
			GkInstallableUnitCategory category = (GkInstallableUnitCategory) cell.getElement();
			if(columnIndex == 0){
				string.append(category.getCategory());
			}
		}
		cell.setText(string.getString());
		cell.setStyleRanges(string.getStyleRanges());
		super.update(cell);
	}
}

package org.goko.common.preferences;

import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.widgets.Shell;
import org.goko.common.preferences.internal.E4PreferenceRegistry;


public class E4PreferencesHandler{
	public static final String PAGE_ID = "goko.org.ui.page.id";
	
	@CanExecute
	public boolean canExecute()
	{
		return true;	
	}
	
	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_SHELL) Shell shell,  E4PreferenceRegistry prefReg, @Named(PAGE_ID) @Optional String pageId)
	{		
		PreferenceManager pm = prefReg.getPreferenceManager();
		PreferenceDialog dialog = new PreferenceDialog(shell, pm);		
		if(StringUtils.isNotBlank(pageId)){
			dialog.setSelectedNode(pageId);
		}
		dialog.create();
		dialog.getTreeViewer().setComparator(new ViewerComparator());
		dialog.getTreeViewer().expandAll();
		dialog.open();
		
	}

	

	
}

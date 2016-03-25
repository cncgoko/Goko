package goko.handlers;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;

import javax.inject.Named;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.URIUtil;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.goko.core.common.exception.GkException;
import org.goko.core.workspace.element.GkProject;
import org.goko.core.workspace.io.IProjectLocation;
import org.goko.core.workspace.service.IWorkspaceService;

public class QuitHandler {	
	
	@Execute
	public void execute(IWorkbench workbench, IEclipseContext context, @Named(IServiceConstants.ACTIVE_SHELL) Shell shell, IWorkspaceService workspaceService) throws InvocationTargetException, InterruptedException, GkException {
		if(checkUnsavedModification(shell, workspaceService)){
			workbench.close();
		}
	}
	
	private boolean checkUnsavedModification(Shell shell, IWorkspaceService workspaceService) throws GkException {
		GkProject project = workspaceService.getProject();
		if(project.isDirty()){

			MessageDialog saveDialog = new MessageDialog(shell,
												"Save",
												null,
												"Current project has unsaved modification. Would you like to save them now ?",
												MessageDialog.QUESTION_WITH_CANCEL,
												new String[]{
													IDialogConstants.YES_LABEL,
													IDialogConstants.NO_LABEL,
													IDialogConstants.CANCEL_LABEL},
												0
												);
			int result = saveDialog.open();
			switch(result){
			case 0: return saveProject(shell, workspaceService);
			case 1: return true;
			case 2: // Cancel is the default state
			default: return false;
			}
		}

		return true;
	}
	
	// FIXME : Use mutualized SaveHandler (@see OpenProjectHandler, SaveProjectHandler...) 
	public boolean saveProject(Shell shell, IWorkspaceService workspaceService) throws GkException{
		boolean saveDone = false;
		GkProject project = workspaceService.getProject();
		IProjectLocation projectLocation = project.getLocation();
		
		if(!projectLocation.isLocationDefined()){
			FileDialog dialog = new FileDialog(shell, SWT.SAVE);
			dialog.setText("Save Goko project...");
			dialog.setFilterNames(new String[]{"Goko projects (*.goko) "});
			dialog.setFilterExtensions(new String[]{"*.goko"});
			String filePath = dialog.open();
			
			if(StringUtils.isNotEmpty(filePath)){
				String fileName = FilenameUtils.getBaseName(filePath);
				String fileBaseName = FilenameUtils.removeExtension(fileName);
				projectLocation.setName( fileBaseName );
				URI projectUri = URIUtil.append(new File(filePath).getParentFile().toURI(), fileBaseName);
				new File(projectUri).mkdirs();
				projectLocation.setLocation(projectUri);
			}
		}
		
		if(projectLocation.isLocationDefined()){
			workspaceService.saveProject(projectLocation, new NullProgressMonitor());
			saveDone = true;
		}
		return saveDone;
	}	
}

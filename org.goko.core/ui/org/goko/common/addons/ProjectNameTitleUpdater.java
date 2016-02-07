package org.goko.common.addons;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MTrimmedWindow;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.goko.core.common.exception.GkException;
import org.goko.core.feature.IFeatureSetManager;
import org.goko.core.feature.TargetBoard;
import org.goko.core.workspace.element.GkProject;
import org.goko.core.workspace.service.AbstractProjectLifecycleListenerAdapter;
import org.goko.core.workspace.service.IWorkspaceService;

/**
 * Addons that monitors the name of the current project and displays it it the title bar
 * @author Psyko
 */
public class ProjectNameTitleUpdater extends AbstractProjectLifecycleListenerAdapter{
	private IWorkspaceService workspaceService;
	private MApplication application;
	private EModelService modelService;
	private IFeatureSetManager featureSetManager;
	
	/**
	 * Update the title of the application to display project name
	 * @throws GkException GkException
	 */
	private void updateTitle() throws GkException{
		if(application != null && modelService != null && workspaceService != null){
			MTrimmedWindow mainMTrimmedWindow = (MTrimmedWindow) modelService.find("goko.application.window", application);
			GkProject project = workspaceService.getProject();
			String projectName = project.getName();
			if(StringUtils.isEmpty(projectName)){
				projectName = "Untitled project";
			}
			if(project.isDirty()){
				projectName += "*";
			}
			TargetBoard targetBoard = featureSetManager.getCurrentTargetBoard();
			mainMTrimmedWindow.setLabel("Goko - "+targetBoard.getLabel()+" - "+projectName);
		}
	}

	@Inject
	public void execute(IWorkspaceService workspaceService, MApplication app, EModelService model, IFeatureSetManager featureSetManager) throws GkException{
		this.workspaceService = workspaceService;
		this.application = app;
		this.modelService = model;
		this.featureSetManager = featureSetManager;
		this.workspaceService.addProjectLifecycleListener(this);
		updateTitle();
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IProjectLifecycleListener#afterCreate()
	 */
	@Override
	public void afterCreate() throws GkException {
		updateTitle();
	}


	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IProjectLifecycleListener#afterSave()
	 */
	@Override
	public void afterSave() throws GkException {
		updateTitle();
	}


	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IProjectLifecycleListener#afterLoad()
	 */
	@Override
	public void afterLoad() throws GkException {
		updateTitle();
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.AbstractProjectLifecycleListenerAdapter#onProjectDirtyStateChange()
	 */
	@Override
	public void onProjectDirtyStateChange() throws GkException {
		updateTitle();
	}
}

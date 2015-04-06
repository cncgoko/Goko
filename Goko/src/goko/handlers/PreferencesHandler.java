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
package goko.handlers;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.InvalidRegistryObjectException;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.contributions.IContributionFactory;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.preference.IPreferenceNode;
import org.eclipse.jface.preference.IPreferencePage;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.preference.PreferenceNode;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.goko.core.log.GkLog;
import org.osgi.service.log.LogService;

public class PreferencesHandler {
	private static final GkLog LOG = GkLog.getLogger(PreferencesHandler.class);

	public static final String PREFS_PAGE = "Goko.org.goko.ui.gkPreferencePages";
	public static final String PAGE_ID = "goko.org.ui.page.id";
	protected static final String ELMT_PAGE = "page";
	protected static final String ATTR_CLASS = "class";
	private static final String ATTR_ID = "id";
	private static final String ATTR_NAME = "name";
	private static final String ATTR_CATEGORY = "category";

	@Inject protected LogService logservice;
	@Inject protected IExtensionRegistry registry;
	@Inject protected IEclipseContext context;

	@CanExecute
	public boolean canExecute(){
		return true;
	}
	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_SHELL) Shell shell, @Named(PAGE_ID) @Optional String pageId){
		PreferenceManager pm;
		try {
			pm = configure();
		} catch (InvalidRegistryObjectException e) {
			LOG.error(e);
			return;
		}
		PreferenceDialog dialog = new PreferenceDialog(shell, pm);
		ScopedPreferenceStore store = new ScopedPreferenceStore(InstanceScope.INSTANCE, "Goko");
		dialog.setPreferenceStore(store);
	       if(StringUtils.isNotBlank(pageId)){
	        	dialog.setSelectedNode(pageId);
	        }
		dialog.create();
		dialog.getTreeViewer().setComparator(new ViewerComparator());
        dialog.getTreeViewer().expandAll();

        dialog.open();
	}

	public PreferenceManager configure() throws InvalidRegistryObjectException{

		PreferenceManager manager = new PreferenceManager();
		IConfigurationElement[] lst = registry.getConfigurationElementsFor(PREFS_PAGE);
		IContributionFactory factory = context.get(IContributionFactory.class);

		for(IConfigurationElement elt : lst){
			if(elt.getAttribute(ATTR_CLASS) != null) {

				if(StringUtils.equals(elt.getName(), ELMT_PAGE)){
					IPreferencePage page =  null;
					String pageClassUri = getClassURI(elt.getNamespaceIdentifier(), elt.getAttribute(ATTR_CLASS));
					if(StringUtils.contains(StringUtils.substring(pageClassUri,14), ":")){
						continue;
					}
					Object object = null;
					try{
						object= factory.create(pageClassUri, context);
						if(object instanceof IPreferencePage){
							page = (IPreferencePage)object;
							if(StringUtils.isEmpty(page.getTitle())){
								page.setTitle(elt.getAttribute(ATTR_NAME));
							}
							ContextInjectionFactory.inject(page, context);
							PreferenceNode node = new PreferenceNode(elt.getAttribute(ATTR_ID), page);
							addNode(manager, node, elt.getAttribute(ATTR_CATEGORY));
						}
					}catch(Exception e){
						LOG.error(e);
						continue;
					}

				}
			}
		}
		return manager;
	}

	private void addNode(PreferenceManager manager, PreferenceNode node, String category) {
		IPreferenceNode parent = null;
		if(StringUtils.isNotBlank(category)){
			parent = findNode(manager, category);
		}
		if (parent == null) {
			manager.addToRoot(node);
		} else {
			parent.add(node);
		}
	}

	private IPreferenceNode findNode(PreferenceManager pm, String categoryId) {
		for (Object o : pm.getElements(PreferenceManager.POST_ORDER)) {
			if (o instanceof IPreferenceNode
					&& ((IPreferenceNode) o).getId().equals(categoryId)) {
				return (IPreferenceNode) o;
			}
		}
		return null;
	}
	private String getClassURI(String definingBundleId, String spec) {
		if(spec.startsWith("platform:")) { return spec; }	// $NON-NLS-1$
		return "bundleclass://" + definingBundleId + '/' + spec;
	}

    static class EmptyPreferencePage extends PreferencePage
    {

            public EmptyPreferencePage(String title)
            {
                    setTitle(title);
                    noDefaultAndApplyButton();
            }

            @Override
            protected Control createContents(Composite parent)
            {
                    return new Label(parent, SWT.NONE);
            }

    }
}
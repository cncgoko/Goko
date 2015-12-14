package org.goko.featuremanager.installer;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.e4.ui.di.UISynchronize;

public class UIProgressMonitor extends NullProgressMonitor implements IProgressMonitor {
	protected IProgressMonitor uiMonitor;
	protected UISynchronize sync;
	
	public UIProgressMonitor( UISynchronize sync, IProgressMonitor uiMonitor) {
		super();
		this.sync = sync;
		this.uiMonitor = uiMonitor;
	}

	@Override
	public void beginTask(final String name, final int totalWork) {
		sync.syncExec(new Runnable() {			
			@Override
			public void run() {
				uiMonitor.beginTask(name, totalWork);
			}
		});
	}

	@Override
	public void done() {
		sync.syncExec(new Runnable() {			
			@Override
			public void run() {
				uiMonitor.done();
			}
		});
	}

	@Override
	public void internalWorked(final double work) {
		sync.syncExec(new Runnable() {			
			@Override
			public void run() {
				uiMonitor.internalWorked(work);
			}
		});
	}

	@Override
	public boolean isCanceled() {		
		return uiMonitor.isCanceled();
	}

	@Override
	public void setCanceled(final boolean value) {
		uiMonitor.setCanceled(value);
	}

	@Override
	public void setTaskName(final String name) {
		sync.syncExec(new Runnable() {			
			@Override
			public void run() {
				uiMonitor.setTaskName(name);
			}
		});
	}

	@Override
	public void subTask(final String name) {
		sync.syncExec(new Runnable() {			
			@Override
			public void run() {
				uiMonitor.subTask(name);
			}
		});
	}

	@Override
	public void worked(final int work) {
		sync.syncExec(new Runnable() {			
			@Override
			public void run() {
				uiMonitor.worked(work);
			}
		});
	}

}

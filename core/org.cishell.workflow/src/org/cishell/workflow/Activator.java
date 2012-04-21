package org.cishell.workflow;

import org.cishell.app.service.scheduler.SchedulerService;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public class Activator implements BundleActivator{

	public static BundleContext context = null;

	/**
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		Activator.context = context;
		Workflow workFlow = new  Workflow();
		System.out.println("Created  - " + workFlow.isRecord());
		workFlow.setRecord(true);
		System.out.println("Started  - " + workFlow.isRecord());
		workFlow.setRecord(false);
		System.out.println("Stoped  - " + workFlow.isRecord());
		WorkflowListener wfListen = WorkflowListener.getInstance();
	}
	
	/**
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		Activator.context = null;
		System.out.println("Bundle Stoped ");
	}
    
}

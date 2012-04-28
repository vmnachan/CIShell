package org.cishell.workflow;

import org.cishell.app.service.scheduler.SchedulerService;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

/**
 * The Class Activator.
 * 
 * Author: P632
 */
public class Activator implements BundleActivator {

	public static BundleContext context = null;

	/**
	 * Start.
	 *
	 * @param context the context
	 * @throws Exception the exception
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		Activator.context = context;
		Workflow workFlow = new Workflow();
		WorkflowListener wfListen = WorkflowListener.getInstance();
	}

	/**
	 * Gets the scheduler service.
	 *
	 * @return the scheduler service
	 */
	protected static SchedulerService getSchedulerService() {
		ServiceReference serviceReference = context
				.getServiceReference(SchedulerService.class.getName());
		SchedulerService manager = null;

		if (serviceReference != null) {
			manager = (SchedulerService) context.getService(serviceReference);
		}

		return manager;
	}

	/**
	 * Stop.
	 *
	 * @param context the context
	 * @throws Exception the exception
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		Activator.context = null;
	}

}

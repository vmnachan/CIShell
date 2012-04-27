package org.cishell.workflow;

import java.util.Calendar;

import org.cishell.app.service.scheduler.SchedulerListener;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.Data;
import org.cishell.reference.gui.scheduler.SchedulerContentModel;
import org.osgi.framework.ServiceReference;

/**
 * Listens for notification from the scheduler and notifies all registered
 * objects.
 * The listener interface for receiving workflow events.
 * The class that is interested in processing a workflow
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addWorkflowListener<code> method. When
 * the workflow event occurs, that object's appropriate
 * method is invoked.
 *
 * Author: P632
 */
public class WorkflowListener implements SchedulerListener {

	/** The Constant INSTANCE. */
	private static final WorkflowListener INSTANCE = new WorkflowListener();

	/**
	 * Instantiates a new workflow listener.
	 */
	private WorkflowListener() {
		SchedulerContentModel.getInstance().register(this);
	}

	/**
	 * Gets the single instance of WorkflowListener.
	 *
	 * @return single instance of WorkflowListener
	 */
	public static WorkflowListener getInstance() {
		return INSTANCE;
	}

	/* (non-Javadoc)
	 * @see org.cishell.app.service.scheduler.SchedulerListener#algorithmScheduled(org.cishell.framework.algorithm.Algorithm, java.util.Calendar)
	 */
	public void algorithmScheduled(Algorithm algorithm, Calendar time) {

	}

	/* (non-Javadoc)
	 * @see org.cishell.app.service.scheduler.SchedulerListener#algorithmRescheduled(org.cishell.framework.algorithm.Algorithm, java.util.Calendar)
	 */
	public void algorithmRescheduled(Algorithm algorithm, Calendar time) {

	}

	/* (non-Javadoc)
	 * @see org.cishell.app.service.scheduler.SchedulerListener#algorithmUnscheduled(org.cishell.framework.algorithm.Algorithm)
	 */
	public void algorithmUnscheduled(Algorithm algorithm) {

	}

	/* (non-Javadoc)
	 * @see org.cishell.app.service.scheduler.SchedulerListener#algorithmStarted(org.cishell.framework.algorithm.Algorithm)
	 */
	public void algorithmStarted(Algorithm algorithm) {

	}

	/* (non-Javadoc)
	 * @see org.cishell.app.service.scheduler.SchedulerListener#algorithmFinished(org.cishell.framework.algorithm.Algorithm, org.cishell.framework.data.Data[])
	 */
	public void algorithmFinished(Algorithm algorithm, Data[] createdData) {
		Workflow workFlow = Workflow.getInstance();
		if (workFlow.isRecord()) {
			ServiceReference serviceReference = Activator.getSchedulerService()
					.getServiceReference(algorithm);
			String servicePID = "";
			if (serviceReference != null) {
				servicePID = (String) serviceReference
						.getProperty("service.pid");
				if (!servicePID.contains("StartRecord")
						&& !servicePID.contains("StopRecord")) {
					workFlow.addToAlgorithmList(servicePID);
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.cishell.app.service.scheduler.SchedulerListener#algorithmError(org.cishell.framework.algorithm.Algorithm, java.lang.Throwable)
	 */
	public void algorithmError(Algorithm algorithm, Throwable error) {

	}

	/* (non-Javadoc)
	 * @see org.cishell.app.service.scheduler.SchedulerListener#schedulerRunStateChanged(boolean)
	 */
	public void schedulerRunStateChanged(boolean isRunning) {

	}

	/* (non-Javadoc)
	 * @see org.cishell.app.service.scheduler.SchedulerListener#schedulerCleared()
	 */
	public void schedulerCleared() {

	}

}

package org.cishell.workflow;

import java.util.Calendar;

import org.cishell.app.service.scheduler.SchedulerListener;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.Data;
import org.cishell.reference.gui.scheduler.SchedulerContentModel;
import org.osgi.framework.ServiceReference;

/**
 * Listens for notification from the scheduler and notifies all registered
 * objects
 */
public class WorkflowListener implements SchedulerListener {

	private static final WorkflowListener INSTANCE = new WorkflowListener();

	private WorkflowListener() {
		SchedulerContentModel.getInstance().register(this);
	}

	public static WorkflowListener getInstance() {
		return INSTANCE;
	}

	public void algorithmScheduled(Algorithm algorithm, Calendar time) {

	}

	public void algorithmRescheduled(Algorithm algorithm, Calendar time) {

	}

	public void algorithmUnscheduled(Algorithm algorithm) {

	}

	public void algorithmStarted(Algorithm algorithm) {

	}

	public void algorithmFinished(Algorithm algorithm, Data[] createdData) {
		Workflow workFlow = Workflow.getInstance();
		if (workFlow.isRecord()) {
			ServiceReference serviceReference = Activator.getSchedulerService()
					.getServiceReference(algorithm);
			String servicePID = "";
			if (serviceReference != null) {
				servicePID = (String) serviceReference
						.getProperty("service.pid");
				if(!servicePID.contains("StartRecord") && !servicePID.contains("StopRecord")){
					workFlow.addToAlgorithmList(servicePID);
				}
			}
		}
	}

	public void algorithmError(Algorithm algorithm, Throwable error) {

	}

	public void schedulerRunStateChanged(boolean isRunning) {

	}

	public void schedulerCleared() {

	}

}

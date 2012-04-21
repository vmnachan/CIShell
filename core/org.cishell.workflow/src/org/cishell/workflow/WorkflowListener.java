package org.cishell.workflow;

import java.util.Calendar;

import org.cishell.app.service.scheduler.SchedulerListener;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.Data;
import org.cishell.reference.gui.scheduler.SchedulerContentModel;

/**
 * Listens for notification from the scheduler and notifies all registered objects
 */
public class WorkflowListener implements SchedulerListener {

    private static final WorkflowListener INSTANCE = new WorkflowListener();
    
    private WorkflowListener(){
    	SchedulerContentModel.getInstance().register(this);
    }

    public static WorkflowListener getInstance(){
        return INSTANCE;
    }

	public void algorithmScheduled(Algorithm algorithm, Calendar time) {
		
		System.out.println("Algo Scheduled - " + algorithm.getClass().getName() + " - " + time.toString());		
	}

	public void algorithmRescheduled(Algorithm algorithm, Calendar time) {
		
		System.out.println("Algo Rescheduled - " + algorithm.getClass().getName() + " - " + time.toString());
	}

	public void algorithmUnscheduled(Algorithm algorithm) {
		
		System.out.println("Algo Unscheduled - " + algorithm.getClass().getName() );	
	}

	public void algorithmStarted(Algorithm algorithm) {
		
		System.out.println("Algo Started - " + algorithm.getClass().getName() );	
	}

	public void algorithmFinished(Algorithm algorithm, Data[] createdData) {
		
		System.out.println("Algo finished - " + algorithm.getClass().getName() + " - " + createdData.toString());
	}

	public void algorithmError(Algorithm algorithm, Throwable error) {
		
		System.out.println("Algo Error - " + algorithm.getClass().getName() + " - " + error.toString());
	}

	public void schedulerRunStateChanged(boolean isRunning) {
		
		System.out.println("Scheduler runstate changed - " + isRunning);
	}

	public void schedulerCleared() {
		
		System.out.println("Scheduler Cleared ");
	}

}

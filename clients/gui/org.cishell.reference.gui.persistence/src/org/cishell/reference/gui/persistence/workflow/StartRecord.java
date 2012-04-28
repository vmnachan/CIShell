package org.cishell.reference.gui.persistence.workflow;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.Data;
import org.cishell.service.conversion.DataConversionService;
import org.cishell.workflow.Workflow;
import org.osgi.service.log.LogService;

/**
 * The Class StartRecord.
 * 
 * Algorithm that sets the record workflow flag to record mode.
 * 
 * Author: P632
 * 
 */
public class StartRecord implements Algorithm {

	private Data[] dataToView;
	private Dictionary parameters;
	private CIShellContext ciShellContext;
	private LogService logger;

	/**
	 * Instantiates a new start record.
	 * 
	 * @param data
	 *            the data
	 * @param parameters
	 *            the parameters
	 * @param context
	 *            the context
	 */
	public StartRecord(Data[] data, Dictionary parameters,
			CIShellContext context) {
		this.dataToView = data;
		this.parameters = parameters;
		this.ciShellContext = context;
		this.logger = (LogService) context.getService(LogService.class
				.getName());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cishell.framework.algorithm.Algorithm#execute()
	 */
	public Data[] execute() throws AlgorithmExecutionException {

		try {
			Workflow.getInstance().setRecord(true);
		} catch (Exception e) {
			String logMessage = "Unable to start the record mode.";
			this.logger.log(LogService.LOG_ERROR, logMessage);
		}
		return new Data[0];
	}
}
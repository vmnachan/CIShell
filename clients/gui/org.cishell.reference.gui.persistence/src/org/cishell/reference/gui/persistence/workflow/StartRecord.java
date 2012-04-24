package org.cishell.reference.gui.persistence.workflow;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.Data;
import org.cishell.service.conversion.DataConversionService;
import org.cishell.workflow.Workflow;
import org.osgi.service.log.LogService;

public class StartRecord implements Algorithm {
	
    private Data[] dataToView;
    private Dictionary parameters;
    private CIShellContext ciShellContext;
    private String input;
    
    public StartRecord(Data[] data, Dictionary parameters, CIShellContext context) {
        this.dataToView = data;
        this.parameters = parameters;
        this.ciShellContext = context;

        Workflow.getInstance().setRecord(true);
    }

    public Data[] execute() throws AlgorithmExecutionException {
    	return new Data[0];
    }
}
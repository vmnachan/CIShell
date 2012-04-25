package org.cishell.reference.gui.persistence.workflow;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;

public class PlayWorkflowFactory implements AlgorithmFactory {

	private BundleContext bundleContext;

	protected void activate(ComponentContext ctxt) {
		bundleContext = ctxt.getBundleContext();
	}

	public Algorithm createAlgorithm(Data[] data, Dictionary parameters,
			CIShellContext context) {
		return new PlayWorkflow(data, parameters, context,bundleContext);
	}

}
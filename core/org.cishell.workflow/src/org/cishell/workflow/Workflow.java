package org.cishell.workflow;

import java.util.HashMap;

public class Workflow {

	private static Workflow instance = null;
	private boolean isRecord;
	private HashMap<String,String> algorithmMap;
	
	protected Workflow() {

		isRecord = false;
		algorithmMap = new HashMap<String,String>();
	}

	public static Workflow getInstance() {
		if (instance == null) {
			instance = new Workflow();
		}
		return instance;
	}

	public boolean isRecord() {
		return isRecord;
	}

	public void setRecord(boolean isRecord) {
		this.isRecord = isRecord;
	}

	public HashMap<String,String> getAlgorithmMap() {
		return algorithmMap;
	}

	public void setAlgorithmMap(HashMap<String,String> algorithmMap) {
		this.algorithmMap = algorithmMap;
	}
	
}

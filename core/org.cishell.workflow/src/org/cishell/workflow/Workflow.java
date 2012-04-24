package org.cishell.workflow;

import java.util.ArrayList;
import java.util.HashMap;

public class Workflow {

	private static Workflow instance = null;
	private boolean isRecord;
	private ArrayList<String> algorithmList;
	
	protected Workflow() {

		isRecord = false;
		algorithmList = new ArrayList<String>();
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

	public ArrayList<String> getAlgorithmList() {
		return algorithmList;
	}

	public void setAlgorithmList(ArrayList<String> algorithmList) {
		this.algorithmList = algorithmList;
	}
	
	public void addToAlgorithmList(String value){
		this.algorithmList.add(value);
	}
	
	public void emptyAlgorithmList(){
		this.algorithmList.clear();
	}
}

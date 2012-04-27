package org.cishell.workflow;

import java.util.ArrayList;

/**
 * The Class Workflow.
 * 
 * Base workflow instance singleton that stores the current workflow state
 * and the recorded algorithm list.
 * 
 * Author: P632
 */
public class Workflow {

	private static Workflow instance = null;
	private boolean isRecord;
	private ArrayList<String> algorithmList;
	
	/**
	 * Instantiates a new workflow.
	 */
	protected Workflow() {

		isRecord = false;
		algorithmList = new ArrayList<String>();
	}

	/**
	 * Gets the single instance of Workflow.
	 *
	 * @return single instance of Workflow
	 */
	public static Workflow getInstance() {
		if (instance == null) {
			instance = new Workflow();
		}
		return instance;
	}

	/**
	 * Checks if is record.
	 *
	 * @return true, if is record
	 */
	public boolean isRecord() {
		return isRecord;
	}

	/**
	 * Sets the record.
	 *
	 * @param isRecord the new record
	 */
	public void setRecord(boolean isRecord) {
		this.isRecord = isRecord;
	}

	/**
	 * Gets the algorithm list.
	 *
	 * @return the algorithm list
	 */
	public ArrayList<String> getAlgorithmList() {
		return algorithmList;
	}

	/**
	 * Sets the algorithm list.
	 *
	 * @param algorithmList the new algorithm list
	 */
	public void setAlgorithmList(ArrayList<String> algorithmList) {
		this.algorithmList = algorithmList;
	}
	
	/**
	 * Adds the to algorithm list.
	 *
	 * @param value the value
	 */
	public void addToAlgorithmList(String value){
		this.algorithmList.add(value);
	}
	
	/**
	 * Empty algorithm list.
	 */
	public void emptyAlgorithmList(){
		this.algorithmList.clear();
	}
}

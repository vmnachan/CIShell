package org.cishell.reference.gui.persistence.testAlgorithm;

import java.io.File;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Iterator;
import java.util.List;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;

import prefuse.data.Table;

public class TestAlgorithm implements Algorithm {
    private Data[] data;
    private Dictionary parameters;
    private CIShellContext ciShellContext;
    
    public TestAlgorithm(Data[] data,
    				  Dictionary parameters,
    				  CIShellContext ciShellContext) {
        this.data = data;
        this.parameters = parameters;
        this.ciShellContext = ciShellContext;
    }

    public Data[] execute() throws AlgorithmExecutionException {
		Table inputTable = (Table) (data[0].getData());
		if (inputTable.getColumnCount() > 1) {
			for (int i = 0; i < inputTable.getRowCount(); i++) {
				for(int j = 0; j < inputTable.getColumnCount(); j++) {
					inputTable.set(i, j, ((String)inputTable.get(i, j)).toUpperCase());
				}
			}
		}
        return createOutputData(inputTable);
    }
		
		private Data[] createOutputData(Table outputTable) {

			List<Data> outputDataList = new ArrayList<Data>();
			Data outputData = new BasicData(outputTable, Table.class.getName());
			Dictionary<String, Object> metadata = outputData.getMetadata();
			metadata.put(DataProperty.PARENT, data[0]);
			metadata.put(DataProperty.TYPE, DataProperty.TABLE_TYPE);
			metadata.put(DataProperty.LABEL, "Table");
			outputDataList.add(outputData);
			return (Data[]) outputDataList.toArray(new Data[] {});
		}

    
	
}
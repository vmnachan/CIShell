package org.cishell.reference.gui.persistence.testAlgorithmGenerator;

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

public class TestAlgorithmGenerator implements Algorithm {
    private Data[] data;
    private Dictionary parameters;
    private CIShellContext ciShellContext;
    
    public TestAlgorithmGenerator(Data[] data,
    				  Dictionary parameters,
    				  CIShellContext ciShellContext) {
        this.data = data;
        this.parameters = parameters;
        this.ciShellContext = ciShellContext;
    }

    public Data[] execute() throws AlgorithmExecutionException {
		Table inputTable = new Table();
		
		inputTable.addColumn("Title", String.class);
		inputTable.addColumn("Author", String.class);
		int index = inputTable.addRow();
		inputTable.set(index, 0, "abc");
		inputTable.set(index, 1, "efg");
		index = inputTable.addRow();
		inputTable.set(index, 0, "hij");
		inputTable.set(index, 1, "lmn");

        return createOutputData(inputTable);
    }
		
		private Data[] createOutputData(Table outputTable) {

			List<Data> outputDataList = new ArrayList<Data>();
			Data outputData = new BasicData(outputTable, Table.class.getName());
			Dictionary<String, Object> metadata = outputData.getMetadata();
			metadata.put(DataProperty.TYPE, DataProperty.TABLE_TYPE);
			metadata.put(DataProperty.LABEL, "Input Table");
			outputDataList.add(outputData);
			return (Data[]) outputDataList.toArray(new Data[] {});
		}

    
	
}
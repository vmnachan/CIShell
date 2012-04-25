package org.cishell.reference.gui.persistence.workflow;

import java.io.File;
import java.util.ArrayList;
import java.util.Dictionary;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.Data;
import org.cishell.reference.app.service.filesaver.FileSaverServiceImpl;
import org.cishell.workflow.Workflow;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class StopRecord implements Algorithm {

	private Data[] dataToView;
	private Dictionary parameters;
	private CIShellContext ciShellContext;
	private String input;

	public StopRecord(Data[] data, Dictionary parameters, CIShellContext context) {
		this.dataToView = data;
		this.parameters = parameters;
		this.ciShellContext = context;
	}

	public Data[] execute() throws AlgorithmExecutionException {

		try {
			FileSaverServiceImpl fileSaver = new FileSaverServiceImpl();
			File saveFile = fileSaver.promptForTargetFile("workflow", "xml");
			String savePath = saveFile.getAbsolutePath();
			
			DocumentBuilderFactory docFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			// root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("workflow");
			doc.appendChild(rootElement);

			ArrayList<String> algoList = Workflow.getInstance()
					.getAlgorithmList();
			for (String algorithm : algoList) {
				// algorithm elements
				Element algo = doc.createElement("Algorithm");
				algo.appendChild(doc.createTextNode(algorithm));
				rootElement.appendChild(algo);
			}
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			
			StreamResult result = new StreamResult(saveFile);
			transformer.transform(source, result);

			System.out.println("File saved!");
			
			Workflow.getInstance().emptyAlgorithmList();
			Workflow.getInstance().setRecord(false);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new Data[0];
	}
}
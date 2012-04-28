package org.cishell.reference.gui.persistence.workflow;

import java.io.File;
import java.util.ArrayList;
import java.util.Dictionary;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.cishell.app.service.filesaver.FileSaveException;
import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.Data;
import org.cishell.reference.app.service.filesaver.FileSaverServiceImpl;
import org.cishell.workflow.Workflow;
import org.osgi.service.log.LogService;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * The Class StopRecord.
 * 
 * Algorithm that saves all the algorithms run in the record mode into a file
 * specified by the user, finally set the record mode to false and empties the
 * workflow algorithm list.
 * 
 * Author: P632
 * 
 */
public class StopRecord implements Algorithm {

	private Data[] dataToView;
	private Dictionary parameters;
	private CIShellContext ciShellContext;
	private LogService logger;

	/**
	 * Instantiates a new stop record.
	 * 
	 * @param data
	 *            the data
	 * @param parameters
	 *            the parameters
	 * @param context
	 *            the context
	 */
	public StopRecord(Data[] data, Dictionary parameters, CIShellContext context) {
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
				Element algo = doc.createElement("algorithm");
				Element pid = doc.createElement("pid");
				pid.appendChild(doc.createTextNode(algorithm));
				algo.appendChild(pid);
				rootElement.appendChild(algo);
			}
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);

			StreamResult result = new StreamResult(saveFile);
			transformer.transform(source, result);

			Workflow.getInstance().emptyAlgorithmList();
			Workflow.getInstance().setRecord(false);

		} catch (ParserConfigurationException e) {
			String logMessage = "Parser error while writing to file.";
			this.logger.log(LogService.LOG_ERROR, logMessage);
		} catch (TransformerConfigurationException e) {
			String logMessage = "Transformer configuration error while writing to file";
			this.logger.log(LogService.LOG_ERROR, logMessage);
		} catch (TransformerException e) {
			String logMessage = "Transformer error while writing to file";
			this.logger.log(LogService.LOG_ERROR, logMessage);
		} catch (FileSaveException e) {
			String logMessage = "Unable to save the file";
			this.logger.log(LogService.LOG_ERROR, logMessage);
		}
		return new Data[0];
	}
}
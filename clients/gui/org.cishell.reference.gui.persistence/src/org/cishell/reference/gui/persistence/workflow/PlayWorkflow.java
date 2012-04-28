package org.cishell.reference.gui.persistence.workflow;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Dictionary;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.cishell.app.service.datamanager.DataManagerService;
import org.cishell.app.service.fileloader.FileLoadException;
import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;
import org.cishell.reference.app.service.fileloader.FileLoaderServiceImpl;
import org.cishell.utilities.AlgorithmUtilities;
import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogService;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * The Class PlayWorkflow.
 * 
 * Algorithm that loads a saved workflow file and executes the listed algorithms on the selected input data.
 * 
 * Author: P632
 * 
 */
public class PlayWorkflow implements Algorithm {

	private Data[] data;
	private Dictionary parameters;
	private CIShellContext ciShellContext;
	private BundleContext bundleContext;
	private LogService logger;

	/**
	 * Instantiates a new play workflow.
	 *
	 * @param data the data
	 * @param parameters the parameters
	 * @param context the context
	 * @param bundleContext the bundle context
	 */
	public PlayWorkflow(Data[] data, Dictionary parameters,
			CIShellContext context, BundleContext bundleContext) {
		this.data = data;
		this.parameters = parameters;
		this.ciShellContext = context;
		this.bundleContext = bundleContext;
		this.logger = (LogService) context.getService(LogService.class
				.getName());
	}

	/* (non-Javadoc)
	 * @see org.cishell.framework.algorithm.Algorithm#execute()
	 */
	public Data[] execute() throws AlgorithmExecutionException {

		FileLoaderServiceImpl fileLoad = new FileLoaderServiceImpl();

		File[] files;
		try {
			files = fileLoad.getFilesToLoadFromUser(true, null);

			ArrayList<String> algorithmList = getAlgorithmPids(files[0]);

			Data[] inData = getSelectedDataFromDataManager();
			for (String algoPID : algorithmList) {

				AlgorithmFactory algorithmFactory = AlgorithmUtilities
						.getAlgorithmFactoryByPID(algoPID, bundleContext);
				try {
					inData = AlgorithmUtilities.executeAlgorithm(
							algorithmFactory, inData, parameters,
							ciShellContext);
					addDataToDataManager(inData);
				} catch (AlgorithmExecutionException e) {
					String logMessage = "Error while execution of algorithm: "
							+ algoPID;
					this.logger.log(LogService.LOG_ERROR, logMessage);
				}
			}
		} catch (FileLoadException fle) {
			String logMessage = "Failed to load file.";
			this.logger.log(LogService.LOG_ERROR, logMessage);
		}

		return new Data[0];
	}

	/**
	 * Adds the data to DataManager using the DataManagerService.
	 *
	 * @param outData the data to be added to the DataManager
	 */
	protected void addDataToDataManager(Data[] outData) {
		if (outData != null) {
			DataManagerService dataManager = (DataManagerService) bundleContext
					.getService(bundleContext
							.getServiceReference(DataManagerService.class
									.getName()));

			if (outData.length != 0) {
				for (int i = 0; i < outData.length; i++) {
					dataManager.addData(outData[i]);
				}

				Data[] dataToSelect = new Data[] { outData[0] };
				dataManager.setSelectedData(dataToSelect);
			}
		}
	}

	/**
	 * Gets the currently selected data from DataManager.
	 *
	 * @return the selected data from DataManager
	 */
	protected Data[] getSelectedDataFromDataManager() {

		DataManagerService dataManager = (DataManagerService) bundleContext
				.getService(bundleContext
						.getServiceReference(DataManagerService.class.getName()));
		return dataManager.getSelectedData();
	}

	/**
	 * Gets the Algorithm pids from the loaded file
	 *
	 * @param file the loaded file
	 * @return the Algorithm Pids
	 */
	public ArrayList<String> getAlgorithmPids(File file) {

		StringBuilder stringBuilder = null;
		try {
			FileInputStream fstream = new FileInputStream(file);

			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			stringBuilder = new StringBuilder();
			String ls = System.getProperty("line.separator");

			while ((strLine = br.readLine()) != null) {
				stringBuilder.append(strLine);
				stringBuilder.append(ls);
			}
			in.close();

		} catch (FileNotFoundException fnfe) {
			String logMessage = "File not found.";
			this.logger.log(LogService.LOG_ERROR, logMessage);
		} catch (IOException e) {
			String logMessage = "Error while reading file.";
			this.logger.log(LogService.LOG_ERROR, logMessage);
		}

		ArrayList<String> list = new ArrayList<String>();
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(stringBuilder.toString()));

			org.w3c.dom.Document doc = db.parse(is);
			NodeList nodes = doc.getElementsByTagName("algorithm");

			// iterate the algorithms
			for (int i = 0; i < nodes.getLength(); i++) {
				Element element = (Element) nodes.item(i);

				NodeList name = element.getElementsByTagName("pid");
				Element line = (Element) name.item(0);
				list.add(getCharacterDataFromElement(line));

			}
		} catch (ParserConfigurationException e) {
			String logMessage = "Error while parsing document.";
			this.logger.log(LogService.LOG_ERROR, logMessage);
		} catch (SAXException e) {
			String logMessage = "Error while parsing document.";
			this.logger.log(LogService.LOG_ERROR, logMessage);
		} catch (IOException e) {
			String logMessage = "Error while parsing document.";
			this.logger.log(LogService.LOG_ERROR, logMessage);
		}

		return list;
	}

	/**
	 * Gets the character data from element.
	 *
	 * @param e the element
	 * @return the character data from element
	 */
	public static String getCharacterDataFromElement(Element e) {
		Node child = e.getFirstChild();
		if (child instanceof CharacterData) {
			CharacterData cd = (CharacterData) child;
			return cd.getData();
		}
		return "?";
	}
}
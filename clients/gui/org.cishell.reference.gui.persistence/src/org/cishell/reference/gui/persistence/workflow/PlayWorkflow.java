package org.cishell.reference.gui.persistence.workflow;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Dictionary;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.cishell.app.service.datamanager.DataManagerService;
import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;
import org.cishell.reference.app.service.fileloader.FileLoaderServiceImpl;
import org.cishell.utilities.AlgorithmUtilities;
import org.osgi.framework.BundleContext;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class PlayWorkflow implements Algorithm {

	private Data[] data;
	private Dictionary parameters;
	private CIShellContext ciShellContext;
	private BundleContext bundleContext;

	public PlayWorkflow(Data[] data, Dictionary parameters,
			CIShellContext context, BundleContext bundleContext) {
		this.data = data;
		this.parameters = parameters;
		this.ciShellContext = context;
		this.bundleContext = bundleContext;
	}

	public Data[] execute() throws AlgorithmExecutionException {

		FileLoaderServiceImpl fileLoad = new FileLoaderServiceImpl();
		try {

			File[] files = fileLoad.getFilesToLoadFromUser(true, null);

			ArrayList<String> algorithmList = get_pids(files[0]);
			
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
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new Data[0];
	}

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

	protected Data[] getSelectedDataFromDataManager() {

		DataManagerService dataManager = (DataManagerService) bundleContext
				.getService(bundleContext
						.getServiceReference(DataManagerService.class.getName()));
		return dataManager.getSelectedData();
	}

	public ArrayList<String> get_pids(File file) {

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

		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
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
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}

		return list;
	}

	public static String getCharacterDataFromElement(Element e) {
		Node child = e.getFirstChild();
		if (child instanceof CharacterData) {
			CharacterData cd = (CharacterData) child;
			return cd.getData();
		}
		return "?";
	}
}
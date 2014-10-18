package com.artisan.orchestrator.hadoop.job.config;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;


/**
 * This class is used to read and write site configurations for
 * hadoop.
 * @author chris
 *
 */
public class SiteConfigEditor {

	
	private Document doc = null;
	
	public SiteConfigEditor(Path filePath) throws SAXException, IOException, ParserConfigurationException {
		load(filePath);
	}
	
	private void load(Path path) throws SAXException, IOException, ParserConfigurationException {
		File fXmlFile = path.toFile();
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		doc = dBuilder.parse(fXmlFile);
	}
	
	public static final SiteConfigEditor create(Path filePath) throws SiteConfigurationException {
		try {
			return new SiteConfigEditor(filePath);
		} catch (SAXException | IOException | ParserConfigurationException e) {
			throw new SiteConfigurationException("Unable to create parser for file " + filePath.getFileName(), e);
		}	
	}
	
	public void setValue(String name, String value) {
		
	}
	
	public String getValue(String name) {
		try {
			return findValue(name);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private String findValue(String name) throws XPathExpressionException {
		XPath xPath = XPathFactory.newInstance().newXPath();
        Node node = (Node) xPath.evaluate("//configuration/property[name[text()='" + name + "']]/value", doc, XPathConstants.NODE);

        System.out.println("value is " + node.getTextContent());
        return null;
	}
}

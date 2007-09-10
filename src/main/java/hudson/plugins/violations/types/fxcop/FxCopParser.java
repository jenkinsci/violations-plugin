package hudson.plugins.violations.types.fxcop;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParserException;
import hudson.plugins.violations.ViolationsParser;
import hudson.plugins.violations.model.FullBuildModel;
import hudson.plugins.violations.model.FullFileModel;
import hudson.plugins.violations.model.Severity;
import hudson.plugins.violations.model.Violation;

/**
 * Parses a fxcop xml report file.
 * FxCopReport
 * 	Namespaces
 * 		Namespace - Name
 *	 		Messages 
 *				Message - Status, TypeName, CheckId, FixCategory
 *					Issue - Path, File, Line, Level
 *					Notes
 * 			Types
 * 				Type - Name, (Kind)
 * 					Messages
 * 					Members
 * 						Accessors
 * 							Accessors (Member)
 *	 					Messages
 *  Targets
 *  	Target - Name
 *  		Messages
 *  		Modules
 *  			Module - Name
 *  				Messages
 *  				Namespaces
 *  		Resources
 *  			Resource - Name
 *  				Messages
 * 	Notes
 * 	Rules
 */
public class FxCopParser implements ViolationsParser {

	private transient FullBuildModel model;

	public void parse(FullBuildModel model, File projectPath, String fileName, String[] sourcePaths) throws IOException {
		parse(model, new FileInputStream(new File(projectPath, fileName)), sourcePaths);
	}
	
	public void parse(FullBuildModel model, InputStream inputStream, String[] sourcePaths) throws IOException {
		this.model = model;
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;
		try {
			docBuilder = docBuilderFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(inputStream);

			NodeList mainNode = doc.getElementsByTagName("FxCopReport");
			
			parseNamespaces(getFirstElementByTagName((Element) mainNode.item(0), "Namespaces"), null);
			parseTargets(getFirstElementByTagName((Element) mainNode.item(0), "Targets"));
			// TODO parse notes, rules
		} catch (ParserConfigurationException pce) {
			throw new IOException(pce);
		} catch (SAXException se) {
			throw new IOException(se);
		}
	}
	
	private void parseTargets(Element targetsElement) {
		if (targetsElement != null ) {
			for (Element target : getNamedChildElements(targetsElement, "Target")) {
				String name = getString(target, "Name");
				parseMessages(getFirstElementByTagName(target, "Messages"), name);
				parseModules(getFirstElementByTagName(target, "Modules"), name);
				parseResources(getFirstElementByTagName(target, "Resources"), name);
			}			
		}
	}
	
	// TODO test
	private void parseResources(Element resources, String parentName) {
		if (resources != null ) {
			for (Element target : getNamedChildElements(resources, "Resource")) {
				String name = getString(target, "Name");
				parseMessages(getFirstElementByTagName(target, "Messages"), name);
			}			
		}
	}

	private void parseModules(Element modulesElement, String parentName) {
		if (modulesElement != null ) {
			for (Element module : getNamedChildElements(modulesElement, "Module")) {
				String name = getString(module, "Name");
				parseMessages(getFirstElementByTagName(module, "Messages"), name);
				parseNamespaces(getFirstElementByTagName(module, "Namespaces"), name);
			}
		}
	}

	private void parseNamespaces(Element namespacesElement, String parentName) {
		if (namespacesElement != null ) {
			for (Element namespace : getNamedChildElements(namespacesElement, "Namespace")) {
				String name = getString(namespace, "Name");

				parseMessages(getFirstElementByTagName(namespace, "Messages"), name);
				parseTypes(getFirstElementByTagName(namespace, "Types"), name);
			}			
		}
	}
	
	private void parseTypes(Element typesElement, String parentName) {
		if (typesElement != null ) {
			for (Element type : getNamedChildElements(typesElement, "Type")) {
				String name = parentName + "." + getString(type, "Name");
				// Kind, ExternallyVisible, Accessibility	

				parseMessages(getFirstElementByTagName(type, "Messages"), name);
				parseMembers(getFirstElementByTagName(type, "Members"), name);
			}			
		}
	}

	private void parseMembers(Element membersElement, String parentName) {
		if (membersElement != null ) {
			for (Element member : getNamedChildElements(membersElement, "Member")) {
				parseMember(member, parentName);
			}			
		}
	}

	private void parseAccessors(Element accessorsElement, String parentName) {
		if (accessorsElement != null ) {
			for (Element member : getNamedChildElements(accessorsElement, "Accessors")) {
				parseMember(member, parentName);
			}			
		}
	}

	private void parseMember(Element member, String parentName) {
		//String name = parentName + "." + getString(member, "Name");
		// Kind, ExternallyVisible, Accessibility, Static

		parseMessages(getFirstElementByTagName(member, "Messages"), parentName);
		parseAccessors(getFirstElementByTagName(member, "Accessors"), parentName);
	}

	private void parseMessages(Element messages, String parentName) {
		parseMessages(messages, parentName, null);
	}
	private void parseMessages(Element messages, String parentName, String subName) {
		if (messages != null ) {
			for (Element message : getNamedChildElements(messages, "Message")) {
				// Status, Id
				
				for (Element issue : getNamedChildElements(message, "Issue")) {
					parseIssue(issue, message, parentName, subName);
				}
				// TODO, parse notes
			}
		}
	}

	private void parseIssue(Element issue, Element parent, String parentName, String subName) {
		String typeName = getString(parent, "TypeName");
		String violationType = getString(parent, "Category") + "#" + getString(parent, "CheckId");
		
		// Path, File, Line, Level
		
		Violation violation = new Violation();
		violation.setType("fxcop");
		violation.setSource(violationType);
		if (subName == null) {
			violation.setMessage(typeName + " - " + issue.getTextContent());
		} else {
			violation.setMessage(subName + " " + typeName + " - " + issue.getTextContent());
		}
		setSeverityLevel(violation, getString(issue, "Level"));

		String filePath = getString(issue, "Path");
		String fileName = getString(issue, "File");
		String fileLine = getString(issue, "Line");
		
		FullFileModel fileModel;
		if ((filePath.length() > 0) && (fileName.length() > 0) && (fileLine.length() > 0)) {
			violation.setLine(fileLine);
			fileModel = model.getFileModel(filePath.replace('\\', '/') + "/" + fileName);
			if (fileModel.getSourceFile() == null) {
				File sourceFile = new File(filePath, fileName);
				if (sourceFile.exists()) {
					fileModel.setSourceFile(sourceFile);
					fileModel.setLastModified(sourceFile.lastModified());
				}
			}
		} else {
			fileModel = model.getFileModel(parentName);
		}
		fileModel.addViolation(violation);
	}
	
	
	/*
	private void parseNamespaceTag(Element namespace, FullBuildModel model, String targetName) {
		String name = getString(namespace, "Name");
		for (Element types : getNamedChildElements(namespace, "Types")) {
			for (Element type : getNamedChildElements(types, "Type")) {
				name = name + "/" + getString(type, "Name");
				for (Element members : getNamedChildElements(type, "Members")) {
					for (Element member : getNamedChildElements(members, "Member")) {
						// Messages, Accessors
						// name = name + "/" + getString(member, "Name");

						parseMessages(member, model, name);
					}
				}
			}
		}
	}

	private void parseMessageTag(Element messageElement, FullBuildModel buildModel, String fakeFileName) {
		String typeName = getString(messageElement, "TypeName");
		String violationType = getString(messageElement, "Category") + "#" + getString(messageElement, "CheckId");

		for (Element issueElement : getNamedChildElements(messageElement, "Issue")) {

			Violation violation = new Violation();
			violation.setType("fxcop");
			violation.setSource(violationType);
			violation.setMessage(typeName + " - " + issueElement.getTextContent());
			setSeverityLevel(violation, getString(issueElement, "Level"));

			String filePath = getString(issueElement, "Path");
			String fileName = getString(issueElement, "File");
			String fileLine = getString(issueElement, "Line");

			FullFileModel fileModel;
			if ((filePath.length() > 0) && (fileName.length() > 0) && (fileLine.length() > 0)) {
				violation.setLine(fileLine);
				fileModel = buildModel.getFileModel(filePath.replace('\\', '/') + "/" + fileName);
				if (fileModel.getSourceFile() == null) {
					File sourceFile = new File(filePath, fileName);
					if (sourceFile.exists()) {
						fileModel.setSourceFile(sourceFile);
						fileModel.setLastModified(sourceFile.lastModified());
					}
				}
			} else {
				fileModel = buildModel.getFileModel(fakeFileName);
			}
			fileModel.addViolation(violation);
		}
	}
*
*/
	private List<Element> getNamedChildElements(Element parent, String name) {
		List<Element> elements = new Vector<Element>();

		Node child = parent.getFirstChild();
		while (child != null) {

			if ((child.getNodeType() == Node.ELEMENT_NODE) && (child.getNodeName() == name)) {

				elements.add((Element) child);

			}

			child = child.getNextSibling();
		}

		return elements;
	}

	private static Element getFirstElementByTagName(Element parent, String tagName) {
		Element foundElement = null;
		Node child = parent.getFirstChild();

		while (child != null) {
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				if (child.getNodeName().equalsIgnoreCase(tagName)) {
					foundElement = (Element) child;
					break;
				}
			}
			child = child.getNextSibling();
		}

		return foundElement;
	}

	private String getString(Element element, String name) {

		if (element.hasAttribute(name)) {
			return element.getAttribute(name);
		} else {
			return "";
		}
	}
	
	private void setSeverityLevel(Violation violation, String issueLevel) {
		if (issueLevel.contains("CriticalError")) {
			violation.setSeverity(Severity.HIGH);
			violation.setSeverityLevel(Severity.HIGH_VALUE);
		} else if (issueLevel.contains("Error")) {
			violation.setSeverity(Severity.MEDIUM_HIGH);
			violation.setSeverityLevel(Severity.MEDIUM_HIGH_VALUE);
		} else if (issueLevel.contains("CriticalWarning")) {
			violation.setSeverity(Severity.MEDIUM);
			violation.setSeverityLevel(Severity.MEDIUM_VALUE);
		} else if (issueLevel.contains("Warning")) {
			violation.setSeverity(Severity.MEDIUM_LOW);
			violation.setSeverityLevel(Severity.MEDIUM_LOW_VALUE);
		} else {
			violation.setSeverity(Severity.LOW);
			violation.setSeverityLevel(Severity.LOW_VALUE);
		}
	}
}

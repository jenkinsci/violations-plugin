package hudson.plugins.violations.types.fxcop;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import hudson.plugins.violations.ViolationsParser;
import hudson.plugins.violations.model.FullBuildModel;
import hudson.plugins.violations.model.FullFileModel;
import hudson.plugins.violations.model.Severity;
import hudson.plugins.violations.model.Violation;

/**
 * Parses a fxcop xml report file.
 * 
 * 
 * This does not uses the XML Pull parser as it can not handle the FxCop XML
 * files. The bug is registered at Sun as http: //bugs.sun.com/bugdatabase/view_bug.do?bug_id=4508058
 */
public class FxCopParser implements ViolationsParser {

    private transient FullBuildModel model;
    private transient File projectPath;
    private transient FxCopRuleSet ruleSet = new FxCopRuleSet();

    public void parse(FullBuildModel model, File projectPath, String fileName, String[] sourcePaths) throws IOException {
        this.projectPath = projectPath;
        this.model = model;
        
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder;
        try {
            docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(new FileInputStream(new File(projectPath, fileName)));

            NodeList mainNode = doc.getElementsByTagName("FxCopReport");

            Element rootElement = (Element) mainNode.item(0);
            parseRules(XmlElementUtil.getFirstElementByTagName(rootElement, "Rules"));
			parseNamespaces(XmlElementUtil.getFirstElementByTagName(rootElement, "Namespaces"), null);
            parseTargets(XmlElementUtil.getFirstElementByTagName(rootElement, "Targets"));
            // TODO parse notes
        } catch (ParserConfigurationException pce) {
            throw new IOException(pce);
        } catch (SAXException se) {
            throw new IOException(se);
        }
    }

    private void parseRules(Element rulesElement) {
    	if (rulesElement != null) {
            for (Element rule : XmlElementUtil.getNamedChildElements(rulesElement, "Rule")) {
            	ruleSet.addRule(rule);
            }
        }
	}

	private void parseTargets(Element targetsElement) {
        if (targetsElement != null) {
            for (Element target : XmlElementUtil.getNamedChildElements(targetsElement, "Target")) {
                String name = getString(target, "Name");
                parseMessages(XmlElementUtil.getFirstElementByTagName(target, "Messages"), name);
                parseModules(XmlElementUtil.getFirstElementByTagName(target, "Modules"), name);
                parseResources(XmlElementUtil.getFirstElementByTagName(target, "Resources"), name);
            }
        }
    }

    // TODO test
    private void parseResources(Element resources, String parentName) {
        if (resources != null) {
            for (Element target : XmlElementUtil.getNamedChildElements(resources, "Resource")) {
                String name = getString(target, "Name");
                parseMessages(XmlElementUtil.getFirstElementByTagName(target, "Messages"), name);
            }
        }
    }

    private void parseModules(Element modulesElement, String parentName) {
        if (modulesElement != null) {
            for (Element module : XmlElementUtil.getNamedChildElements(modulesElement, "Module")) {
                String name = getString(module, "Name");
                parseMessages(XmlElementUtil.getFirstElementByTagName(module, "Messages"), name);
                parseNamespaces(XmlElementUtil.getFirstElementByTagName(module, "Namespaces"), name);
            }
        }
    }

    private void parseNamespaces(Element namespacesElement, String parentName) {
        if (namespacesElement != null) {
            for (Element namespace : XmlElementUtil.getNamedChildElements(namespacesElement, "Namespace")) {
                String name = getString(namespace, "Name");

                parseMessages(XmlElementUtil.getFirstElementByTagName(namespace, "Messages"), name);
                parseTypes(XmlElementUtil.getFirstElementByTagName(namespace, "Types"), name);
            }
        }
    }

    private void parseTypes(Element typesElement, String parentName) {
        if (typesElement != null) {
            for (Element type : XmlElementUtil.getNamedChildElements(typesElement, "Type")) {
                String name = parentName + "." + getString(type, "Name");
                // Kind, ExternallyVisible, Accessibility

                parseMessages(XmlElementUtil.getFirstElementByTagName(type, "Messages"), name);
                parseMembers(XmlElementUtil.getFirstElementByTagName(type, "Members"), name);
            }
        }
    }

    private void parseMembers(Element membersElement, String parentName) {
        if (membersElement != null) {
            for (Element member : XmlElementUtil.getNamedChildElements(membersElement, "Member")) {
                parseMember(member, parentName);
            }
        }
    }

    private void parseAccessors(Element accessorsElement, String parentName) {
        if (accessorsElement != null) {
            for (Element member : XmlElementUtil.getNamedChildElements(accessorsElement, "Accessor")) {
                parseMember(member, parentName);
            }
        }
    }

    private void parseMember(Element member, String parentName) {
        // Kind, ExternallyVisible, Accessibility, Static

        parseMessages(XmlElementUtil.getFirstElementByTagName(member, "Messages"), parentName);
        parseAccessors(XmlElementUtil.getFirstElementByTagName(member, "Accessors"), parentName);
    }

    private void parseMessages(Element messages, String parentName) {
        parseMessages(messages, parentName, null);
    }

    private void parseMessages(Element messages, String parentName, String subName) {
        if (messages != null) {
            for (Element message : XmlElementUtil.getNamedChildElements(messages, "Message")) {
                // Status, Id

                for (Element issue : XmlElementUtil.getNamedChildElements(message, "Issue")) {
                    parseIssue(issue, message, parentName, subName);
                }
                // TODO, parse notes
            }
        }
    }

    private void parseIssue(Element issue, Element parent, String parentName, String subName) {
        // Path, File, Line, Level
        String typeName = getString(parent, "TypeName");
        String category = getString(parent, "Category");
		String checkId = getString(parent, "CheckId");

        Violation violation = new Violation();
        violation.setType("fxcop");
        violation.setSource(category + "#" + checkId);
        setSeverityLevel(violation, getString(issue, "Level"));
        
        StringBuilder msgBuilder = new StringBuilder();
        if (subName != null) {
        	msgBuilder.append(subName);
        	msgBuilder.append(" ");
        }
        
        FxCopRule rule = ruleSet.getRule(category, checkId);
        if (rule != null) {
        	msgBuilder.append("<a href=\"");
        	msgBuilder.append(rule.getUrl());
        	msgBuilder.append("\">");
            msgBuilder.append(typeName);
            msgBuilder.append("</a>");
            violation.setPopupMessage(rule.getDescription());
        } else {
            msgBuilder.append(typeName);
        }
        msgBuilder.append(" - ");
        msgBuilder.append(issue.getTextContent());
        violation.setMessage(msgBuilder.toString());

        String filePath = getString(issue, "Path");
        String fileName = getString(issue, "File");
        String fileLine = getString(issue, "Line");

        FullFileModel fileModel;
        if ((filePath.length() > 0) && (fileName.length() > 0) && (fileLine.length() > 0)) {
            violation.setLine(fileLine);
            //fileModel = model.getFileModel(filePath.substring(2) + "/" + fileName);
            fileModel = model.getFileModel(resolveName(filePath + File.separatorChar + fileName));
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

    private String resolveName(String absoluteName) {
        String remote = projectPath.getAbsolutePath().replace('\\', '/');
        String lcRemote = remote.toLowerCase(Locale.US);
        String name = absoluteName.replace('\\', '/');
        String lcName = name.toLowerCase(Locale.US);
        if (lcName.startsWith(lcRemote)) {
            name = name.substring(lcRemote.length());
        } else {
            // remove dos drive
            name = name.substring(2);
        }
        // if name starts with a / strip it.
        if (name.startsWith("/")) {
            name = name.substring(1);
        }
        return name;
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

package hudson.plugins.violations.types.gendarme;

import hudson.plugins.violations.ViolationsParser;
import hudson.plugins.violations.model.FullBuildModel;
import hudson.plugins.violations.model.FullFileModel;
import hudson.plugins.violations.model.Severity;
import hudson.plugins.violations.model.Violation;
import hudson.plugins.violations.parse.ParseUtil;
import hudson.plugins.violations.types.fxcop.XmlElementUtil;
import hudson.plugins.violations.util.AbsoluteFileFinder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Parse Gendarme violations
 * @author mathias.kluba@gmail.com
 *
 */
public class GendarmeParser implements ViolationsParser {

	static final Logger logger = Logger.getLogger(GendarmeParser.class.toString());
	
	static final String TYPE_NAME = "gendarme";
	private FullBuildModel model;
    private File reportParentFile;
    private File projectPath;
    private String[] sourcePaths;
    private HashMap<String, GendarmeRule> rules;
	
	public void parse(FullBuildModel model, File projectPath, String fileName,
			String[] sourcePaths) throws IOException {
		logger.info("Starting Gendarme parsing");
		
		this.projectPath = projectPath;
        this.model = model;
        this.reportParentFile = new File(fileName).getParentFile();
        this.sourcePaths = sourcePaths;
        
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;		
		try {
			docBuilder = docBuilderFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(new FileInputStream(new File(projectPath, fileName)));
			
			NodeList mainNode = doc.getElementsByTagName("gendarme-output");
			
			Element rootElement = (Element) mainNode.item(0);
			Element resultsElement = (Element)rootElement.getElementsByTagName("results").item(0);
			Element rulesElement = (Element)rootElement.getElementsByTagName("rules").item(0);
			
			// load all rules into the cache
			parseRules(XmlElementUtil.getNamedChildElements(rulesElement, "rule"));
			
			// parse each violations
            parseViolations(XmlElementUtil.getNamedChildElements(resultsElement, "rule"));
            
		} catch (ParserConfigurationException pce) {
			throw new IOException(pce);
		} catch (SAXException se) {
			throw new IOException(se);
		}		
	}

	private void parseViolations(List<Element> ruleElements) {

		// searching source files
		AbsoluteFileFinder finder = new AbsoluteFileFinder();
		// add the project path to the search source
        finder.addSourcePath(this.projectPath.getPath());
        // if there's additional paths, add them too 
        if (this.sourcePaths != null) {
            finder.addSourcePaths(this.sourcePaths);
        }
        
		for(Element ruleElement : ruleElements){
			String ruleName = ruleElement.getAttribute("Name");
			GendarmeRule rule =  this.rules.get(ruleName);
			String problem = ruleElement.getElementsByTagName("problem").item(0).getTextContent();
			String solution = ruleElement.getElementsByTagName("solution").item(0).getTextContent();
			
			List<Element> targetElements = XmlElementUtil.getNamedChildElements(ruleElement, "target");
						
			for(Element targetElement : targetElements){
				//String targetName = targetElement.getAttribute("Name");
				String targetAssembly = targetElement.getAttribute("Assembly");
				DotNetAssembly assembly = new DotNetAssembly(targetAssembly);
				
				Element defectElement = (Element) targetElement.getElementsByTagName("defect").item(0);
				String severityString = defectElement.getAttribute("Severity");
				String source = defectElement.getAttribute("Source");
				//String location = defectElement.getAttribute("Location");
				String confidence = defectElement.getAttribute("Confidence");
				String filePath = "";
				String fileName = "";
				int line = 0;

				if(rule.getType() == GendarmeRuleType.Method){
					Pattern pattern = Pattern.compile("^(.*)\\(.([0-9]*)\\)$");
					Matcher matcher = pattern.matcher(source);
					logger.info("matcher.groupCount() : "+matcher.groupCount());
					
					logger.info("matcher.matches() : "+matcher.matches());
					logger.info("source : "+source);
					if(matcher.matches()) {
						for(int cpt = 0; cpt < matcher.groupCount(); cpt++){
							logger.info("group("+((int)(cpt+1))+"): "+matcher.group(cpt+1));
						}
					}
					if(matcher.matches()){
						String fullPath = matcher.group(1);
						File sourceFile = new File(fullPath);
						fileName = sourceFile.getName();
						filePath = sourceFile.getParent();
						line = Integer.parseInt(matcher.group(2)); 
					}
				}
				
				// create the violation
				Violation violation = new Violation();

				// construct the error message
				StringBuilder messageBuilder = new StringBuilder();
				if(rule.getUrl() != null){
					messageBuilder.append("<a href=\"").append(rule.getUrl().toString()).append("\">");
					messageBuilder.append(rule.getName());
					messageBuilder.append("</a>");
				}
				else {
					messageBuilder.append(rule.getName());
				}
				
				messageBuilder.append(" - ").append(problem).append("<br/>");
				messageBuilder.append("Solution: ").append(solution).append("<br/>");
				messageBuilder.append("Confidence: ").append(confidence);
				
				violation.setMessage(messageBuilder.toString());
				violation.setPopupMessage(problem);	
				
				// construct the severity
				if(severityString.equals("Low")){
					violation.setSeverityLevel(Severity.LOW_VALUE);
					violation.setSeverity(Severity.LOW);
				}
				else if(severityString.equals("Medium")){
					violation.setSeverityLevel(Severity.MEDIUM_VALUE);
					violation.setSeverity(Severity.MEDIUM);
				}
				else if(severityString.equals("High")){
					violation.setSeverityLevel(Severity.HIGH_VALUE);
					violation.setSeverity(Severity.HIGH);
				}
				else {
					violation.setSeverityLevel(Severity.MEDIUM_VALUE);
					violation.setSeverity(Severity.MEDIUM);
				}
				violation.setType(TYPE_NAME);
				violation.setSource(rule.getName());

				// try to get the file
				// TODO : test it with Linux Master => Windows Slave node. Unix/Windows path could be a problem.
				FullFileModel fileModel;
		        if ((filePath.length() > 0) && (fileName.length() > 0) ) {
		        	violation.setLine(line);
		        	
		        	// get the display name of the file
		        	String displayName = ParseUtil.resolveAbsoluteName(this.projectPath, filePath + File.separatorChar + fileName);

		        	// try to get the source file, add it if not already exists
		            fileModel = model.getFileModel(displayName);
		            if (fileModel.getSourceFile() == null) {
		            	finder.addSourcePath(filePath);
		                File sourceFile = finder.getFileForName(fileName);
		                if (sourceFile != null && sourceFile.exists()) {
		                    fileModel.setSourceFile(sourceFile);
		                    fileModel.setLastModified(sourceFile.lastModified());
		                    logger.info("fileModel.getSourceFile() : "+fileModel.getSourceFile().getAbsolutePath());
		                }
		                else {
		                	logger.info("sourceFile.exists()==false: "+filePath+","+ fileName);
		                }
		            }
		            else{
		            	logger.info("fileModel.getSourceFile() != null: "+displayName);		            	
		            }
		        } else {
		        	// if there's no source files, just put the assembly name
		            fileModel = model.getFileModel(assembly.getName()+".dll");		            
		            logger.info("fileModel.getSourceFile() : "+(fileModel.getSourceFile() == null? "null" : fileModel.getSourceFile().getAbsolutePath()));
		        }
		        logger.info("fileModel.getDisplayName() : "+fileModel.getDisplayName());
		        logger.info("reportParentFile : "+reportParentFile);
		        logger.info("fileName : "+fileName);
		        logger.info("filePath : "+filePath);
		        
		        // Add the violation to the model
	            fileModel.addViolation(violation);
			}
		}
	}
	
	private void parseRules(List<Element> ruleElements) {
		rules = new HashMap<String, GendarmeRule>();
		
		for(Element ruleElement : ruleElements){
			
			// create the Gendarme rule
			GendarmeRule rule = new GendarmeRule();
			rule.setName(ruleElement.getAttribute("Name"));
			rule.setTypeName(ruleElement.getTextContent());
			String typeString = ruleElement.getAttribute("Type");
			if(typeString.equals("Type"))
				rule.setType(GendarmeRuleType.Type);
			else if(typeString.equals("Method"))
				rule.setType(GendarmeRuleType.Method);
			else if(typeString.equals("Assembly"))
				rule.setType(GendarmeRuleType.Assembly);
			try {
				rule.setUrl(new URL(ruleElement.getAttribute("Uri")));
			} catch (MalformedURLException e) {
				rule.setUrl(null);
			}
			
			// add the rule to the cache
			rules.put(rule.getName(), rule);
		}
	}

}

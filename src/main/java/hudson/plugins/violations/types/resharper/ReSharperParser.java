package hudson.plugins.violations.types.resharper;

import hudson.plugins.violations.ViolationsParser;
import hudson.plugins.violations.model.FullBuildModel;
import hudson.plugins.violations.model.FullFileModel;
import hudson.plugins.violations.model.Severity;
import hudson.plugins.violations.model.Violation;
import hudson.plugins.violations.util.AbsoluteFileFinder;
import hudson.plugins.violations.util.HashMapWithDefault;
import hudson.util.IOException2;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ReSharperParser implements ViolationsParser {

    private static final HashMapWithDefault<String, String> SEVERITIES = new HashMapWithDefault<String, String>(
            Severity.LOW);

    static {
        SEVERITIES.put("ERROR", Severity.HIGH);
        SEVERITIES.put("WARNING", Severity.MEDIUM);
        SEVERITIES.put("SUGGESTION", Severity.MEDIUM_LOW);
        SEVERITIES.put("HINT", Severity.LOW);
    }

    private final AbsoluteFileFinder absoluteFileFinder = new AbsoluteFileFinder();

    private final Map<String, IssueType> issueTypes = new HashMap<String, IssueType>();

    private Issue parseIssue(final Element issueElement) {
        final Issue issue = new Issue();
        issue.setTypeId(getString(issueElement, "TypeId"));
        issue.setFile(getString(issueElement, "File"));
        issue.setOffset(getString(issueElement, "Offset"));
        issue.setLine(getString(issueElement, "Line"));
        issue.setMessage(getString(issueElement, "Message"));
        return issue;
    }

    private IssueType parseIssueType(final Element issueTypeElement) {
        final IssueType issueType = new IssueType();
        issueType.setId(getString(issueTypeElement, "Id"));
        issueType.setCategory(getString(issueTypeElement, "Category"));
        issueType.setDescription(getString(issueTypeElement, "Description"));
        issueType.setSeverity(getString(issueTypeElement, "Severity"));
        issueType.setWikiUrl(getString(issueTypeElement, "WikiUrl"));
        return issueType;
    }

    private String getString(final Element el, final String attribute) {
        String ret = el.getAttribute(attribute);
        if (ret == null || ret.trim().equals("")) {
            ret = "";
        }
        return ret;
    }

    private FullFileModel getFileModel(final FullBuildModel model,
            final String name, final File sourceFile) {
        final FullFileModel fileModel = model.getFileModel(name);
        final File other = fileModel.getSourceFile();

        if (sourceFile == null
                || ((other != null) && (other.equals(sourceFile) || other
                        .exists()))) {
            return fileModel;
        }

        fileModel.setSourceFile(sourceFile);
        fileModel.setLastModified(sourceFile.lastModified());
        return fileModel;
    }

    public void parse(final FullBuildModel model, final File projectPath,
            final String fileName, final String[] sourcePaths)
            throws IOException {
        absoluteFileFinder.addSourcePath(projectPath.getAbsolutePath());
        absoluteFileFinder.addSourcePaths(sourcePaths);

        final DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
                .newInstance();
        DocumentBuilder docBuilder;
        try {
            docBuilder = docBuilderFactory.newDocumentBuilder();
            final Document docElement = docBuilder.parse(new FileInputStream(
                    new File(projectPath, fileName)));

            NodeList nl = docElement.getElementsByTagName("IssueType");
            if (nl == null)
                return;

            for (int i = 0; i < nl.getLength(); i++) {
                final Element issueTypeElement = (Element) nl.item(i);
                final IssueType issueType = parseIssueType(issueTypeElement);
                issueTypes.put(issueType.getId(), issueType);
            }

            nl = docElement.getElementsByTagName("Issue");
            if (nl == null)
                return;

            for (int i = 0; i < nl.getLength(); i++) {
                final Element issueElement = (Element) nl.item(i);
                final Issue issue = parseIssue(issueElement);
                final IssueType issueType = issueTypes.get(issue.getTypeId());

                if (issueType == null) // couldn't find the issue type, skip it
                    continue;

                final Violation violation = new Violation();
                violation.setType("resharper");
                violation.setMessage(issue.getMessage());
                violation.setPopupMessage(issueType.getDescription() + " - "
                        + issue.getMessage());
                violation.setSource(issueType.getCategory());
                violation.setLine(issue.getLine());
                violation.setSeverity(SEVERITIES.get(issueType.getSeverity()));
                violation.setSeverityLevel(Severity.getSeverityLevel(violation
                        .getSeverity()));

                final File file = absoluteFileFinder.getFileForName(issue
                        .getFile());
                final FullFileModel fullFileModel = getFileModel(model, issue
                        .getFile().replace('\\', '/'), file);
                fullFileModel.addViolation(violation);
            }
        } catch (final ParserConfigurationException pce) {
            throw new IOException2(pce);
        } catch (final SAXException se) {
            throw new IOException2(se);
        }
    }

}

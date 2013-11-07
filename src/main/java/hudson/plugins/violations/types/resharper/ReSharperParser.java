package hudson.plugins.violations.types.resharper;


public class ReSharperParser extends ViolationsDOMParser {

	private static final HashMapWithDefault<String, String> SEVERITIES = new HashMapWithDefault<String, String>(Severity.LOW);

	static {
		SEVERITIES.put("ERROR", Severity.HIGH);
		SEVERITIES.put("WARNING", Severity.MEDIUM);
		SEVERITIES.put("SUGGESTION", Severity.MEDIUM_LOW);
		SEVERITIES.put("HINT", Severity.LOW);
	}

	private final AbsoluteFileFinder absoluteFileFinder = new AbsoluteFileFinder();

	private final Map<String, IssueType> issueTypes = new HashMap<String, IssueType>();

	private Issue parseIssue(final Element issueElement) throws Exception {
		final Issue issue = new Issue();
		issue.setTypeId(getString(issueElement, "TypeId"));
		issue.setFile(getString(issueElement, "File"));
		issue.setOffset(getString(issueElement, "Offset"));
		issue.setLine(getString(issueElement, "Line"));
		issue.setMessage(getString(issueElement, "Message"));
		return issue;
	}

	private IssueType parseIssueType(final Element issueTypeElement) throws Exception {
		final IssueType issueType = new IssueType();
		issueType.setId(getString(issueTypeElement, "Id"));
		issueType.setCategory(getString(issueTypeElement, "Category"));
		issueType.setDescription(getString(issueTypeElement, "Description"));
		issueType.setSeverity(getString(issueTypeElement, "Severity"));
		issueType.setWikiUrl(getString(issueTypeElement, "WikiUrl"));
		return issueType;
	}

	private String getString(final Element el, final String attribute) throws Exception {
		String ret = el.getAttribute(attribute);
		if (ret == null || ret.trim().equals("")) {
			ret = "";
		}
		return ret;
	}

	@Override
	protected void execute() throws Exception {

		absoluteFileFinder.addSourcePath(getProjectPath().getAbsolutePath());
		absoluteFileFinder.addSourcePaths(getSourcePaths());

		final Element docElement = getDocument().getDocumentElement();

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
			violation.setPopupMessage(issueType.getDescription());
			violation.setSource(issueType.getCategory());
			violation.setLine(issue.getLine());
			violation.setSeverity(SEVERITIES.get(issueType.getSeverity()));
			violation.setSeverityLevel(Severity.getSeverityLevel(violation.getSeverity()));

			final File file = absoluteFileFinder.getFileForName(issue.getFile());
			if (file == null) // could not find the file, skip this validation
				continue;
			final FullFileModel fullFileModel = getFileModel(issue.getFile().replace('\\', '/'), file);
			fullFileModel.addViolation(violation);
		}
	}

}

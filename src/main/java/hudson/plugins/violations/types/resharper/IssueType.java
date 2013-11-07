package hudson.plugins.violations.types.resharper;

public class IssueType {

	private String Category;

	private String Description;

	private String Id;

	private String Severity;

	private String WikiUrl;

	public String getCategory() {
		return Category;
	}

	public String getDescription() {
		return Description;
	}

	public String getId() {
		return Id;
	}

	public String getSeverity() {
		return Severity;
	}

	public String getWikiUrl() {
		return WikiUrl;
	}

	public void setCategory(final String category) {
		Category = category;
	}

	public void setDescription(final String description) {
		Description = description;
	}

	public void setId(final String id) {
		Id = id;
	}

	public void setSeverity(final String severity) {
		Severity = severity;
	}

	public void setWikiUrl(final String wikiUrl) {
		WikiUrl = wikiUrl;
	}

}

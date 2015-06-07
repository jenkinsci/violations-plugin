package hudson.plugins.violations.types.resharper;

public class Issue {

	private String File;

	private String Line;

	private String Message;

	private String Offset;

	private String TypeId;

	public String getFile() {
		return File;
	}

	public String getLine() {
		return Line;
	}

	public String getMessage() {
		return Message;
	}

	public String getOffset() {
		return Offset;
	}

	public String getTypeId() {
		return TypeId;
	}

	public void setFile(final String file) {
		File = file;
	}

	public void setLine(final String line) {
		Line = line;
	}

	public void setMessage(final String message) {
		Message = message;
	}

	public void setOffset(final String offset) {
		Offset = offset;
	}

	public void setTypeId(final String typeId) {
		TypeId = typeId;
	}
}

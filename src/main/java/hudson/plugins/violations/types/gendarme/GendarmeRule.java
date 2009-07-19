package hudson.plugins.violations.types.gendarme;

import java.net.URL;

public class GendarmeRule {
	private String name;
	
	private String typeName;

	private GendarmeRuleType type;
	
	private URL url;
	
	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public GendarmeRuleType getType() {
		return type;
	}

	public void setType(GendarmeRuleType type) {
		this.type = type;
	}

	public URL getUrl() {
		return url;
	}

	public void setUrl(URL url) {
		this.url = url;
	}
}

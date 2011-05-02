package hudson.plugins.violations.types.gendarme;

public class DotNetAssembly {
	private String fullName;
	
	private String name;
	
	private String version;
	
	private String culture;
	
	private String publicKeyToken;
	
	public DotNetAssembly(String fullName) {
		this.fullName = fullName;
		
		String[] splitted = this.fullName.split(",");
		int cpt = 0;
		for(String s : splitted){
			if(cpt == 0) {
				this.name = s.trim();
			}
			else {
				String[] keyValue = s.trim().split("=");
				if(keyValue[0].equals("Version")){
					this.version = keyValue[1];
				}
				else if(keyValue[0].equals("Culture")){
					this.culture = keyValue[1];
				}
				else if(keyValue[0].equals("PublicKeyToken")){
					this.publicKeyToken = keyValue[1];
				}
			}
			cpt++;
		}
	}
	
	public String getFullName() {
		return fullName;
	}

	public String getName() {
		return name;
	}

	public String getVersion() {
		return version;
	}

	public String getCulture() {
		return culture;
	}

	public String getPublicKeyToken() {
		return publicKeyToken;
	}	
}

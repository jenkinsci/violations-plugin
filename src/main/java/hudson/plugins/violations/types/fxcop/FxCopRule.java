package hudson.plugins.violations.types.fxcop;

/**
 * Internal model for a FxCop rule
 *
 * @author Erik Ramfelt
 */
public class FxCopRule {
    private transient String name;
    private transient String typeName;
    private transient String category;
    private transient String checkId;
    private transient String url;
    private transient String description;

    /**
     * @param typeName
     * @param category
     * @param checkId
     */
    public FxCopRule(String typeName, String category, String checkId) {
        this.typeName = typeName;
        this.category = category;
        this.checkId = checkId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTypeName() {
        return typeName;
    }

    public String getCategory() {
        return category;
    }

    public String getCheckId() {
        return checkId;
    }
}

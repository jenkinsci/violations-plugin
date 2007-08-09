package hudson.plugins.violations.render;

import hudson.model.Build;

/**
 * Class for rendering no violations.
 */
public class NoViolationsFile {
    private final String name;
    private final Build  build;

    /**
     * Create a new no violations render page.
     * @param name the name of the file.
     * @param build the build.
     */
    public NoViolationsFile(String name, Build build) {
        this.name = name;
        this.build = build;
    }

    /**
     * Get the name.
     * @return the name for this page.
     */
    public String getName() {
        return name;
    }

    /**
     * Get the build object.
     * @return the build for this page.
     */
    public Build getBuild() {
        return build;
    }
}

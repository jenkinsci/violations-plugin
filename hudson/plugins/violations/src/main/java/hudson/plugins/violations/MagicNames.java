package hudson.plugins.violations;


/**
 * A simple class to contain strings and names
 * used by the violations plugin.
 */
public final class MagicNames {
    private static final String PLUGIN_HOME = "/plugin/violations";

    private static final String ICON_NAME = "dialog-warning.png";

    /** The dir name to use to generate violation reports into */
    public static final String VIOLATIONS = "violations";

    /** The 16x16 violations icon */
    public static final String ICON_16
        = PLUGIN_HOME + "/images/16x16/" + ICON_NAME;

    /** The 24x24 violations icon */
    public static final String ICON_24
        = PLUGIN_HOME + "/images/24x24/" + ICON_NAME;

    /** The 32x32 violations icon */
    public static final String ICON_32
        = PLUGIN_HOME + "/images/32x32/" + ICON_NAME;


    /** The 48x28 violations icon */
    public static final String ICON_48
        = PLUGIN_HOME + "/images/48x48/" + ICON_NAME;


    /** private constuctor */
    private MagicNames() {
    }
}

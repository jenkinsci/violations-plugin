package hudson.plugins.violations.hudson;

import java.util.Map;

import hudson.maven.MavenReporter;
import hudson.maven.MavenReporterDescriptor;
import org.kohsuke.stapler.StaplerRequest;

import hudson.plugins.violations.ViolationsConfig;
import hudson.plugins.violations.ViolationsPublisher;
import hudson.plugins.violations.TypeConfig;


public class ViolationsMavenDescriptor
    extends MavenReporterDescriptor {
    
    public ViolationsMavenDescriptor() {
        super(ViolationsMavenReporter.class);
    }

    /** {@inheritDoc} */
    @Override
    public String getDisplayName() {
        return "violations";
    }

    /** {@inheritDoc} */
    @Override
    public String getConfigPage() {
        return getViewPage(ViolationsPublisher.class, "config.jelly");
    }

    /** {@inheritDoc} */
    @Override
    public String getHelpFile() {
        return "/plugin/violations/help.html";
    }

    /** {@inheritDoc} */
    @Override
    public MavenReporter newInstance(final StaplerRequest req)
        throws FormException {
        ViolationsMavenReporter pub = new ViolationsMavenReporter();
        req.bindParameters(pub, "violations.");
        req.bindParameters(pub.getConfig(), "config.");
        for (Map.Entry<String, TypeConfig> entry
                 : pub.getConfig().getTypeConfigs().entrySet()) {
            String type = entry.getKey();
            TypeConfig c = entry.getValue();
            req.bindParameters(c, type + ".");
            // the ugly hack - see emma and clover plugins
            if ("".equals(req.getParameter(type + ".min"))) {
                c.setMin(TypeConfig.DEFAULT_MIN);
            }

            if ("".equals(req.getParameter(type + ".max"))) {
                c.setMax(TypeConfig.DEFAULT_MAX);
            }
        }
        // Ensure that the numbers make sense
        pub.getConfig().fix();
        return pub;
    }

    /**
     * Return a default config to be used for configuration screen.
     * @return a default config.
     */
    public ViolationsConfig getConfig() {
        return new ViolationsConfig();
    }
}

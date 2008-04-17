package hudson.plugins.violations;

import hudson.Plugin;

import hudson.tasks.Publisher;
import hudson.maven.MavenReporters;
import hudson.plugins.violations.hudson.ViolationsMavenReporter;

/**
 * Entry point of a plugin.
 * Add a publisher for violations
 * @plugin violations
 */
public class PluginImpl extends Plugin {
    /**
     * The start method for the plugin.
     * @throws Exception if there is a problem.
     */
    @Override
    public void start() throws Exception {
        Publisher.PUBLISHERS.addRecorder(ViolationsPublisher.DESCRIPTOR);
        MavenReporters.LIST.add(ViolationsMavenReporter.DESCRIPTOR);
    }
}

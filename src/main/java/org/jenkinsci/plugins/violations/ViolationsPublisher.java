package org.jenkinsci.plugins.violations;

import java.io.IOException;
import java.util.List;

import org.jenkinsci.plugins.violations.parser.ViolationsParser;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;

import hudson.EnvVars;
import hudson.FilePath;
import hudson.Launcher;
import hudson.matrix.MatrixAggregator;
import hudson.matrix.MatrixBuild;
import hudson.model.AbstractProject;
import hudson.model.Action;
import hudson.model.BuildListener;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.plugins.analysis.core.BuildResult;
import hudson.plugins.analysis.core.FilesParser;
import hudson.plugins.analysis.core.HealthAwarePublisher;
import hudson.plugins.analysis.core.ParserResult;
import hudson.plugins.analysis.util.PluginLogger;

public class ViolationsPublisher extends HealthAwarePublisher {

 private static final String INCLUDE_ALL_FILES = "**";

 private static final String PLUGIN_NAME = "VIOLATIONS";

 private static final long serialVersionUID = 6369581633551160418L;
 private List<ViolationConfig> violationConfigs;

 @DataBoundConstructor
 public ViolationsPublisher(List<String> patterns, List<String> reporters, List<ViolationConfig> violationConfigs) {
  super(PLUGIN_NAME);
  if (violationConfigs == null || violationConfigs.isEmpty()) {
   this.violationConfigs = ViolationConfigHelper.getViolationConfigs(patterns, reporters);
  } else {
   /**
    * Used by job DSL. Dont know how to supply the violationConfigs like this
    * with Jelly templates.
    */
   this.violationConfigs = ViolationConfigHelper.getViolationConfigs(violationConfigs);
  }
 }

 @Override
 public MatrixAggregator createAggregator(final MatrixBuild build, final Launcher launcher,
   final BuildListener listener) {
  return new ViolationsAnnotationsAggregator(build, launcher, listener, this, getDefaultEncoding(),
    usePreviousBuildAsReference(), useOnlyStableBuildsAsReference());
 }

 public List<ViolationConfig> getAllViolationConfigs() {
  return ViolationConfigHelper.getViolationConfigs(violationConfigs);
 }

 @Override
 public ViolationsDescriptor getDescriptor() {
  return (ViolationsDescriptor) super.getDescriptor();
 }

 @Override
 public Action getProjectAction(final AbstractProject<?, ?> project) {
  return new ViolationsProjectAction(project);
 }

 @Override
 public BuildResult perform(final Run<?, ?> build, final FilePath workspace, final PluginLogger logger)
   throws InterruptedException, IOException {
  logger.log("Collecting violations analysis files...");
  EnvVars environment = build.getEnvironment(TaskListener.NULL);
  ViolationConfigHelper.expandPatterns(environment, violationConfigs);
  FilesParser parser = new FilesParser(PLUGIN_NAME, INCLUDE_ALL_FILES,
    new ViolationsParser(getDefaultEncoding(), violationConfigs), shouldDetectModules(), isMavenBuild(build));

  ParserResult project = workspace.act(parser);
  logger.logLines(project.getLogMessages());

  ViolationsResult result = new ViolationsResult(build, getDefaultEncoding(), project, usePreviousBuildAsReference(),
    useOnlyStableBuildsAsReference());
  build.addAction(new ViolationsResultAction(build, this, result));

  return result;
 }

 @DataBoundSetter
 public void setViolationConfigs(List<ViolationConfig> violationConfigs) {
  this.violationConfigs = ViolationConfigHelper.getViolationConfigs(violationConfigs);
 }

}

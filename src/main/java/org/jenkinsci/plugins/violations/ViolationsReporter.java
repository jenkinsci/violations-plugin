package org.jenkinsci.plugins.violations;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.component.configurator.ComponentConfigurationException;
import org.jenkinsci.plugins.violations.parser.ViolationsParser;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;

import hudson.FilePath;
import hudson.maven.MavenAggregatedReport;
import hudson.maven.MavenBuild;
import hudson.maven.MavenBuildProxy;
import hudson.maven.MavenModule;
import hudson.maven.MojoInfo;
import hudson.plugins.analysis.core.FilesParser;
import hudson.plugins.analysis.core.HealthAwareReporter;
import hudson.plugins.analysis.core.ParserResult;
import hudson.plugins.analysis.util.PluginLogger;
import hudson.remoting.VirtualChannel;

public class ViolationsReporter extends HealthAwareReporter<ViolationsResult> {
 private static final String PLUGIN_NAME = "VIOLATIONS";

 private static final long serialVersionUID = 2272875032054063496L;

 private List<ViolationConfig> violationConfigs;

 @DataBoundConstructor
 public ViolationsReporter(final String healthy, final String unHealthy, final String thresholdLimit,
   final boolean useDeltaValues, final String unstableTotalAll, final String unstableTotalHigh,
   final String unstableTotalNormal, final String unstableTotalLow, final String unstableNewAll,
   final String unstableNewHigh, final String unstableNewNormal, final String unstableNewLow,
   final String failedTotalAll, final String failedTotalHigh, final String failedTotalNormal,
   final String failedTotalLow, final String failedNewAll, final String failedNewHigh, final String failedNewNormal,
   final String failedNewLow, final boolean canRunOnFailed, final boolean usePreviousBuildAsReference,
   final boolean useStableBuildAsReference, final boolean canComputeNew, List<String> patterns, List<String> reporters,
   List<ViolationConfig> violationConfigs) {
  super(healthy, unHealthy, thresholdLimit, useDeltaValues, unstableTotalAll, unstableTotalHigh, unstableTotalNormal,
    unstableTotalLow, unstableNewAll, unstableNewHigh, unstableNewNormal, unstableNewLow, failedTotalAll,
    failedTotalHigh, failedTotalNormal, failedTotalLow, failedNewAll, failedNewHigh, failedNewNormal, failedNewLow,
    canRunOnFailed, usePreviousBuildAsReference, useStableBuildAsReference, canComputeNew, PLUGIN_NAME);
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
 protected boolean acceptGoal(final String goal) {
  return "violations".equals(goal) || "check".equals(goal) || "site".equals(goal);
 }

 @Override
 protected MavenAggregatedReport createMavenAggregatedReport(final MavenBuild build, final ViolationsResult result) {
  return new ViolationsMavenResultAction(build, this, getDefaultEncoding(), result);
 }

 @Override
 protected ViolationsResult createResult(final MavenBuild build, final ParserResult project) {
  return new ViolationsReporterResult(build, getDefaultEncoding(), project, usePreviousBuildAsReference(),
    useOnlyStableBuildsAsReference());
 }

 public List<ViolationConfig> getAllViolationConfigs() {
  return ViolationConfigHelper.getViolationConfigs(violationConfigs);
 }

 private FilePath getFileName(final MojoInfo mojo, final MavenProject pom) {
  try {
   String configurationValue = mojo.getConfigurationValue("outputFile", String.class);
   if (StringUtils.isNotBlank(configurationValue)) {
    return new FilePath((VirtualChannel) null, configurationValue);
   }
  } catch (ComponentConfigurationException exception) {
   // ignore and use fall back value
  }
  return getTargetPath(pom).child("**");
 }

 @Override
 public List<ViolationsProjectAction> getProjectActions(final MavenModule module) {
  return Collections.singletonList(new ViolationsProjectAction(module, getResultActionClass()));
 }

 @Override
 protected Class<ViolationsMavenResultAction> getResultActionClass() {
  return ViolationsMavenResultAction.class;
 }

 public List<ViolationConfig> getViolationConfigs() {
  return violationConfigs;
 }

 @Override
 public ParserResult perform(final MavenBuildProxy build, final MavenProject pom, final MojoInfo mojo,
   final PluginLogger logger) throws InterruptedException, IOException {
  FilesParser violationsCollector = new FilesParser(PLUGIN_NAME,
    new ViolationsParser(getDefaultEncoding(), violationConfigs), getModuleName(pom));

  return getFileName(mojo, pom).act(violationsCollector);
 }

 @DataBoundSetter
 public void setViolationConfigs(List<ViolationConfig> violationConfigs) {
  this.violationConfigs = ViolationConfigHelper.getViolationConfigs(violationConfigs);
 }
}

package org.jenkinsci.plugins.violations;

import java.util.List;
import java.util.Map;

import hudson.maven.AggregatableAction;
import hudson.maven.MavenAggregatedReport;
import hudson.maven.MavenBuild;
import hudson.maven.MavenModule;
import hudson.maven.MavenModuleSet;
import hudson.maven.MavenModuleSetBuild;
import hudson.model.AbstractBuild;
import hudson.model.Action;
import hudson.plugins.analysis.core.HealthDescriptor;
import hudson.plugins.analysis.core.ParserResult;

@Deprecated
public class MavenViolationsResultAction extends ViolationsResultAction
  implements AggregatableAction, MavenAggregatedReport {
 private final String defaultEncoding;

 public MavenViolationsResultAction(final AbstractBuild<?, ?> owner, final HealthDescriptor healthDescriptor,
   final String defaultEncoding) {
  super(owner, healthDescriptor, new ViolationsResult(owner, defaultEncoding, new ParserResult(), false, false));
  this.defaultEncoding = defaultEncoding;
 }

 public MavenViolationsResultAction(final AbstractBuild<?, ?> owner, final HealthDescriptor healthDescriptor,
   final String defaultEncoding, final ViolationsResult result) {
  super(owner, healthDescriptor, result);
  this.defaultEncoding = defaultEncoding;
 }

 @Override
 public MavenAggregatedReport createAggregatedAction(final MavenModuleSetBuild build,
   final Map<MavenModule, List<MavenBuild>> moduleBuilds) {
  return new MavenViolationsResultAction(build, getHealthDescriptor(), defaultEncoding);
 }

 @Override
 public Class<? extends AggregatableAction> getIndividualActionType() {
  return getClass();
 }

 @Override
 public Action getProjectAction(final MavenModuleSet moduleSet) {
  return new ViolationsProjectAction(moduleSet);
 }

 @Override
 public void update(final Map<MavenModule, List<MavenBuild>> moduleBuilds, final MavenBuild newBuild) {
  // not used anymore
 }
}
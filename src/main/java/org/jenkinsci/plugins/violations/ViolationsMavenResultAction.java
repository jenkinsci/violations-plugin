package org.jenkinsci.plugins.violations;

import java.util.List;
import java.util.Map;

import hudson.maven.MavenAggregatedReport;
import hudson.maven.MavenBuild;
import hudson.maven.MavenModule;
import hudson.maven.MavenModuleSet;
import hudson.maven.MavenModuleSetBuild;
import hudson.model.AbstractBuild;
import hudson.model.Action;
import hudson.plugins.analysis.core.HealthDescriptor;
import hudson.plugins.analysis.core.MavenResultAction;
import hudson.plugins.analysis.core.ParserResult;

public class ViolationsMavenResultAction extends MavenResultAction<ViolationsResult> {
 public ViolationsMavenResultAction(final AbstractBuild<?, ?> owner, final HealthDescriptor healthDescriptor,
   final String defaultEncoding, final ViolationsResult result) {
  super(new ViolationsResultAction(owner, healthDescriptor, result), defaultEncoding, "VIOLATIONS");
 }

 @Override
 public MavenAggregatedReport createAggregatedAction(final MavenModuleSetBuild build,
   final Map<MavenModule, List<MavenBuild>> moduleBuilds) {
  return new ViolationsMavenResultAction(build, getHealthDescriptor(), getDefaultEncoding(), new ViolationsResult(build,
    getDefaultEncoding(), new ParserResult(), usePreviousBuildAsStable(), useOnlyStableBuildsAsReference()));
 }

 @Override
 protected ViolationsResult createResult(final ViolationsResult existingResult,
   final ViolationsResult additionalResult) {
  return new ViolationsReporterResult(getOwner(), additionalResult.getDefaultEncoding(),
    aggregate(existingResult, additionalResult), existingResult.usePreviousBuildAsStable(),
    existingResult.useOnlyStableBuildsAsReference());
 }

 @Override
 public Class<? extends MavenResultAction<ViolationsResult>> getIndividualActionType() {
  return ViolationsMavenResultAction.class;
 }

 @Override
 public Action getProjectAction(final MavenModuleSet moduleSet) {
  return new ViolationsProjectAction(moduleSet, ViolationsMavenResultAction.class);
 }
}

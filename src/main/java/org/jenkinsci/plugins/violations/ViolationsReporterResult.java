package org.jenkinsci.plugins.violations;

import hudson.model.Run;
import hudson.plugins.analysis.core.BuildResult;
import hudson.plugins.analysis.core.ParserResult;
import hudson.plugins.analysis.core.ResultAction;

public class ViolationsReporterResult extends ViolationsResult {
 private static final long serialVersionUID = 6414012312137436141L;

 public ViolationsReporterResult(final Run<?, ?> build, final String defaultEncoding, final ParserResult result,
   final boolean usePreviousBuildAsReference, final boolean useStableBuildAsReference) {
  super(build, defaultEncoding, result, usePreviousBuildAsReference, useStableBuildAsReference,
    ViolationsMavenResultAction.class);
 }

 @Override
 protected Class<? extends ResultAction<? extends BuildResult>> getResultActionType() {
  return ViolationsMavenResultAction.class;
 }
}

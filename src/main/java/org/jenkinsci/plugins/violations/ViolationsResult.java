package org.jenkinsci.plugins.violations;

import org.jenkinsci.plugins.violations.parser.ViolationFileAnnotation;

import com.thoughtworks.xstream.XStream;

import hudson.model.Run;
import hudson.plugins.analysis.core.BuildHistory;
import hudson.plugins.analysis.core.BuildResult;
import hudson.plugins.analysis.core.ParserResult;
import hudson.plugins.analysis.core.ResultAction;

public class ViolationsResult extends BuildResult {
 private static final long serialVersionUID = 2768250056765266658L;

 ViolationsResult(final Run<?, ?> build, final BuildHistory history, final ParserResult result,
   final String defaultEncoding, final boolean canSerialize) {
  super(build, history, result, defaultEncoding);

  if (canSerialize) {
   serializeAnnotations(result.getAnnotations());
  }
 }

 public ViolationsResult(final Run<?, ?> build, final String defaultEncoding, final ParserResult result,
   final boolean usePreviousBuildAsReference, final boolean useStableBuildAsReference) {
  this(build, defaultEncoding, result, usePreviousBuildAsReference, useStableBuildAsReference,
    ViolationsResultAction.class);
 }

 protected ViolationsResult(final Run<?, ?> build, final String defaultEncoding, final ParserResult result,
   final boolean usePreviousBuildAsReference, final boolean useStableBuildAsReference,
   final Class<? extends ResultAction<ViolationsResult>> actionType) {
  this(build, new BuildHistory(build, actionType, usePreviousBuildAsReference, useStableBuildAsReference), result,
    defaultEncoding, true);
 }

 @Override
 protected void configure(final XStream xstream) {
  xstream.alias("warning", ViolationFileAnnotation.class);
 }

 @Override
 protected String createDeltaMessage() {
  return createDefaultDeltaMessage(ViolationsDescriptor.RESULT_URL, getNumberOfNewWarnings(),
    getNumberOfFixedWarnings());
 }

 @Override
 public String getDisplayName() {
  return Messages.Violations_ProjectAction_Name();
 }

 @Override
 public String getHeader() {
  return Messages.Violations_ResultAction_Header();
 }

 @Override
 protected Class<? extends ResultAction<? extends BuildResult>> getResultActionType() {
  return ViolationsResultAction.class;
 }

 @Override
 protected String getSerializationFileName() {
  return "violations-warnings.xml";
 }

 @Override
 public String getSummary() {
  return "Violations: "
    + createDefaultSummary(ViolationsDescriptor.RESULT_URL, getNumberOfAnnotations(), getNumberOfModules());
 }
}

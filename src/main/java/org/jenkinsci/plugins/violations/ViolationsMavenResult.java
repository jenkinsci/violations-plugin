package org.jenkinsci.plugins.violations;

import hudson.model.AbstractBuild;
import hudson.plugins.analysis.core.BuildResult;
import hudson.plugins.analysis.core.ParserResult;
import hudson.plugins.analysis.core.ResultAction;

@Deprecated
public class ViolationsMavenResult extends ViolationsResult {
  private static final long serialVersionUID = -4913938782537266259L;

  public ViolationsMavenResult(
      final AbstractBuild<?, ?> build, final String defaultEncoding, final ParserResult result) {
    super(build, defaultEncoding, result, false, false, MavenViolationsResultAction.class);
  }

  @Override
  protected Class<? extends ResultAction<? extends BuildResult>> getResultActionType() {
    return MavenViolationsResultAction.class;
  }
}

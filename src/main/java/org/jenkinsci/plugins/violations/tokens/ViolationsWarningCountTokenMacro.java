package org.jenkinsci.plugins.violations.tokens;

import org.jenkinsci.plugins.violations.ViolationsMavenResultAction;
import org.jenkinsci.plugins.violations.ViolationsResultAction;

import hudson.Extension;
import hudson.plugins.analysis.tokens.AbstractAnnotationsCountTokenMacro;

@Extension(optional = true)
public class ViolationsWarningCountTokenMacro extends AbstractAnnotationsCountTokenMacro {
  @SuppressWarnings("unchecked")
  public ViolationsWarningCountTokenMacro() {
    super("VIOLATIONS_COUNT", ViolationsResultAction.class, ViolationsMavenResultAction.class);
  }
}

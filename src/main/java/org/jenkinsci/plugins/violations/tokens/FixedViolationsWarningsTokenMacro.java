package org.jenkinsci.plugins.violations.tokens;

import org.jenkinsci.plugins.violations.ViolationsMavenResultAction;
import org.jenkinsci.plugins.violations.ViolationsResultAction;

import hudson.Extension;
import hudson.plugins.analysis.tokens.AbstractFixedAnnotationsTokenMacro;

@Extension(optional = true)
public class FixedViolationsWarningsTokenMacro extends AbstractFixedAnnotationsTokenMacro {
  @SuppressWarnings("unchecked")
  public FixedViolationsWarningsTokenMacro() {
    super("VIOLATIONS_FIXED", ViolationsResultAction.class, ViolationsMavenResultAction.class);
  }
}

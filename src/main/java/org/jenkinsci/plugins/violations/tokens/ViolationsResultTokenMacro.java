package org.jenkinsci.plugins.violations.tokens;

import org.jenkinsci.plugins.violations.ViolationsMavenResultAction;
import org.jenkinsci.plugins.violations.ViolationsResultAction;

import hudson.Extension;
import hudson.plugins.analysis.tokens.AbstractResultTokenMacro;

@Extension(optional = true)
public class ViolationsResultTokenMacro extends AbstractResultTokenMacro {
  @SuppressWarnings("unchecked")
  public ViolationsResultTokenMacro() {
    super("VIOLATIONS_RESULT", ViolationsResultAction.class, ViolationsMavenResultAction.class);
  }
}

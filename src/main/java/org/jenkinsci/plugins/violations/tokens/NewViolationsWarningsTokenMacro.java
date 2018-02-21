package org.jenkinsci.plugins.violations.tokens;

import org.jenkinsci.plugins.violations.ViolationsMavenResultAction;
import org.jenkinsci.plugins.violations.ViolationsResultAction;

import hudson.Extension;
import hudson.plugins.analysis.tokens.AbstractNewAnnotationsTokenMacro;

@Extension(optional = true)
public class NewViolationsWarningsTokenMacro extends AbstractNewAnnotationsTokenMacro {
  @SuppressWarnings("unchecked")
  public NewViolationsWarningsTokenMacro() {
    super("VIOLATIONS_NEW", ViolationsResultAction.class, ViolationsMavenResultAction.class);
  }
}

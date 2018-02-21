package org.jenkinsci.plugins.violations.parser;

import hudson.plugins.analysis.util.model.AbstractAnnotation;
import hudson.plugins.analysis.util.model.Priority;
import se.bjurr.violations.lib.model.SEVERITY;
import se.bjurr.violations.lib.model.Violation;

public class ViolationFileAnnotation extends AbstractAnnotation {
  private static final long serialVersionUID = -130544179540031099L;

  private static Priority toPriority(SEVERITY severity) {
    if (severity == SEVERITY.ERROR) {
      return Priority.HIGH;
    }
    if (severity == SEVERITY.WARN) {
      return Priority.NORMAL;
    }
    return Priority.LOW;
  }

  private final Violation violation;

  public ViolationFileAnnotation(final Violation violation, String moduleName) {
    super(
        toPriority(violation.getSeverity()),
        violation.getMessage(),
        violation.getStartLine(),
        violation.getEndLine(),
        violation.getReporter(),
        violation.getSource().orNull());
    this.setOrigin("violations");
    this.violation = violation;
    setFileName(violation.getFile());
    setModuleName(moduleName);
    if (violation.getColumn().isPresent()) {
      setColumnPosition(violation.getColumn().get());
    }
  }

  @Override
  public String getMessage() {
    return violation.getMessage();
  }

  @Override
  public String getPathName() {
    return violation.getFile();
  }

  @Override
  public String getShortFileName() {
    return violation.getFile();
  }

  @Override
  public String getToolTip() {
    return violation.getMessage();
  }
}

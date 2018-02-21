package org.jenkinsci.plugins.violations;

import org.jvnet.localizer.Localizable;

import hudson.plugins.analysis.core.AbstractHealthDescriptor;
import hudson.plugins.analysis.core.HealthDescriptor;
import hudson.plugins.analysis.util.model.AnnotationProvider;

public class ViolationsHealthDescriptor extends AbstractHealthDescriptor {
  private static final long serialVersionUID = -3404826986876607396L;

  public ViolationsHealthDescriptor(final HealthDescriptor healthDescriptor) {
    super(healthDescriptor);
  }

  @Override
  protected Localizable createDescription(final AnnotationProvider result) {
    if (result.getNumberOfAnnotations() == 0) {
      return Messages._Violations_ResultAction_HealthReportNoItem();
    } else if (result.getNumberOfAnnotations() == 1) {
      return Messages._Violations_ResultAction_HealthReportSingleItem();
    } else {
      return Messages._Violations_ResultAction_HealthReportMultipleItem(
          result.getNumberOfAnnotations());
    }
  }
}

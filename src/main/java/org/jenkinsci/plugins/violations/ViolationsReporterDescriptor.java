package org.jenkinsci.plugins.violations;

import java.util.List;

import hudson.Extension;
import hudson.plugins.analysis.core.ReporterDescriptor;

@Extension(ordinal = 100, optional = true) // NOVIOLATIONS
public class ViolationsReporterDescriptor extends ReporterDescriptor {
  public ViolationsReporterDescriptor() {
    super(ViolationsReporter.class, new ViolationsDescriptor());
  }

  public List<ViolationConfig> getAllViolationConfigs() {
    return ViolationConfigHelper.getAllViolationConfigs();
  }
}

package org.jenkinsci.plugins.violations;

import hudson.model.Run;
import hudson.plugins.analysis.core.AbstractResultAction;
import hudson.plugins.analysis.core.HealthDescriptor;
import hudson.plugins.analysis.core.PluginDescriptor;

public class ViolationsResultAction extends AbstractResultAction<ViolationsResult> {
  public ViolationsResultAction(
      final Run<?, ?> owner,
      final HealthDescriptor healthDescriptor,
      final ViolationsResult result) {
    super(owner, new ViolationsHealthDescriptor(healthDescriptor), result);
  }

  @Override
  protected PluginDescriptor getDescriptor() {
    return new ViolationsDescriptor();
  }

  @Override
  public String getDisplayName() {
    return Messages.Violations_ProjectAction_Name();
  }
}

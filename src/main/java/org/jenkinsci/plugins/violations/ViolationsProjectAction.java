package org.jenkinsci.plugins.violations;

import hudson.model.AbstractProject;
import hudson.plugins.analysis.core.AbstractProjectAction;
import hudson.plugins.analysis.core.ResultAction;

public class ViolationsProjectAction extends AbstractProjectAction<ResultAction<ViolationsResult>> {
 public ViolationsProjectAction(final AbstractProject<?, ?> project) {
  this(project, ViolationsResultAction.class);
 }

 public ViolationsProjectAction(final AbstractProject<?, ?> project,
   final Class<? extends ResultAction<ViolationsResult>> type) {
  super(project, type, Messages._Violations_ProjectAction_Name(), Messages._Violations_Trend_Name(),
    ViolationsDescriptor.PLUGIN_ID, ViolationsDescriptor.ICON_URL, ViolationsDescriptor.RESULT_URL);
 }
}

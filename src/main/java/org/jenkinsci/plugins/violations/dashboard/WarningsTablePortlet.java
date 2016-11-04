package org.jenkinsci.plugins.violations.dashboard;

import org.jenkinsci.plugins.violations.ViolationsProjectAction;
import org.jenkinsci.plugins.violations.Messages;
import org.kohsuke.stapler.DataBoundConstructor;

import hudson.Extension;
import hudson.model.Descriptor;
import hudson.plugins.analysis.core.AbstractProjectAction;
import hudson.plugins.analysis.dashboard.AbstractWarningsTablePortlet;
import hudson.plugins.view.dashboard.DashboardPortlet;

public class WarningsTablePortlet extends AbstractWarningsTablePortlet {
 @Extension(optional = true)
 public static class WarningsPerJobDescriptor extends Descriptor<DashboardPortlet> {
  @Override
  public String getDisplayName() {
   return Messages.Portlet_WarningsTable();
  }
 }

 @DataBoundConstructor
 public WarningsTablePortlet(final String name, final boolean canHideZeroWarningsProjects) {
  super(name, canHideZeroWarningsProjects);
 }

 @Override
 protected Class<? extends AbstractProjectAction<?>> getAction() {
  return ViolationsProjectAction.class;
 }
}

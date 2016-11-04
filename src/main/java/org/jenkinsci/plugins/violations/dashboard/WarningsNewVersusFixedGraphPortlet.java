package org.jenkinsci.plugins.violations.dashboard;

import org.jenkinsci.plugins.violations.ViolationsProjectAction;
import org.jenkinsci.plugins.violations.Messages;
import org.kohsuke.stapler.DataBoundConstructor;

import hudson.Extension;
import hudson.model.Descriptor;
import hudson.plugins.analysis.core.AbstractProjectAction;
import hudson.plugins.analysis.dashboard.AbstractWarningsGraphPortlet;
import hudson.plugins.analysis.graph.BuildResultGraph;
import hudson.plugins.analysis.graph.NewVersusFixedGraph;
import hudson.plugins.view.dashboard.DashboardPortlet;

public final class WarningsNewVersusFixedGraphPortlet extends AbstractWarningsGraphPortlet {
 @Extension(optional = true)
 public static class WarningsGraphDescriptor extends Descriptor<DashboardPortlet> {
  @Override
  public String getDisplayName() {
   return Messages.Portlet_WarningsNewVsFixedGraph();
  }
 }

 @DataBoundConstructor
 public WarningsNewVersusFixedGraphPortlet(final String name, final String width, final String height,
   final String dayCountString) {
  super(name, width, height, dayCountString);

  configureGraph(getGraphType());
 }

 @Override
 protected Class<? extends AbstractProjectAction<?>> getAction() {
  return ViolationsProjectAction.class;
 }

 @Override
 protected BuildResultGraph getGraphType() {
  return new NewVersusFixedGraph();
 }

 @Override
 protected String getPluginName() {
  return "violations";
 }
}

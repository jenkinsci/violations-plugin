package org.jenkinsci.plugins.violations;

import org.kohsuke.stapler.DataBoundConstructor;

import hudson.Extension;
import hudson.plugins.analysis.views.WarningsCountColumn;
import hudson.views.ListViewColumnDescriptor;

public class ViolationsColumn extends WarningsCountColumn<ViolationsProjectAction> {
  @Extension
  public static class ColumnDescriptor extends ListViewColumnDescriptor {
    @Override
    public String getDisplayName() {
      return Messages.Violations_Warnings_Column();
    }

    @Override
    public boolean shownByDefault() {
      return false;
    }
  }

  @DataBoundConstructor
  public ViolationsColumn() { // NOPMD: data binding
    super();
  }

  @Override
  public String getColumnCaption() {
    return Messages.Violations_Warnings_ColumnHeader();
  }

  @Override
  protected Class<ViolationsProjectAction> getProjectAction() {
    return ViolationsProjectAction.class;
  }
}

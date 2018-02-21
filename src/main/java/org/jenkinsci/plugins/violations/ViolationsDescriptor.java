package org.jenkinsci.plugins.violations;

import java.util.List;

import hudson.Extension;
import hudson.plugins.analysis.core.PluginDescriptor;

@Extension(ordinal = 100)
public final class ViolationsDescriptor extends PluginDescriptor {
  static final String ICON_URL = "/plugin/violations/icons/violations-24x24.png";
  static final String ICON_URL_PREFIX = "/plugin/violations/icons/";
  static final String PLUGIN_ID = "violations";
  static final String RESULT_URL = PluginDescriptor.createResultUrlName(PLUGIN_ID);

  public ViolationsDescriptor() {
    super(ViolationsPublisher.class);
  }

  public List<ViolationConfig> getAllViolationConfigs() {
    return ViolationConfigHelper.getAllViolationConfigs();
  }

  @Override
  public String getDisplayName() {
    return Messages.Violations_Publisher_Name();
  }

  @Override
  public String getIconUrl() {
    return ICON_URL;
  }

  @Override
  public String getPluginName() {
    return PLUGIN_ID;
  }

  @Override
  public String getSummaryIconUrl() {
    return ICON_URL_PREFIX + "violations-48x48.png";
  }
}

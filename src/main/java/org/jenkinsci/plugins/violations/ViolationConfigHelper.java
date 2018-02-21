package org.jenkinsci.plugins.violations;

import static com.google.common.collect.Lists.newArrayList;
import hudson.EnvVars;

import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import se.bjurr.violations.lib.reports.Parser;

public class ViolationConfigHelper {

  public static void expandPatterns(EnvVars environment, List<ViolationConfig> violationConfigs) {
    for (final ViolationConfig vc : violationConfigs) {
      vc.setPattern(environment.expand(vc.getPattern()));
    }
  }

  public static List<ViolationConfig> getAllViolationConfigs() {
    return getViolationConfigs(null);
  }

  private static List<ViolationConfig> getValidatedViolationConfigs(
      List<ViolationConfig> violationConfigs) {
    if (violationConfigs == null) {
      return newArrayList();
    }
    for (final ViolationConfig vc : violationConfigs) {
      final String pattern = vc.getPattern();
      try {
        Pattern.compile(pattern);
      } catch (final Exception e) {
        throw new RuntimeException(
            "Pattern \"" + pattern + "\" for " + vc.getReporter() + " is not valid regexp!", e);
      }
    }
    Collections.sort(violationConfigs);
    return violationConfigs;
  }

  public static List<ViolationConfig> getViolationConfigs(
      List<String> patterns, List<String> reporters) {
    final List<ViolationConfig> violationConfigs = newArrayList();
    if (patterns == null || reporters == null) {
      return getViolationConfigs(violationConfigs);
    }
    for (int i = 0; i < reporters.size(); i++) {
      final String reporterString = reporters.get(i);
      final Parser reporter = Parser.valueOf(reporterString);
      violationConfigs.add(new ViolationConfig(reporter, patterns.get(i), reporter.name()));
    }
    return getViolationConfigs(violationConfigs);
  }

  public static List<ViolationConfig> getViolationConfigs(List<ViolationConfig> violationConfigs) {
    if (violationConfigs == null) {
      violationConfigs = newArrayList();
    }
    final List<Parser> toAdd = newArrayList(Parser.values());
    for (final Parser reporter : Parser.values()) {
      for (final ViolationConfig vc : violationConfigs) {
        if (vc.getParser() == reporter) {
          toAdd.remove(reporter);
        }
      }
    }
    for (final Parser reporter : toAdd) {
      violationConfigs.add(new ViolationConfig(reporter, "", reporter.name()));
    }
    return getValidatedViolationConfigs(violationConfigs);
  }
}

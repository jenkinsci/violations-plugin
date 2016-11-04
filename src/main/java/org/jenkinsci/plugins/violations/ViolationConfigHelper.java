package org.jenkinsci.plugins.violations;

import static com.google.common.collect.Lists.newArrayList;

import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import hudson.EnvVars;
import se.bjurr.violations.lib.reports.Reporter;

public class ViolationConfigHelper {

 public static void expandPatterns(EnvVars environment, List<ViolationConfig> violationConfigs) {
  for (ViolationConfig vc : violationConfigs) {
   vc.setPattern(environment.expand(vc.getPattern()));
  }
 }

 public static List<ViolationConfig> getAllViolationConfigs() {
  return getViolationConfigs(null);
 }

 private static List<ViolationConfig> getValidatedViolationConfigs(List<ViolationConfig> violationConfigs) {
  if (violationConfigs == null) {
   return newArrayList();
  }
  for (ViolationConfig vc : violationConfigs) {
   String pattern = vc.getPattern();
   try {
    Pattern.compile(pattern);
   } catch (Exception e) {
    throw new RuntimeException("Pattern \"" + pattern + "\" for " + vc.getReporter() + " is not valid regexp!", e);
   }
  }
  Collections.sort(violationConfigs);
  return violationConfigs;
 }

 public static List<ViolationConfig> getViolationConfigs(List<String> patterns, List<String> reporters) {
  List<ViolationConfig> violationConfigs = newArrayList();
  if (patterns == null || reporters == null) {
   return getViolationConfigs(violationConfigs);
  }
  for (int i = 0; i < reporters.size(); i++) {
   String reporterString = reporters.get(i);
   Reporter reporter = Reporter.valueOf(reporterString);
   violationConfigs.add(new ViolationConfig(reporter, patterns.get(i)));
  }
  return getViolationConfigs(violationConfigs);
 }

 public static List<ViolationConfig> getViolationConfigs(List<ViolationConfig> violationConfigs) {
  if (violationConfigs == null) {
   violationConfigs = newArrayList();
  }
  List<Reporter> toAdd = newArrayList(Reporter.values());
  for (Reporter reporter : Reporter.values()) {
   for (ViolationConfig vc : violationConfigs) {
    if (vc.getReporter() == reporter) {
     toAdd.remove(reporter);
    }
   }
  }
  for (Reporter reporter : toAdd) {
   violationConfigs.add(new ViolationConfig(reporter, ""));
  }
  return getValidatedViolationConfigs(violationConfigs);
 }
}

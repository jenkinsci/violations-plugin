package org.jenkinsci.plugins.violations;

import static com.google.common.collect.Lists.newArrayList;
import static org.assertj.core.api.Assertions.assertThat;
import static se.bjurr.violations.lib.reports.Reporter.FINDBUGS;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import se.bjurr.violations.lib.reports.Reporter;

public class ViolationsReporterTest {
 private ViolationsReporter sut;

 @Before
 public void before() {
  List<String> patterns = null;
  List<String> reporters = null;
  sut = createViolationsReporter(patterns, reporters);
 }

 private ViolationsReporter createViolationsReporter(List<String> patterns, List<String> reporters) {
  String healthy = null;
  String unHealthy = null;
  String thresholdLimit = null;
  boolean useDeltaValues = false;
  String unstableTotalAll = null;
  String unstableTotalHigh = null;
  String unstableTotalNormal = null;
  String unstableTotalLow = null;
  String unstableNewAll = null;
  String unstableNewHigh = null;
  String unstableNewNormal = null;
  String unstableNewLow = null;
  String failedTotalAll = null;
  String failedTotalHigh = null;
  String failedTotalNormal = null;
  String failedTotalLow = null;
  String failedNewAll = null;
  String failedNewHigh = null;
  String failedNewNormal = null;
  String failedNewLow = null;
  boolean canRunOnFailed = false;
  boolean usePreviousBuildAsReference = false;
  boolean useStableBuildAsReference = false;
  boolean canComputeNew = false;
  return new ViolationsReporter(healthy, unHealthy, thresholdLimit, useDeltaValues, unstableTotalAll, unstableTotalHigh,
    unstableTotalNormal, unstableTotalLow, unstableNewAll, unstableNewHigh, unstableNewNormal, unstableNewLow,
    failedTotalAll, failedTotalHigh, failedTotalNormal, failedTotalLow, failedNewAll, failedNewHigh, failedNewNormal,
    failedNewLow, canRunOnFailed, usePreviousBuildAsReference, useStableBuildAsReference, canComputeNew, patterns,
    reporters, null);
 }

 private ViolationConfig findConfig(Reporter find) {
  for (ViolationConfig vc : sut.getAllViolationConfigs()) {
   if (vc.getReporter() == find) {
    return vc;
   }
  }
  return null;
 }

 @Test
 public void testGetAllViolationsFirstTime() {
  List<ViolationConfig> actual = sut.getAllViolationConfigs();

  assertThat(actual)//
    .hasSize(Reporter.values().length);
 }

 @Test
 public void testGetAllViolationsWithOnePreviouslyConfigured() {
  List<ViolationConfig> violationConfigs = newArrayList();
  violationConfigs.add(new ViolationConfig(FINDBUGS, ".*"));
  sut.setViolationConfigs(violationConfigs);

  List<ViolationConfig> actual = sut.getAllViolationConfigs();

  assertThat(actual)//
    .hasSize(Reporter.values().length);
  assertThat(findConfig(FINDBUGS).getPattern())//
    .isEqualTo(".*");
 }

 @Test
 public void testSaveReporter() {
  List<String> patterns = newArrayList(".+?");
  List<String> reporters = newArrayList(FINDBUGS.name());
  sut = createViolationsReporter(patterns, reporters);

  List<ViolationConfig> actual = sut.getAllViolationConfigs();

  assertThat(actual)//
    .hasSize(Reporter.values().length);
  assertThat(findConfig(FINDBUGS).getPattern())//
    .isEqualTo(".+?");
 }

 @Test(expected = RuntimeException.class)
 public void testSaveReporterIllegalRegexp() {
  List<String> patterns = newArrayList(".++?");
  List<String> reporters = newArrayList(FINDBUGS.name());
  sut = createViolationsReporter(patterns, reporters);
 }
}

package org.jenkinsci.plugins.violations;

import static com.google.common.collect.Lists.newArrayList;
import static org.assertj.core.api.Assertions.assertThat;
import static se.bjurr.violations.lib.reports.Parser.FINDBUGS;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import se.bjurr.violations.lib.reports.Parser;

public class ViolationsReporterTest {
  private ViolationsReporter sut;

  @Before
  public void before() {
    final List<String> patterns = null;
    final List<String> reporters = null;
    sut = createViolationsReporter(patterns, reporters);
  }

  private ViolationsReporter createViolationsReporter(
      List<String> patterns, List<String> reporters) {
    final String healthy = null;
    final String unHealthy = null;
    final String thresholdLimit = null;
    final boolean useDeltaValues = false;
    final String unstableTotalAll = null;
    final String unstableTotalHigh = null;
    final String unstableTotalNormal = null;
    final String unstableTotalLow = null;
    final String unstableNewAll = null;
    final String unstableNewHigh = null;
    final String unstableNewNormal = null;
    final String unstableNewLow = null;
    final String failedTotalAll = null;
    final String failedTotalHigh = null;
    final String failedTotalNormal = null;
    final String failedTotalLow = null;
    final String failedNewAll = null;
    final String failedNewHigh = null;
    final String failedNewNormal = null;
    final String failedNewLow = null;
    final boolean canRunOnFailed = false;
    final boolean usePreviousBuildAsReference = false;
    final boolean useStableBuildAsReference = false;
    final boolean canComputeNew = false;
    return new ViolationsReporter(
        healthy,
        unHealthy,
        thresholdLimit,
        useDeltaValues,
        unstableTotalAll,
        unstableTotalHigh,
        unstableTotalNormal,
        unstableTotalLow,
        unstableNewAll,
        unstableNewHigh,
        unstableNewNormal,
        unstableNewLow,
        failedTotalAll,
        failedTotalHigh,
        failedTotalNormal,
        failedTotalLow,
        failedNewAll,
        failedNewHigh,
        failedNewNormal,
        failedNewLow,
        canRunOnFailed,
        usePreviousBuildAsReference,
        useStableBuildAsReference,
        canComputeNew,
        patterns,
        reporters,
        null);
  }

  private ViolationConfig findConfig(Parser find) {
    for (final ViolationConfig vc : sut.getAllViolationConfigs()) {
      if (vc.getParser() == find) {
        return vc;
      }
    }
    return null;
  }

  @Test
  public void testGetAllViolationsFirstTime() {
    final List<ViolationConfig> actual = sut.getAllViolationConfigs();

    assertThat(actual) //
        .hasSize(Parser.values().length);
  }

  @Test
  public void testGetAllViolationsWithOnePreviouslyConfigured() {
    final List<ViolationConfig> violationConfigs = newArrayList();
    violationConfigs.add(new ViolationConfig(FINDBUGS, ".*", FINDBUGS.name()));
    sut.setViolationConfigs(violationConfigs);

    final List<ViolationConfig> actual = sut.getAllViolationConfigs();

    assertThat(actual) //
        .hasSize(Parser.values().length);
    assertThat(findConfig(FINDBUGS).getPattern()) //
        .isEqualTo(".*");
  }

  @Test
  public void testSaveReporter() {
    final List<String> patterns = newArrayList(".+?");
    final List<String> reporters = newArrayList(FINDBUGS.name());
    sut = createViolationsReporter(patterns, reporters);

    final List<ViolationConfig> actual = sut.getAllViolationConfigs();

    assertThat(actual) //
        .hasSize(Parser.values().length);
    assertThat(findConfig(FINDBUGS).getPattern()) //
        .isEqualTo(".+?");
  }

  @Test(expected = RuntimeException.class)
  public void testSaveReporterIllegalRegexp() {
    final List<String> patterns = newArrayList(".++?");
    final List<String> reporters = newArrayList(FINDBUGS.name());
    sut = createViolationsReporter(patterns, reporters);
  }
}

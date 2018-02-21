package org.jenkinsci.plugins.violations.parser;

import static com.google.common.base.Charsets.UTF_8;
import static com.google.common.collect.Lists.newArrayList;
import static org.assertj.core.api.Assertions.assertThat;
import static se.bjurr.violations.lib.reports.Parser.FINDBUGS;
import hudson.plugins.analysis.util.model.FileAnnotation;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;
import java.util.List;

import org.jenkinsci.plugins.violations.ViolationConfig;
import org.junit.Test;

import com.google.common.io.Resources;

public class ViolationsParserTest {
  private final String moduleName = "moduleName";

  private File findbugsReportFile() throws URISyntaxException {
    final URL uri = Resources.getResource("findbugs-report.xml");
    final File file = new File(uri.toURI());
    return file;
  }

  @Test(expected = java.lang.RuntimeException.class)
  public void testParseCreateWithIllegalRegexp() throws Exception {
    final List<ViolationConfig> violationConfigs =
        newArrayList( //
            new ViolationConfig(FINDBUGS, "**", FINDBUGS.name()));
    final ViolationsParser sut = new ViolationsParser(UTF_8.name(), violationConfigs);
    final File file = findbugsReportFile();

    final Collection<FileAnnotation> actual = sut.parse(file, moduleName);

    assertThat(actual) //
        .isEmpty();
  }

  @Test
  public void testParseFileWithFindBugs() throws Exception {
    final List<ViolationConfig> violationConfigs =
        newArrayList( //
            new ViolationConfig(FINDBUGS, ".*", FINDBUGS.name()));
    final ViolationsParser sut = new ViolationsParser(UTF_8.name(), violationConfigs);
    final File file = findbugsReportFile();

    final Collection<FileAnnotation> actual = sut.parse(file, moduleName);

    assertThat(actual) //
        .hasSize(5);
  }

  @Test
  public void testParseFileWithoutAnyMatchingReporter() throws Exception {
    final ViolationsParser sut = new ViolationsParser();
    final File file = findbugsReportFile();

    final Collection<FileAnnotation> actual = sut.parse(file, moduleName);

    assertThat(actual) //
        .isEmpty();
  }
}

package org.jenkinsci.plugins.violations.parser;

import static com.google.common.base.Charsets.UTF_8;
import static com.google.common.base.Optional.absent;
import static com.google.common.base.Throwables.propagate;
import static com.google.common.collect.Lists.newArrayList;
import static java.util.logging.Level.SEVERE;
import static java.util.regex.Pattern.matches;
import static org.apache.commons.lang.StringUtils.EMPTY;
import static se.bjurr.violations.lib.parsers.FindbugsParser.setFindbugsMessagesXml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.lang.NotImplementedException;
import org.jenkinsci.plugins.violations.ViolationConfig;
import org.jenkinsci.plugins.violations.ViolationConfigHelper;

import com.google.common.base.Optional;
import com.google.common.io.CharStreams;
import com.google.common.io.Files;

import hudson.plugins.analysis.core.AbstractAnnotationParser;
import hudson.plugins.analysis.util.model.FileAnnotation;
import se.bjurr.violations.lib.model.Violation;
import se.bjurr.violations.lib.reports.Reporter;

public class ViolationsParser extends AbstractAnnotationParser {
 private static Logger LOG = Logger.getLogger(ViolationsParser.class.getName());
 private static final long serialVersionUID = 2373561770223062283L;

 private static void setupFindBugsMessages() {
  try {
   final String findbugsMessagesXml = CharStreams
     .toString(new InputStreamReader(ViolationsParser.class.getResourceAsStream("findbugs-messages.xml"), UTF_8));
   setFindbugsMessagesXml(findbugsMessagesXml);
  } catch (final IOException e) {
   propagate(e);
  }
 }

 private final List<ViolationConfig> violationConfigs;

 public ViolationsParser() {
  super(EMPTY);
  violationConfigs = newArrayList();
 }

 public ViolationsParser(final String defaultEncoding, List<ViolationConfig> violationConfigs) {
  super(defaultEncoding);
  this.violationConfigs = ViolationConfigHelper.getViolationConfigs(violationConfigs);
  setupFindBugsMessages();
 }

 private Optional<Reporter> findReporter(List<ViolationConfig> violationConfigs, File file) {
  for (ViolationConfig candidateReporter : violationConfigs) {
   String candidatePattern = candidateReporter.getPattern();
   if (matches(candidatePattern, file.getAbsolutePath())) {
    return Optional.of(candidateReporter.getReporter());
   }
  }
  return absent();
 }

 @Override
 public Collection<FileAnnotation> parse(File file, String moduleName) throws InvocationTargetException {
  try {
   Optional<Reporter> reporter = findReporter(violationConfigs, file);
   if (!reporter.isPresent()) {
    return newArrayList();
   }
   List<Violation> violations = parseFile(file, reporter.get());
   List<FileAnnotation> fileAnnotations = toFileAnnotations(violations, moduleName);
   return intern(fileAnnotations);
  } catch (Exception e) {
   LOG.log(SEVERE, e.getMessage(), e);
   return newArrayList();
  }
 }

 @Override
 public Collection<FileAnnotation> parse(final InputStream inputStream, final String moduleName)
   throws InvocationTargetException {
  throw new NotImplementedException("parse(file,moduleName) is overridden, so this should never be invoked.");
 }

 private List<Violation> parseFile(File file, Reporter reporter) throws IOException, Exception {
  String fileContent = Files.toString(file, UTF_8);
  return reporter.getViolationsParser().parseFile(fileContent);
 }

 private List<FileAnnotation> toFileAnnotations(List<Violation> violations, String moduleName) {
  List<FileAnnotation> fileAnnotations = newArrayList();
  for (Violation violation : violations) {
   FileAnnotation fileAnnotation = new ViolationFileAnnotation(violation, moduleName);
   fileAnnotations.add(fileAnnotation);
  }
  return fileAnnotations;
 }

}

package org.jenkinsci.plugins.violations;

import hudson.Extension;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;
import hudson.util.ListBoxModel;

import java.io.Serializable;

import javax.annotation.Nonnull;

import org.kohsuke.accmod.Restricted;
import org.kohsuke.accmod.restrictions.NoExternalUse;
import org.kohsuke.stapler.DataBoundConstructor;

import se.bjurr.violations.lib.reports.Parser;

public class ViolationConfig extends AbstractDescribableImpl<ViolationConfig>
    implements Serializable, Comparable<ViolationConfig> {
  private static final long serialVersionUID = 9009372864417543781L;

  private String pattern;
  private Parser parser;
  private String reporter;

  public ViolationConfig() {}

  @DataBoundConstructor
  public ViolationConfig(final Parser parser, final String pattern, final String reporter) {
    this.parser = parser;
    this.pattern = pattern;
    this.reporter = reporter;
  }

  public String getPattern() {
    return this.pattern;
  }

  public Parser getParser() {
    return this.parser;
  }

  public String getReporter() {
    if (this.reporter == null) {
      return this.parser.name();
    }
    return reporter;
  }

  public void setPattern(final String pattern) {
    this.pattern = pattern;
  }

  public void setParser(final Parser parser) {
    this.parser = parser;
  }

  public void setReporter(final String reporter) {
    this.reporter = reporter;
  }

  @Override
  public String toString() {
    return "ViolationConfig [pattern="
        + pattern
        + ", parser="
        + parser
        + ", reporter="
        + reporter
        + "]";
  }

  @Extension
  public static class DescriptorImpl extends Descriptor<ViolationConfig> {
    @Nonnull
    @Override
    public String getDisplayName() {
      return "Violations Parser Config";
    }

    @Restricted(NoExternalUse.class)
    public ListBoxModel doFillParserItems() {
      final ListBoxModel items = new ListBoxModel();
      for (final Parser parser : Parser.values()) {
        items.add(parser.name());
      }
      return items;
    }
  }

  @Override
  public int compareTo(ViolationConfig o) {
    return this.parser.name().compareTo(o.parser.name());
  }
}

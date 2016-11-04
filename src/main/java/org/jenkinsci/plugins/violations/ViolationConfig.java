package org.jenkinsci.plugins.violations;

import java.io.Serializable;

import org.kohsuke.stapler.DataBoundConstructor;

import se.bjurr.violations.lib.reports.Reporter;

public class ViolationConfig implements Serializable, Comparable<ViolationConfig> {
 private static final long serialVersionUID = 6664329842273455651L;

 private String pattern;

 private Reporter reporter;

 public ViolationConfig() {

 }

 @DataBoundConstructor
 public ViolationConfig(Reporter reporter, String pattern) {
  this.reporter = reporter;
  this.pattern = pattern;
 }

 @Override
 public int compareTo(ViolationConfig o) {
  return reporter.name().compareTo(o.reporter.name());
 }

 @Override
 public boolean equals(Object obj) {
  if (this == obj) {
   return true;
  }
  if (obj == null) {
   return false;
  }
  if (getClass() != obj.getClass()) {
   return false;
  }
  ViolationConfig other = (ViolationConfig) obj;
  if (pattern == null) {
   if (other.pattern != null) {
    return false;
   }
  } else if (!pattern.equals(other.pattern)) {
   return false;
  }
  if (reporter != other.reporter) {
   return false;
  }
  return true;
 }

 public String getPattern() {
  return pattern;
 }

 public Reporter getReporter() {
  return reporter;
 }

 @Override
 public int hashCode() {
  final int prime = 31;
  int result = 1;
  result = prime * result + (pattern == null ? 0 : pattern.hashCode());
  result = prime * result + (reporter == null ? 0 : reporter.hashCode());
  return result;
 }

 public void setPattern(String pattern) {
  this.pattern = pattern;
 }

 public void setReporter(Reporter reporter) {
  this.reporter = reporter;
 }

 @Override
 public String toString() {
  return "ViolationConfig [reporter=" + reporter + ", pattern=" + pattern + "]";
 }
}

package org.jenkinsci.plugins.violations;

import hudson.Launcher;
import hudson.matrix.MatrixBuild;
import hudson.matrix.MatrixRun;
import hudson.model.Action;
import hudson.model.BuildListener;
import hudson.plugins.analysis.core.AnnotationsAggregator;
import hudson.plugins.analysis.core.HealthDescriptor;
import hudson.plugins.analysis.core.ParserResult;

public class ViolationsAnnotationsAggregator extends AnnotationsAggregator {
  public ViolationsAnnotationsAggregator(
      final MatrixBuild build,
      final Launcher launcher,
      final BuildListener listener,
      final HealthDescriptor healthDescriptor,
      final String defaultEncoding,
      final boolean usePreviousBuildAsReference,
      final boolean useStableBuildAsReference) {
    super(
        build,
        launcher,
        listener,
        healthDescriptor,
        defaultEncoding,
        usePreviousBuildAsReference,
        useStableBuildAsReference);
  }

  @Override
  protected Action createAction(
      final HealthDescriptor healthDescriptor,
      final String defaultEncoding,
      final ParserResult aggregatedResult) {
    return new ViolationsResultAction(
        build,
        healthDescriptor,
        new ViolationsResult(
            build,
            defaultEncoding,
            aggregatedResult,
            usePreviousBuildAsReference(),
            useOnlyStableBuildsAsReference()));
  }

  private ViolationsResultAction getAction(final MatrixRun run) {
    return run.getAction(ViolationsResultAction.class);
  }

  @Override
  protected ViolationsResult getResult(final MatrixRun run) {
    return getAction(run).getResult();
  }

  @Override
  protected boolean hasResult(final MatrixRun run) {
    return getAction(run) != null;
  }
}

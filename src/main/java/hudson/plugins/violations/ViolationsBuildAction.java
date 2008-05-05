package hudson.plugins.violations;

import java.io.IOException;

import java.util.Calendar;

import java.awt.Color;
import java.awt.BasicStroke;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.LogarithmicAxis;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.data.category.CategoryDataset;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;

import org.kohsuke.stapler.StaplerResponse;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerProxy;

import hudson.util.ChartUtil;
import hudson.util.DataSetBuilder;
import hudson.util.ShiftedCategoryAxis;
import hudson.util.ColorPalette;

import hudson.model.Result;
import hudson.model.Action;
import hudson.model.Actionable;
import hudson.model.AbstractBuild;

import hudson.model.HealthReport;
import hudson.model.HealthReportingAction;

import hudson.maven.*;
import java.util.*;

import hudson.plugins.violations.graph.SeverityTypeDataSet;
import hudson.plugins.violations.util.StringUtil;

import hudson.plugins.violations.hudson.AbstractViolationsBuildAction;
import hudson.plugins.violations.hudson.maven.ViolationsMavenAggregatedBuildAction;

/**
 * This is the build action for the
 * violations. It has the violation report for
 * the build and is able to graph the violations.
 * The rendering of the build is done by the associated
 * summary.jelly script.
 */

public class ViolationsBuildAction
    extends AbstractViolationsBuildAction
    implements AggregatableAction
{

    private static final double LOG_VALUE_FOR_ZERO = 0.5;
    private boolean  useLog = false;

    private static final int X_SIZE = 400;
    private static final int Y_SIZE = 200;
    private static final double PADDING = 5.0;

    private ViolationsReport report;

    /**
     * Construct a build action.
     * @param owner the build that has created this action.
     * @param report the report for this build.
     */
    public ViolationsBuildAction(
        AbstractBuild<?, ?> owner,
        ViolationsReport report) {
        super(owner);
        this.report = report;
    }

    /**
     * Constructor used for M2 projects.
     * This needs to be created during a "postExecute"
     * due to sequencing issue (1582). However,
     * it may be usefull to do this anyway.
     * report is set later.
     * @param owner the build that has created this action.
     */
    public ViolationsBuildAction(
        AbstractBuild<?, ?> owner) {
        super(owner);
    }

    public MavenAggregatedReport createAggregatedAction(
        MavenModuleSetBuild build,
        Map<MavenModule, List<MavenBuild>> moduleBuilds) {
        return new ViolationsMavenAggregatedBuildAction(build);
    }


    /**
     * Set the report.
     * Used in M2.
     * @param report the report for this build.
     */
    public void setReport(
        ViolationsReport report) {
        report.setBuild(getBuild());
        this.report = report;
    }

    public ViolationsReport getReport() {
        report.setBuild(getBuild());
        return report;
    }

    /**
     * get rhe previous valid build result.
     * @return the previous violations build action.
     */
    /*
    public ViolationsBuildAction getPreviousResult() {
        return (ViolationsBuildAction) super.getPreviousResult();
    }
    */

}

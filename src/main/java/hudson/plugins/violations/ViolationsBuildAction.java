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
import hudson.model.Build;

import hudson.model.HealthReport;
import hudson.model.HealthReportingAction;

/**
 * This is the build action for the
 * violations. It has the violation report for
 * the build and is able to graph the violations.
 * The rendering of the build is done by the associated
 * summary.jelly script.
 */

public class ViolationsBuildAction
    extends Actionable
    implements Action, HealthReportingAction, StaplerProxy {

    private static final int X_SIZE = 400;
    private static final int Y_SIZE = 200;
    private static final double PADDING = 5.0;

    private final Build            owner;
    private final ViolationsReport report;

    /**
     * Construct a build action.
     * @param owner the build that has created this action.
     * @param report the report for this build.
     */
    public ViolationsBuildAction(
        Build owner,
        ViolationsReport report) {
        this.owner  = owner;
        this.report = report;
    }

    /**
     * Get the build that owns this action.
     * @return the build.
     */
    public Build getBuild() {
        return owner;
    }

    // -------------------------------------------
    //  These are the magic methods to tell hudson
    //  the url, the object to go to when the url
    //  is pressed.
    // -------------------------------------------

    /**
     * The object to use then the URL is pressed
     * This handles the url's from "${buildpage}/violations/" except
     * for any overridded by do methods in this class (in this
     * instance doGraph).
     * @return the violations report.
     */
    public Object getTarget() {
        return getReport();
    }

    /**
     * The display name for the url to be displayed
     * on the build page right hand side.
     * @return the display name.
     */
    public String getDisplayName() {
        return "Violations";
    }

    /**
     * Get the icon to display.
     * FIXME: MAY NEED TO SPECIFY ROOT DIR.
     * @return the violations 24x24 icon.
     */
    public String getIconFileName() {
        return MagicNames.ICON_24;
    }

    /**
     * The url to get to the "target" object.
     * @return the url
     */
    public String getUrlName() {
        return MagicNames.VIOLATIONS;
    }

    /**
     * Get the search url.
     * @return the url.
     */
    public String getSearchUrl() {
        return getUrlName();
    }
    
    // -------------------------------------------
    //  These are the magic methods to tell hudson
    //  the url, the object to go to when the url
    //  is pressed.
    // -------------------------------------------


    /**
     * Get the report.
     * @return the report.
     */
    public ViolationsReport getReport() {
        return report;
    }

    /**
     * get rhe previous valid build result.
     * @return the previous violations build action.
     */
    public ViolationsBuildAction getPreviousResult() {
        Build<?, ?> b = owner;
        Build<?, ?> previous = b.getPreviousBuild();
        return getViolationsAction(previous);
    }

    /**
     * Get the build report.
     * @return the build report (null if not available).
     */
    @Override
    public HealthReport getBuildHealth() {
        return report.getBuildHealth();
    }


    /**
     * This corresponds to the url ./graph.
     * @param req the request parameters.
     * @param rsp the response.
     * @throws IOException if there is an error writing the graph
     */
    public void doGraph(StaplerRequest req, StaplerResponse rsp)
        throws IOException {
        if (ChartUtil.awtProblem) {
            // not available. send out error message
            rsp.sendRedirect2(req.getContextPath() + "/images/headless.png");
            return;
        }
        Calendar t = owner.getTimestamp();

        if (req.checkIfModified(t, rsp)) {
            return; // up to date
        }
        DataSetBuilder<String, ChartUtil.NumberOnlyBuildLabel> dsb
            = new DataSetBuilder<String, ChartUtil.NumberOnlyBuildLabel>();

        for (ViolationsBuildAction a = this; a != null;
             a = a.getPreviousResult()) {
            ViolationsReport report = a.getReport();
            report.setBuild(owner);
            ChartUtil.NumberOnlyBuildLabel label
                = new ChartUtil.NumberOnlyBuildLabel(a.owner);
            for (String type: report.getViolations().keySet()) {
                /*
                System.out.println(
                    "adding type " + type + " number "
                    + report.getViolations().get(type)
                    + " for build " + a.owner.getNumber());
                */
                dsb.add(report.getViolations().get(type), type, label);
            }
        }
        ChartUtil.generateGraph(
            req, rsp, createChart(dsb.build()), X_SIZE, Y_SIZE);
    }


    /**
     * Get the violations build action from a build.
     * @param start the build to start looking in.
     * @return the action if found, null otherwise.
     */
    public static ViolationsBuildAction getViolationsAction(Build start) {
        Build<?, ?> b = start;
        while (b != null) {
            if (b.getResult() == Result.FAILURE) {
                b = b.getPreviousBuild();
                continue;
            }
            ViolationsBuildAction r = b.getAction(ViolationsBuildAction.class);
            if (r != null) {
                return r;
            }
            b = b.getPreviousBuild();
        }
        return null;
    }

    // FIXME this should be in a utility class
    private JFreeChart createChart(CategoryDataset dataset) {

        final JFreeChart chart = ChartFactory.createLineChart(
                null,                   // chart title
                null,                   // unused
                "count",                    // range axis label
                dataset,                  // data
                PlotOrientation.VERTICAL, // orientation
                true,                     // include legend
                true,                     // tooltips
                false                     // urls
        );

        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...

        final LegendTitle legend = chart.getLegend();
        legend.setPosition(RectangleEdge.RIGHT);

        chart.setBackgroundPaint(Color.white);

        final CategoryPlot plot = chart.getCategoryPlot();

        // plot.setAxisOffset(new Spacer(Spacer.ABSOLUTE, 5.0, 5.0, 5.0, 5.0));
        plot.setBackgroundPaint(Color.WHITE);
        plot.setOutlinePaint(null);
        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.black);

        CategoryAxis domainAxis = new ShiftedCategoryAxis(null);
        plot.setDomainAxis(domainAxis);
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);
        domainAxis.setLowerMargin(0.0);
        domainAxis.setUpperMargin(0.0);
        domainAxis.setCategoryMargin(0.0);

        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        //rangeAxis.setUpperBound(100);
        rangeAxis.setLowerBound(0);

        final LineAndShapeRenderer renderer
            = (LineAndShapeRenderer) plot.getRenderer();
        renderer.setStroke(new BasicStroke(2.0f));
        ColorPalette.apply(renderer);

        // crop extra space around the graph
        plot.setInsets(new RectangleInsets(PADDING, 0, 0, PADDING));

        return chart;
    }
}

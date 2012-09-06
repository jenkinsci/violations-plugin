package hudson.plugins.violations.hudson;

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

import hudson.model.Action;
import hudson.model.Actionable;
import hudson.model.HealthReportingAction;
import hudson.model.HealthReport;
import hudson.model.AbstractBuild;

import hudson.util.ChartUtil;
import hudson.util.DataSetBuilder;
import hudson.util.ShiftedCategoryAxis;
import hudson.util.ColorPalette;

import hudson.plugins.violations.graph.SeverityTypeDataSet;
import hudson.plugins.violations.util.StringUtil;

import hudson.plugins.violations.MagicNames;
import hudson.plugins.violations.ViolationsReport;


/**
 * Common base class for recording violations results.
 */
public abstract class AbstractViolationsBuildAction
    <T extends AbstractViolationsBuildAction>
    extends Actionable
    implements Action, HealthReportingAction, StaplerProxy {
    protected final AbstractBuild<?, ?>   owner;

    private static final double LOG_VALUE_FOR_ZERO = 0.5;
    private boolean  useLog = false;

    private static final int X_SIZE = 500;
    private static final int Y_SIZE = 200;
    private static final double PADDING = 5.0;

    /**
     * Constructor setting the owner build.
     */
    protected AbstractViolationsBuildAction(AbstractBuild<?, ?> owner) {
        this.owner = owner;
    }


    /**
     * Get the report.
     * @return the report.
     */
    abstract public ViolationsReport getReport();


    /**
     * The object to use then the URL is pressed
     * This handles the url's from "${buildpage}/violations/" except
     * for any overridden by do methods in this class (in this
     * instance doGraph).
     * @return the violations report.
     */
    public Object getTarget() {
        return getReport();
    }

    
    /**
     * Get the build that owns this action.
     * @return the build.
     */
    public AbstractBuild<?, ?> getBuild() {
        return owner;
    }

    /**
     * Get the build report.
     * @return the build report (null if not available).
     */
    @Override
    public HealthReport getBuildHealth() {
        return getReport().getBuildHealth();
    }

    /**
     * The display name for the url to be displayed
     * on the build page right hand side.
     * @return the display name.
     */
    public String getDisplayName() {
        return "Violations";
    }

    // -------------------------------------------
    //  These are the magic methods to tell hudson
    //  the url, the object to go to when the url
    //  is pressed.
    // -------------------------------------------

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


    /**
     * Get the icon to display.
     * FIXME: MAY NEED TO SPECIFY ROOT DIR.
     * @return the violations 24x24 icon.
     */
    public String getIconFileName() {
        return MagicNames.ICON_24;
    }

    /**
     * Gets the first valid violations report.
     * @return the first violations report found for this build.
     */
    public ViolationsReport findReport() {
        return ViolationsReport.findViolationsReport(owner);
    }

    /**
     * Gets the test result of the previous build, if it's recorded, or null.
     */
    public T getPreviousResult() {
        ViolationsReport report =
            ViolationsReport.findViolationsReport(owner);
        return report == null
            ? null
            : report.getBuild().getAction((Class<T>) getClass());
    }

    /**
     * This corresponds to the url ./graph.
     * @param req the request parameters.
     * @param rsp the response.
     * @throws IOException if there is an error writing the graph
     */
    public void doGraph(StaplerRequest req, StaplerResponse rsp)
        throws IOException {
        String type = req.getParameter("type");

        if (ChartUtil.awtProblemCause != null) {
            // not available. send out error message
            rsp.sendRedirect2(req.getContextPath() + "/images/headless.png");
            return;
        }
        Calendar t = getBuild().getTimestamp();

        if (!StringUtil.isBlank(type)) {
            ChartUtil.generateGraph(
                req, rsp, new SeverityTypeDataSet(
                    getReport(), type).createChart(),
                X_SIZE, Y_SIZE);
            return;
        }

        DataSetBuilder<String, ChartUtil.NumberOnlyBuildLabel> dsb
            = new DataSetBuilder<String, ChartUtil.NumberOnlyBuildLabel>();

        for (ViolationsReport r: ViolationsReport.iteration(getBuild())) {
            ChartUtil.NumberOnlyBuildLabel label
                = new ChartUtil.NumberOnlyBuildLabel(r.getBuild());
            for (String ty: r.getViolations().keySet()) {
                dsb.add(roundUp(r.getViolations().get(ty)), ty, label);
            }
        }
        ChartUtil.generateGraph(
            req, rsp, createChart(dsb.build()), X_SIZE, Y_SIZE);
    }

    private double roundUp(int val) {
        if (!useLog) {
            return 1.0 * val;
        }
        if (val < 1) {
            return LOG_VALUE_FOR_ZERO;
        } else {
            return 1.0 * val;
        }
    }

    // FIXME this should be in a utility class
    protected JFreeChart createChart(CategoryDataset dataset) {

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

        if (useLog) {
            final NumberAxis rangeAxis2 = new LogarithmicAxis("Log(y)");
            plot.setRangeAxis(rangeAxis2);
        }
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

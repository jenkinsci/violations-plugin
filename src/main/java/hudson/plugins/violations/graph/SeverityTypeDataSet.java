package hudson.plugins.violations.graph;

import hudson.plugins.violations.TypeSummary;
import hudson.plugins.violations.ViolationsReport;
import hudson.plugins.violations.model.Severity;
import hudson.util.ChartUtil.NumberOnlyBuildLabel;
import hudson.util.DataSetBuilder;
import hudson.util.ShiftedCategoryAxis;
import hudson.util.StackedAreaRenderer2;

import java.awt.Color;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.StackedAreaRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;



/**
 * Class to construct a dataset for severities
 * of a particular type.
 */
public class SeverityTypeDataSet  {
    // Some ikky colors
    private static final Color RED    = new Color(0xEF, 0x29, 0x29);
    private static final Color VIOLET = new Color(0xEE, 0x82, 0xEE);
    private static final Color YELLOW = new Color(0xCC, 0xCC, 0x00);
    private static final Color GRAY   = new Color(0x30, 0x30, 0x30);

    // Some constants
    private static final double INSET = 5.0;
    private static final float  ALPHA = 0.8f;
    private final ViolationsReport report;
    private final String type;

    /**
     * Create a SeverityTypeData set from a report and a type.
     * @param report the current report.
     * @param type   the type of violation to build from.
     */
    public SeverityTypeDataSet(ViolationsReport report, String type) {
        this.report = report;
        this.type = type;
    }

    /**
     * Build the data set.
     * @return the dataset.
     */
    public CategoryDataset buildDataSet() {
        DataSetBuilder<Row, NumberOnlyBuildLabel> builder
            = new DataSetBuilder<Row, NumberOnlyBuildLabel>();
        for (ViolationsReport r = report; r != null; r = r.previous()) {
            if (r.getTypeSummaries() == null) {
                continue;
            }
            TypeSummary t = r.getTypeSummaries().get(type);
            if (t == null) {
                continue; // Old report
            }
            if (t.getSeverityArray() == null
                || t.getSeverityArray().length != Severity.NUMBER_SEVERITIES) {
                continue; // Old report
            }

            int[] nums = t.getSeverityArray();
            builder.add(
                nums[Severity.MEDIUM_VALUE] + nums[Severity.MEDIUM_HIGH_VALUE]
                + nums[Severity.MEDIUM_LOW_VALUE],
                MEDIUM_ROW,
                new NumberOnlyBuildLabel(r.getBuild()));
            builder.add(
                nums[Severity.HIGH_VALUE],
                HIGH_ROW,
                new NumberOnlyBuildLabel(r.getBuild()));
            builder.add(
                nums[Severity.LOW_VALUE],
                LOW_ROW,
                new NumberOnlyBuildLabel(r.getBuild()));
        }
        return builder.build();
    }

    /**
     * Create a JFree chart for this dataset.
     * @return the chart.
     */
    public JFreeChart createChart() {
        CategoryDataset dataset = buildDataSet();

        JFreeChart chart = ChartFactory.createStackedAreaChart(
            null,                     // chart title
            null,                     // unused
            "count",                  // range axis label
            dataset,                  // data
            PlotOrientation.VERTICAL, // orientation
            true,                    // include legend
            true,                     // tooltips
            false                     // urls
        );

        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...

        final LegendTitle legend = chart.getLegend();
        legend.setPosition(RectangleEdge.RIGHT);
        chart.setBackgroundPaint(Color.white);

        final CategoryPlot plot = chart.getCategoryPlot();

        plot.setBackgroundPaint(Color.WHITE);
        plot.setOutlinePaint(null);
        plot.setForegroundAlpha(ALPHA);
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

        StackedAreaRenderer renderer = new StackedAreaRenderer2();
        plot.setRenderer(renderer);
        renderer.setSeriesPaint(2, RED);
        renderer.setSeriesPaint(1, VIOLET);
        renderer.setSeriesPaint(0, YELLOW);

        // crop extra space around the graph
        plot.setInsets(new RectangleInsets(0, 0, 0, INSET));

        return chart;
    }
    private static class Row implements Comparable<Row> {
        private final String tag;
        private final int    number;
        public Row(String tag, int number) {
            this.tag = tag;
            this.number = number;
        }
        public String toString() {
            return tag;
        }
        public int compareTo(Row other) {
            return number == other.number ? 0
                : number < other.number ? 1
                : -1;
        }
    }
    private static final Row HIGH_ROW = new Row(Severity.HIGH, 0);
    private static final Row MEDIUM_ROW = new Row(Severity.MEDIUM, 1);
    private static final Row LOW_ROW = new Row(Severity.LOW, 2);
}

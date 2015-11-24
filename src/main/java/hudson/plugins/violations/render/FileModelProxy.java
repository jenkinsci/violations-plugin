package hudson.plugins.violations.render;

import static com.google.common.base.Suppliers.memoize;
import static com.google.common.collect.Lists.newArrayList;
import static java.util.logging.Level.WARNING;
import hudson.Functions;
import hudson.model.AbstractBuild;
import hudson.plugins.violations.generate.XMLUtil;
import hudson.plugins.violations.model.FileModel;
import hudson.plugins.violations.model.Severity;
import hudson.plugins.violations.model.Violation;
import hudson.plugins.violations.parse.FileModelParser;
import hudson.plugins.violations.parse.ParseXML;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;

import com.google.common.base.Supplier;

/**
 * A proxy class for FileModel used to allow lazy loading of FileModel. This
 * class is also used to render the FileModel.
 */
public class FileModelProxy {
    private static final Logger LOG = Logger.getLogger(FileModelProxy.class.getName());

    private final File xmlFile;
    private final Supplier<FileModel> fileModel = memoize(new Supplier<FileModel>() {
        @Override
        public FileModel get() {
            if (!xmlFile.exists()) {
                LOG.log(WARNING, "The file " + xmlFile + " does not exist");
                return null;
            }
            try {
                FileModel t = new FileModel();
                ParseXML.parse(xmlFile, new FileModelParser().fileModel(t));
                return t;
            } catch (Exception ex) {
                LOG.log(WARNING, "Unable to parse " + xmlFile, ex);
                return null;
            }

        }
    });
    private String contextPath;
    private AbstractBuild<?, ?> build;

    /**
     * Construct this proxy.
     *
     * @param xmlFile
     *            the xmlfile to create the FileModel from.
     */
    public FileModelProxy(File xmlFile) {
        this.xmlFile = xmlFile;
    }

    /**
     * Fluid setting of the build attribute.
     *
     * @param build
     *            the owner build.
     * @return this object.
     */
    public FileModelProxy build(AbstractBuild<?, ?> build) {
        this.build = build;
        return this;
    }

    /**
     * Fluid setting of context path. This is used for getting icons.
     *
     * @param contextPath
     *            the current WEB context path.
     * @return this object.
     */
    public FileModelProxy contextPath(String contextPath) {
        this.contextPath = contextPath;
        return this;
    }

    /**
     * get the current build.
     *
     * @return the current build.
     */
    public AbstractBuild<?, ?> getBuild() {
        return build;
    }

    /**
     * Get the type line.
     *
     * @param type
     *            the violation type.
     * @return a rendered line containing the number of violations and the
     *         number of suppressed violations.
     */
    public String typeLine(String type) {
        FileModel.LimitType l = fileModel.get().getLimitTypeMap().get(type);
        StringBuilder b = new StringBuilder();
        if (l == null) {
            return type + " ?number?";
        }
        b.append(type);
        b.append("&nbsp;&nbsp;&nbsp;");
        b.append(l.getNumber());
        b.append(" violation");
        if (l.getNumber() > 1) {
            b.append("s");
        }
        if (l.getSuppressed() != 0) {
            b.append(" (");
            b.append(l.getSuppressed());
            b.append(" not shown)");
        }
        return b.toString();
    }

    /**
     * Wheter to show lines.
     *
     * @return true if the file model contains lines.
     */
    public boolean getShowLines() {
        return getFileModel().getLines().size() != 0;
    }

    public Set<Map.Entry<String, TreeSet<Violation>>> getTypeMapEntries() {
        return getFileModel().getTypeMap().entrySet();
    }

    public Set<Map.Entry<Integer, Set<Violation>>> getViolationEntries() {
        return getFileModel().getLineViolationMap().entrySet();
    }

    public String getDisplayName() {
        return getFileModel().getDisplayName();
    }

    /**
     * This gets called from the index.jelly script to render the marked up
     * contents of the file.
     *
     * @return a table of lines and associated violations in html.
     */
    public List<BlockData> getFileContent() {
        List<BlockData> blockDataList = newArrayList();
        StringBuilder b = new StringBuilder();
        int previousLine = -1;
        int startLine = 0;
        int currentLine = -1;

        for (Map.Entry<Integer, String> e : fileModel.get().getLines().entrySet()) {
            currentLine = e.getKey();
            String line = e.getValue();
            // Check if at start of block
            if (currentLine != (previousLine + 1)) {
                // Start of block
                // Check if need to write previous block
                if (b.length() > 0) {
                    blockDataList.add(new BlockData(startLine, previousLine, b.toString(), new File(fileModel.get()
                            .getDisplayName()).getName()));
                }
                b = new StringBuilder();
                startLine = currentLine;
            }
            previousLine = currentLine;

            Set<Violation> v = fileModel.get().getLineViolationMap().get(currentLine);

            // TR
            b.append("<tr " + (v != null ? "class='violation'" : "") + ">");

            // Visual Studio
            if (v != null) {
                // Only first one needed for line
                for (Violation violation : v) {
                    b.append("<td class='source icon'>");
                    b.append(this.getVisualStudioLink(violation));
                    b.append("</td>");
                    break;
                }
            } else {
                b.append("<td class='source icon'/>\n");
            }

            // Icon
            if (v != null) {
                showIcon(b, v);
            } else {
                b.append("<td class='source icon'/>\n");
            }

            // Line number
            b.append("<td class='source line' id='line" + currentLine + "'>");
            if (v != null) {
                addVDiv(b);
            }
            b.append(currentLine);
            if (v != null) {
                showDiv(b, v);
                b.append("</div>");
            }
            b.append("</td>");
            b.append("<td class='source message'>");
            b.append(XMLUtil.escapeHTMLContent(contextPath + "/plugin/violations/images/tab.png", line));
            b.append("</td>\n");
            b.append("</tr>\n");
        }
        if (b.length() > 0) {
            blockDataList.add(new BlockData(startLine, previousLine, b.toString(), new File(fileModel.get()
                    .getDisplayName()).getName()));
        }
        return blockDataList;
    }

    public String getVisualStudioLink(Violation v) {
        StringBuilder ret = new StringBuilder();
        String uriBase = "devenv:?file=" + this.fileModel.get().getDisplayName();

        ret.append("<a href=\"");
        String uri = String.valueOf(uriBase + "&line=" + v.getLine());
        ret.append(uri);
        ret.append("\"><img src=\"/plugin/violations/images/16x16/vs2010-play.png\" alt=\"Visual Studio 2010\"></a>");
        return ret.toString();
    }

    public String getViolationsSummary() {
        StringBuilder ret = new StringBuilder();

        for (Entry<String, TreeSet<Violation>> t : this.fileModel.get().getTypeMap().entrySet()) {
            ret.append("<table class=\"pane\">");
            ret.append("<tbody>");
            ret.append("<tr><td class=\"pane-header\" colspan=\"5\">");
            ret.append(this.typeLine(t.getKey()));
            ret.append("</td></tr>");
            for (Violation v : t.getValue()) {
                ret.append("<tr>");
                if (v.getSource().toUpperCase().contains("Security".toUpperCase())) {
                    ret.append("<td class=\"pane\">");
                    ret.append("<img src=\"/plugin/violations/images/16x16/security.png\" alt=\"Security violation\">");
                    ret.append("</td>");
                } else {
                    ret.append("<td  class=\"pane\" />");
                }
                ret.append("<td class=\"pane\">");
                ret.append(getVisualStudioLink(v));
                ret.append("</td>");
                ret.append("<td class=\"pane\">");
                if (this.getShowLines()) {
                    ret.append("<a href=\"#line");
                    ret.append(v.getLine());
                    ret.append("\">");
                    ret.append(v.getLine());
                    ret.append("</a>");
                } else {
                    ret.append(v.getLine());
                }

                ret.append("</td>");
                ret.append("<td class=\"pane\">");
                ret.append(this.severityColumn(v));
                ret.append("</td>");

                ret.append("<td class=\"pane\" width=\"99%\">");
                ret.append(v.getMessage());
                ret.append("</td>");

                ret.append("</tr>");
            }

            ret.append("</table>");
            ret.append("<p></p>");
        }

        return ret.toString();
    }

    /**
     * Get the file model. If the file model is present, return it, otherwise
     * parse the xml file.
     *
     * @return the file model or null if unable to parse.
     */
    public FileModel getFileModel() {
        return this.fileModel.get();
    }

    private String getSeverityIcon(int level) {
        String color = null;
        switch (level) {
        case Severity.HIGH_VALUE:
            color = "red";
            break;
        case Severity.LOW_VALUE:
            color = "yellow";
            break;
        default:
            color = "violet"; // medium (low,-,high)
        }
        return "/plugin/violations/images/16x16/" + color + "-warning.png";
    }

    /**
     * Get the URL for the icon, taking context into account
     *
     * @param v
     *            the violation
     * @return URL
     */
    public String getSeverityIconUrl(Violation v) {
        return contextPath + getSeverityIcon(v.getSeverityLevel());
    }

    /**
     * Get the severity column for a violation.
     *
     * @param v
     *            the violation.
     * @return a string to place in the severity column of the violation table.
     */
    public String severityColumn(Violation v) {
        StringBuilder b = new StringBuilder();
        addVDiv(b);
        b.append("<a class='healthReport'>");
        b.append("<img src='" + getSeverityIconUrl(v) + "' alt='" + v.getSeverity() + "'/>");
        b.append("</a>");
        b.append("<div class='healthReportDetails'>\n");
        b.append("<table class='violationPopup'>\n");
        b.append("<tr>\n");
        b.append("<th>Severity</th>\n");
        b.append("<td>");
        b.append(v.getSeverity());
        b.append("</td>\n");
        b.append("</tr>\n");

        b.append("<tr>\n");
        b.append("<th>Class</th>\n");
        b.append("<td>");
        b.append(Functions.escape(v.getSource()));
        b.append("</td>\n");
        b.append("</tr>\n");

        b.append("<tr>\n");
        b.append("<th>Detail</th>\n");
        b.append("<td class='message'>");
        b.append(Functions.escape(v.getSourceDetail()));
        b.append("</td>\n");
        b.append("</tr>\n");

        b.append("</table>\n");
        b.append("</div>\n");
        b.append("</div>\n");
        return b.toString();
    }

    private void addVDiv(StringBuilder b) {
        b.append("<div class='healthReport'");
        b.append("onmouseover=\"this.className='healthReport hover';return true;\"");
        b.append("onmouseout=\"this.className='healthReport';return true;\">");
    }

    private void showIcon(StringBuilder b, Set<Violation> violations) {
        // Get the worst icon in the set
        int level = Severity.LOW_VALUE;
        for (Violation v : violations) {
            if (v.getSeverityLevel() < level) {
                level = v.getSeverityLevel();
            }
        }
        b.append("<td class='source icon'>");
        addVDiv(b);
        b.append("<a class='healthReport'>");
        b.append("<img src='" + contextPath + getSeverityIcon(level) + "'/>");
        b.append("</a>");
        showDiv(b, violations);
        b.append("</div>");
        b.append("</td>");
    }

    private void showDiv(StringBuilder b, Set<Violation> violations) {
        b.append("<div class='healthReportDetails'>\n");
        b.append(" <table class='violationPopup'>\n");
        b.append("  <thead>\n");
        b.append("   <tr>\n");
        b.append("     <th> Type</th>\n");
        b.append("     <th> Class</th>\n");
        b.append("     <th> Description</th>\n");
        b.append("   </tr>\n");
        b.append("  </thead>\n");
        b.append("  <tbody>\n");
        for (Violation v : violations) {
            b.append("   <tr>\n");
            b.append("     <td>");
            b.append(v.getType());
            b.append("</td>\n");
            b.append("     <td>");
            b.append(v.getSource());
            b.append("</td>\n");
            b.append("     <td width='100%' class='message'>");
            b.append(XMLUtil.escapeContent(v.getPopupMessage()));
            b.append("</td>\n");
            b.append("   </tr>\n");
        }
        b.append("  </tbody>\n");
        b.append(" </table>\n");
        b.append("</div>\n");
    }

    public String getFileNameAlt() {
        return new File(fileModel.get().getDisplayName()).getName();
    }

    public String getSummaryTable() {

        StringBuilder gst = new StringBuilder();
        int count = 0;

        gst.append(" <table class='violations' width='100%'>\n");
        gst.append("   <tr>\n");
        gst.append("     <td class='violations-header'> # </td>\n");
        gst.append("     <td class='violations-header'> Type </td>\n");
        gst.append("     <td class='violations-header'> Class</td>\n");
        gst.append("     <td class='violations-header'> Message</td>\n");
        gst.append("     <td class='violations-header'> Description</td>\n");
        gst.append("   </tr>\n");

        Map<Integer, Set<Violation>> violationsMap = fileModel.getLineViolationMap();

        Iterator<Integer> itForMap = violationsMap.keySet().iterator();
        while(itForMap.hasNext()) {
            Set<Violation> violations = violationsMap.get(itForMap.next());
            for (Violation v : violations) {
                ++count;
                gst.append("   <tr>\n");
                gst.append("     <td class='violations'>");
                gst.append(count);
                gst.append("</td>\n");
                gst.append("     <td class='violations'>");
                gst.append(v.getType());
                gst.append("</td>\n");
                gst.append("     <td class='violations'>");
                gst.append(v.getSource());
                gst.append("</td>\n");
                gst.append("     <td class='violations'>");
                gst.append(v.getMessage());
                gst.append("</td>\n");
                gst.append("     <td class='violations'>");
                gst.append(v.getPopupMessage());
                gst.append("</td>\n");
                gst.append("   </tr>\n");
            }
        }
        gst.append(" </table>\n");
        gst.append("<p><br>\n");
        gst.append("<h3>Total Number of violations:  \n");
        gst.append(count);
        gst.append("</h3><p>\n");
        return gst.toString();
    }

}

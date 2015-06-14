package hudson.plugins.violations;

import static com.google.common.base.Joiner.on;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Lists.transform;
import static com.google.common.collect.Maps.uniqueIndex;
import static org.junit.Assert.fail;
import hudson.plugins.violations.model.BuildModel.FileCount;
import hudson.plugins.violations.model.Violation;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;

public class ReportedFileAsserter {

    private ViolationsReportAsserter violationsReportAsserter;
    private String reportedFile;

    public ReportedFileAsserter(
            ViolationsReportAsserter violationsReportAsserter,
            String reportedFile) {
        this.violationsReportAsserter = violationsReportAsserter;
        this.reportedFile = reportedFile;
    }

    public ReportedFileAsserter wasReported() {
        getOrFail();
        return this;
    }

    private FileCount getOrFail() {
        Set<FileCount> fileCounts = violationsReportAsserter
                .getViolationsReport().getModel().getTypeMap()
                .get(violationsReportAsserter.getTypeDescriptor().getName());
        ImmutableMap<String, FileCount> nameToFileCount = nameToFileCount(fileCounts);
        if (nameToFileCount.containsKey(reportedFile))
            return nameToFileCount.get(reportedFile);
        fail("Could not find \"" + reportedFile + "\" in:\n"
                + on('\n').join(nameToFileCount.keySet()));
        return null;

    }

    public ReportedFileAsserter reportedViolation(int line, String source,
            String message) {
        for (Violation v : getViolations())
            if (v.getLine() == line && v.getSource().equals(source)
                    && v.getMessage().equals(message))
                return this;
        fail("Could not find message \"" + message + "\" at " + line
                + " Found:\n"
                + Joiner.on('\n').join(readableStrings(getViolations())));
        return this;
    }

    private Iterable<String> readableStrings(List<Violation> violations) {
        return transform(violations, new Function<Violation, String>() {
            public String apply(Violation input) {
                return input.getLine() + " Source: " + input.getSource()
                        + " Message: " + input.getMessage();
            }
        });
    }

    private List<Violation> getViolations() {
        List<Violation> violations = newArrayList();
        Map<Integer, Set<Violation>> lineMap = getOrFail().getFileModel()
                .getLineViolationMap();
        for (Integer line : lineMap.keySet())
            for (Violation violation : lineMap.get(line))
                violations.add(violation);
        return violations;
    }

    private ImmutableMap<String, FileCount> nameToFileCount(
            Set<FileCount> fileCounts) {
        return uniqueIndex(fileCounts, new Function<FileCount, String>() {
            public String apply(FileCount input) {
                return input.getName();
            }
        });
    }

    public ViolationsReportAsserter and() {
        return violationsReportAsserter;
    }
}

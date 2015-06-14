package hudson.plugins.violations;

public class ViolationsReportAsserter {
    private ViolationsReport violationsReport;
    private TypeDescriptor typeDescriptor;

    public ViolationsReportAsserter(ViolationsReport violationsReport,
            TypeDescriptor typeDescriptor) {
        this.violationsReport = violationsReport;
        this.typeDescriptor = typeDescriptor;
    }

    public static ViolationsReportAsserter assertThat(
            ViolationsReport violationsReport, TypeDescriptor typeDescriptor) {
        return new ViolationsReportAsserter(violationsReport, typeDescriptor);
    }

    public ReportedFileAsserter assertThat(String reportedFile) {
        return new ReportedFileAsserter(this, reportedFile);
    }

    TypeDescriptor getTypeDescriptor() {
        return typeDescriptor;
    }

    ViolationsReport getViolationsReport() {
        return violationsReport;
    }
}

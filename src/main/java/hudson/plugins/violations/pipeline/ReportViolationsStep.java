package hudson.plugins.violations.pipeline;

import hudson.Extension;
import org.jenkinsci.plugins.workflow.steps.AbstractStepDescriptorImpl;
import org.jenkinsci.plugins.workflow.steps.AbstractStepImpl;
import org.kohsuke.stapler.DataBoundConstructor;
import javax.annotation.Nonnull;

/**
 *
 * @author bushc
 */
public class ReportViolationsStep extends AbstractStepImpl {
    @DataBoundConstructor
    public ReportViolationsStep(){}
    
    @Extension
    public static class ReportViolationsDescriptorImpl extends AbstractStepDescriptorImpl {
        
        public ReportViolationsDescriptorImpl(){ super(ReportViolationsStepExecution.class); }
        
        @Override
        public String getFunctionName(){
            return "reportViolations";
        }
        
        @Nonnull
        @Override
        public String getDisplayName(){
            return "Scan for static analysis violations";
        }
        
    }
}

package hudson.plugins.violations.pipeline;

import hudson.FilePath;
import hudson.Launcher;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.plugins.violations.ViolationsPublisher;
import org.jenkinsci.plugins.workflow.steps.AbstractSynchronousNonBlockingStepExecution;
import org.jenkinsci.plugins.workflow.steps.StepContextParameter;

/**
 *
 * @author bushc
 */
public class ReportViolationsStepExecution extends AbstractSynchronousNonBlockingStepExecution<Void> {
    
    @StepContextParameter
    private transient TaskListener listener;
    
    @StepContextParameter
    private transient FilePath ws;
    
    @StepContextParameter
    private transient Run build;
    
    @StepContextParameter
    private transient Launcher launcher;
    
    
    @Override
    protected Void run() throws Exception {
        listener.getLogger().println("Analyzing static analysis output.");
        
        ViolationsPublisher publisher = new ViolationsPublisher();
        publisher.perform(build, ws, launcher, listener);
        
        return null;
    }
}

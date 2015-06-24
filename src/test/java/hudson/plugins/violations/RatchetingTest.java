package hudson.plugins.violations;

import static com.google.common.collect.Lists.newArrayList;
import static hudson.model.Result.ABORTED;
import static hudson.model.Result.FAILURE;
import static hudson.model.Result.NOT_BUILT;
import static hudson.model.Result.SUCCESS;
import static hudson.model.Result.UNSTABLE;
import static hudson.plugins.violations.ViolationsPublisher.handleRatcheting;
import static hudson.plugins.violations.ViolationsPublisher.shouldDoRatcheting;
import static hudson.plugins.violations.types.checkstyle.CheckstyleDescriptor.CHECKSTYLE;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.lang.System.out;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import hudson.model.BuildListener;
import hudson.plugins.violations.ViolationsReport.TypeReport;

import java.util.Collection;

import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

public class RatchetingTest extends JenkinsRule {
    @Rule
    public JenkinsRule j = new JenkinsRule();

    @Test
    public void testThatRatchetingIsOnlyEnabledForSuccessfulBuilds() throws Exception {
        ViolationsConfig config = new ViolationsConfig();
        config.setAutoUpdateMax(TRUE);
        config.setAutoUpdateUnstable(TRUE);
        assertFalse(shouldDoRatcheting(config, UNSTABLE));
        assertFalse(shouldDoRatcheting(config, ABORTED));
        assertFalse(shouldDoRatcheting(config, FAILURE));
        assertFalse(shouldDoRatcheting(config, NOT_BUILT));
        assertFalse(shouldDoRatcheting(config, UNSTABLE));
        config.setAutoUpdateMax(FALSE);
        config.setAutoUpdateUnstable(FALSE);
        assertFalse(shouldDoRatcheting(config, SUCCESS));
        config.setAutoUpdateMax(FALSE);
        config.setAutoUpdateUnstable(TRUE);
        assertTrue(shouldDoRatcheting(config, SUCCESS));
        config.setAutoUpdateMax(TRUE);
        config.setAutoUpdateUnstable(FALSE);
        assertTrue(shouldDoRatcheting(config, SUCCESS));
        config.setAutoUpdateMax(TRUE);
        config.setAutoUpdateUnstable(TRUE);
        assertTrue(shouldDoRatcheting(config, SUCCESS));
    }

    @Test
    public void testThatUnstableLimitCanBeUpdated() {
        BuildListener listener = mock(BuildListener.class);
        when(listener.getLogger()).thenReturn(out);

        Collection<TypeReport> typeReports = newArrayList(type(CHECKSTYLE, 1));
        ViolationsConfig config = new ViolationsConfig();
        config.setAutoUpdateUnstable(TRUE);
        config.getTypeConfigs().get(CHECKSTYLE).setUnstable(4);
        handleRatcheting(SUCCESS, typeReports, listener, config);
        assertEquals("Expected ratcheting to have been updated", 2, config.getTypeConfigs().get(CHECKSTYLE)
                .getUnstable().intValue());
    }

    @Test
    public void testThatMaxCanBeUpdated() {
        BuildListener listener = mock(BuildListener.class);
        when(listener.getLogger()).thenReturn(out);

        Collection<TypeReport> typeReports = newArrayList(type(CHECKSTYLE, 7));
        ViolationsConfig config = new ViolationsConfig();
        config.setAutoUpdateMax(TRUE);
        config.getTypeConfigs().get(CHECKSTYLE).setMax(15);
        handleRatcheting(SUCCESS, typeReports, listener, config);
        assertEquals("Expected ratcheting to have been updated", 8, config.getTypeConfigs().get(CHECKSTYLE).getMax());
    }

    @Test
    public void testThatMinIsUpdatedIfMaxIsLessThenMinCanBeUpdated() {
        BuildListener listener = mock(BuildListener.class);
        when(listener.getLogger()).thenReturn(out);

        Collection<TypeReport> typeReports = newArrayList(type(CHECKSTYLE, 7));
        ViolationsConfig config = new ViolationsConfig();
        config.setAutoUpdateMax(TRUE);
        config.getTypeConfigs().get(CHECKSTYLE).setMax(15);
        config.getTypeConfigs().get(CHECKSTYLE).setMin(9);
        handleRatcheting(SUCCESS, typeReports, listener, config);
        assertEquals("Expected ratcheting to have been updated", 8, config.getTypeConfigs().get(CHECKSTYLE).getMax());
        assertEquals("Expected ratcheting to have been updated", 7, config.getTypeConfigs().get(CHECKSTYLE).getMin());
    }

    private TypeReport type(String checkstyle, int i) {
        return new TypeReport(checkstyle, "", i);
    }
}

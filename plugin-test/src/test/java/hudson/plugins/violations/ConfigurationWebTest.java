package hudson.plugins.violations;

import static com.google.common.base.MoreObjects.firstNonNull;
import static java.lang.System.getProperty;
import static java.lang.Thread.sleep;
import static java.util.logging.Level.OFF;
import static org.junit.Assert.assertEquals;

import java.util.logging.Logger;

import org.apache.commons.logging.LogFactory;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.jayway.jsonpath.JsonPath;

public class ConfigurationWebTest {
    private static final String PROP_JENKINS_URL = "jenkins";
    private static final Logger logger = Logger.getLogger(ConfigurationWebTest.class.getName());
    private static WebDriver webDriver;

    static {
        LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log",
                "org.apache.commons.logging.impl.NoOpLog");
        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(OFF);
        java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(OFF);
    }

    private static String getJenkinsBaseUrl() {
        return firstNonNull(getProperty(PROP_JENKINS_URL), "http://localhost:8080/jenkins");
    }

    @BeforeClass
    public static void before() throws Exception {
        webDriver = new FirefoxDriver();
        waitForJenkinsToStart();
    }

    @AfterClass
    public static void after() throws Exception {
        webDriver.quit();
    }

    @Test
    public void testThatExampleProjectM2FreedstyleBuildCanBeBuilt() throws Exception {
        String jobName = "m2-freestyle-build";
        startJob(jobName);
        waitForJob(jobName);
        assertResult(jobName, "unstable");
    }

    @Test
    public void testThatExampleProjectM2FreedstyleSiteCanBeBuilt() throws Exception {
        String jobName = "m2-freestyle-site";
        startJob(jobName);
        waitForJob(jobName);
        assertResult(jobName, "unstable");
    }

    // Requires Maven2 installation to be present
    @Ignore
    @Test
    public void testThatExampleProjectM2M2BuildCanBeBuilt() throws Exception {
        String jobName = "m2-m2-build";
        startJob(jobName);
        waitForJob(jobName);
        assertResult(jobName, "unstable");
    }

    // Requires Maven2 installation to be present
    @Ignore
    @Test
    public void testThatExampleProjectM2M2SiteCanBeBuilt() throws Exception {
        String jobName = "m2-m2-site";
        startJob(jobName);
        waitForJob(jobName);
        assertResult(jobName, "unstable");
    }

    private void assertResult(String jobName, String expectedResult) {
        webDriver.get(getJenkinsBaseUrl() + "/job/" + jobName + "/lastBuild/api/json?pretty=true");
        String json = webDriver.getPageSource().replaceAll("<.+?>", "");
        String result = JsonPath.read(json, "$.['result']");
        assertEquals("Not unstable!\n" + json, expectedResult.toUpperCase(), result.toUpperCase());
    }

    private void waitForJob(String jobName) throws InterruptedException {
        while (!consoleText(jobName).contains("Finished: ")) {
            logger.info("Waiting for jenkins job to finnish");
            sleep(500);
        }
    }

    private String consoleText(String jobName) {
        try {
            webDriver.get(getJenkinsBaseUrl() + "/job/" + jobName + "/lastBuild/consoleText");
            return webDriver.getPageSource();
        } catch (Exception e) {
            return "";
        }
    }

    private void startJob(String jobName) {
        webDriver.get(getJenkinsBaseUrl() + "/job/" + jobName + "/build?delay=0sec");
    }

    private static void waitForJenkinsToStart() throws Exception {
        while (webDriver.getPageSource().contains("getting ready")) {
            logger.info("Jenkins not ready...");
            sleep(500);
        }
        logger.info("Jenkins ready!");
        sleep(3000);
    }
}

package hudson.plugins.violations;

import static com.google.common.base.MoreObjects.firstNonNull;
import static java.lang.System.getProperty;
import static java.lang.Thread.sleep;
import static java.util.logging.Level.OFF;
import static org.junit.Assert.assertEquals;
import static org.openqa.selenium.By.id;
import static org.openqa.selenium.By.xpath;

import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.logging.LogFactory;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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

    @Test
    public void testThatExampleProjectM2M2BuildCanBeBuilt() throws Exception {
        String jobName = "m2-m2-build";
        startJob(jobName);
        waitForJob(jobName);
        assertResult(jobName, "unstable");
    }

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
        setMavenHome();
        sleep(1000);
    }

    private static void setMavenHome() throws Exception {
        webDriver.get(getJenkinsBaseUrl() + "/configure");
        if (!webDriver.findElements(xpath("//input[@value='maven2']")).isEmpty()) {
            return; // Already configured
        }
        waitForAndGet(id("yui-gen8-button")).click();
        waitForAndGet(xpath("//input[@name='_.name']")).sendKeys("maven2");
        /**
         * Looks like a bug in Jenkins here. Sometimes the version is a field
         * and sometimes a select.
         */
        List<WebElement> versionSelect = webDriver.findElements(xpath("//select[@name='_.id']"));
        if (versionSelect.isEmpty()) {
            waitForAndGet(xpath("//input[@name='_.id']")).sendKeys("2.2.1");
        } else {
            List<WebElement> options = versionSelect.get(0).findElements(By.tagName("option"));
            for (WebElement option : options) {
                if (option.getText().startsWith("2.2.1")) {
                    option.click();
                    break;
                }
            }
        }

        removeOnBeforeUnload();
        waitForAndGet(id("yui-gen10-button")).click();
    }

    private static void removeOnBeforeUnload() {
        ((JavascriptExecutor) webDriver).executeScript("window.onbeforeunload=null;", "");
    }

    private static WebElement waitForAndGet(By by) throws Exception {
        for (int i = 0; i < 10; i++) {
            if (!webDriver.findElements(by).isEmpty()) {
                break;
            }
            sleep(50);
        }
        return webDriver.findElement(by);
    }
}

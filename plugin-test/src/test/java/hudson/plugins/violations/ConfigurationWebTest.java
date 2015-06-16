package hudson.plugins.violations;

import static com.gargoylesoftware.htmlunit.BrowserVersion.FIREFOX_24;
import static com.google.common.base.MoreObjects.firstNonNull;
import static java.lang.System.getProperty;
import static java.lang.Thread.sleep;
import static java.util.logging.Level.OFF;
import static org.junit.Assert.assertTrue;
import static org.openqa.selenium.By.id;
import static org.openqa.selenium.By.xpath;

import java.util.logging.Logger;

import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

public class ConfigurationWebTest {
    private static final String PROP_JENKINS_URL = "jenkins";
    private static final String PROP_HEADLESS = "headless";
    private static final Logger logger = Logger.getLogger(ConfigurationWebTest.class.getName());
    private static final String TEST_JOB_NAME = "testJob";
    private WebDriver webDriver;

    static {
        LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log",
                "org.apache.commons.logging.impl.NoOpLog");
        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(OFF);
        java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(OFF);
    }

    private String getJenkinsBaseUrl() {
        return firstNonNull(getProperty(PROP_JENKINS_URL), "http://localhost:8080");
    }

    private boolean isHeadless() {
        return !firstNonNull(getProperty(PROP_HEADLESS), "false").equals("false");
    }

    @Before
    public void before() throws InterruptedException {
        if (isHeadless()) {
            HtmlUnitDriver webDriverHtmlUnit = new HtmlUnitDriver(FIREFOX_24);
            webDriverHtmlUnit.setJavascriptEnabled(true);
            this.webDriver = webDriverHtmlUnit;
        } else {
            webDriver = new FirefoxDriver();
        }
        waitForJenkinsToStart();
    }

    @After
    public void after() {
        deleteJob();
        webDriver.quit();
    }

    @Test
    public void testPluginConfiguration() throws InterruptedException {
        /**
         * Create new job and enable plugin on it.
         */
        createJob();
        enablePlugin();

        /**
         * Enter valid details, start job and validate config output.
         */
        webDriver.findElement(xpath("//input[@name='config.limit']")).sendKeys("50");
        webDriver.findElement(xpath("//input[@name='checkstyle.pattern']")).sendKeys("**/css-checkstyle.xml");
        removeOnBeforeUnload();
        saveJob();
        startJob();
        waitForJob(1);
        String consoleText = consoleText(1);
        assertTrue(consoleText, consoleText.contains("SUCCESS"));
    }

    private void waitForJob(int index) throws InterruptedException {
        while (!consoleText(index).contains("Finished: SUCCESS")) {
            logger.info("Waiting for jenkins job to finnish");
            sleep(500);
        }
    }

    private String consoleText(int index) {
        try {
            webDriver.get(getJenkinsBaseUrl() + "/job/" + TEST_JOB_NAME + "/" + index + "/consoleText");
            return webDriver.getPageSource();
        } catch (Exception e) {
            return "";
        }
    }

    private void startJob() {
        webDriver.get(getJenkinsBaseUrl() + "/job/" + TEST_JOB_NAME + "/build?delay=0sec");
    }

    private void saveJob() {
        webDriver.findElement(xpath("//button[@id='yui-gen12-button']")).click();
    }

    private void createJob() {
        webDriver.get(getJenkinsBaseUrl() + "/view/All/newJob");
        webDriver.findElement(id("name")).sendKeys(TEST_JOB_NAME);
        webDriver.findElement(xpath("//input[@value='hudson.model.FreeStyleProject']")).click();
        webDriver.findElement(id("ok-button")).click();
    }

    private void enablePlugin() throws InterruptedException {
        scrollDown();
        try {
            webDriver.findElement(xpath("//button[@suffix='publisher']")).click();
            webDriver.findElement(xpath("//a[text()='Report Violations']")).click();
        } catch (Exception e) {
            logger.info("Did not find publisher button");
        }
        try {
            webDriver.findElement(xpath("//input[@name='hudson-plugins-violations-ViolationsPublisher']")).click();
        } catch (Exception e) {
            logger.info("Did not find publisher checkbox");
        }
        scrollDown();
    }

    private void deleteJob() {
        webDriver.get(getJenkinsBaseUrl() + "/job/" + TEST_JOB_NAME + "/delete");
        webDriver.findElement(xpath("//button[@id='yui-gen1-button']")).click();
    }

    private void removeOnBeforeUnload() {
        ((JavascriptExecutor) webDriver).executeScript("window.onbeforeunload=null;", "");
    }

    private void scrollDown() throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            ((JavascriptExecutor) webDriver).executeScript("window.scrollBy(0,1000)", "");
            sleep(50);
        }
    }

    private void waitForJenkinsToStart() throws InterruptedException {
        webDriver.get(getJenkinsBaseUrl());
        while (webDriver.getPageSource().contains("getting ready")) {
            logger.info("Jenkins not ready...");
            sleep(500);
        }
        logger.info("Jenkins ready!");
        sleep(3000);
    }
}

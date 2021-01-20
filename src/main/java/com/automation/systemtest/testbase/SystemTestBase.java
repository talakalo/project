package com.automation.systemtest.testbase;

import com.automation.selenium.BrowserType;
import com.automation.selenium.WebDriverFactory;
import com.automation.systemtest.testbase.rules.StopwatchRule;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.PostConstruct;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/com/automation/system-test-base-context.xml"})
public class SystemTestBase {
    
    protected static WebDriver testDriver;
    
    @Autowired
    protected WebDriverFactory driverFactory;
    
    @Rule
    public StopwatchRule stopwatchRule = new StopwatchRule();
    
    @PostConstruct
    public void initTestBase() {
        testDriver = driverFactory.createWebDriver(BrowserType.CHROME);
    }
    
    
}

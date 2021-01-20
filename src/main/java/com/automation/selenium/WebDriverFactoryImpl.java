package com.automation.selenium;

import com.google.common.io.Files;
import org.apache.commons.io.IOUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static io.github.bonigarcia.wdm.config.DriverManagerType.CHROME;

public class WebDriverFactoryImpl implements WebDriverFactory {
    
    private static final Logger log = LoggerFactory.getLogger(WebDriverFactoryImpl.class);
    
    private static final String CHROME_DRIVER_WINDOWS = "chromedriver.exe";
    private static final String CHROME_DRIVER_LINUX = "chromedriver";
    private static final String FIREFOX_DRIVER_EXE = "geckodriver.exe";
    private static final String CHROME_DRIVER_PROP = "webdriver.chrome.driver";
    private static final String FIREFOX_DRIVER_PROP = "webdriver.gecko.driver";
    private static final String WIN_DRIVER_FOLDER = "C:\\driver";
    private static final String LINUX_DRIVER_FOLDER = "/usr/local/bin/chromedriver";
    
    
    @Value("#{testDriverProperties['webdriver_implicitly_wait']}")
    public long WAIT_TIME;
    
    @Override
    public WebDriver createWebDriver(BrowserType browserType) {
        WebDriver driver = null;
        
        switch (browserType) {
            case CHROME:
                initBrowserWebDriver(browserType);
                driver = new ChromeDriver(getDefaultChromeCapabilities());
                break;
            case FIREFOX:
                initBrowserWebDriver(browserType);
                driver = new FirefoxDriver(getDefaultFFOptions());
                break;
        }
        
        configureDriver(driver, browserType);
        return driver;
    }
    
    @Override
    public WebDriver createWebDriver(BrowserType browserType, DesiredCapabilities desiredCapabilities) {
        WebDriver driver = null;
        
        log.info(String.format("Initializing driver for %s", browserType.getName()));
        switch (browserType) {
            case CHROME:
                initBrowserWebDriver(browserType);
                driver = new ChromeDriver(desiredCapabilities);
                break;
            case FIREFOX:
                initBrowserWebDriver(browserType);
                driver = new FirefoxDriver(desiredCapabilities);
                break;
        }
        
        configureDriver(driver, browserType);
        return driver;
    }
    private void configureDriver(WebDriver driver, BrowserType browserType) {
        driver.manage().timeouts().implicitlyWait(WAIT_TIME, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        driver.switchTo().window(driver.getWindowHandle());
        log.info("Successfully initialised '" + browserType.name() + "' browser.");
    }
    
    private FirefoxOptions getDefaultFFOptions() {
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        return firefoxOptions.merge(getDefaultFFCapabilities());
    }
    
    private DesiredCapabilities getDefaultFFCapabilities() {
        DesiredCapabilities dc = DesiredCapabilities.firefox();
        dc.setCapability(FirefoxDriver.PROFILE, createDefaultFirefoxProfile());
        return dc;
    }
    
    private DesiredCapabilities getDefaultChromeCapabilities() {
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("download.default_directory", "C:\\downloads");
        prefs.put("safebrowsing.enabled", "true"); // avoid message "This type of file can harm your computer" when downloading XML files
        prefs.put("plugins.always_open_pdf_externally", true);
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("prefs", prefs);
        
        options.addArguments(
                "disable-infobars",
                "--start-maximized",
                "--safebrowsing-disable-download-protection",
                "--no-sandbox",
                "--disable-dev-shm-usage");
        
        if (Boolean.valueOf(System.getProperty("headless")) == true) {
            options.addArguments("headless");
        }
        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
        capabilities.setCapability(ChromeOptions.CAPABILITY, options);
        
        return capabilities;
    }
    
    
    private FirefoxProfile createDefaultFirefoxProfile() {
        FirefoxProfile profile = new FirefoxProfile();
        
        profile.setPreference("browser.download.folderList", 2);
        profile.setPreference("browser.download.manager.showWhenStarting", false);
        profile.setPreference("browser.download.dir", "C:\\downloads");
        profile.setPreference("browser.helperApps.neverAsk.saveToDisk", "text/csv, application/zip, application/pdf");
        
        profile.setPreference("pdfjs.disabled", true);
        profile.setPreference("plugin.scan.Acrobat", "99.0");
        profile.setPreference("plugin.scan.plid.all", false);
        
        return profile;
    }
    
    private void initBrowserWebDriver(BrowserType browserType) {
        String fileName;
        String driverProperty;
        String driverFolder;
        switch (browserType) {
            case CHROME:
                if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                    fileName = CHROME_DRIVER_WINDOWS;
                } else {
                    fileName = CHROME_DRIVER_LINUX;
                }
                driverProperty = CHROME_DRIVER_PROP;
                driverFolder = browserType.getName();
                break;
            case FIREFOX:
                fileName = FIREFOX_DRIVER_EXE;
                driverProperty = FIREFOX_DRIVER_PROP;
                driverFolder = browserType.getName();
                break;
            default:
                log.error(String.format("Invalid or no browser type specified %s", browserType.toString()));
                return;
        }
        
        try {
            String driverPath;
            File targetFile;
            if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                driverPath = String.format(WIN_DRIVER_FOLDER + "\\%s", fileName);
                targetFile = new File(driverPath);
            } else {
                driverPath = String.format(LINUX_DRIVER_FOLDER + "\\%s", fileName);
                targetFile = new File(driverPath);
            }
            
            Files.createParentDirs(targetFile);
            Files.touch(targetFile);
            
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(String.format("com/automation/selenium/%s/%s", driverFolder, fileName));
            OutputStream outStream = new FileOutputStream(targetFile);
            outStream.write(IOUtils.toByteArray(inputStream));
            outStream.close();
            
            System.setProperty(driverProperty, driverPath);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("Error initializing driver");
        }
    }
}

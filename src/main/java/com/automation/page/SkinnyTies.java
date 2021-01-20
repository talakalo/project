package com.automation.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class SkinnyTies {
    WebDriver driver;
    
    public SkinnyTies(WebDriver driver) {
        this.driver = driver;
    }
    
    private final String WORD_TO_SEARCH = "BLACK POPLIN SKINNY TIE";
    private final String URL="";
    private By searchButton = By.xpath("//a[contains(text(),'Search')]");
    private By search_result_main = By.xpath("//*[@id=\"main\"]/section");
    private By priceSpan = By.xpath("//*[@id=\"main\"]/section/div/div/div/div/div/div/span");
    private By tieDescription = By.xpath("//*[@id=\"main\"]/section/div/div");
    private By search_result_field = By.xpath("//*[@id=\"main\"]/section/div/div");
    private By search_input = By.xpath("//*[@id=\"Search\"]/div/div[1]/form/input[1]");
    
    private int price;
    private String description;
    private static final Logger log = LoggerFactory.getLogger(SkinnyTies.class);
    
    void PrintSumOf(List<?> list) {
    };
    String baseURL="https://skinnyties.com/";
    
    public List<?> getAllPrices(WebDriver driver) {
        List<?> result = new ArrayList<>();
        // Grab the table
        WebElement table = driver.findElement(By.xpath("//table"));
        
        // Now get all the TR elements from the table
        List<WebElement> allRows = table.findElements(By.tagName("tr"));
        // And iterate over them, getting the cells
        for (WebElement row : allRows) {
            List<WebElement> cells = row.findElements(By.tagName("td"));
            //Policy policy = getPolicyFromRow(cells);
            //result.add(policy);
        }
        return result;
    }
    
    public void navigatePageByUrl(WebDriver driver) {
        log.info("Navigating to url: {}", baseURL);
        driver.get(baseURL);
    }
    public void navigateToUrl(String url) {
        driver.get(url);
    }
    public void navigateToUrl() {
        driver.get(URL);
    }
    public void ClickSearch(WebElement element) {
        driver.findElement(searchButton).click();
    }
    
    public String getDescription(WebElement element) {
        return driver.findElement(tieDescription).getText();
    }
    
    public void doSearchAndGet(WebDriver driver) {
        driver.findElement(searchButton).click();
        driver.findElement(search_input).sendKeys(WORD_TO_SEARCH);
        
    }
    public int getPrice(WebDriver driver) {
        int parseResult = 0;
        String resultPrice =driver.findElement(priceSpan).getText();
        try
        {
            parseResult= Integer.parseInt(resultPrice);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parseResult;
        
    }
    
}
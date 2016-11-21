package com.koval.storage;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

/**
 * Created by Volodymyr Kovalenko
 */

public class WebDriverHook {
    public static WebDriver driver;

    @Before
    public void openBrowser(){
        System.setProperty("webdriver.chrome.driver", "src/test/resources/lib/chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().deleteAllCookies();
        driver.manage().window().maximize();
    }
    @After
    public void close(Scenario scenario) {
        if (scenario.isFailed()) {
            try{
                scenario.write(driver.getCurrentUrl());
                byte[] screen = ((TakesScreenshot)driver).getScreenshotAs(OutputType.BYTES);
                scenario.embed(screen, "image/png");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        driver.quit();
    }
}

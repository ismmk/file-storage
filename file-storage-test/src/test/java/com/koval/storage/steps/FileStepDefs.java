package com.koval.storage.steps;

import com.koval.storage.WebDriverHook;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.openqa.selenium.By;

import java.io.File;

import static org.junit.Assert.assertFalse;

/**
 * Created by Volodymyr Kovalenko
 */
public class FileStepDefs {
    @When("^I open main page$")
    public void openMainPage() {
        WebDriverHook.driver.get(System.getenv("filestorage.main_page"));
    }

    @When("^Put \"([^\"]*)\" in file input$")
    public void putFile(String file) {
        WebDriverHook.driver.findElement(By.name("file")).sendKeys(new File(file).getAbsolutePath());
    }

    @When("^Press upload button$")
    public void uploadButtonClick() {
        WebDriverHook.driver.findElement(By.name("upload")).click();
    }

    @When("^Press search button$")
    public void searchButtonClick() {
        WebDriverHook.driver.findElement(By.name("search")).click();
    }

    @Then("^I see \"([^\"]*)\" file$")
    public void iSeeFile(String file) {
      assertFalse( WebDriverHook.driver.findElements(new By.ByCssSelector("div[value=\""+file+"\"]")).isEmpty()) ;
    }

}

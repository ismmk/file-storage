package com.koval.storage;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

/**
 * Created by Volodymyr Kovalenko
 */
@RunWith(Cucumber.class)
@CucumberOptions(features = "classpath:features" ,
    plugin = {"html:target/cucumber-html-report", "json:cucumber-json"})
public class BDDTests {
}

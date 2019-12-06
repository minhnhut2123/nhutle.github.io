package StepDefinitions;
import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
 
@RunWith(Cucumber.class)
@CucumberOptions(
plugin = {"pretty","json:target/cucumber-reports/cucumber-reports.json"}
)

public class RunCucumberTest{
		
}

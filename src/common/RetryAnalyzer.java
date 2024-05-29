package common;

import java.io.FileReader;
import java.util.Properties;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import com.aventstack.extentreports.Status;

public class RetryAnalyzer implements IRetryAnalyzer {
	int counter = 0;
	int retryLimit;

	@Override
	public boolean retry(ITestResult result) {
		try {
			FileReader reader = new FileReader("./common.properties");
			Properties prop = new Properties();
			prop.load(reader);
			retryLimit = Integer.parseInt(prop.getProperty("limit"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (!result.isSuccess()) {
			if (counter < retryLimit) {
				counter++;
				result.setStatus(ITestResult.FAILURE);
				extendReportsFailOperations(result);
				return true;
			}
		} else {
			result.setStatus(ITestResult.SUCCESS);
		}
		return false;
	}

	public void extendReportsFailOperations(ITestResult iTestResult) {
		Object testClass = iTestResult.getInstance();
		WebDriver webDriver = ((BaseTest) testClass).getDriver();
		String base64Screenshot = "data:image/png;base64,"
				+ ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.BASE64);
		ExtentTestManager.getTest().log(Status.FAIL, "Test Failed", ExtentTestManager.getTest()
				.addScreenCaptureFromBase64String(base64Screenshot).getModel().getMedia().get(0));
	}
}

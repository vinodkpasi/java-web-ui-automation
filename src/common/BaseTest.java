package common;
import lombok.Getter;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static common.ExtentTestManager.startTest;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;

@Getter
public class BaseTest {
	public WebDriver driver;

	@SuppressWarnings("deprecation")
	@BeforeMethod
	public void setUp(Method method) throws IOException, InterruptedException {
		FileReader reader = new FileReader("./common.properties");
		Properties prop = new Properties();
		prop.load(reader);
		String browser = prop.getProperty("browser");
		String url = prop.getProperty("url");
		driver = Util.getBrowserDriver(browser);
		driver.manage().window().maximize();
		driver.get(url);		
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		String testMethodName = method.getName();
		String descriptiveTestName = method.getAnnotation(Test.class).description();
		startTest(testMethodName, descriptiveTestName);
	}

	@AfterMethod
	public void teardown() throws InterruptedException {
		driver.quit();
	}
}

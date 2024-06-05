package common;

import org.testng.Assert;

import com.aventstack.extentreports.Status;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import io.github.bonigarcia.wdm.WebDriverManager;

public class Util {
	public static void verifyNewWindowUrl(WebDriver driver, String expectedUrl) {
		String originalWindow = driver.getWindowHandle();
		for (String windowHandle : driver.getWindowHandles()) {
			if (!originalWindow.contentEquals(windowHandle)) {
				driver.switchTo().window(windowHandle);
				String unreadiest = driver.getCurrentUrl();
				common.Util.logInfo("The current url :" + unreadiest);
				Assert.assertTrue(unreadiest.contains(expectedUrl));
				driver.close();
				driver.switchTo().window(originalWindow);
			}
		}
	}

	public static void logInfo(String message) {
		ExtentTestManager.getTest().log(Status.INFO, message);
	}

	public static WebDriver getBrowserDriver(String browser) {
		WebDriver driver = null;
		if (browser == null)
			browser = "chrome";
        driver = switch (browser) {
            case "firefox" -> {
                WebDriverManager.firefoxdriver().setup();
                yield new FirefoxDriver();
            }
            case "edge" -> {
                WebDriverManager.edgedriver().setup();
                yield new EdgeDriver();
            }
            default -> {
                WebDriverManager.chromedriver().setup();
                yield new ChromeDriver();
            }
        };
		return driver;
	}

	public static String getBaseURL(String urlValue) throws MalformedURLException {
		URL url = new URL(urlValue);
		String path = url.getFile().substring(0, url.getFile().lastIndexOf('/'));
        return url.getProtocol() + "://" + url.getHost() + path;
	}

	public static void scrollIntoView(WebDriver driver, WebElement element)  {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView()",element);
	}

	public static void jsClick(WebDriver driver, WebElement element)  {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].click()",element);
	}
}

package common;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.*;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

public class Login {

    public Login(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    @FindBy(name = "phoneNumber")
    @CacheLookup
    public WebElement txtPhoneNumber;

    @FindBy(name = "password")
    @CacheLookup
    public WebElement txtPassword;

    @FindBy(css = "input[autocomplete='one-time-code']")
    @CacheLookup
    public List<WebElement> txtOTP;

    @FindBy(xpath = "//button[@type='submit']")
    @CacheLookup
    public WebElement btnLogin;

    @FindBy(xpath = "//button/span[text()='Verify']")
    @CacheLookup
    public WebElement btnVerify;

    public void loginToApp(String phoneNumber, String password, String otp) throws InterruptedException {
        txtPhoneNumber.sendKeys(phoneNumber);
        txtPassword.sendKeys(password);
        btnLogin.click();
        for (int i = 0; i < txtOTP.size(); i++) {
            txtOTP.get(i).sendKeys(String.valueOf(otp.charAt(i)));
        }
        btnVerify.click();
    }

    public void loginToApp() throws IOException, InterruptedException {
        FileReader reader = new FileReader("./common.properties");
        Properties prop = new Properties();
        prop.load(reader);
        String phoneNumber = prop.getProperty("phoneNumber");
        String password = prop.getProperty("password");
        String otp = prop.getProperty("otp");
        loginToApp(phoneNumber, password, otp);
    }
}

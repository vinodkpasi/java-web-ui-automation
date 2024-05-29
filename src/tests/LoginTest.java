package tests;

import java.io.IOException;

import org.testng.annotations.Test;
import common.BaseTest;
import common.Login;
import page_objects.HomePage;
import org.testng.Assert;

public class LoginTest extends BaseTest {
    @Test
    public void ValidLogin() throws InterruptedException, IOException {
        Login login = new Login(driver);
        login.loginToApp();
        HomePage homePage = new HomePage(driver);
        Assert.assertTrue(homePage.iconUser.isDisplayed());
    }
}

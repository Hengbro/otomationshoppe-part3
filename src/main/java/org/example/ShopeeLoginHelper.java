package org.example;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ShopeeLoginHelper {

    public static void waitForHomeOrLogin(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3000));
        wait.until(webDriver -> webDriver.getCurrentUrl().contains("shopee.co.id"));
    }

    public static void loginShopee(WebDriver driver, String username, String password) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3000));
        System.out.println("Melakukan login...");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("loginKey")));
        WebElement usernameField = driver.findElement(By.name("loginKey"));
        WebElement passwordField = driver.findElement(By.name("password"));

        usernameField.clear();
        usernameField.sendKeys(username);
        passwordField.clear();
        passwordField.sendKeys(password);

        WebElement loginButton = wait.until(driver1 -> {
            WebElement btn = driver1.findElement(By.xpath("//button[contains(text(),'Log in')]"));
            if (btn.isEnabled() && btn.isDisplayed()) {
                return btn;
            }
            return null;
        });
        Utils.takeScreenshot(driver, "halaman_login_"+ System.currentTimeMillis() + ".png");

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        loginButton.click();

        System.out.println("Login dikirim. Menunggu verifikasi...");
    }

    public static void waitForVerificationToPass(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5000));
        wait.until(webDriver -> !webDriver.getCurrentUrl().contains("verify"));
        System.out.println("Verifikasi selesai. Lanjutkan ke pencarian.");
    }
}


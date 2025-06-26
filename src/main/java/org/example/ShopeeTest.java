package org.example;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Set;

public class ShopeeTest {

    private static final String CHROME_PROFILE_PATH = System.getProperty("user.home") + "/selenium-profile";
    private static final String COOKIE_PATH = "cookies.data";

    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", "/Users/home/Downloads/chromedriver-mac-x64/chromedriver");

        ChromeOptions options = new ChromeOptions();
        ShopeeAntiDetectionHelper.applyAntiDetection(options, CHROME_PROFILE_PATH);

        WebDriver driver = new ChromeDriver(options);
        ShopeeAntiDetectionHelper.hideWebDriverFlag(driver);

        try {
            // Akses Shopee
            driver.get("https://shopee.co.id");

            // Load cookies lalu refresh
            ShopeeAntiDetectionHelper.loadCookies(driver, COOKIE_PATH);
            driver.navigate().refresh();

            Utils.takeScreenshot(driver, "home_" + System.currentTimeMillis() + ".png");
            Utils.scrollOnceWithDelay(driver, 5000);
            // Tunggu apakah user perlu login
            ShopeeLoginHelper.waitForHomeOrLogin(driver);

            if (driver.getCurrentUrl().contains("login")) {
                // Proses login
                ShopeeLoginHelper.loginShopee(driver, "(+62) 821-6074-0192", "heNg19!@");

                if (driver.getCurrentUrl().contains("captcha")) {
                    System.out.println("Deteksi captcha, mencoba memuat ulang halaman...");

                    int maxRetry = 5;
                    boolean captchaCleared = false;

                    for (int attempt = 1; attempt <= maxRetry; attempt++) {
                        System.out.println("Reload captcha (Percobaan " + attempt + " dari " + maxRetry + ")");
                        driver.navigate().refresh();
                        Utils.waitDelay(4000); // Tambahkan delay agar Shopee punya waktu memuat ulang

                        if (!driver.getCurrentUrl().contains("captcha")) {
                            System.out.println("Captcha berhasil dihindari setelah refresh ke-" + attempt);
                            captchaCleared = true;
                            break;
                        }

                        Utils.takeScreenshot(driver, "captcha_retry_" + attempt + "_" + System.currentTimeMillis() + ".png");
                    }

                    if (!captchaCleared) {
                        System.out.println("Captcha tetap muncul, menunggu user menyelesaikannya secara manual...");
                        Utils.takeScreenshot(driver, "captcha_manual_" + System.currentTimeMillis() + ".png");
                        ShopeeAntiDetectionHelper.waitForCaptchaSolve(driver);
                    }
                }


                // Verifikasi login berhasil
                boolean loginSuccess = false;
                try {
                    new WebDriverWait(driver, Duration.ofSeconds(20))
                            .until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div.navbar__username")));
                    loginSuccess = true;
                } catch (TimeoutException e) {
                    System.err.println("Login gagal: elemen navbar__username tidak ditemukan.");
                }

                if (!loginSuccess) {
                    System.err.println("Gagal login, proses dihentikan.");
                    return;
                }

                ShopeeAntiDetectionHelper.saveCookies(driver, COOKIE_PATH);
                System.out.println("Login berhasil, cookies disimpan.");
                Utils.takeScreenshot(driver, "search_3" + System.currentTimeMillis() + ".png");
            }

            // Lanjut pencarian
            ShopeeSearchHelper.waitForSearchPageWithDebug(driver);
            ShopeeSearchHelper.searchShopee(driver, "sepatu futsal");
            Utils.takeScreenshot(driver, "search_1" + System.currentTimeMillis() + ".png");
            Utils.scrollOnceWithDelay(driver, 5000);
            Utils.takeScreenshot(driver, "search_2" + System.currentTimeMillis() + ".png");
            Utils.waitDelay(5000);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Utils.waitDelay(10000);
            driver.quit();
        }
    }

}

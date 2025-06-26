package org.example;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ShopeeSearchHelper {

    public static void waitForSearchPageWithDebug(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        By[] possibleSelectors = new By[]{
                By.className("shopee-searchbar-input__input"),
                By.cssSelector("input[type='search']"),
                By.cssSelector("input[placeholder*='Cari']"),
                By.cssSelector("input[aria-label*='search']")
        };

        Exception lastException = null;
        for (By selector : possibleSelectors) {
            try {
                wait.until(ExpectedConditions.visibilityOfElementLocated(selector));
                System.out.println("Elemen pencarian ditemukan dengan selector: " + selector);
                return;
            } catch (Exception e) {
                lastException = e;
            }
        }

        System.err.println("Gagal menemukan elemen pencarian dengan semua selector yang dicoba.");
        if (lastException != null) {
            System.err.println("Last error: " + lastException.getMessage());
        }
        Utils.debugPageInfo(driver);
        throw new RuntimeException("Gagal menemukan elemen pencarian.");
    }

    public static void searchShopee(WebDriver driver, String keyword) {
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        Utils.takeScreenshot(driver, "search_" + System.currentTimeMillis() + ".png");
        WebElement searchBox = null;

        By[] possibleSelectors = new By[]{
                By.className("shopee-searchbar-input__input"),
                By.cssSelector("input[type='search']"),
                By.cssSelector("input[placeholder*='Cari']"),
                By.cssSelector("input[aria-label*='search']")
        };

        for (By selector : possibleSelectors) {
            try {
                searchBox = driver.findElement(selector);
                if (searchBox.isDisplayed()) break;
            } catch (Exception ignored) {}
        }

        if (searchBox == null) {
            throw new RuntimeException("Elemen input pencarian tidak ditemukan.");
        }

        searchBox.clear();
        searchBox.sendKeys(keyword);
        searchBox.sendKeys(Keys.ENTER);
    }
}

package org.example;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.*;
import java.util.*;
import org.openqa.selenium.Cookie;

public class ShopeeAntiDetectionHelper {

    public static void applyAntiDetection(ChromeOptions options, String chromeProfilePath) {
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--disable-infobars");
        options.addArguments("user-data-dir=" + chromeProfilePath);
        options.setExperimentalOption("excludeSwitches", Arrays.asList("enable-automation"));
        options.setExperimentalOption("useAutomationExtension", false);
    }

    public static void hideWebDriverFlag(WebDriver driver) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("Object.defineProperty(navigator, 'webdriver', {get: () => undefined})");
            js.executeScript("navigator.chrome = {runtime: {}};");
            js.executeScript("Object.defineProperty(navigator, 'plugins', {get: () => [1,2,3,4,5]});");
            js.executeScript("Object.defineProperty(navigator, 'languages', {get: () => ['en-US', 'en']});");
        } catch (Exception e) {
            System.err.println("Gagal menyembunyikan WebDriver flag: " + e.getMessage());
        }
    }

    public static void saveCookies(WebDriver driver, String path) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path))) {
            oos.writeObject(driver.manage().getCookies());
        } catch (IOException e) {
            System.err.println("Gagal menyimpan cookies: " + e.getMessage());
        }
    }

    public static void loadCookies(WebDriver driver, String path) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path))) {
            Set<Cookie> cookies = (Set<Cookie>) ois.readObject();
            for (Cookie cookie : cookies) {
                driver.manage().addCookie(cookie);
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Gagal memuat cookies: " + e.getMessage());
        }
    }

    public static void waitForCaptchaSolve(WebDriver driver) {
        System.out.println("Silakan selesaikan captcha secara manual di browser. Klik Enter jika sudah selesai.");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

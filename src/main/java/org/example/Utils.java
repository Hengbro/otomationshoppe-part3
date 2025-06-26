package org.example;

import org.openqa.selenium.*;
import org.openqa.selenium.io.FileHandler;

import java.io.File;
import java.util.Scanner;

public class Utils {

    public static void takeScreenshot(WebDriver driver, String filename) {
        try {
            TakesScreenshot ts = (TakesScreenshot) driver;
            File source = ts.getScreenshotAs(OutputType.FILE);
            FileHandler.copy(source, new File(filename));
            System.out.println("Screenshot saved: " + filename);
        } catch (Exception e) {
            System.err.println("Gagal mengambil screenshot: " + e.getMessage());
        }
    }

    public static void debugPageInfo(WebDriver driver) {
        try {
            System.out.println("=== Debug Halaman ===");
            System.out.println("URL: " + driver.getCurrentUrl());
            System.out.println("Title: " + driver.getTitle());

            String html = driver.getPageSource();
            System.out.println("HTML snippet (500 char):\n" + (html.length() > 500 ? html.substring(0, 500) : html));
            System.out.println("=====================");
        } catch (Exception e) {
            System.out.println("Gagal debug halaman: " + e.getMessage());
        }
    }

    public static void waitForUserCaptchaSolve() {
        System.out.println("Captcha terdeteksi. Silakan selesaikan Captcha di browser, lalu tekan ENTER untuk lanjut...");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
    }

    public static void scrollOnceWithDelay(WebDriver driver, long delayMs) {
        try {
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
            Thread.sleep(delayMs);
        } catch (InterruptedException e) {
            System.err.println("Terjadi interupsi saat menunggu setelah scroll.");
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            System.err.println("Gagal melakukan scroll: " + e.getMessage());
        }
    }

    public static void waitDelay(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

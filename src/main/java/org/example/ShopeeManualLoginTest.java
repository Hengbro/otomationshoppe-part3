package org.example;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.*;

public class ShopeeManualLoginTest {

    private static final String COOKIE_PATH = "cookies.data";

    public static void main(String[] args) throws IOException {
        WebDriver driver = new ChromeDriver();

        try {
            driver.get("https://shopee.co.id/buyer/login");

            // Hapus flag webdriver
            ((JavascriptExecutor) driver).executeScript(
                    "Object.defineProperty(navigator, 'webdriver', {get: () => false})"
            );

            System.out.println("Silakan login secara manual...");

            // Tunggu user login & verifikasi OTP
            Thread.sleep(60000); // 60 detik, bisa disesuaikan

            if (!driver.getCurrentUrl().contains("login")) {
                System.out.println("Login berhasil, menyimpan cookies...");
                saveCookies(driver, COOKIE_PATH);
            } else {
                System.err.println("Login gagal atau belum selesai.");
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }

    private static void saveCookies(WebDriver driver, String filePath) throws IOException {
        File file = new File(filePath);
        file.delete();
        file.createNewFile();

        FileWriter fw = new FileWriter(file);
        BufferedWriter bw = new BufferedWriter(fw);

        for (Cookie ck : driver.manage().getCookies()) {
            bw.write(
                    ck.getName() + ";" +
                            ck.getValue() + ";" +
                            ck.getDomain() + ";" +
                            ck.getPath() + ";" +
                            ck.getExpiry() + ";" +
                            ck.isSecure()
            );
            bw.newLine();
        }

        bw.close();
        fw.close();
        System.out.println("💾 Cookies disimpan ke " + filePath);
    }
}

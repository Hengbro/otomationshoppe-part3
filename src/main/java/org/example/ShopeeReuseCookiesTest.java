package org.example;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.*;
import java.util.Date;
import java.util.StringTokenizer;

public class ShopeeReuseCookiesTest {
    private static final String COOKIE_PATH = "cookies.data";

    public static void main(String[] args) {
        WebDriver driver = new ChromeDriver();

        try {
            driver.get("https://shopee.co.id");

            loadCookies(driver, COOKIE_PATH);

            driver.navigate().refresh();
            Thread.sleep(5000);

            System.out.println("✅ URL saat ini: " + driver.getCurrentUrl());
            // Lanjutkan automation...
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }

    private static void loadCookies(WebDriver driver, String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            System.err.println("❌ File cookies tidak ditemukan.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                StringTokenizer tok = new StringTokenizer(line, ";");
                String name = tok.nextToken();
                String value = tok.nextToken();
                String domain = tok.nextToken();
                String path = tok.nextToken();
                String expiry = tok.nextToken();
                boolean isSecure = Boolean.parseBoolean(tok.nextToken());

                Date expiryDate = null;
                if (!"null".equals(expiry)) {
                    try {
                        long millis = Long.parseLong(expiry);
                        expiryDate = new Date(millis);
                    } catch (NumberFormatException e) {
                        System.err.println("Format expiry tidak valid: " + expiry);
                    }
                }

                Cookie ck = new Cookie.Builder(name, value)
                        .domain(domain)
                        .path(path)
                        .expiresOn(expiryDate)
                        .isSecure(isSecure)
                        .build();
                driver.manage().addCookie(ck);
            }
        }
        System.out.println("🍪 Cookies berhasil dimuat dari " + filePath);
    }
}

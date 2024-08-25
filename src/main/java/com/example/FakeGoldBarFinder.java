package com.example;

// Import necessary Selenium libraries and WebDriverManager for ChromeDriver management
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FakeGoldBarFinder {
    private final WebDriver driver;
    private final WebDriverWait wait;

    public FakeGoldBarFinder() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get("https://sdetchallenge.fetch.com/");
    }

    public void findFakeBar() {
        //Compare [0, 1, 2] with [3, 4, 5]
        String result1 = weigh(Arrays.asList(0, 1, 2), Arrays.asList(3, 4, 5));

        List<Integer> fakeGroup;
        boolean isHeavier;

        if (result1.equals("=")) {
            //Compare [0, 1, 2] with [6, 7, 8]
            String result2 = weigh(Arrays.asList(0, 1, 2), Arrays.asList(6, 7, 8));
            if (result2.equals("=")) {
                throw new RuntimeException("No fake bar found");
            } else if (result2.equals("<")) {
                fakeGroup = Arrays.asList(6, 7, 8);
                isHeavier = true;
            } else {
                fakeGroup = Arrays.asList(6, 7, 8);
                isHeavier = false;
            }
        } else if (result1.equals("<")) {
            fakeGroup = Arrays.asList(0, 1, 2);
            isHeavier = false;
        } else {
            fakeGroup = Arrays.asList(3, 4, 5);
            isHeavier = false;
        }

        // Identify the fake bar in the fake group
        int fakeBar = identifyFakeBar(fakeGroup, isHeavier);

        //Click the fake bar
        clickFakeBarButton(fakeBar);
    }

    // Function to weigh two groups of bars and return the result
    private String weigh(List<Integer> left, List<Integer> right) {
        fillBowl("left", left);
        fillBowl("right", right);
        clickWeigh();
        String result = getWeighResult();
        clickReset(); // Reset the bowls after each weighing
        return result;
    }

    // Function to fill either the left or right bowl with a set of bars
    private void fillBowl(String side, List<Integer> bars) {
        for (int i = 0; i < bars.size(); i++) {
            WebElement input = driver.findElement(By.id(side + "_" + i));
            input.clear();
            input.sendKeys(String.valueOf(bars.get(i)));
        }
    }

    // Function to click the weigh button and wait for the result to be displayed
    private void clickWeigh() {
        WebElement weighButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("weigh")));
        weighButton.click();
        try {
            Thread.sleep(5000); // Wait for 5 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div.result")));
    }

    // Function to retrieve the weighing result displayed on the webpage
    private String getWeighResult() {
        WebElement resultElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.result")));
        String returnText = resultElement.getText();
        String symbol = returnText.substring(returnText.length() - 1);
        return symbol;
    }

    // Function to click the reset button after each weighing
    private void clickReset() {
        driver.findElement(By.xpath("(//button[@id='reset'])[2]")).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("weigh")));
    }

    // Function to click on the right fake button
    private void clickFakeBarButton(int fakeBar) {
        System.out.println("Clicking button for bar: " + fakeBar);
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(By.id("coin_" + fakeBar)));
        button.click();
    }

    // Function to identify the fake bar
    private int identifyFakeBar(List<Integer> fakeGroup, boolean isHeavier) {
        // Compare the first two bars
        String result = weigh(Collections.singletonList(fakeGroup.get(0)), Collections.singletonList(fakeGroup.get(1)));

        if (result.equals("=")) {
            return fakeGroup.get(2);
        } else if (result.equals("<")) {
            return isHeavier ? fakeGroup.get(1) : fakeGroup.get(0);
        } else {
            return isHeavier ? fakeGroup.get(0) : fakeGroup.get(1);
        }
    }

    // Function to validate the alert and list out the weighings
    private void validateResult() {
        String alertText = wait.until(ExpectedConditions.alertIsPresent()).getText();
        if (alertText.equals("Yay! You find it!")) {
            System.out.println("Success: " + alertText);
        } else {
            System.out.println("Failure: " + alertText);
        }

        driver.switchTo().alert().accept();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        List<WebElement> gameInfoDivs = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("div.game-info")));

        for (int i = 0; i < gameInfoDivs.size(); i++) {
            WebElement gameInfoDiv = gameInfoDivs.get(i);
            List<WebElement> weighingElements = gameInfoDiv.findElements(By.cssSelector("ol li"));
            List<String> weighings = new ArrayList<>();

            for (WebElement weighing : weighingElements) {
                weighings.add(weighing.getText());
            }

            System.out.println("Weighings");
            for (String weighing : weighings) {
                System.out.println(weighing);
            }
        }

    }
    // Function to close the webdriver
    public void close() {
        driver.quit();
    }


    public static void main(String[] args) {
        FakeGoldBarFinder challenge = new FakeGoldBarFinder();
        challenge.findFakeBar();
        challenge.validateResult();
        challenge.close();
    }
}


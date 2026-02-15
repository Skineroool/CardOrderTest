package ru.netology.test;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class CardOrderTest {
    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeAll
    public static void setupDriver() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        // Важные опции для Linux/CI
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--disable-gpu");
        options.addArguments("--remote-allow-origins=*");

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get("http://localhost:9999");
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void shouldSendFormSuccessfully() {
        // Заполняем поле "Фамилия и имя"
        WebElement nameField = driver.findElement(By.cssSelector("[data-test-id='name'] input"));
        nameField.sendKeys("Иванов Иван");

        // Заполняем поле "Мобильный телефон"
        WebElement phoneField = driver.findElement(By.cssSelector("[data-test-id='phone'] input"));
        phoneField.sendKeys("+79270000000");

        // Ставим чекбокс согласия
        WebElement agreement = driver.findElement(By.cssSelector("[data-test-id='agreement']"));
        agreement.click();

        // Нажимаем кнопку "Продолжить"
        WebElement button = driver.findElement(By.cssSelector("button"));
        button.click();

        // Ждём появления сообщения об успехе
        WebElement successMessage = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("[data-test-id='order-success']")
                )
        );

        assertTrue(successMessage.isDisplayed());
        assertTrue(successMessage.getText().contains("успешно отправлена"));
    }
}
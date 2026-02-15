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
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        //options.addArguments("--headless");
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
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Иванов Иван");

        // Заполняем поле "Мобильный телефон"
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79270000000");

        // Ставим чекбокс согласия
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();

        // Нажимаем кнопку "Продолжить"
        driver.findElement(By.cssSelector("button")).click();

        // Ждём появления сообщения об успехе и проверяем текст
        WebElement successMessage = wait.until(
                WebDriverConditions.visibilityOfElementLocated(
                        By.cssSelector("[data-test-id='order-success']")
                )
        );

        assertTrue(successMessage.isDisplayed());
        assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.",
                successMessage.getText().trim());
    }

    // Вспомогательный класс для ожиданий
    private static class WebDriverConditions {
        public static ExpectedCondition<WebElement> visibilityOfElementLocated(By locator) {
            return driver -> {
                WebElement element = driver.findElement(locator);
                return element != null && element.isDisplayed() ? element : null;
            };
        }
    }
}
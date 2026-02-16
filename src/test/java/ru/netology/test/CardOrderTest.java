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
        options.addArguments("--headless");

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

        // Ждём появления сообщения об успехе
        WebElement successMessage = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("[data-test-id='order-success']")
                )
        );

        // ТОЛЬКО ЭТА ЧАСТЬ НОВАЯ - точная проверка текста
        String expectedText = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        String actualText = successMessage.getText().trim();

        assertEquals(expectedText, actualText, "Текст сообщения об успехе не соответствует ожидаемому");
    }
}
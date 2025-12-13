import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class appOrderTest {
    private WebDriver driver;

    @BeforeAll
    public static void setupAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        driver = new ChromeDriver();
        ChromeOptions options = new ChromeOptions();

        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");

        driver = new ChromeDriver(options);

        driver.get("http://localhost:9999");
    }

    @AfterEach
    void tearsDown() {
        driver.quit();
        driver = null;
    }

    @Test
    void shouldSuccessWithMale() {
        driver.findElement(By.className("input__control")).sendKeys("Андрей Рублев");
        driver.findElement(By.cssSelector("[type = \"tel\"]")).sendKeys("+79127564577");
        driver.findElement(By.className("checkbox")).click();
        driver.findElement(By.className("button")).click();

        String text = driver.findElement(By.className("paragraph")).getText();

        assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", text.trim());
    }

    @Test
    void shouldSuccessWithFemale() {
        driver.findElement(By.className("input__control")).sendKeys("Анна-Никита Иванова");
        driver.findElement(By.cssSelector("[type = \"tel\"]")).sendKeys("+79127564500");
        driver.findElement(By.className("checkbox")).click();
        driver.findElement(By.className("button")).click();

        String text = driver.findElement(By.className("paragraph")).getText();

        assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", text.trim());
    }

    @Test
    void shouldSuccessWithShortName() {
        driver.findElement(By.className("input__control")).sendKeys("Гриша");
        driver.findElement(By.cssSelector("[type = \"tel\"]")).sendKeys("+79127563300");
        driver.findElement(By.className("checkbox")).click();
        driver.findElement(By.className("button")).click();

        String text = driver.findElement(By.className("paragraph")).getText();

        assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", text.trim());
    }

    @Test
    void shouldNotSuccessWithUnClickedCheckbox() {
        driver.findElement(By.className("input__control")).sendKeys("Наталья Королёва");
        driver.findElement(By.cssSelector("[type = \"tel\"]")).sendKeys("+79127564577");
        driver.findElement(By.className("button")).click();

        List<WebElement> inputSub = driver.findElements(By.className("input__sub"));

        assertEquals("rgba(255, 92, 92, 1)", inputSub.get(0).getCssValue("color"));
    }

    @Test
    void shouldNotSuccessWithEmptyName() {
        driver.findElement(By.cssSelector("[type = \"tel\"]")).sendKeys("+79220064577");
        driver.findElement(By.className("checkbox")).click();
        driver.findElement(By.className("button")).click();

        List<WebElement> text = driver.findElements(By.className("input__sub"));

        assertEquals("Поле обязательно для заполнения", text.get(0).getText().trim());
    }

    @Test
    void shouldNotSuccessWithEmptyTelephone() {
        driver.findElement(By.className("input__control")).sendKeys("Регина Дубовицкая");
        driver.findElement(By.className("checkbox")).click();
        driver.findElement(By.className("button")).click();

        List<WebElement> inputSub = driver.findElements(By.className("input__sub"));

        assertEquals("Поле обязательно для заполнения", inputSub.get(1).getText().trim());
    }

    @Test
    void shouldNotSuccessWithAllFieldsIsEmpty() {
        driver.findElement(By.className("checkbox")).click();
        driver.findElement(By.className("button")).click();

        List<WebElement> inputSub = driver.findElements(By.className("input__sub"));

        assertEquals("Поле обязательно для заполнения", inputSub.get(0).getText().trim());
    }

    @Test
    void shouldNotSuccessWithLatinName() {
        driver.findElement(By.className("input__control")).sendKeys("Goolya");
        driver.findElement(By.cssSelector("[type = \"tel\"]")).sendKeys("+79127564500");
        driver.findElement(By.className("checkbox")).click();
        driver.findElement(By.className("button")).click();

        List<WebElement> inputSub = driver.findElements(By.className("input__sub"));

        String text = inputSub.get(0).getText();

        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", text.trim());
    }

    @Test
    void shouldNotSuccessWithTelephoneStarts8() {
        driver.findElement(By.className("input__control")).sendKeys("Дмитрий Иванович Федоров");
        driver.findElement(By.cssSelector("[type = \"tel\"]")).sendKeys("89127564500");
        driver.findElement(By.className("checkbox")).click();
        driver.findElement(By.className("button")).click();

        List<WebElement> inputSub = driver.findElements(By.className("input__sub"));

        String text = inputSub.get(1).getText();

        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", text.trim());
    }

    @Test
    void shouldNotSuccessWithTelephoneHave10Numbers() {
        driver.findElement(By.className("input__control")).sendKeys("Дмитрий Иванович Федоров");
        driver.findElement(By.cssSelector("[type = \"tel\"]")).sendKeys("+7912756450");
        driver.findElement(By.className("checkbox")).click();
        driver.findElement(By.className("button")).click();

        List<WebElement> inputSub = driver.findElements(By.className("input__sub"));

        String text = inputSub.get(1).getText();

        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", text.trim());
    }

    @Test
    void shouldNotSuccessWithTelephoneHave12Numbers() {
        driver.findElement(By.className("input__control")).sendKeys("Дмитрий Иванович Федоров");
        driver.findElement(By.cssSelector("[type = \"tel\"]")).sendKeys("+791275645098");
        driver.findElement(By.className("checkbox")).click();
        driver.findElement(By.className("button")).click();

        List<WebElement> inputSub = driver.findElements(By.className("input__sub"));

        String text = inputSub.get(1).getText();

        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", text.trim());
    }
}
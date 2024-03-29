package ru.netology;

import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import com.codeborne.selenide.logevents.SelenideLogger;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;
import static java.time.Duration.ofSeconds;

public class CardOrderTest {
    String name = DataGenerator.makeName();
    String phone = DataGenerator.makePhone();
    String city = DataGenerator.makeCity();

    public static void correctFieldsCheks() {
        String name = DataGenerator.makeName();
        String phone = DataGenerator.makePhone();
        String city = DataGenerator.makeCity();
        $("[data-test-id=city] input").setValue(city);
        $("[data-test-id=date] input").sendKeys(Keys.BACK_SPACE);
        $("[data-test-id=date] input").sendKeys(DataGenerator.forwardDate(3));
        $("[data-test-id=name] input").setValue(name);
        $("[data-test-id=phone] input").setValue(phone);
    }

    public static void clickButton() {
        $("[data-test-id=agreement]").click();
        $(".button__text").click();
    }
    @BeforeAll
    static void setUpAllAllure() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }
    @BeforeEach
    void setUp() {
        open("http://localhost:9999/");
    }


    @Test
    void shouldNewRequest() {
        $("[data-test-id=city] input").setValue(city);
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id=date] input").sendKeys(DataGenerator.forwardDate(3));
        $("[data-test-id=name] input").setValue(name);
        $("[data-test-id=phone] input").setValue(phone);
        $("[data-test-id=agreement] .checkbox__box").click();
        $(withText("Запланировать")).click();
        $("[data-test-id='success-notification']").shouldBe(visible, ofSeconds(15));
        $$("[data-test-id='success-notification']>.notification__content").findBy(text("Встреча успешно запланирована на")).shouldBe(visible);
        $("[placeholder='Дата встречи']").sendKeys(Keys.chord(Keys.CONTROL, "a")
                + Keys.BACK_SPACE);
        $("[placeholder='Дата встречи']").setValue(DataGenerator.forwardDate(5));
        $(withText("Запланировать")).click();
        $("[data-test-id='replan-notification']").shouldBe(visible);
        $("[data-test-id='replan-notification']>.notification__content").shouldHave(text("У вас уже" +
                " запланирована встреча на другую дату. Перепланировать?"));
        $("[data-test-id=date] input").setValue(DataGenerator.forwardDate(5));
        $("[data-test-id=replan-notification] .notification__title").shouldHave(exactText("Необходимо" +
                " подтверждение"));
        $(withText("Перепланировать")).click();
        $("[data-test-id='success-notification']>.notification__content").shouldBe(visible)
                .shouldHave(exactText("Встреча успешно запланирована на " + DataGenerator.forwardDate(5)));
    }


    @Test
    void shouldRequest() {
        $("[data-test-id=city] input").setValue(city);
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(DataGenerator.forwardDate(3));
        $("[data-test-id=name] input").setValue(name);
        $("[data-test-id=phone] input").setValue(phone);
        $("[data-test-id=agreement]").click();
        $(".button__text").click();
        $(withText("Успешно!"))
                .shouldBe(visible, Duration.ofSeconds(15));
        $$(".notification").findBy(text("Встреча успешно запланирована на")).shouldBe(visible);
    }
}




package ru.netology.delivery.test;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.delivery.data.DataGenerator;


import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.value;
import static com.codeborne.selenide.Condition.visible;
import static org.openqa.selenium.By.cssSelector;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;

class DeliveryTest {
    @BeforeEach
    void setup() {
        open(System.getProperty("sut.url", "http://localhost:9999"));
    }

    private void selectDate(String planningDate, String planningDay,
                            long currentCalendarDateOffset, long meetingDateOffset) {
        $("[data-test-id='date'] input").click();
        if (!DataGenerator.generateDate(currentCalendarDateOffset, "MM")
                .equals(DataGenerator.generateDate(meetingDateOffset, "MM"))) {
            $(cssSelector(".calendar__arrow_direction_right:not(.calendar__arrow_double)"))
                    .click();
        }
        $$(cssSelector("[data-day]")).findBy(text(planningDay)).click();
    }

    @Test
    @DisplayName("Should successfully plan and replan meeting")
    void shouldSuccessfullyPlanAndReplanMeeting() {
        var validUser = DataGenerator.Registration.generateUser("ru");

        $("[data-test-id='city'] input")
                .setValue(validUser.getCity())
                .shouldHave(value(validUser.getCity()));

        String planningDate = DataGenerator.generateDate(7, "dd.MM.yyyy");
        String planningDay = DataGenerator.generateDate(7, "dd").replaceFirst("^0", "");
        selectDate(planningDate, planningDay, 3, 7);

        $("[data-test-id='name'] input")
                .setValue(validUser.getName())
                .shouldHave(value(validUser.getName()));
        $("[data-test-id='phone'] input")
                .setValue(validUser.getPhone())
                .shouldHave(value(DataGenerator.formatPhone(validUser.getPhone())));
        $("[data-test-id='agreement']").click();

        $$("button").findBy(text("Запланировать")).click();

        $("[data-test-id='success-notification']")
                .shouldBe(visible)
                .shouldHave(text("Встреча успешно запланирована на " + planningDate));

        String replanDate = DataGenerator.generateDate(10, "dd.MM.yyyy");
        String replanDay = DataGenerator.generateDate(10, "dd").replaceFirst("^0", "");
        selectDate(replanDate, replanDay, 7, 10);

        $$("button").findBy(text("Запланировать")).click();

        $("[data-test-id='replan-notification']")
                .shouldBe(visible)
                .shouldHave(
                        text("У вас уже запланирована встреча на другую дату. Перепланировать?")
                );

        $("[data-test-id='replan-notification']")
                .$$("button")
                .findBy(text("Перепланировать"))
                .shouldBe(Condition.enabled)
                .click();

        $("[data-test-id='success-notification']")
                .shouldBe(visible)
                .shouldHave(text("Встреча успешно запланирована на " + replanDate));
    }
}

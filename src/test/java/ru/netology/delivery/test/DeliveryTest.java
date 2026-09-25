package ru.netology.delivery.test;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.delivery.data.DataGenerator;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;

class DeliveryTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successful plan and replan meeting")
    void shouldSuccessfulPlanAndReplanMeeting() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        var daysToAddForSecondMeeting = 7;
        var secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);

        $("[data-test-id='city'] input").setValue(validUser.getCity());

        $("[data-test-id='date'] input").clear();
        $("[data-test-id='date'] input").setValue(firstMeetingDate);

        $("[data-test-id='name'] input").setValue(validUser.getName());
        $("[data-test-id='phone'] input").setValue(validUser.getPhone());
        $("[data-test-id='agreement']").click();

        $$("button").findBy(text("Запланировать")).click();

        $("[data-test-id='success-notification']")
                .shouldBe(visible)
                .shouldHave(text("Встреча успешно запланирована"));

        $("[data-test-id='date'] input").clear();
        $("[data-test-id='date'] input").setValue(secondMeetingDate);

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
                .shouldHave(text("Встреча успешно запланирована"));
    }
}
package ru.netology.ibank.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.ibank.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

class AuthTest {

    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successfully login with active registered user")
    void shouldSuccessfullyLoginWithActiveRegisteredUser() {

        var registeredUser = DataGenerator.getRegisteredUser("active");

        $("[data-test-id='login'] input")
                .setValue(registeredUser.getLogin());

        $("[data-test-id='password'] input")
                .setValue(registeredUser.getPassword());

        $("button.button")
                .click();

        $("h2")
                .shouldHave(exactText("Личный кабинет"))
                .shouldBe(visible);
    }

    @Test
    @DisplayName("Should get error message if login with not registered user")
    void shouldGetErrorIfNotRegisteredUser() {

        var notRegisteredUser = DataGenerator.getUser("active");

        $("[data-test-id='login'] input")
                .setValue(notRegisteredUser.getLogin());

        $("[data-test-id='password'] input")
                .setValue(notRegisteredUser.getPassword());

        $("button.button")
                .click();

        $("[data-test-id='error-notification'] .notification__content")
                .shouldHave(
                        text("Ошибка! Неверно указан логин или пароль"),
                        Duration.ofSeconds(10)
                )
                .shouldBe(visible);
    }

    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIfBlockedUser() {

        var blockedUser = DataGenerator.getRegisteredUser("blocked");

        $("[data-test-id='login'] input")
                .setValue(blockedUser.getLogin());

        $("[data-test-id='password'] input")
                .setValue(blockedUser.getPassword());

        $("button.button")
                .click();

        $("[data-test-id='error-notification'] .notification__content")
                .shouldHave(
                        text("Ошибка! Пользователь заблокирован"),
                        Duration.ofSeconds(10)
                )
                .shouldBe(visible);
    }

    @Test
    @DisplayName("Should get error message if login with wrong login")
    void shouldGetErrorIfWrongLogin() {

        var registeredUser = DataGenerator.getRegisteredUser("active");

        var wrongLogin = DataGenerator.getRandomLogin();

        $("[data-test-id='login'] input")
                .setValue(wrongLogin);

        $("[data-test-id='password'] input")
                .setValue(registeredUser.getPassword());

        $("button.button")
                .click();

        $("[data-test-id='error-notification'] .notification__content")
                .shouldHave(
                        text("Ошибка! Неверно указан логин или пароль"),
                        Duration.ofSeconds(10)
                )
                .shouldBe(visible);
    }

    @Test
    @DisplayName("Should get error message if login with wrong password")
    void shouldGetErrorIfWrongPassword() {

        var registeredUser = DataGenerator.getRegisteredUser("active");

        var wrongPassword = DataGenerator.getRandomPassword();

        $("[data-test-id='login'] input")
                .setValue(registeredUser.getLogin());

        $("[data-test-id='password'] input")
                .setValue(wrongPassword);

        $("button.button")
                .click();

        $("[data-test-id='error-notification'] .notification__content")
                .shouldHave(
                        text("Ошибка! Неверно указан логин или пароль"),
                        Duration.ofSeconds(10)
                )
                .shouldBe(visible);
    }
}
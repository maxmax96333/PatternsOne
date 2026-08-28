package ru.netology.ibank.data;

import lombok.Value;

import java.util.Random;

public class DataGenerator {

    private static final Random random = new Random();

    private DataGenerator() {
    }

    public static RegistrationDto getUser(String status) {
        return new RegistrationDto(
                getRandomLogin(),
                getRandomPassword(),
                status
        );
    }

    public static RegistrationDto getRegisteredUser(String status) {
        RegistrationDto user = getUser(status);

        ApiHelper.sendRequest(user);

        return user;
    }

    public static String getRandomLogin() {
        return "user" + random.nextInt(1000000);
    }

    public static String getRandomPassword() {
        return "Password" + random.nextInt(1000000);
    }

    @Value
    public static class RegistrationDto {
        String login;
        String password;
        String status;
    }
}
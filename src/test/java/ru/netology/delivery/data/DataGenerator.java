package ru.netology.delivery.data;

import com.github.javafaker.Faker;
import lombok.Value;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Random;

public class DataGenerator {

    private DataGenerator() {
    }

    public static String generateDate(long addDays, String pattern) {
        return LocalDate.now()
                .plusDays(addDays)
                .format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String generateCity(Faker faker) {
        String[] cities = {
                "Москва",
                "Санкт-Петербург",
                "Казань",
                "Тула",
                "Воронеж",
                "Калуга"
        };

        return cities[new Random().nextInt(cities.length)];
    }

    public static String generateName(Faker faker) {
        return faker.name().fullName().replace('ё', 'е').replace('Ё', 'Е');
    }

    public static String generatePhone(Faker faker) {
        return "+7" + faker.number().digits(10);
    }

    public static String formatPhone(String phone) {
        return phone.replaceFirst("(\\+7)(\\d{3})(\\d{3})(\\d{2})(\\d{2})", "$1 $2 $3 $4 $5");
    }

    public static class Registration {
        private static Faker faker;

        private Registration() {
        }

        public static UserInfo generateUser(String locale) {
            faker = new Faker(new Locale(locale));

            return new UserInfo(
                    generateCity(faker),
                    generateName(faker),
                    generatePhone(faker)
            );
        }
    }

    @Value
    public static class UserInfo {
        String city;
        String name;
        String phone;
    }
}

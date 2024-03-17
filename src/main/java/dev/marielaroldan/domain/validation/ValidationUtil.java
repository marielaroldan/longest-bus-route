package dev.marielaroldan.domain.validation;

import io.vavr.control.Validation;

public class ValidationUtil {
    public static Validation<String, Long> validateLong(String longAsString) {
        try {
            return Validation.valid(Long.parseLong(longAsString));
        } catch (Exception e) {
            return Validation.invalid("Error parsing long ids");
        }
    }

    public static Validation<String, Integer> validateInteger(String numberAsString) {
        try {
            return Validation.valid(Integer.parseInt(numberAsString));
        } catch (NumberFormatException e) {
            return Validation.invalid("Error parsing integer ids");
        }
    }

    public static Validation<String, String> validateString(String stringValue) {
        return stringValue == null || stringValue.isEmpty() ? Validation.invalid("Error in String value")
                :Validation.valid(stringValue);
    }
}

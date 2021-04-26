package com.example.demo_web.model.validator;

public class MediaPersonValidator {
    private static final String DIGIT_PATTERN = "\\d+";

    private MediaPersonValidator() {}

    public static boolean isValidId(String id) {
        return id.matches(DIGIT_PATTERN);
    }
}

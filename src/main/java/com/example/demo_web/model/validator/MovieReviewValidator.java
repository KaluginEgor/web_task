package com.example.demo_web.model.validator;

import com.example.demo_web.model.entity.MovieReview;

import java.util.LinkedHashMap;
import java.util.Map;

public class MovieReviewValidator {
    private static final String TITLE_PATTERN = "[A-Za-zА-Яа-яЁё0-9]{1,100}";
    private static final String BODY_PATTERN = "[A-Za-zА-Яа-яЁё0-9]{1,1000}";
    private static final String TITLE = "title";
    private static final String BODY = "body";

    private MovieReviewValidator(){}

    public static Map<String, Boolean> validateData(String title, String body) {
        Map<String, Boolean> validations = new LinkedHashMap<>();
        validations.put(TITLE, isTitleValid(TITLE));
        validations.put(BODY, isBodyValid(BODY));
        return validations;
    }

    public static boolean isValid(MovieReview review) {
        return isTitleValid(review.getTitle()) && isBodyValid(review.getBody());
    }

    private static boolean isTitleValid(String title) {
        return title.matches(TITLE_PATTERN);
    }

    private static boolean isBodyValid(String body) {
        return body.matches(BODY_PATTERN);
    }
}

package com.example.demo_web.model.validator;

import com.example.demo_web.model.entity.MovieRating;
import com.example.demo_web.model.entity.MovieReview;

import java.util.LinkedHashMap;
import java.util.Map;

public class MovieValidator {
    private static final String MOVIE_REVIEW_TITLE_PATTERN = "[A-Za-zА-Яа-яЁё0-9]{1,100}";
    private static final String MOVIE_REVIEW_BODY_PATTERN = "[A-Za-zА-Яа-яЁё0-9]{1,1000}";
    private static final String TITLE = "title";
    private static final String BODY = "body";
    private MovieValidator() {}

    public static Map<String, Boolean> validateMovieReviewData(String title, String body) {
        Map<String, Boolean> validations = new LinkedHashMap<>();
        validations.put(TITLE, isMovieReviewTitleValid(TITLE));
        validations.put(BODY, isMovieReviewBodyValid(BODY));
        return validations;
    }

    public static boolean isMovieReviewValid(MovieReview review) {
        return isMovieReviewTitleValid(review.getTitle()) && isMovieReviewBodyValid(review.getBody());
    }

    public static boolean isMovieReviewTitleValid(String title) {
        return title.matches(MOVIE_REVIEW_TITLE_PATTERN);
    }

    public static boolean isMovieReviewBodyValid(String body) {
        return body.matches(MOVIE_REVIEW_BODY_PATTERN);
    }

    public static boolean isMovieRatingValid(MovieRating movieRating) {
        float value = movieRating.getValue();
        return (value >= 0 && value <= 10);
    }


}

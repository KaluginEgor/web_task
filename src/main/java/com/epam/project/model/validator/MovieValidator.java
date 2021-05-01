package com.epam.project.model.validator;

import com.epam.project.controller.command.Attribute;
import com.epam.project.model.entity.GenreType;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.LinkedHashMap;
import java.util.Map;

public class MovieValidator {
    private static final String DIGIT_PATTERN = "\\d+";
    private static final String RATING_PATTEN = "[1-9]|10";
    private static final String TITLE_PATTERN = "[A-Za-zА-Яа-яЁё0-9\\s()\"':?!.,-]{1,1000}";
    private static final String TEXT_BODY_PATTERN = "[A-Za-zА-Яа-яЁё0-9\\s()\"':?!.,-]{1,10000}";
    private static final String PICTURE_PATTERN = "[^\\s]+(.*?)\\.(jpg|jpeg|png|gif|JPG|JPEG|PNG|GIF)$";
    private static final String GENRE_PATTERN = "[0-" + (GenreType.values().length - 1) + "]";

    private MovieValidator() {}

    public static boolean isValidId(String id) {
        return id.matches(DIGIT_PATTERN);
    }

    public static boolean isTitleValid(String title) {
        return title.matches(TITLE_PATTERN);
    }

    public static boolean isTextBodyValid(String text) {
        return text.matches(TEXT_BODY_PATTERN);
    }

    public static boolean isReleaseDateValid(String stringReleaseDate) {
        try {
            LocalDate birthday = LocalDate.parse(stringReleaseDate);
            return birthday.isBefore(LocalDate.now());
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static boolean isPictureValid(String picture) {
        return picture.matches(PICTURE_PATTERN);
    }

    public static boolean areGenresValid(String[] genresId) {
        if (genresId != null) {
            for (String genreId : genresId) {
                if (!genreId.matches(GENRE_PATTERN)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean areMediaPersonsIdValid(String[] stringMediaPersonsId) {
        if (stringMediaPersonsId != null) {
            for (String stringMediaPersonId : stringMediaPersonsId) {
                if (!stringMediaPersonId.matches(DIGIT_PATTERN)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean isRatingValueValid(String stringValue) {
        return stringValue.matches(RATING_PATTEN);
    }

    public static Map<String, Boolean> validateMovieData(String title, String description, String stringReleaseDate, String picture,
                                                  String[] stringGenresId, String[] stringMediaPeopleId) {
        Map<String, Boolean> validations = new LinkedHashMap<>();
        validations.put(Attribute.MOVIE_TITLE, isTitleValid(title));
        validations.put(Attribute.MOVIE_DESCRIPTION, isTextBodyValid(description));
        validations.put(Attribute.MOVIE_RELEASE_DATE, isReleaseDateValid(stringReleaseDate));
        validations.put(Attribute.PICTURE, isPictureValid(picture));
        validations.put(Attribute.MOVIE_GENRE, areGenresValid(stringGenresId));
        validations.put(Attribute.MOVIE_CREW, areMediaPersonsIdValid(stringMediaPeopleId));
        return validations;
    }

    public static Map<String, Boolean> validateMovieRatingData(String stringMovieId, String stringUserId, String stringValue) {
        Map<String, Boolean> validations = new LinkedHashMap<>();
        validations.put(Attribute.MOVIE_ID, isValidId(stringMovieId));
        validations.put(Attribute.USER_ID, isValidId(stringUserId));
        validations.put(Attribute.MOVIE_RATING_VALUE, isRatingValueValid(stringValue));
        return validations;
    }

    public static Map<String, Boolean> validateMovieReviewData(String title, String body, String stringMovieId, String stringUserId) {
        Map<String, Boolean> validations = new LinkedHashMap<>();
        validations.put(Attribute.MOVIE_REVIEW_TITLE, isTitleValid(title));
        validations.put(Attribute.MOVIE_REVIEW_BODY, isTextBodyValid(body));
        validations.put(Attribute.MOVIE_ID, isValidId(stringMovieId));
        validations.put(Attribute.USER_ID, isValidId(stringUserId));
        return validations;
    }
}

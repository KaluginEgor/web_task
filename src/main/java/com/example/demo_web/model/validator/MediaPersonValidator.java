package com.example.demo_web.model.validator;

import com.example.demo_web.controller.command.Attribute;
import com.example.demo_web.controller.command.RequestParameter;
import com.example.demo_web.model.entity.OccupationType;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

public class MediaPersonValidator {
    private static final String DIGIT_PATTERN = "\\d+";
    private static final String NAME_PATTERN = "[A-Za-zА-Яа-яЁё]{1,40}";
    private static final String BIO_PATTERN = "[A-Za-zА-Яа-яЁё0-9\\s?!.,-]{1,10000}";
    private static final String OCCUPATION_ID_PATTERN = "[0-" + (OccupationType.values().length - 1) + "]";
    private static final String PICTURE_PATTERN = "[^\\s]+(.*?)\\.(jpg|jpeg|png|gif|JPG|JPEG|PNG|GIF)$";

    private MediaPersonValidator() {}

    public static boolean isValidId(String id) {
        return id.matches(DIGIT_PATTERN);
    }

    public static boolean isValidName(String name) { return name.matches(NAME_PATTERN);}

    public static boolean isValidBio(String bio) { return bio.matches(BIO_PATTERN);}

    public static boolean isOccupationIdValid(String stringOccupationId) {
        return stringOccupationId.matches(OCCUPATION_ID_PATTERN);
    }

    public static boolean isBirthdayValid(String stringBirthday) {
        try {
            LocalDate birthday = LocalDate.parse(stringBirthday);
            return birthday.isBefore(LocalDate.now());
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static boolean isPictureValid(String picture) {
        return picture.matches(PICTURE_PATTERN);
    }

    public static boolean areMoviesIdValid(String[] stringMoviesId) {
        if (stringMoviesId != null) {
            for (String stringMovieId : stringMoviesId) {
                if (!stringMovieId.matches(DIGIT_PATTERN)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static Map<String, Boolean> validateData(String firstName, String secondName, String bio, String stringOccupationId,
                                                    String stringBirthday, String picture, String[] stringMoviesId) {
        Map<String, Boolean> validations = new LinkedHashMap<>();
        validations.put(Attribute.FIRST_NAME, isValidName(firstName));
        validations.put(Attribute.SECOND_NAME, isValidName(secondName));
        validations.put(Attribute.BIO, isValidBio(bio));
        validations.put(Attribute.MEDIA_PERSON_OCCUPATION_TYPE, isOccupationIdValid(stringOccupationId));
        validations.put(Attribute.MEDIA_PERSON_BIRTHDAY, isBirthdayValid(stringBirthday));
        validations.put(Attribute.PICTURE, isPictureValid(picture));
        validations.put(Attribute.MEDIA_PERSON_MOVIES, areMoviesIdValid(stringMoviesId));
        return validations;
    }
}

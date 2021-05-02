package com.epam.project.model.validator;

import com.epam.project.controller.command.Attribute;
import com.epam.project.model.entity.OccupationType;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The type Media person validator.
 */
public class MediaPersonValidator {
    private static final String DIGIT_PATTERN = "\\d+";
    private static final String NAME_PATTERN = "[A-Za-zА-Яа-яЁё]{1,40}";
    private static final String BIO_PATTERN = "[A-Za-zА-Яа-яЁё0-9\\s()\"':?!.,-]{1,10000}";
    private static final String PICTURE_PATTERN = "[^\\s]+(.*?)\\.(jpg|jpeg|png|gif|JPG|JPEG|PNG|GIF)$";

    private MediaPersonValidator() {}

    /**
     * Is valid id boolean.
     *
     * @param id the id
     * @return the boolean
     */
    public static boolean isValidId(String id) {
        return id.matches(DIGIT_PATTERN);
    }

    /**
     * Is valid name boolean.
     *
     * @param name the name
     * @return the boolean
     */
    public static boolean isValidName(String name) { return name.matches(NAME_PATTERN);}

    /**
     * Is valid bio boolean.
     *
     * @param bio the bio
     * @return the boolean
     */
    public static boolean isValidBio(String bio) { return bio.matches(BIO_PATTERN);}

    /**
     * Is occupation id valid boolean.
     *
     * @param stringOccupationId the string occupation id
     * @return the boolean
     */
    public static boolean isOccupationIdValid(String stringOccupationId) {
        if (stringOccupationId.matches(DIGIT_PATTERN)) {
            return (Integer.parseInt(stringOccupationId) >= 0 && Integer.parseInt(stringOccupationId) < OccupationType.values().length);
        } else {
            return false;
        }
    }

    /**
     * Is birthday valid boolean.
     *
     * @param stringBirthday the string birthday
     * @return the boolean
     */
    public static boolean isBirthdayValid(String stringBirthday) {
        try {
            LocalDate birthday = LocalDate.parse(stringBirthday);
            return birthday.isBefore(LocalDate.now());
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Is picture valid boolean.
     *
     * @param picture the picture
     * @return the boolean
     */
    public static boolean isPictureValid(String picture) {
        return picture.matches(PICTURE_PATTERN);
    }

    /**
     * Are movies id valid boolean.
     *
     * @param stringMoviesId the string movies id
     * @return the boolean
     */
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

    /**
     * Validate data map.
     *
     * @param firstName          the first name
     * @param secondName         the second name
     * @param bio                the bio
     * @param stringOccupationId the string occupation id
     * @param stringBirthday     the string birthday
     * @param picture            the picture
     * @param stringMoviesId     the string movies id
     * @return the map
     */
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

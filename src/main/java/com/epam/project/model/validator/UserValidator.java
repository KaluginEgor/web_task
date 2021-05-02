package com.epam.project.model.validator;

import com.epam.project.controller.command.Attribute;
import com.epam.project.model.entity.UserRole;
import com.epam.project.model.entity.UserState;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The type User validator.
 */
public class UserValidator {
    private static final String DIGIT_PATTERN = "[+-]?\\d+";
    private static final String LOGIN_PATTERN = "[\\wа-яЁё_.]{1,40}";
    private static final String NAME_PATTERN = "[A-Za-zА-Яа-яЁё]{1,40}";
    private static final String PICTURE_PATTERN = "[^\\s]+(.*?)\\.(jpg|jpeg|png|gif|JPG|JPEG|PNG|GIF)$";
    private static final String PASSWORD_PATTERN = "\\w{8,100}";
    private static final String EMAIL_PATTERN = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"" +
            "(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@" +
            "(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}" +
            "(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";

    private UserValidator() {}

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
     * Is valid login boolean.
     *
     * @param login the login
     * @return the boolean
     */
    public static boolean isValidLogin(String login) {
        return login.matches(LOGIN_PATTERN);
    }

    /**
     * Is valid name boolean.
     *
     * @param name the name
     * @return the boolean
     */
    public static boolean isValidName(String name) { return name.matches(NAME_PATTERN);}

    /**
     * Is valid email boolean.
     *
     * @param email the email
     * @return the boolean
     */
    public static boolean isValidEmail(String email) {
        return email.matches(EMAIL_PATTERN);
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
     * Is valid password boolean.
     *
     * @param password the password
     * @return the boolean
     */
    public static boolean isValidPassword(String password) {
        return password.matches(PASSWORD_PATTERN);
    }

    /**
     * Validate registration data map.
     *
     * @param login      the login
     * @param email      the email
     * @param firstName  the first name
     * @param secondName the second name
     * @param password   the password
     * @return the map
     */
    public static Map<String, Boolean> validateRegistrationData(String login, String email, String firstName, String secondName, String password) {
        Map<String, Boolean> validations = new LinkedHashMap<>();
        validations.put(Attribute.LOGIN, isValidLogin(login));
        validations.put(Attribute.EMAIL, isValidEmail(email));
        validations.put(Attribute.FIRST_NAME, isValidName(firstName));
        validations.put(Attribute.SECOND_NAME, isValidName(secondName));
        validations.put(Attribute.PASSWORD, isValidPassword(password));
        return validations;
    }

    /**
     * Validate login data map.
     *
     * @param login    the login
     * @param password the password
     * @return the map
     */
    public static Map<String, Boolean> validateLoginData(String login, String password) {
        Map<String, Boolean> validations = new LinkedHashMap<>();
        validations.put(Attribute.LOGIN, isValidLogin(login));
        validations.put(Attribute.PASSWORD, isValidPassword(password));
        return validations;
    }

    /**
     * Validate update data map.
     *
     * @param email      the email
     * @param firstName  the first name
     * @param secondName the second name
     * @param picture    the picture
     * @return the map
     */
    public static Map<String,Boolean> validateUpdateData(String email, String firstName, String secondName, String picture) {
        Map<String, Boolean> validations = new LinkedHashMap<>();
        validations.put(Attribute.EMAIL, isValidEmail(email));
        validations.put(Attribute.FIRST_NAME, isValidName(firstName));
        validations.put(Attribute.SECOND_NAME, isValidName(secondName));
        validations.put(Attribute.PICTURE, isPictureValid(picture));
        return validations;
    }
}

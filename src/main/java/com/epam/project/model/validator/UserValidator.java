package com.epam.project.model.validator;

import com.epam.project.controller.command.Attribute;
import com.epam.project.model.entity.UserRole;
import com.epam.project.model.entity.UserState;

import java.util.LinkedHashMap;
import java.util.Map;

public class UserValidator {
    private static final String DIGIT_PATTERN = "[+-]?\\d+";
    private static final String LOGIN_PATTERN = "[A-Za-zА-Яа-яЁё0-9]{1,40}";
    private static final String NAME_PATTERN = "[A-Za-zА-Яа-яЁё]{1,40}";
    private static final String EMAIL_PATTERN = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,}$";
    private static final String PICTURE_PATTERN = "[^\\s]+(.*?)\\.(jpg|jpeg|png|gif|JPG|JPEG|PNG|GIF)$";
    private static final String PASSWORD_PATTERN = ".{8,100}";
    private static final String ROLE_PATTERN = "[0-" + (UserRole.values().length - 1) + "]";
    private static final String STATE_PATTERN = "[0-" + (UserState.values().length - 1) + "]";
    private static final String LOGIN = "login";
    private static final String EMAIL = "email";
    private static final String FIRST_NAME = "firstName";
    private static final String SECOND_NAME = "secondName";
    private static final String PASSWORD = "password";

    private UserValidator() {}

    public static boolean isValidId(String id) {
        return id.matches(DIGIT_PATTERN);
    }

    public static boolean isValidLogin(String login) {
        return login.matches(LOGIN_PATTERN);
    }

    public static boolean isValidName(String name) { return name.matches(NAME_PATTERN);}

    public static boolean isValidEmail(String email) {
        return email.matches(EMAIL_PATTERN);
    }

    public static boolean isValidRole(String stringRole) {
        return stringRole.matches(ROLE_PATTERN);
    }

    public static boolean isValidState(String stringState) {
        return stringState.matches(STATE_PATTERN);
    }

    public static boolean isPictureValid(String picture) {
        return picture.matches(PICTURE_PATTERN);
    }

    public static boolean isValidPassword(String password) {
        return password.matches(PASSWORD_PATTERN);
    }

    public static Map<String, Boolean> validateRegistrationData(String login, String email, String firstName, String secondName, String password) {
        Map<String, Boolean> validations = new LinkedHashMap<>();
        validations.put(LOGIN, isValidLogin(login));
        validations.put(EMAIL, isValidEmail(email));
        validations.put(FIRST_NAME, isValidName(firstName));
        validations.put(SECOND_NAME, isValidName(secondName));
        validations.put(PASSWORD, isValidPassword(password));
        return validations;
    }

    public static Map<String, Boolean> validateLoginData(String login, String password) {
        Map<String, Boolean> validations = new LinkedHashMap<>();
        validations.put(LOGIN, isValidLogin(login));
        validations.put(PASSWORD, isValidPassword(password));
        return validations;
    }

    public static Map<String,Boolean> validateUpdateData(String email, String firstName, String secondName, String picture) {
        Map<String, Boolean> validations = new LinkedHashMap<>();
        validations.put(Attribute.EMAIL, isValidEmail(email));
        validations.put(Attribute.FIRST_NAME, isValidName(firstName));
        validations.put(Attribute.SECOND_NAME, isValidName(secondName));
        validations.put(Attribute.PICTURE, isPictureValid(picture));
        return validations;
    }
}

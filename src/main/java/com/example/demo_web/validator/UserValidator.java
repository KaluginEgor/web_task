package com.example.demo_web.validator;

public class UserValidator {
    private static final String LOGIN_PATTERN = "[A-Za-zА-Яа-яЁё0-9]{4,}";
    private static final String NAME_PATTERN = "[A-Za-zА-Яа-яЁё0]{4,}";
    private static final String EMAIL_PATTERN = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,}$";
    private static final String PASSWORD_PATTERN = ".{8,}";

    private UserValidator() {}

    public static boolean isValidLogin(String login) {
        return login.matches(LOGIN_PATTERN);
    }

    public static boolean isValidName(String name) { return name.matches(NAME_PATTERN);}

    public static boolean isValidEmail(String email) {
        return email.matches(EMAIL_PATTERN);
    }

    public static boolean isValidPassword(String password) {
        return password.matches(PASSWORD_PATTERN);
    }
}

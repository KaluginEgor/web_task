package com.example.demo_web.validator;

public class UserValidator {
    private final String LOGIN_PATTERN = "[A-Za-zА-Яа-яЁё0-9]{4,}";
    private final String EMAIL_PATTERN = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,}$";
    private final String PASSWORD_PATTERN = ".{8,}";

    public boolean isValidLogin(String login) {
        return login.matches(LOGIN_PATTERN);
    }

    public boolean isValidEmail(String email) {
        return email.matches(EMAIL_PATTERN);
    }

    public boolean isValidPassword(String password) {
        return password.matches(PASSWORD_PATTERN);
    }
}

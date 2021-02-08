package com.example.demo_web.builder.impl;

import com.example.demo_web.builder.Builder;
import com.example.demo_web.entity.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserBuilder implements Builder<User> {
    //private static final String DEFAULT_ID_COLUMN = "id";
    //private static final String IS_ADMIN_COLUMN = "is_admin";
    private static final String LOGIN_COLUMN = "login";
    private static final String EMAIL_COLUMN = "email";
    private static final String PASSWORD_COLUMN = "password";
    //private static final String FIRST_NAME_COLUMN = "first_name";
    //private static final String SECOND_NAME_COLUMN = "second_name";

    @Override
    public User build(ResultSet resultSet) throws SQLException {
        if (resultSet.wasNull()) {
            return null;
        }
        String login = resultSet.getString(LOGIN_COLUMN);
        String email = resultSet.getString(EMAIL_COLUMN);
        String password = resultSet.getString(PASSWORD_COLUMN);
        return new User(login, email, password);
    }
}

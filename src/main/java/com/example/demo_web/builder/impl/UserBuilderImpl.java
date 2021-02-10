package com.example.demo_web.builder.impl;

import com.example.demo_web.entity.User;
import com.example.demo_web.entity.UserRole;
import com.example.demo_web.entity.UserState;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;
import java.util.UUID;

public class UserBuilderImpl implements com.example.demo_web.builder.UserBuilder<User> {
    private static final String DEFAULT_ID_COLUMN = "user_id";
    private static final String LOGIN_COLUMN = "user_login";
    private static final String EMAIL_COLUMN = "user_email";
    private static final String FIRST_NAME_COLUMN = "user_first_name";
    private static final String SECOND_NAME_COLUMN = "user_second_name";
    private static final String ROLE_ID = "user_role_name";
    private static final String STATE_ID = "user_state_name";

    @Override
    public User build(ResultSet resultSet) throws SQLException {
        if (resultSet.wasNull()) {
            return null;
        }
        Integer userId = resultSet.getInt(DEFAULT_ID_COLUMN);
        String userLogin = resultSet.getString(LOGIN_COLUMN);
        String userEmail = resultSet.getString(EMAIL_COLUMN);
        String userFirstName = resultSet.getString(FIRST_NAME_COLUMN);
        String userSecondName = resultSet.getString(SECOND_NAME_COLUMN);
        UserRole userRole = UserRole.valueOf(resultSet.getString(ROLE_ID).toUpperCase(Locale.ROOT));
        UserState userState = UserState.valueOf(resultSet.getString(STATE_ID).toUpperCase(Locale.ROOT));
        return new User(userId, userLogin, userEmail, userFirstName, userSecondName, userRole, userState);
    }

    @Override
    public void setDefaultFields(User user) {
        String activationKey = UUID.randomUUID().toString();
        user.setActivationKey(activationKey);
        user.setUserRole(UserRole.USER);
        user.setUserState(UserState.INACTIVE);
    }
}

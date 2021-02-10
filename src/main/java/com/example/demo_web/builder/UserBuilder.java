package com.example.demo_web.builder;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface UserBuilder<T> {
    T build(ResultSet resultSet) throws SQLException;
    void setDefaultFields(T t);
}

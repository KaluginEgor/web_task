package com.example.demo_web.builder.impl;

import com.example.demo_web.builder.BaseBuilder;
import com.example.demo_web.entity.Actor;
import com.example.demo_web.entity.Movie;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class ActorBuilder implements BaseBuilder<Actor> {
    private static final String DEFAULT_ID_COLUMN = "actor_id";
    private static final String FIRST_NAME_COLUMN = "actor_first_name";
    private static final String SECOND_NAME_COLUMN = "actor_second_name";
    private static final String BIO_COLUMN = "actor_bio";
    private static final String BIRTHDAY_COLUMN = "actor_birthday";
    private static final String PICTURE_COLUMN = "actor_picture";

    @Override
    public Actor build(ResultSet resultSet) throws SQLException {
        if (resultSet.wasNull()) {
            return null;
        }
        Actor actor = new Actor();
        Integer actorId = resultSet.getInt(DEFAULT_ID_COLUMN);
        actor.setId(actorId);
        String actorFirstName = resultSet.getString(FIRST_NAME_COLUMN);
        actor.setFirstName(actorFirstName);
        String actorSecondName = resultSet.getString(SECOND_NAME_COLUMN);
        actor.setFirstName(actorSecondName);
        String actorBio = resultSet.getString(BIO_COLUMN);
        actor.setBio(actorBio);
        LocalDate actorBirthday = resultSet.getDate(BIRTHDAY_COLUMN).toLocalDate();
        actor.setBirthday(actorBirthday);
        String actorPicture = resultSet.getString(PICTURE_COLUMN);
        actor.setPicture(actorPicture);
        return actor;
    }

    @Override
    public void setDefaultFields(Actor actor) {

    }
}

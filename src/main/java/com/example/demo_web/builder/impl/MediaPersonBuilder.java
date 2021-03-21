package com.example.demo_web.builder.impl;

import com.example.demo_web.builder.BaseBuilder;
import com.example.demo_web.entity.MediaPerson;
import com.example.demo_web.entity.OccupationType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class MediaPersonBuilder implements BaseBuilder<MediaPerson> {
    private static final String DEFAULT_ID_COLUMN = "media_person_id";
    private static final String FIRST_NAME_COLUMN = "media_person_first_name";
    private static final String SECOND_NAME_COLUMN = "media_person_second_name";
    private static final String OCCUPATION_TYPE_COLUMN = "media_person_occupation_name";
    private static final String BIO_COLUMN = "media_person_bio";
    private static final String BIRTHDAY_COLUMN = "media_person_birthday";
    private static final String PICTURE_COLUMN = "media_person_picture";

    @Override
    public MediaPerson build(ResultSet resultSet) throws SQLException {
        if (resultSet.wasNull()) {
            return null;
        }
        MediaPerson mediaPerson = new MediaPerson();
        Integer actorId = resultSet.getInt(DEFAULT_ID_COLUMN);
        mediaPerson.setId(actorId);
        String actorFirstName = resultSet.getString(FIRST_NAME_COLUMN);
        mediaPerson.setFirstName(actorFirstName);
        String actorSecondName = resultSet.getString(SECOND_NAME_COLUMN);
        mediaPerson.setSecondName(actorSecondName);
        OccupationType occupationType = OccupationType.valueOf(resultSet.getString(OCCUPATION_TYPE_COLUMN));
        mediaPerson.setOccupationType(occupationType);
        String actorBio = resultSet.getString(BIO_COLUMN);
        mediaPerson.setBio(actorBio);
        LocalDate actorBirthday = resultSet.getDate(BIRTHDAY_COLUMN).toLocalDate();
        mediaPerson.setBirthday(actorBirthday);
        String actorPicture = resultSet.getString(PICTURE_COLUMN);
        mediaPerson.setPicture(actorPicture);
        return mediaPerson;
    }

    @Override
    public void setDefaultFields(MediaPerson mediaPerson) {

    }
}

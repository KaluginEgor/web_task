package com.example.demo_web.builder.impl;

import com.example.demo_web.builder.BaseBuilder;
import com.example.demo_web.entity.Movie;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class MovieBuilder implements BaseBuilder<Movie> {
    private static final String DEFAULT_ID_COLUMN = "movie_id";
    private static final String TITLE_COLUMN = "movie_title";
    private static final String DESCRIPTION_COLUMN = "movie_description";
    private static final String RATING_COLUMN = "movie_rating";
    private static final String RELEASE_DATE_COLUMN = "movie_release_date";
    private static final String PICTURE_COLUMN = "movie_picture";

    @Override
    public Movie build(ResultSet resultSet) throws SQLException {
        if (resultSet.wasNull()) {
            return null;
        }
        Movie movie = new Movie();
        Integer movieId = resultSet.getInt(DEFAULT_ID_COLUMN);
        movie.setId(movieId);
        String movieTitle = resultSet.getString(TITLE_COLUMN);
        movie.setTitle(movieTitle);
        String movieDescription = resultSet.getString(DESCRIPTION_COLUMN);
        movie.setDescription(movieDescription);
        Float movieRating = resultSet.getFloat(RATING_COLUMN);
        movie.setRating(movieRating);
        LocalDate movieReleaseDate = resultSet.getDate(RELEASE_DATE_COLUMN).toLocalDate();
        movie.setReleaseDate(movieReleaseDate);
        String moviePicture = resultSet.getString(PICTURE_COLUMN);
        movie.setPicture(moviePicture);
        return movie;
    }

    @Override
    public void setDefaultFields(Movie movie) {

    }
}

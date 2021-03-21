package com.example.demo_web.builder.impl;

import com.example.demo_web.builder.BaseBuilder;
import com.example.demo_web.entity.MovieRating;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MovieRatingBuilder implements BaseBuilder<MovieRating> {
    private static final String DEFAULT_ID_COLUMN = "rating_id";
    private static final String VALUE_COLUMN = "rating_value";
    private static final String MOVIE_ID_COLUMN = "movie_id";
    private static final String USER_ID_COLUMN = "user_id";

    @Override
    public MovieRating build(ResultSet resultSet) throws SQLException {
        if (resultSet.wasNull()) {
            return null;
        }
        MovieRating movieRating = new MovieRating();
        Integer movieRatingId = resultSet.getInt(DEFAULT_ID_COLUMN);
        movieRating.setId(movieRatingId);
        Float movieRatingValue = resultSet.getFloat(VALUE_COLUMN);
        movieRating.setValue(movieRatingValue);
        Integer movieId = resultSet.getInt(MOVIE_ID_COLUMN);
        movieRating.setMovieId(movieId);
        Integer userId = resultSet.getInt(USER_ID_COLUMN);
        movieRating.setUserId(userId);
        return movieRating;
    }

    @Override
    public void setDefaultFields(MovieRating movieRating) {

    }
}

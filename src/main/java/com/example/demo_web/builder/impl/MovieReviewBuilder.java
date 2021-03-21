package com.example.demo_web.builder.impl;

import com.example.demo_web.builder.BaseBuilder;
import com.example.demo_web.entity.MovieRating;
import com.example.demo_web.entity.MovieReview;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class MovieReviewBuilder implements BaseBuilder<MovieReview> {
    private static final String DEFAULT_ID_COLUMN = "review_id";
    private static final String TITLE_COLUMN = "review_title";
    private static final String BODY_COLUMN = "review_body";
    private static final String CREATION_DATE_COLUMN = "review_creation_date";
    private static final String MOVIE_ID_COLUMN = "movie_id";
    private static final String USER_ID_COLUMN = "user_id";


    @Override
    public MovieReview build(ResultSet resultSet) throws SQLException {
        if (resultSet.wasNull()) {
            return null;
        }
        MovieReview movieReview = new MovieReview();
        Integer movieReviewId = resultSet.getInt(DEFAULT_ID_COLUMN);
        movieReview.setId(movieReviewId);
        String movieReviewTitle = resultSet.getString(TITLE_COLUMN);
        movieReview.setMovieTitle(movieReviewTitle);
        String movieReviewBody = resultSet.getString(BODY_COLUMN);
        movieReview.setMovieTitle(movieReviewBody);
        LocalDate movieReviewCreationDate = resultSet.getDate(CREATION_DATE_COLUMN).toLocalDate();
        movieReview.setCreationDate(movieReviewCreationDate);
        Integer movieId = resultSet.getInt(MOVIE_ID_COLUMN);
        movieReview.setMovieId(movieId);
        Integer userId = resultSet.getInt(USER_ID_COLUMN);
        movieReview.setUserId(userId);
        return movieReview;
    }

    @Override
    public void setDefaultFields(MovieReview movieReview) {

    }
}

package com.example.demo_web.model.dao.impl;

import com.example.demo_web.model.pool.ConnectionPool;
import com.example.demo_web.model.dao.MovieReviewDao;
import com.example.demo_web.model.entity.MovieReview;
import com.example.demo_web.exception.ConnectionException;
import com.example.demo_web.exception.DaoException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MovieReviewDaoImpl implements MovieReviewDao {
    private static final String SQL_SELECT_MOVIE_REVIEWS_BY_MOVIE_ID = "SELECT MR.review_id, MR.review_title, MR.review_body, MR.review_creation_date, MR.movie_id, MR.user_id FROM movie_reviews MR INNER JOIN movies M on MR.movie_id = M.movie_id WHERE M.movie_id = ? AND MR.review_is_deleted = 0;";

    private static final String DEFAULT_ID_COLUMN = "review_id";
    private static final String TITLE_COLUMN = "review_title";
    private static final String BODY_COLUMN = "review_body";
    private static final String CREATION_DATE_COLUMN = "review_creation_date";
    private static final String MOVIE_ID_COLUMN = "movie_id";
    private static final String USER_ID_COLUMN = "user_id";

    private static MovieReviewDao instance = new MovieReviewDaoImpl();

    private MovieReviewDaoImpl(){}

    public static MovieReviewDao getInstance() {
        return instance;
    }

    @Override
    public List<MovieReview> findAll() throws DaoException {
        return null;
    }

    @Override
    public MovieReview findEntityById(Integer id) throws DaoException {
        return null;
    }

    @Override
    public boolean delete(Integer id) throws DaoException {
        return false;
    }

    @Override
    public boolean delete(MovieReview movieReview) throws DaoException {
        return false;
    }

    @Override
    public MovieReview create(MovieReview movieReview) throws DaoException {
        return null;
    }

    @Override
    public MovieReview update(MovieReview movieReview) throws DaoException {
        return null;
    }

    @Override
    public List<MovieReview> findByMovieId(Integer id) throws DaoException {
        List<MovieReview> movieReviews = new ArrayList<>();
        MovieReview movieReview;
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_MOVIE_REVIEWS_BY_MOVIE_ID)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                movieReview = buildMovieReview(resultSet);
                movieReviews.add(movieReview);
            }
            return movieReviews;
        } catch (ConnectionException | SQLException e) {
            throw new DaoException(e);
        }
    }

    public MovieReview buildMovieReview(ResultSet resultSet) throws SQLException {
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
}

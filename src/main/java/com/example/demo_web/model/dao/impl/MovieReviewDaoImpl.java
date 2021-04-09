package com.example.demo_web.model.dao.impl;

import com.example.demo_web.model.pool.ConnectionPool;
import com.example.demo_web.model.dao.MovieReviewDao;
import com.example.demo_web.model.entity.MovieReview;
import com.example.demo_web.exception.ConnectionException;
import com.example.demo_web.exception.DaoException;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MovieReviewDaoImpl implements MovieReviewDao {
    private static final String SQL_SELECT_MOVIE_REVIEWS_BY_MOVIE_ID = "SELECT MR.review_id, MR.review_title, MR.review_body, MR.review_creation_date, MR.movie_id, MR.user_id, M.movie_title, U.user_login FROM movie_reviews MR INNER JOIN users U on MR.user_id = u.user_id INNER JOIN movies M on MR.movie_id = M.movie_id WHERE M.movie_id = ? AND MR.review_is_deleted = 0;";

    private static final String SQL_INSERT_MOVIE_REVIEW = "INSERT INTO movie_reviews (review_title, review_body, review_creation_date, review_is_deleted, movie_id, user_id) VALUES (?, ?, ?, 0, ?, ?);";

    private static final String SQL_UPDATE_MOVIE_REVIEW = "UPDATE movie_reviews MR SET MR.review_title = ?, MR.review_body = ?, MR.review_creation_date = ? WHERE MR.review_id = ?;";

    private static final String REVIEW_ID_COLUMN = "review_id";
    private static final String TITLE_COLUMN = "review_title";
    private static final String BODY_COLUMN = "review_body";
    private static final String CREATION_DATE_COLUMN = "review_creation_date";
    private static final String MOVIE_ID_COLUMN = "movie_id";
    private static final String USER_ID_COLUMN = "user_id";
    private static final String MOVIE_TITLE_COLUMN = "movie_title";
    private static final String USER_LOGIN_TITLE = "user_login";

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
    public MovieReview create(MovieReview movieReview) throws DaoException {
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT_MOVIE_REVIEW, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, movieReview.getTitle());
            preparedStatement.setString(2, movieReview.getBody());
            preparedStatement.setDate(3, java.sql.Date.valueOf(movieReview.getCreationDate()));
            preparedStatement.setInt(4, movieReview.getMovieId());
            preparedStatement.setInt(5, movieReview.getUserId());
            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                movieReview.setId(generatedKeys.getInt(1));
            }
            return movieReview;
        } catch (ConnectionException | SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public MovieReview update(MovieReview movieReview) throws DaoException {
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_MOVIE_REVIEW)) {
            preparedStatement.setString(1, movieReview.getTitle());
            preparedStatement.setString(2, movieReview.getBody());
            preparedStatement.setDate(3, java.sql.Date.valueOf(movieReview.getCreationDate()));
            preparedStatement.setInt(4, movieReview.getId());
            preparedStatement.executeUpdate();
            return movieReview;
        } catch (ConnectionException | SQLException e) {
            throw new DaoException(e);
        }
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
        Integer movieReviewId = resultSet.getInt(REVIEW_ID_COLUMN);
        movieReview.setId(movieReviewId);
        String movieReviewTitle = resultSet.getString(TITLE_COLUMN);
        movieReview.setTitle(movieReviewTitle);
        String movieReviewBody = resultSet.getString(BODY_COLUMN);
        movieReview.setBody(movieReviewBody);
        LocalDate movieReviewCreationDate = resultSet.getDate(CREATION_DATE_COLUMN).toLocalDate();
        movieReview.setCreationDate(movieReviewCreationDate);
        Integer movieId = resultSet.getInt(MOVIE_ID_COLUMN);
        movieReview.setMovieId(movieId);
        Integer userId = resultSet.getInt(USER_ID_COLUMN);
        movieReview.setUserId(userId);
        String movieTitle = resultSet.getString(MOVIE_TITLE_COLUMN);
        movieReview.setMovieTitle(movieTitle);
        String userLogin = resultSet.getString(USER_LOGIN_TITLE);
        movieReview.setUserLogin(userLogin);
        return movieReview;
    }
}

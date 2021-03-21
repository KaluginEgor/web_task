package com.example.demo_web.dao.impl;

import com.example.demo_web.builder.BaseBuilder;
import com.example.demo_web.builder.impl.MovieRatingBuilder;
import com.example.demo_web.builder.impl.MovieReviewBuilder;
import com.example.demo_web.connection.ConnectionPool;
import com.example.demo_web.dao.MovieRatingDao;
import com.example.demo_web.dao.MovieReviewDao;
import com.example.demo_web.entity.MovieRating;
import com.example.demo_web.entity.MovieReview;
import com.example.demo_web.exception.ConnectionException;
import com.example.demo_web.exception.DaoException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MovieReviewDaoImpl implements MovieReviewDao {
    private static final String SQL_SELECT_MOVIE_REVIEWS_BY_MOVIE_ID = "SELECT MR.review_id, MR.review_title, MR.review_body, MR.review_creation_date, MR.movie_id, MR.user_id FROM movie_reviews MR INNER JOIN movies M on MR.movie_id = M.movie_id WHERE M.movie_id = ? AND MR.review_is_deleted = 0;";

    private static final BaseBuilder<MovieReview> movieReviewBuilder = new MovieReviewBuilder();

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
    public boolean create(MovieReview movieReview) throws DaoException {
        return false;
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
                movieReview = movieReviewBuilder.build(resultSet);
                movieReviews.add(movieReview);
            }
            return movieReviews;
        } catch (ConnectionException | SQLException e) {
            throw new DaoException(e);
        }
    }
}

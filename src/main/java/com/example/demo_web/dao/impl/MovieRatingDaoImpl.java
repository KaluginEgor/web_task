package com.example.demo_web.dao.impl;

import com.example.demo_web.builder.BaseBuilder;
import com.example.demo_web.builder.impl.MovieRatingBuilder;
import com.example.demo_web.connection.ConnectionPool;
import com.example.demo_web.dao.MovieRatingDao;
import com.example.demo_web.entity.MovieRating;
import com.example.demo_web.exception.ConnectionException;
import com.example.demo_web.exception.DaoException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MovieRatingDaoImpl implements MovieRatingDao {
    private static final String SQL_SELECT_MOVIE_RATINGS_BY_MOVIE_ID = "SELECT MR.rating_id, MR.rating_value, MR.movie_id, MR.user_id FROM movie_ratings MR INNER JOIN movies M on MR.movie_id = M.movie_id WHERE M.movie_id = ? AND MR.rating_is_deleted = 0;";

    private static final BaseBuilder<MovieRating> movieRatingBuilder = new MovieRatingBuilder();

    private static MovieRatingDao instance = new MovieRatingDaoImpl();

    private MovieRatingDaoImpl(){}

    public static MovieRatingDao getInstance() {
        return instance;
    }

    @Override
    public List<MovieRating> findAll() throws DaoException {
        return null;
    }

    @Override
    public MovieRating findEntityById(Integer id) throws DaoException {
        return null;
    }

    @Override
    public boolean delete(Integer id) throws DaoException {
        return false;
    }

    @Override
    public boolean delete(MovieRating movieRating) throws DaoException {
        return false;
    }

    @Override
    public boolean create(MovieRating movieRating) throws DaoException {
        return false;
    }

    @Override
    public MovieRating update(MovieRating movieRating) throws DaoException {
        return null;
    }

    @Override
    public List<MovieRating> findByMovieId(Integer id) throws DaoException {
        List<MovieRating> movieRatings = new ArrayList<>();
        MovieRating movieRating;
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_MOVIE_RATINGS_BY_MOVIE_ID)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                movieRating = movieRatingBuilder.build(resultSet);
                movieRatings.add(movieRating);
            }
            return movieRatings;
        } catch (ConnectionException | SQLException e) {
            throw new DaoException(e);
        }
    }
}

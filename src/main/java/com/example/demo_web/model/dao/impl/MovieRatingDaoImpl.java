package com.example.demo_web.model.dao.impl;

import com.example.demo_web.model.pool.ConnectionPool;
import com.example.demo_web.model.dao.MovieRatingDao;
import com.example.demo_web.model.entity.MovieRating;
import com.example.demo_web.exception.ConnectionException;
import com.example.demo_web.exception.DaoException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MovieRatingDaoImpl implements MovieRatingDao {
    private static final String SQL_SELECT_MOVIE_RATINGS_BY_MOVIE_ID = "SELECT MR.rating_id, MR.rating_value, MR.movie_id, MR.user_id, M.movie_title FROM movie_ratings MR INNER JOIN movies M on MR.movie_id = M.movie_id WHERE M.movie_id = ? AND MR.rating_is_deleted = 0;";

    private static final String SQL_INSERT_MOVIE_RATING = "INSERT INTO movie_ratings (rating_value, rating_is_deleted, movie_id, user_id) VALUES (?, 0, ?, ?);";

    private static final String SQL_UPDATE_MOVIE_RATING = "UPDATE movie_ratings SET movie_ratings.rating_value = ? WHERE movie_ratings.rating_id = ?;";

    private static final String SQL_DELETE_MOVIE_RATING = "DELETE FROM movie_ratings MR WHERE MR.rating_id = ?;";

    private static final String DEFAULT_ID_COLUMN = "rating_id";
    private static final String VALUE_COLUMN = "rating_value";
    private static final String MOVIE_ID_COLUMN = "movie_id";
    private static final String USER_ID_COLUMN = "user_id";
    private static final String MOVIE_TITLE_COLUMN = "movie_title";

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
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_MOVIE_RATING)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException | ConnectionException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public MovieRating create(MovieRating movieRating) throws DaoException {
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT_MOVIE_RATING, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setFloat(1, movieRating.getValue());
            preparedStatement.setInt(2, movieRating.getMovieId());
            preparedStatement.setInt(3, movieRating.getUserId());
            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                movieRating.setId(generatedKeys.getInt(1));
            }
            return movieRating;
        } catch (ConnectionException | SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public MovieRating update(MovieRating movieRating) throws DaoException {
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_MOVIE_RATING)) {
            preparedStatement.setFloat(1, movieRating.getValue());
            preparedStatement.setInt(2, movieRating.getId());
            preparedStatement.executeUpdate();
            return movieRating;
        } catch (ConnectionException | SQLException e) {
            throw new DaoException(e);
        }
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
                movieRating = buildMovieRating(resultSet);
                movieRatings.add(movieRating);
            }
            return movieRatings;
        } catch (ConnectionException | SQLException e) {
            throw new DaoException(e);
        }
    }

    private MovieRating buildMovieRating(ResultSet resultSet) throws SQLException {
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
        String movieTitle = resultSet.getString(MOVIE_TITLE_COLUMN);
        movieRating.setMovieTitle(movieTitle);
        return movieRating;
    }
}

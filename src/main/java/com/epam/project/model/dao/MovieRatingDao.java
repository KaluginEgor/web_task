package com.epam.project.model.dao;

import com.epam.project.exception.DaoException;
import com.epam.project.model.dao.column.MovieRatingsColumn;
import com.epam.project.model.entity.MovieRating;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.intellij.lang.annotations.Language;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Movie rating dao.
 */
public class MovieRatingDao extends AbstractMovieRatingDao {
    private static final Logger logger = LogManager.getLogger(MovieRatingDao.class);

    @Language("SQL")
    private static final String SQL_SELECT_MOVIE_RATINGS_BY_MOVIE_ID = "SELECT MR.rating_id, MR.rating_value, MR.movie_id, MR.user_id, M.movie_title FROM movie_ratings MR INNER JOIN movies M on MR.movie_id = M.movie_id WHERE M.movie_id = ?;";

    @Language("SQL")
    private static final String SQL_SELECT_MOVIE_RATINGS_BY_USER_ID = "SELECT MR.rating_id, MR.rating_value, MR.movie_id, MR.user_id, M.movie_title FROM movie_ratings MR INNER JOIN movies M on MR.movie_id = M.movie_id INNER JOIN users U on MR.user_id = U.user_id WHERE U.user_id = ?;";

    @Language("SQL")
    private static final String SQL_INSERT_MOVIE_RATING = "INSERT INTO movie_ratings (rating_value, movie_id, user_id) VALUES (?, ?, ?);";

    @Language("SQL")
    private static final String SQL_UPDATE_MOVIE_RATING = "UPDATE movie_ratings SET movie_ratings.rating_value = ? WHERE movie_ratings.rating_id = ?;";

    @Language("SQL")
    private static final String SQL_DELETE_MOVIE_RATING = "DELETE FROM movie_ratings MR WHERE MR.rating_id = ?;";

    @Language("SQL")
    private static final String SQL_COUNT_RATING_BY_MOVIE_ID = "SELECT COUNT(*) AS movie_ratings_count FROM movie_ratings WHERE movie_id = ?;";

    @Language("SQL")
    private static final String SQL_SELECT_MOVIE_RATINGS_BY_ID = "SELECT MR.rating_id, MR.rating_value, MR.movie_id, MR.user_id, M.movie_title FROM movie_ratings MR INNER JOIN movies M on MR.movie_id = M.movie_id WHERE MR.rating_id = ?;";

    @Language("SQL")
    private static final String SQL_EXISTS = "SELECT EXISTS (SELECT rating_id FROM movie_ratings WHERE rating_id = ? AND movie_id = ? AND user_id = ?) AS movie_rating_existence;";

    @Language("SQL")
    private static final String SQL_MOVIE_RATING_IS_UNIQUE = "SELECT EXISTS (SELECT rating_id FROM movie_ratings WHERE movie_id = ? AND user_id = ?) AS movie_rating_existence;";

    private static final AbstractMovieRatingDao instance = new MovieRatingDao();

    private MovieRatingDao(){}

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static AbstractMovieRatingDao getInstance() {
        return instance;
    }

    @Override
    public boolean delete(Integer id) throws DaoException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_MOVIE_RATING)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException(e);
        }
    }

    @Override
    public MovieRating create(MovieRating movieRating) throws DaoException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT_MOVIE_RATING, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setFloat(1, movieRating.getValue());
            preparedStatement.setInt(2, movieRating.getMovieId());
            preparedStatement.setInt(3, movieRating.getUserId());
            int id = executeUpdateAndGetGeneratedId(preparedStatement);
            movieRating.setId(id);
            return movieRating;
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException(e);
        }
    }

    @Override
    public MovieRating update(MovieRating movieRating) throws DaoException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_MOVIE_RATING)) {
            preparedStatement.setFloat(1, movieRating.getValue());
            preparedStatement.setInt(2, movieRating.getId());
            preparedStatement.executeUpdate();
            return movieRating;
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException(e);
        }
    }

    @Override
    public List<MovieRating> findByMovieId(Integer id) throws DaoException {
        List<MovieRating> movieRatings = new ArrayList<>();
        MovieRating movieRating;
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_MOVIE_RATINGS_BY_MOVIE_ID)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    movieRating = buildMovieRating(resultSet);
                    movieRatings.add(movieRating);
                }
            }
            return movieRatings;
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException(e);
        }
    }

    @Override
    public List<MovieRating> findByUserId(Integer id) throws DaoException {
        List<MovieRating> movieRatings = new ArrayList<>();
        MovieRating movieRating;
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_MOVIE_RATINGS_BY_USER_ID)) {
            preparedStatement.setInt(1, id);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    movieRating = buildMovieRating(resultSet);
                    movieRatings.add(movieRating);
                }
            }
            return movieRatings;
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException(e);
        }
    }

    @Override
    public int countRatingsByMovieId(Integer movieId) throws DaoException {
        int ratingsCount = 0;
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_COUNT_RATING_BY_MOVIE_ID)) {
            preparedStatement.setInt(1, movieId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    ratingsCount = resultSet.getInt(1);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException(e);
        }
        return ratingsCount;
    }

    @Override
    public MovieRating findEntityById(Integer id) throws DaoException {
        MovieRating movieRating = new MovieRating();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_MOVIE_RATINGS_BY_ID)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    movieRating = buildMovieRating(resultSet);
                }
            }
            return movieRating;
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException(e);
        }
    }

    @Override
    public boolean exists(int ratingId, int movieId, int userId) throws DaoException {
        boolean result;
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_EXISTS)) {
            preparedStatement.setInt(1, ratingId);
            preparedStatement.setInt(2, movieId);
            preparedStatement.setInt(3, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.next();
                result = resultSet.getInt(1) == 1;
            }
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException(e);
        }
        return result;
    }

    @Override
    public boolean isUnique(int movieId, int userId) throws DaoException {
        boolean result;
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_MOVIE_RATING_IS_UNIQUE)) {
            preparedStatement.setInt(1, movieId);
            preparedStatement.setInt(2, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.next();
                result = resultSet.getInt(1) == 0;
            }
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException(e);
        }
        return result;
    }

    @Override
    public List<MovieRating> findAll() throws DaoException {
        throw new UnsupportedOperationException();
    }

    private MovieRating buildMovieRating(ResultSet resultSet) throws SQLException {
        if (resultSet.wasNull()) {
            return null;
        }
        MovieRating movieRating = new MovieRating();
        Integer movieRatingId = resultSet.getInt(MovieRatingsColumn.ID);
        movieRating.setId(movieRatingId);
        Integer movieRatingValue = resultSet.getInt(MovieRatingsColumn.VALUE);
        movieRating.setValue(movieRatingValue);
        Integer movieId = resultSet.getInt(MovieRatingsColumn.MOVIE_ID);
        movieRating.setMovieId(movieId);
        Integer userId = resultSet.getInt(MovieRatingsColumn.USER_ID);
        movieRating.setUserId(userId);
        String movieTitle = resultSet.getString(MovieRatingsColumn.MOVIE_TITLE);
        movieRating.setMovieTitle(movieTitle);
        return movieRating;
    }
}

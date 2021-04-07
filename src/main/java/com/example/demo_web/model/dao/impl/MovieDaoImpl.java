package com.example.demo_web.model.dao.impl;

import com.example.demo_web.model.pool.ConnectionPool;
import com.example.demo_web.model.dao.MovieDao;
import com.example.demo_web.model.entity.GenreType;
import com.example.demo_web.model.entity.Movie;
import com.example.demo_web.exception.ConnectionException;
import com.example.demo_web.exception.DaoException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MovieDaoImpl implements MovieDao {
    private static final String SQL_SELECT_ALL_MOVIES = "SELECT M.movie_id, M.movie_title, M.movie_description, M.movie_rating, M.movie_release_date, M.movie_picture FROM movies M WHERE M.movie_is_deleted = 0;";

    private static final String SQL_SELECT_ALL_MOVIES_WITH_LIMIT = "SELECT M.movie_id, M.movie_title, M.movie_description, M.movie_rating, M.movie_release_date, M.movie_picture FROM movies M WHERE M.movie_is_deleted = 0 LIMIT ?, ?;";

    private static final String SQL_SELECT_MOVIE_BY_ID = "SELECT M.movie_id, M.movie_title, M.movie_description, M.movie_rating, M.movie_release_date, M.movie_picture FROM movies M WHERE M.movie_id = ? AND M.movie_is_deleted = 0;";

    private static final String SQL_COUNT_MOVIES = "SELECT COUNT(*) AS movies_count FROM movies WHERE movie_is_deleted = 0;";

    private static final String SQL_SELECT_GENRE_TYPES_BY_MOVIE_ID = "SELECT MG.movie_genre_name FROM movie_genre MG INNER JOIN genres_movies GM on MG.movie_genre_id = GM.movie_genre_id INNER JOIN movies M on GM.movie_genre_id = M.movie_id WHERE MG.movie_genre_id = ? AND M.movie_is_deleted = 0;";

    private static final String SQL_SELECT_MOVIES_BY_ACTOR_ID = "SELECT M.movie_id, M.movie_title, M.movie_description, M.movie_rating, M.movie_release_date, M.movie_picture FROM movies M INNER JOIN media_persons_movies MPM on M.movie_id = MPM.movie_id INNER JOIN media_persons MP on MPM.media_person_id = MP.media_person_id WHERE MP.media_person_id = ? AND M.movie_is_deleted = 0;";

    private static final String SQL_SELECT_ALL_MOVIE_TITLES = "SELECT M.movie_id, M.movie_title FROM movies M WHERE M.movie_is_deleted = 0;";

    private static final String DEFAULT_ID_COLUMN = "movie_id";
    private static final String TITLE_COLUMN = "movie_title";
    private static final String DESCRIPTION_COLUMN = "movie_description";
    private static final String RATING_COLUMN = "movie_rating";
    private static final String RELEASE_DATE_COLUMN = "movie_release_date";
    private static final String PICTURE_COLUMN = "movie_picture";

    private static MovieDao instance = new MovieDaoImpl();

    private MovieDaoImpl(){}

    public static MovieDao getInstance() {
        return instance;
    }

    @Override
    public List<Movie> findAll() throws DaoException {
        List<Movie> movieList = new ArrayList<>();
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_ALL_MOVIES)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                Movie movie;
                while (resultSet.next()) {
                    movie = buildMovie(resultSet);
                    movieList.add(movie);
                }
            }
            return movieList;
        } catch (SQLException | ConnectionException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Movie findEntityById(Integer id) throws DaoException {
        Movie movie = null;
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_MOVIE_BY_ID)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                movie = buildMovie(resultSet);
            }
            return movie;
        } catch (SQLException | ConnectionException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public boolean delete(Integer id) throws DaoException {
        return false;
    }

    @Override
    public boolean delete(Movie movie) throws DaoException {
        return false;
    }

    @Override
    public Movie create(Movie movie) throws DaoException {
        return null;
    }

    @Override
    public Movie update(Movie movie) throws DaoException {
        return null;
    }

    @Override
    public List<Movie> findAllBetween(int begin, int end) throws DaoException {
        List<Movie> movieList = new ArrayList<>();
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_ALL_MOVIES_WITH_LIMIT)) {
            preparedStatement.setInt(1, begin);
            preparedStatement.setInt(2, end);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                Movie movie;
                while (resultSet.next()) {
                    movie = buildMovie(resultSet);
                    movieList.add(movie);
                }
            }
            return movieList;
        } catch (SQLException | ConnectionException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<GenreType> findGenreTypesByMovieId(Integer id) throws DaoException {
        List<GenreType> genreTypes = new ArrayList<>();
        GenreType genreType;
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_GENRE_TYPES_BY_MOVIE_ID)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                genreType = GenreType.valueOf(resultSet.getString(1));
                genreTypes.add(genreType);
            }
            return genreTypes;
        } catch (ConnectionException | SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public int countMovies() throws DaoException {
        int moviesCount = 0;
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_COUNT_MOVIES)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                moviesCount = resultSet.getInt(1);
            }
        } catch (ConnectionException | SQLException e) {
            //logger.error(e);
            throw new DaoException(e);
        }
        return moviesCount;
    }

    @Override
    public List<Movie> findByActorId(Integer id) throws DaoException {
        List<Movie> movieList = new ArrayList<>();
        Movie movie;
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_MOVIES_BY_ACTOR_ID)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                movie = buildMovie(resultSet);
                movieList.add(movie);
            }
            return movieList;
        } catch (ConnectionException | SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Map<Integer, String> findAllTitles() throws DaoException {
        Map<Integer, String> foundTitles = new LinkedHashMap<>();
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_ALL_MOVIE_TITLES)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            Integer movieId;
            String movieTitle;
            while (resultSet.next()) {
                movieId = resultSet.getInt("movie_id");
                movieTitle = resultSet.getString("movie_title");
                foundTitles.put(movieId, movieTitle);
            }
        } catch (ConnectionException | SQLException e) {
            e.printStackTrace();
        }
        return foundTitles;
    }

    private Movie buildMovie(ResultSet resultSet) throws SQLException {
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
        movie.setAverageRating(movieRating);
        LocalDate movieReleaseDate = resultSet.getDate(RELEASE_DATE_COLUMN).toLocalDate();
        movie.setReleaseDate(movieReleaseDate);
        String moviePicture = resultSet.getString(PICTURE_COLUMN);
        movie.setPicture(moviePicture);
        return movie;
    }
}

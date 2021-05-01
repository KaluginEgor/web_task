package com.epam.project.model.dao.impl;

import com.epam.project.exception.DaoException;
import com.epam.project.model.dao.AbstractMovieDao;
import com.epam.project.model.dao.column.MoviesColumn;
import com.epam.project.model.entity.GenreType;
import com.epam.project.model.entity.Movie;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MovieDao extends AbstractMovieDao {
    private static final String SQL_SELECT_ALL_MOVIE_TITLES = "SELECT M.movie_id, M.movie_title FROM movies M ORDER BY M.movie_title;";

    private static final String SQL_SELECT_ALL_MOVIES_WITH_LIMIT = "SELECT M.movie_id, M.movie_title, M.movie_description, M.movie_rating, M.movie_release_date, M.movie_picture FROM movies M ORDER BY M.movie_title LIMIT ?, ?;";

    private static final String SQL_SELECT_MOVIE_BY_ID = "SELECT M.movie_id, M.movie_title, M.movie_description, M.movie_rating, M.movie_release_date, M.movie_picture FROM movies M WHERE M.movie_id = ?;";

    private static final String SQL_COUNT_MOVIES = "SELECT COUNT(*) AS movies_count FROM movies;";

    private static final String SQL_SELECT_GENRE_TYPES_BY_MOVIE_ID = "SELECT MG.movie_genre_name FROM movie_genre MG INNER JOIN genres_movies GM on MG.movie_genre_id = GM.movie_genre_id INNER JOIN movies M on GM.movie_id = M.movie_id WHERE M.movie_id = ?;";

    private static final String SQL_SELECT_MOVIES_BY_ACTOR_ID = "SELECT M.movie_id, M.movie_title, M.movie_description, M.movie_rating, M.movie_release_date, M.movie_picture FROM movies M INNER JOIN media_persons_movies MPM on M.movie_id = MPM.movie_id INNER JOIN media_persons MP on MPM.media_person_id = MP.media_person_id WHERE MP.media_person_id = ? ORDER BY M.movie_title;";

    private static final String SQL_INSERT_MOVIE_GENRE = "INSERT INTO genres_movies (movie_id, movie_genre_id) VALUES (?, ?);";

    private static final String SQL_INSERT_MOVIE_MEDIA_PERSON = "INSERT INTO media_persons_movies (movie_id, media_person_id) VALUES (?, ?);";

    private static final String SQL_DELETE_MOVIE_CREW = "DELETE FROM media_persons_movies MPM WHERE MPM.movie_id = ?;";

    private static final String SQL_DELETE_MOVIE_GENRES = "DELETE FROM genres_movies GM WHERE GM.movie_id = ?;";

    private static final String SQL_INSERT_MOVIE = "INSERT INTO movies (movie_title, movie_description, movie_rating, movie_release_date, movie_picture) VALUES (?, ?, 0, ?, ?);";

    private static final String SQL_UPDATE_MOVIE = "UPDATE movies M SET M.movie_title = ?, M.movie_description = ?, M.movie_release_date = ?, M.movie_picture = ? WHERE M.movie_id = ?;";

    private static final String SQL_DELETE_MOVIE = "DELETE FROM movies M WHERE M.movie_id = ?;";

    private static final String SQL_SELECT_MOVIES_BY_TITLE_PART = "SELECT M.movie_id, M.movie_title, M.movie_description, M.movie_rating, M.movie_release_date, M.movie_picture FROM movies M WHERE UPPER(M.movie_title) LIKE ?;";

    private static final String SQL_SELECT_RATING_BY_ID = "SELECT M.movie_rating FROM movies M WHERE M.movie_id = ?;";

    private static final String SQL_UPDATE_RATING_BY_ID = "UPDATE movies M SET M.movie_rating = ? WHERE M.movie_id = ?;";

    private static final String SQL_EXISTS_ID = "SELECT EXISTS (SELECT movie_id FROM movies WHERE movie_id = ?) AS movie_existence;";

    private static final String SQL_MOVIE_IS_UNIQUE = "SELECT EXISTS (SELECT movie_id FROM movies WHERE movie_title = ? AND movie_release_date = ?) AS movie_existence;";

    private static final AbstractMovieDao instance = new MovieDao();

    private MovieDao(){}

    public static AbstractMovieDao getInstance() {
        return instance;
    }

    @Override
    public boolean idExists(int id) throws DaoException {
        return idExists(id, SQL_EXISTS_ID);
    }

    @Override
    public Map<Integer, String> findAllTitles() throws DaoException {
        Map<Integer, String> allTitles = new LinkedHashMap<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_ALL_MOVIE_TITLES)) {
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                Integer id;
                String title;
                while (resultSet.next()) {
                    id = resultSet.getInt(MoviesColumn.ID);
                    title = resultSet.getString(MoviesColumn.TITLE);
                    allTitles.put(id, title);
                }
            }
            return allTitles;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Movie findEntityById(Integer id) throws DaoException {
        Movie movie = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_MOVIE_BY_ID)) {
            preparedStatement.setInt(1, id);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    movie = buildMovie(resultSet);
                }
            }
            return movie;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public boolean delete(Integer id) throws DaoException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_MOVIE)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Movie create(Movie movie) throws DaoException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT_MOVIE, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, movie.getTitle());
            preparedStatement.setString(2, movie.getDescription());
            preparedStatement.setDate(3, java.sql.Date.valueOf(movie.getReleaseDate()));
            preparedStatement.setString(4, movie.getPicture());
            int id = executeUpdateAndGetGeneratedId(preparedStatement);
            movie.setId(id);
            return movie;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Movie update(Movie movie) throws DaoException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_MOVIE)) {
            preparedStatement.setString(1, movie.getTitle());
            preparedStatement.setString(2, movie.getDescription());
            preparedStatement.setDate(3, java.sql.Date.valueOf(movie.getReleaseDate()));
            preparedStatement.setString(4, movie.getPicture());
            preparedStatement.setInt(5, movie.getId());
            preparedStatement.executeUpdate();
            return movie;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<Movie> findAllBetween(int begin, int end) throws DaoException {
        List<Movie> movieList = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_ALL_MOVIES_WITH_LIMIT)) {
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
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<GenreType> findGenreTypesByMovieId(Integer id) throws DaoException {
        List<GenreType> genreTypes = new ArrayList<>();
        GenreType genreType;
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_GENRE_TYPES_BY_MOVIE_ID)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    genreType = GenreType.valueOf(resultSet.getString(1));
                    genreTypes.add(genreType);
                }
            }
            return genreTypes;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public int countMovies() throws DaoException {
        int moviesCount = 0;
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_COUNT_MOVIES)) {
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    moviesCount = resultSet.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return moviesCount;
    }

    @Override
    public List<Movie> findByActorId(Integer id) throws DaoException {
        List<Movie> movieList = new ArrayList<>();
        Movie movie;
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_MOVIES_BY_ACTOR_ID)) {
            preparedStatement.setInt(1, id);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    movie = buildMovie(resultSet);
                    movieList.add(movie);
                }
            }
            return movieList;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public boolean insertMovieGenre(Integer movieId, Integer genreId) throws DaoException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT_MOVIE_GENRE)) {
            preparedStatement.setInt(1, movieId);
            preparedStatement.setInt(2, genreId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return true;
    }

    @Override
    public boolean insertMovieMediaPerson(Integer movieId, Integer mediaPersonId) throws DaoException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT_MOVIE_MEDIA_PERSON)) {
            preparedStatement.setInt(1, movieId);
            preparedStatement.setInt(2, mediaPersonId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return true;
    }

    @Override
    public boolean deleteMovieCrew(Integer movieId) throws DaoException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_MOVIE_CREW)) {
            preparedStatement.setInt(1, movieId);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public boolean deleteMovieGenres(Integer movieId) throws DaoException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_MOVIE_GENRES)) {
            preparedStatement.setInt(1, movieId);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<Movie> findByTitlePart(String movieTitle) throws DaoException {
        List<Movie> movieList = new ArrayList<>();
        movieTitle = movieTitle.toUpperCase();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_MOVIES_BY_TITLE_PART)) {
            preparedStatement.setString(1, "%" + movieTitle + "%");
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    movieList.add(buildMovie(resultSet));
                }
            }
            return movieList;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public float findRatingById(Integer movieId) throws DaoException {
        float rating = 0;
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_RATING_BY_ID)) {
            preparedStatement.setInt(1, movieId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    rating = resultSet.getFloat(MoviesColumn.RATING);
                }
            }
            return rating;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public boolean updateRatingById(float rating, Integer movieId) throws DaoException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_RATING_BY_ID)) {
            preparedStatement.setFloat(1, rating);
            preparedStatement.setInt(2, movieId);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public boolean isUnique(String title, LocalDate releaseDate) throws DaoException {
        boolean result;
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_MOVIE_IS_UNIQUE)) {
            preparedStatement.setString(1, title);
            preparedStatement.setDate(2, java.sql.Date.valueOf(releaseDate));
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.next();
                result = resultSet.getInt(1) == 0;
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return result;
    }

    @Override
    public List<Movie> findAll() throws DaoException {
        throw new UnsupportedOperationException();
    }

    private Movie buildMovie(ResultSet resultSet) throws SQLException {
        if (resultSet.wasNull()) {
            return null;
        }
        Movie movie = new Movie();
        Integer movieId = resultSet.getInt(MoviesColumn.ID);
        movie.setId(movieId);
        String movieTitle = resultSet.getString(MoviesColumn.TITLE);
        movie.setTitle(movieTitle);
        String movieDescription = resultSet.getString(MoviesColumn.DESCRIPTION);
        movie.setDescription(movieDescription);
        Float movieRating = resultSet.getFloat(MoviesColumn.RATING);
        movie.setAverageRating(movieRating);
        LocalDate movieReleaseDate = resultSet.getDate(MoviesColumn.RELEASE_DATE).toLocalDate();
        movie.setReleaseDate(movieReleaseDate);
        String moviePicture = resultSet.getString(MoviesColumn.PICTURE);
        movie.setPicture(moviePicture);
        return movie;
    }
}

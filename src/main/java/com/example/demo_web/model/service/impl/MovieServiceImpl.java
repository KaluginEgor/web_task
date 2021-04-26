package com.example.demo_web.model.service.impl;

import com.example.demo_web.model.dao.*;

import com.example.demo_web.model.dao.impl.MediaPersonDao;
import com.example.demo_web.model.dao.impl.MovieDao;
import com.example.demo_web.model.dao.impl.MovieRatingDao;
import com.example.demo_web.model.dao.impl.MovieReviewDao;
import com.example.demo_web.exception.DaoException;
import com.example.demo_web.exception.ServiceException;
import com.example.demo_web.model.entity.*;
import com.example.demo_web.model.service.MovieService;
import com.example.demo_web.model.util.message.ErrorMessage;
import com.example.demo_web.model.validator.MediaPersonValidator;
import com.example.demo_web.model.validator.MovieValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MovieServiceImpl implements MovieService {
    private static final MovieService instance = new MovieServiceImpl();
    private static final Logger logger = LogManager.getLogger(MovieServiceImpl.class);
    private AbstractMovieDao movieDao = MovieDao.getInstance();
    private AbstractMediaPersonDao mediaPersonDao = MediaPersonDao.getInstance();
    private AbstractMovieReviewDao reviewDao = MovieReviewDao.getInstance();
    private AbstractMovieRatingDao ratingDao = MovieRatingDao.getInstance();
    private static final String DEFAULT_MOVIE_PICTURE = "C:/Epam/pictures/movie.jpg";

    private MovieServiceImpl() {}

    public static MovieService getInstance() {
        return instance;
    }

    @Override
    public List<Movie> findAllBetween(int begin, int end) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction();
        transaction.init(movieDao);
        List<Movie> allUsers;
        try {
            allUsers = new ArrayList<>(movieDao.findAllBetween(begin, end));
        } catch (DaoException e) {
            logger.error(e);
            throw new ServiceException(e);
        } finally {
            transaction.end();
        }
        return allUsers;
    }

    @Override
    public int countMovies() throws ServiceException {
        int moviesCount;
        EntityTransaction transaction = new EntityTransaction();
        transaction.init(movieDao);
        try {
            moviesCount = movieDao.countMovies();
        } catch (DaoException e) {
            logger.error(e);
            throw new ServiceException(e);
        } finally {
            transaction.end();
        }
        return moviesCount;
    }

    @Override
    public Map.Entry<Optional<Movie>, Optional<String>> findById(String stringMovieId) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction();
        Optional<Movie> movie = Optional.empty();
        Optional<String> errorMessage = Optional.empty();
        if (!MediaPersonValidator.isValidId(stringMovieId)) {
            errorMessage = Optional.of(ErrorMessage.INCORRECT_FIND_MOVIE_PARAMETERS);
        } else {
            try {
                transaction.initTransaction(movieDao, mediaPersonDao, reviewDao, ratingDao);
                int movieId = Integer.valueOf(stringMovieId);
                if (movieDao.idExists(movieId)) {
                    movie = Optional.of(movieDao.findEntityById(movieId));
                    List<GenreType> genreTypes = movieDao.findGenreTypesByMovieId(movieId);
                    movie.get().setGenres(genreTypes);
                    List<MediaPerson> crew = mediaPersonDao.findByMovieId(movieId);
                    movie.get().setCrew(crew);
                    List<MovieReview> movieReviews = reviewDao.findByMovieId(movieId);
                    movie.get().setReviews(movieReviews);
                    List<MovieRating> ratingList = ratingDao.findByMovieId(movieId);
                    movie.get().setRatingList(ratingList);
                } else {
                    errorMessage = Optional.of(ErrorMessage.TRY_FIND_NOT_EXISTING_MOVIE);
                }
                transaction.commit();
            } catch (DaoException e) {
                transaction.rollback();
                throw new ServiceException(e);
            } finally {
                transaction.endTransaction();
            }
        }
        return Map.entry(movie, errorMessage);
    }


    @Override
    public List<Movie> findAll() throws ServiceException {
        EntityTransaction transaction = new EntityTransaction();
        transaction.init(movieDao);
        List<Movie> movies = new ArrayList<>();
        try {
            movies = movieDao.findAll();
            return movies;
        } catch (DaoException e) {
            throw new ServiceException(e);
        } finally {
            transaction.end();
        }
    }

    @Override
    public Movie create(String title, String description, LocalDate releaseDate, String picture, String[] stringGenres, String[] stringMediaPersonsId) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction();
        transaction.initTransaction(movieDao, reviewDao, ratingDao);
        try {
            if (picture.isEmpty()) {
                picture = DEFAULT_MOVIE_PICTURE;
            }
            Movie movieToCreate = convertToMovie(title, description, releaseDate, picture, stringGenres, stringMediaPersonsId);
            Movie createdMovie = movieDao.create(movieToCreate);
            createdMovie.setGenres(movieToCreate.getGenres());
            createdMovie.setCrew(movieToCreate.getCrew());
            for (GenreType genre : createdMovie.getGenres()) {
                movieDao.insertMovieGenre(createdMovie.getId(), genre.ordinal());
            }
            for (MediaPerson mediaPerson : createdMovie.getCrew()) {
                movieDao.insertMovieMediaPerson(createdMovie.getId(), mediaPerson.getId());
            }
            List<MovieReview> movieReviews = reviewDao.findByMovieId(createdMovie.getId());
            createdMovie.setReviews(movieReviews);
            List<MovieRating> ratingList = ratingDao.findByMovieId(createdMovie.getId());
            createdMovie.setRatingList(ratingList);
            transaction.commit();
            return createdMovie;
        } catch (DaoException e) {
            transaction.rollback();
            throw new ServiceException(e);
        } finally {
            transaction.endTransaction();
        }
    }

    @Override
    public Movie update(int id, String title, String description, LocalDate releaseDate, String picture, String[] stringGenres, String[] stringMediaPersonsId) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction();
        transaction.initTransaction(movieDao, reviewDao, ratingDao);
        try {
            if (picture.isEmpty()) {
                picture = DEFAULT_MOVIE_PICTURE;
            }
            Movie movieToUpdate = convertToMovie(title, description, releaseDate, picture, stringGenres, stringMediaPersonsId);
            movieToUpdate.setId(id);
            movieDao.update(movieToUpdate);
            movieDao.deleteMovieGenres(movieToUpdate.getId());
            for (GenreType genre : movieToUpdate.getGenres()) {
                movieDao.insertMovieGenre(movieToUpdate.getId(), genre.ordinal());
            }
            movieDao.deleteMovieCrew(movieToUpdate.getId());
            for (MediaPerson mediaPerson : movieToUpdate.getCrew()) {
                movieDao.insertMovieMediaPerson(movieToUpdate.getId(), mediaPerson.getId());
            }
            List<MovieReview> movieReviews = reviewDao.findByMovieId(movieToUpdate.getId());
            movieToUpdate.setReviews(movieReviews);
            List<MovieRating> ratingList = ratingDao.findByMovieId(movieToUpdate.getId());
            movieToUpdate.setRatingList(ratingList);
            transaction.commit();
            return movieToUpdate;
        } catch (DaoException e) {
            transaction.rollback();
            throw new ServiceException(e);
        } finally {
            transaction.endTransaction();
        }
    }

    @Override
    public boolean delete(int id) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction();
        transaction.init(movieDao);
        try {
            movieDao.delete(id);
            return true;
        } catch (DaoException e) {
            throw new ServiceException(e);
        } finally {
            transaction.end();
        }
    }

    @Override
    public Map.Entry<List<Movie>, Optional<String>> findByNamePart(String title) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction();
        List<Movie> movies = new ArrayList<>();
        Optional<String> errorMessage = Optional.empty();
        if (!MovieValidator.isTitleValid(title)) {
            errorMessage = Optional.of(ErrorMessage.INCORRECT_FIND_MOVIE_PARAMETERS);
        } else {
            try {
                transaction.init(movieDao);
                movies = movieDao.findByTitlePart(title);
            } catch (DaoException e) {
                throw new ServiceException(e);
            } finally {
                transaction.end();
            }
        }
        return Map.entry(movies, errorMessage);
    }

    private Movie convertToMovie(String title, String description, LocalDate releaseDate, String picture, String[] stringGenres, String[] stringMediaPersonsId) throws DaoException {
        Movie movie = new Movie();
        movie.setTitle(title);
        movie.setDescription(description);
        movie.setReleaseDate(releaseDate);
        movie.setPicture(picture);
        List<GenreType> genres = new ArrayList<>();
        if (stringGenres != null) {
            for (String stringGenre : stringGenres) {
                GenreType genreType = GenreType.valueOf(stringGenre);
                genres.add(genreType);
            }
        }
        movie.setGenres(genres);
        List<MediaPerson> mediaPersons = new ArrayList<>();
        if (stringMediaPersonsId != null) {
            for (String stringId : stringMediaPersonsId) {
                MediaPerson mediaPerson = mediaPersonDao.findEntityById(Integer.valueOf(stringId));
                mediaPersons.add(mediaPerson);
            }
        }
        movie.setCrew(mediaPersons);
        return movie;
    }
}

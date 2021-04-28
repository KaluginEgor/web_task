package com.example.demo_web.model.service.impl;

import com.example.demo_web.controller.command.Attribute;
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
import com.example.demo_web.model.validator.MovieValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

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
        List<Movie> allUsers;
        try {
            transaction.init(movieDao);
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
        try {
            transaction.init(movieDao);
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
        if (!MovieValidator.isValidId(stringMovieId)) {
            errorMessage = Optional.of(ErrorMessage.INCORRECT_MOVIE_ID_PARAMETER);
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
        List<Movie> movies = new ArrayList<>();
        try {
            transaction.init(movieDao);
            movies = movieDao.findAll();
            return movies;
        } catch (DaoException e) {
            throw new ServiceException(e);
        } finally {
            transaction.end();
        }
    }

    @Override
    public Map.Entry<Optional<Movie>, Optional<String>> create(String title, String description, String stringReleaseDate,
                                                               String picture, String[] stringGenres,
                                                               String[] stringMediaPersonsId) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction();
        Optional<Movie> movie = Optional.empty();
        Optional<String> errorMessage = Optional.empty();
        List<Integer> mediaPersonsId;
        if (stringMediaPersonsId != null) {
            mediaPersonsId = Arrays.asList(stringMediaPersonsId).stream().map(Integer::parseInt).collect(Collectors.toList());
        } else  {
            mediaPersonsId = new ArrayList<>();
        }
        if (!checkAllMediaPersonsExist(mediaPersonsId)) {
            errorMessage = Optional.of(ErrorMessage.SOME_MEDIA_PERSONS_ID_NOT_EXIST);
        } else {
            Movie movieToCreate = convertToMovie(title, description, stringReleaseDate, picture, stringGenres);
            if (!isUnique(title, movieToCreate.getReleaseDate())) {
                errorMessage = Optional.of(ErrorMessage.MOVIE_IS_NOT_UNIQUE);
            } else {
                try {
                    transaction.initTransaction(movieDao, reviewDao, ratingDao);
                    movie = Optional.of(movieDao.create(movieToCreate));
                    List<MediaPerson> movieCrew = new ArrayList<>();
                    if (mediaPersonsId != null) {
                        for (Integer mediaPersonId : mediaPersonsId) {
                            MediaPerson mediaPerson = mediaPersonDao.findEntityById(mediaPersonId);
                            movieCrew.add(mediaPerson);
                        }
                    }
                    movie.get().setGenres(movieToCreate.getGenres());
                    movie.get().setCrew(movieCrew);
                    for (GenreType genre : movie.get().getGenres()) {
                        movieDao.insertMovieGenre(movie.get().getId(), genre.ordinal());
                    }
                    for (MediaPerson mediaPerson : movie.get().getCrew()) {
                        movieDao.insertMovieMediaPerson(movie.get().getId(), mediaPerson.getId());
                    }
                    List<MovieReview> movieReviews = new ArrayList<>();
                    movie.get().setReviews(movieReviews);
                    List<MovieRating> ratingList = new ArrayList<>();
                    movie.get().setRatingList(ratingList);
                    transaction.commit();
                } catch (DaoException e) {
                    transaction.rollback();
                    throw new ServiceException(e);
                } finally {
                    transaction.endTransaction();
                }
            }
        }
        return Map.entry(movie, errorMessage);
    }

    @Override
    public Map.Entry<Optional<Movie>, Optional<String>> update(String stringId, String title, String description, String stringReleaseDate,
                                                               String picture, String[] stringGenres,
                                                               String[] stringMediaPersonsId) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction();
        Optional<Movie> movie = Optional.empty();
        Optional<String> errorMessage = Optional.empty();
        List<Integer> mediaPersonsId;
        if (stringMediaPersonsId != null) {
            mediaPersonsId = Arrays.asList(stringMediaPersonsId).stream().map(Integer::parseInt).collect(Collectors.toList());
        } else  {
            mediaPersonsId = new ArrayList<>();
        }
        if (!MovieValidator.isValidId(stringId)) {
            errorMessage = Optional.of(ErrorMessage.INCORRECT_MOVIE_ID_PARAMETER);
        } else {
            int movieId = Integer.valueOf(stringId);
            if (checkAllMediaPersonsExist(mediaPersonsId)) {
                try {
                    transaction.initTransaction(mediaPersonDao, movieDao, reviewDao, ratingDao);
                    if (!movieDao.idExists(movieId)) {
                        errorMessage = Optional.of(ErrorMessage.TRY_UPDATE_NOT_EXISTING_MOVIE);
                    } else {
                        Movie movieToUpdate = convertToMovie(title, description, stringReleaseDate, picture, stringGenres);
                        movieToUpdate.setId(movieId);
                        List<MediaPerson> movieCrew = new ArrayList<>();
                        if (mediaPersonsId != null) {
                            for (Integer mediaPersonId : mediaPersonsId) {
                                MediaPerson mediaPerson = mediaPersonDao.findEntityById(mediaPersonId);
                                movieCrew.add(mediaPerson);
                            }
                        }
                        movieToUpdate.setCrew(movieCrew);
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
                        movie = Optional.of(movieToUpdate);
                        transaction.commit();
                    }
                } catch (DaoException e) {
                    transaction.rollback();
                    throw new ServiceException(e);
                } finally {
                    transaction.endTransaction();
                }
            } else {
                errorMessage = Optional.of(ErrorMessage.SOME_MEDIA_PERSONS_ID_NOT_EXIST);
            }
        }
        return Map.entry(movie, errorMessage);
    }

    @Override
    public Optional<String> delete(String stringId) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction();
        Optional<String> errorMessage = Optional.empty();
        if (!MovieValidator.isValidId(stringId)) {
            errorMessage = Optional.of(ErrorMessage.INCORRECT_DELETE_MOVIE_PARAMETERS);
        } else {
            try {
                transaction.init(movieDao);
                int movieId = Integer.valueOf(stringId);
                if (movieDao.idExists(movieId)) {
                    movieDao.delete(movieId);
                } else {
                    errorMessage = Optional.of(ErrorMessage.TRY_DELETE_NOT_EXISTING_MOVIE);
                }
            } catch (DaoException e) {
                throw new ServiceException(e);
            } finally {
                transaction.end();
            }
        }
        return errorMessage;
    }

    @Override
    public Map.Entry<List<Movie>, Optional<String>> findByNamePart(String title) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction();
        List<Movie> movies = new ArrayList<>();
        Optional<String> errorMessage = Optional.empty();
        if (!MovieValidator.isTitleValid(title)) {
            errorMessage = Optional.of(ErrorMessage.INCORRECT_MOVIE_ID_PARAMETER);
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

    @Override
    public Map.Entry<List<String>, List<String>> validateData(String title, String description, String stringReleaseDate,
                                                              String[] stringGenresId, String[] stringMediaPersonsId) {
        Map<String, Boolean> validationResult = MovieValidator.validateMovieData(title, description, stringReleaseDate,
                stringGenresId, stringMediaPersonsId);
        List<String> errorMessages = defineValidationErrorMessages(validationResult);
        List<String> validParameters = defineValidParameters(validationResult);
        return Map.entry(validParameters, errorMessages);
    }

    private static List<String> defineValidParameters(Map<String, Boolean> map) {
        return map.entrySet().stream().filter(entry -> Boolean.TRUE.equals(entry.getValue())).map(Map.Entry::getKey).collect(Collectors.toList());
    }

    private static List<String> defineValidationErrorMessages(Map<String, Boolean> map) {
        List<String> errorMessages = new ArrayList<>();
        map.entrySet().stream().filter(entry -> Boolean.FALSE.equals(entry.getValue())).forEach(entry ->
                errorMessages.add(defineErrorValidationMessage(entry)));
        return errorMessages;
    }

    private static String defineErrorValidationMessage(Map.Entry<String, Boolean> entry) {
        String message = null;
        switch (entry.getKey()) {
            case Attribute.MOVIE_TITLE:
                message = ErrorMessage.NOT_VALID_TITLE;
                break;
            case Attribute.MOVIE_DESCRIPTION:
                message = ErrorMessage.NOT_VALID_DESCRIPTION;
                break;
            case Attribute.MOVIE_RELEASE_DATE:
                message = ErrorMessage.NOT_VALID_RELEASE_DATE;
                break;
            case Attribute.PICTURE:
                message = ErrorMessage.NOT_VALID_PICTURE;
                break;
            case Attribute.MOVIE_GENRE:
                message = ErrorMessage.NOT_VALID_GENRES_ID;
                break;
            case Attribute.MOVIE_CREW:
                message = ErrorMessage.NOT_VALID_MEDIA_PERSONS_ID;
                break;
        }
        return message;
    }

    private boolean checkAllMediaPersonsExist(List<Integer> mediaPersonsId) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction();
        if (!mediaPersonsId.isEmpty()) {
            try {
                transaction.init(mediaPersonDao);
                for (Integer mediaPersonId : mediaPersonsId) {
                    if (!mediaPersonDao.idExists(mediaPersonId)) {
                        return false;
                    }
                }
                return true;
            } catch (DaoException e) {
                logger.error(e);
                throw new ServiceException(e);
            } finally {
                transaction.end();
            }
        } else {
            return true;
        }
    }

    private boolean isUnique(String title, LocalDate releaseDate) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction();
        try {
            transaction.init(movieDao);
            return movieDao.isUnique(title, releaseDate);
        } catch (DaoException e) {
            logger.error(e);
            throw new ServiceException(e);
        } finally {
            transaction.end();
        }
    }


    private Movie convertToMovie(String title, String description, String stringReleaseDate, String picture, String[] stringGenres) {
        Movie movie = new Movie();
        movie.setTitle(title);
        movie.setDescription(description);
        LocalDate releaseDate = LocalDate.parse(stringReleaseDate);
        movie.setReleaseDate(releaseDate);
        movie.setPicture(picture);
        List<GenreType> genres = new ArrayList<>();
        if (stringGenres != null) {
            for (String stringGenre : stringGenres) {
                int genreIndex = Integer.valueOf(stringGenre);
                GenreType genreType = GenreType.values()[genreIndex];
                genres.add(genreType);
            }
        }
        movie.setGenres(genres);
        return movie;
    }
}

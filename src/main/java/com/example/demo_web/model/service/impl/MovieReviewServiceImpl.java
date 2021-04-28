package com.example.demo_web.model.service.impl;

import com.example.demo_web.controller.command.Attribute;
import com.example.demo_web.exception.DaoException;
import com.example.demo_web.exception.ServiceException;
import com.example.demo_web.model.dao.*;
import com.example.demo_web.model.dao.impl.MovieDao;
import com.example.demo_web.model.dao.impl.MovieReviewDao;
import com.example.demo_web.model.dao.impl.UserDao;
import com.example.demo_web.model.entity.MovieReview;
import com.example.demo_web.model.service.MovieReviewService;
import com.example.demo_web.model.util.message.ErrorMessage;
import com.example.demo_web.model.validator.MovieValidator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class MovieReviewServiceImpl implements MovieReviewService {
    private static final MovieReviewService instance = new MovieReviewServiceImpl();
    private AbstractMovieReviewDao movieReviewDao = MovieReviewDao.getInstance();
    private AbstractUserDao userDao = UserDao.getInstance();
    private AbstractMovieDao movieDao = MovieDao.getInstance();

    private MovieReviewServiceImpl() {}

    public static MovieReviewService getInstance() {
        return instance;
    }

    @Override
    public Optional<String> create(String reviewTitle, String reviewBody, String stringMovieId, String stringUserId) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction();
        Optional<String> errorMessage = Optional.empty();
        try {
            transaction.initTransaction(movieReviewDao, movieDao, userDao);
            MovieReview movieReview = convertToMovieReview(reviewTitle, reviewBody, stringMovieId, stringUserId);
            if (!movieDao.idExists(movieReview.getMovieId())) {
                errorMessage = Optional.of(ErrorMessage.MOVIE_ID_NOT_EXIST);
            } else if (!userDao.idExists(movieReview.getUserId())) {
                errorMessage = Optional.of(ErrorMessage.USER_ID_NOT_EXIST);
            } else {
                if (!movieReviewDao.isUnique(movieReview.getMovieId(), movieReview.getUserId())) {
                    errorMessage = Optional.of(ErrorMessage.MOVIE_REVIEW_IS_NOT_UNIQUE);
                } else {
                    movieReviewDao.create(movieReview);
                }
            }
            transaction.commit();
        } catch (DaoException e) {
            transaction.rollback();
            throw new ServiceException(e);
        } finally {
            transaction.endTransaction();
        }
        return errorMessage;
    }

    @Override
    public Optional<String> update(String stringMovieReviewId, String reviewTitle, String reviewBody, String stringMovieId, String stringUserId) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction();
        Optional<String> errorMessage = Optional.empty();
        if (!MovieValidator.isValidId(stringMovieReviewId)) {
            errorMessage = Optional.of(ErrorMessage.INCORRECT_UPDATE_MOVIE_REVIEW_ID_PARAMETER);
        } else {
            int reviewId = Integer.parseInt(stringMovieReviewId);
            try {
                transaction.initTransaction(movieReviewDao, movieDao, userDao);
                MovieReview movieReview = convertToMovieReview(reviewTitle, reviewBody, stringMovieId, stringUserId);
                movieReview.setId(reviewId);
                if (!movieDao.idExists(movieReview.getMovieId())) {
                    errorMessage = Optional.of(ErrorMessage.MOVIE_ID_NOT_EXIST);
                } else if (!userDao.idExists(movieReview.getUserId())) {
                    errorMessage = Optional.of(ErrorMessage.USER_ID_NOT_EXIST);
                } else {
                    if (!movieReviewDao.exists(reviewId, movieReview.getMovieId(), movieReview.getUserId() )) {
                        errorMessage = Optional.of(ErrorMessage.TRY_UPDATE_NOT_EXISTING_MOVIE_REVIEW);
                    } else {
                        movieReviewDao.update(movieReview);
                    }
                }
                transaction.commit();
            } catch (DaoException e) {
                transaction.rollback();
                throw new ServiceException(e);
            } finally {
                transaction.endTransaction();
            }
        }
        return errorMessage;
    }

    @Override
    public Optional<String> delete(String stringReviewId, String stringMovieId, String stringUserId) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction();
        Optional<String> errorMessage = Optional.empty();
        if (!MovieValidator.isValidId(stringReviewId)) {
            errorMessage = Optional.of(ErrorMessage.INCORRECT_DELETE_MOVIE_REVIEW_ID_PARAMETER);
        } else {
            try {
                transaction.init(movieReviewDao);
                int reviewId = Integer.parseInt(stringReviewId);
                int movieId = Integer.parseInt(stringMovieId);
                int userId = Integer.parseInt(stringUserId);
                if (movieReviewDao.exists(reviewId, movieId, userId)) {
                    movieReviewDao.delete(reviewId);
                } else {
                    errorMessage = Optional.of(ErrorMessage.TRY_DELETE_NOT_EXISTING_MOVIE_REVIEW);
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
    public Map.Entry<Optional<MovieReview>, Optional<String>> findById(String stringMovieReviewId, String stringMovieId, String stringUserId) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction();
        Optional<MovieReview> movieReview = Optional.empty();
        Optional<String> errorMessage = Optional.empty();
        if (!MovieValidator.isValidId(stringMovieReviewId)) {
            errorMessage = Optional.of(ErrorMessage.INCORRECT_FIND_MOVIE_REVIEW_ID_PARAMETER);
        } else {
            try {
                transaction.init(movieReviewDao);
                int movieReviewId = Integer.parseInt(stringMovieReviewId);
                int movieId = Integer.parseInt(stringMovieId);
                int userId = Integer.parseInt(stringUserId);
                if (movieReviewDao.exists(movieReviewId, movieId, userId)) {
                    movieReview = Optional.of(movieReviewDao.findEntityById(movieReviewId));
                } else {
                    errorMessage = Optional.of(ErrorMessage.TRY_FIND_NOT_EXISTING_MOVIE_REVIEW);
                }
            } catch (DaoException e) {
                throw new ServiceException(e);
            } finally {
                transaction.end();
            }
        }
        return Map.entry(movieReview, errorMessage);
    }

    @Override
    public Map.Entry<List<String>, List<String>> validateData(String title, String body, String stringMovieId, String stringUserId) {
        Map<String, Boolean> validationResult = MovieValidator.validateMovieReviewData(title, body, stringMovieId, stringUserId);
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
        String message = switch (entry.getKey()) {
            case Attribute.MOVIE_REVIEW_TITLE -> ErrorMessage.NOT_VALID_MOVIE_REVIEW_TITLE;
            case Attribute.MOVIE_REVIEW_BODY -> ErrorMessage.NOT_VALID_MOVIE_REVIEW_BODY;
            case Attribute.MOVIE_ID -> ErrorMessage.NOT_VALID_MOVIE_ID;
            case Attribute.USER_ID -> ErrorMessage.NOT_VALID_USER_ID;
            default -> null;
        };
        return message;
    }

    private MovieReview convertToMovieReview(String reviewTitle, String reviewBody, String stringMovieId, String stringUserId) {
        MovieReview review = new MovieReview();
        review.setTitle(reviewTitle);
        review.setBody(reviewBody);
        int movieId = Integer.parseInt(stringMovieId);
        review.setMovieId(movieId);
        int userId = Integer.parseInt(stringUserId);
        review.setUserId(userId);
        review.setCreationDate(LocalDate.now());
        return review;
    }
}

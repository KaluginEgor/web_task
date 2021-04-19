package com.example.demo_web.model.service.impl;

import com.example.demo_web.model.dao.AbstractMovieDao;
import com.example.demo_web.model.dao.AbstractMovieRatingDao;
import com.example.demo_web.model.dao.AbstractUserDao;
import com.example.demo_web.model.dao.EntityTransaction;
import com.example.demo_web.model.dao.impl.MovieDao;
import com.example.demo_web.model.dao.impl.MovieRatingDao;
import com.example.demo_web.model.dao.impl.UserDao;
import com.example.demo_web.model.entity.Movie;
import com.example.demo_web.model.entity.MovieRating;
import com.example.demo_web.model.entity.User;
import com.example.demo_web.exception.DaoException;
import com.example.demo_web.exception.ServiceException;
import com.example.demo_web.model.service.MovieRatingService;
import com.example.demo_web.model.util.evaluator.UserRatingEvaluator;
import com.example.demo_web.model.validator.MovieValidator;

public class MovieRatingServiceImpl implements MovieRatingService {

    private AbstractMovieRatingDao abstractMovieRatingDao = MovieRatingDao.getInstance();
    private AbstractUserDao abstractUserDao = UserDao.getInstance();

    @Override
    public MovieRating create(int movieId, int userId, float value) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction();
        transaction.init(abstractMovieRatingDao);
        try {
            MovieRating movieRating = convertToMovieRating(movieId, userId,value);
            MovieRating createdMovieRating = new MovieRating();
            if (MovieValidator.isMovieRatingValid(movieRating)) {
                createdMovieRating = abstractMovieRatingDao.create(movieRating);
                updateUserRating(movieRating);
            }
            return createdMovieRating;
        } catch (DaoException e) {
            throw new ServiceException(e);
        } finally {
            transaction.end();
        }
    }

    @Override
    public MovieRating update(int movieRatingId, int movieId, int userId, float value) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction();
        transaction.init(abstractMovieRatingDao);
        try {
            MovieRating movieRating = convertToMovieRating(movieId, userId,value);
            movieRating.setId(movieRatingId);
            MovieRating updatedMovieRating = new MovieRating();
            if (MovieValidator.isMovieRatingValid(movieRating)) {
                updatedMovieRating = abstractMovieRatingDao.update(movieRating);
                //updateUserRating(movieRating); //todo
            }
            return updatedMovieRating;
        } catch (DaoException e) {
            throw new ServiceException(e);
        } finally {
            transaction.end();
        }
    }

    @Override
    public boolean delete(int id) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction();
        transaction.init(abstractMovieRatingDao);
        try {
            abstractMovieRatingDao.delete(id);
            return true;
        } catch (DaoException e) {
            throw new ServiceException(e);
        } finally {
            transaction.end();
        }
    }

    private void updateMovieRating(MovieRating movieRating) throws ServiceException {

    }

    private void updateUserRating(MovieRating movieRating) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction();
        transaction.init(abstractUserDao);
        try {
            User user = abstractUserDao.findEntityById(movieRating.getUserId());
            int newUserRating = UserRatingEvaluator.updateRating(user.getRating(), calculateMovieRatingDifference(movieRating));
            abstractUserDao.updateUserRating(user.getId(), newUserRating);
        } catch (DaoException e) {
            throw new ServiceException(e);
        } finally {
            transaction.end();
        }
    }

    private float calculateMovieRatingDifference(MovieRating movieRating) throws DaoException {
        AbstractMovieDao abstractMovieDao = MovieDao.getInstance();
        Movie movie = abstractMovieDao.findEntityById(movieRating.getMovieId());
        return Math.abs(movie.getAverageRating() - movieRating.getValue());
    }

    private MovieRating convertToMovieRating(int movieId, int userId, float value) {
        MovieRating movieRating = new MovieRating();
        movieRating.setMovieId(movieId);
        movieRating.setUserId(userId);
        movieRating.setValue(value);
        return movieRating;
    }
}

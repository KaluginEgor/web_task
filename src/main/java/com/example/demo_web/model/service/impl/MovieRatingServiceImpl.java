package com.example.demo_web.model.service.impl;

import com.example.demo_web.model.dao.MovieDao;
import com.example.demo_web.model.dao.MovieRatingDao;
import com.example.demo_web.model.dao.UserDao;
import com.example.demo_web.model.dao.impl.MovieDaoImpl;
import com.example.demo_web.model.dao.impl.MovieRatingDaoImpl;
import com.example.demo_web.model.dao.impl.UserDaoImpl;
import com.example.demo_web.model.entity.Movie;
import com.example.demo_web.model.entity.MovieRating;
import com.example.demo_web.model.entity.User;
import com.example.demo_web.exception.DaoException;
import com.example.demo_web.exception.ServiceException;
import com.example.demo_web.model.service.MovieRatingService;
import com.example.demo_web.model.util.evaluator.UserRatingEvaluator;
import com.example.demo_web.model.validator.MovieValidator;

public class MovieRatingServiceImpl implements MovieRatingService {

    private MovieRatingDao movieRatingDao = MovieRatingDaoImpl.getInstance();
    private UserDao userDao = UserDaoImpl.getInstance();

    @Override
    public MovieRating create(int movieId, int userId, float value) throws ServiceException {
        try {
            MovieRating movieRating = convertToMovieRating(movieId, userId,value);
            MovieRating createdMovieRating = new MovieRating();
            if (MovieValidator.isMovieRatingValid(movieRating)) {
                createdMovieRating = movieRatingDao.create(movieRating);
                updateUserRating(movieRating);
            }
            return createdMovieRating;
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public MovieRating update(int movieRatingId, int movieId, int userId, float value) throws ServiceException {
        try {
            MovieRating movieRating = convertToMovieRating(movieId, userId,value);
            movieRating.setId(movieRatingId);
            MovieRating updatedMovieRating = new MovieRating();
            if (MovieValidator.isMovieRatingValid(movieRating)) {
                updatedMovieRating = movieRatingDao.update(movieRating);
                //updateUserRating(movieRating); //todo
            }
            return updatedMovieRating;
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean delete(int id) throws ServiceException {
        try {
            movieRatingDao.delete(id);
            return true;
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    private void updateMovieRating(MovieRating movieRating) throws ServiceException {

    }

    private void updateUserRating(MovieRating movieRating) throws ServiceException {
        try {
            User user = userDao.findEntityById(movieRating.getUserId());
            int newUserRating = UserRatingEvaluator.updateRating(user.getRating(), calculateMovieRatingDifference(movieRating));
            userDao.updateUserRating(user.getId(), newUserRating);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    private float calculateMovieRatingDifference(MovieRating movieRating) throws DaoException {
        MovieDao movieDao = MovieDaoImpl.getInstance();
        Movie movie = movieDao.findEntityById(movieRating.getMovieId());
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

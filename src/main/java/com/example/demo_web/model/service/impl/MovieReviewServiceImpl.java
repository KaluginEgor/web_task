package com.example.demo_web.model.service.impl;

import com.example.demo_web.exception.DaoException;
import com.example.demo_web.exception.ServiceException;
import com.example.demo_web.model.dao.AbstractMovieReviewDao;
import com.example.demo_web.model.dao.EntityTransaction;
import com.example.demo_web.model.dao.impl.MovieReviewDao;
import com.example.demo_web.model.entity.MovieReview;
import com.example.demo_web.model.service.MovieReviewService;
import com.example.demo_web.model.validator.MovieValidator;

import java.time.LocalDate;

public class MovieReviewServiceImpl implements MovieReviewService {
    private AbstractMovieReviewDao abstractMovieReviewDao = MovieReviewDao.getInstance();

    @Override
    public MovieReview create(String reviewTitle, String reviewBody, int movieId, int userId) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction();
        transaction.init(abstractMovieReviewDao);
        try {
            MovieReview movieReview = convertToMovieReview(reviewTitle, reviewBody, movieId, userId);
            MovieReview createdMovieReview = new MovieReview();
            if (MovieValidator.isMovieReviewValid(movieReview)) {
                createdMovieReview = abstractMovieReviewDao.create(movieReview);
            }
            return createdMovieReview;
        } catch (DaoException e) {
            throw new ServiceException(e);
        } finally {
            transaction.end();
        }
    }

    @Override
    public MovieReview update(int movieReviewId, String reviewTitle, String reviewBody, int movieId, int userId) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction();
        transaction.init(abstractMovieReviewDao);
        try {
            MovieReview movieReviewToUpdate = convertToMovieReview(reviewTitle, reviewBody, movieId, userId);
            movieReviewToUpdate.setId(movieReviewId);
            if (MovieValidator.isMovieReviewValid(movieReviewToUpdate)) {
                abstractMovieReviewDao.update(movieReviewToUpdate);
            }
            return movieReviewToUpdate;
        } catch (DaoException e) {
            throw new ServiceException(e);
        } finally {
            transaction.end();
        }
    }

    @Override
    public boolean delete(int id) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction();
        transaction.init(abstractMovieReviewDao);
        try {
            abstractMovieReviewDao.delete(id);
            return true;
        } catch (DaoException e) {
            throw new ServiceException(e);
        } finally {
            transaction.end();
        }
    }

    @Override
    public MovieReview findById(int id) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction();
        transaction.init(abstractMovieReviewDao);
        try {
            MovieReview movieReview = abstractMovieReviewDao.findEntityById(id);
            return movieReview;
        } catch (DaoException e) {
            throw new ServiceException(e);
        } finally {
            transaction.end();
        }
    }

    private MovieReview convertToMovieReview(String reviewTitle, String reviewBody, int movieId, int userId) {
        MovieReview review = new MovieReview();
        review.setTitle(reviewTitle);
        review.setBody(reviewBody);
        review.setMovieId(movieId);
        review.setUserId(userId);
        review.setCreationDate(LocalDate.now());
        return review;
    }
}

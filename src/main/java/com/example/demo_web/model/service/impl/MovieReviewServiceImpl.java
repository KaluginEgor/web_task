package com.example.demo_web.model.service.impl;

import com.example.demo_web.exception.DaoException;
import com.example.demo_web.exception.ServiceException;
import com.example.demo_web.model.dao.MovieReviewDao;
import com.example.demo_web.model.dao.impl.MovieReviewDaoImpl;
import com.example.demo_web.model.entity.MovieRating;
import com.example.demo_web.model.entity.MovieReview;
import com.example.demo_web.model.service.MovieReviewService;
import com.example.demo_web.model.validator.MovieValidator;

import java.time.LocalDate;

public class MovieReviewServiceImpl implements MovieReviewService {
    private MovieReviewDao movieReviewDao = MovieReviewDaoImpl.getInstance();

    @Override
    public MovieReview create(String reviewTitle, String reviewBody, int movieId, int userId) throws ServiceException {
        try {
            MovieReview movieReview = convertToMovieReview(reviewTitle, reviewBody, movieId, userId);
            MovieReview createdMovieReview = new MovieReview();
            if (MovieValidator.isMovieReviewValid(movieReview)) {
                createdMovieReview = movieReviewDao.create(movieReview);
            }
            return createdMovieReview;
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public MovieReview update(int movieReviewId, String reviewTitle, String reviewBody, int movieId, int userId) throws ServiceException {
        try {
            MovieReview movieReview = convertToMovieReview(reviewTitle, reviewBody, movieId, userId);
            MovieReview updatedMovieReview = new MovieReview();
            if (MovieValidator.isMovieReviewValid(movieReview)) {
                updatedMovieReview = movieReviewDao.update(movieReview);
            }
            return updatedMovieReview;
        } catch (DaoException e) {
            throw new ServiceException(e);
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

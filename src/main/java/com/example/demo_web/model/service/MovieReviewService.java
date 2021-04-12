package com.example.demo_web.model.service;

import com.example.demo_web.exception.ServiceException;
import com.example.demo_web.model.entity.MovieReview;

public interface MovieReviewService {
    MovieReview create(String reviewTitle, String reviewBody, int movieId, int userId) throws ServiceException;
    MovieReview update(int movieReviewId, String reviewTitle, String reviewBody, int movieId, int userId) throws ServiceException;
    boolean delete(int id) throws ServiceException;
    MovieReview findById(int id) throws ServiceException;
}

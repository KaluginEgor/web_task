package com.example.demo_web.model.service;

import com.example.demo_web.model.entity.MovieRating;
import com.example.demo_web.exception.ServiceException;

public interface MovieRatingService {

    MovieRating create(int movieId, int userId, float value) throws ServiceException;
    MovieRating update(int movieRatingId, int movieId, int userId, float value) throws ServiceException;

}

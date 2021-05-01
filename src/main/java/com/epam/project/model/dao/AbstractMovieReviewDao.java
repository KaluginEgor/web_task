package com.epam.project.model.dao;

import com.epam.project.exception.DaoException;
import com.epam.project.model.entity.MovieReview;

import java.util.List;

public abstract class AbstractMovieReviewDao extends AbstractBaseDao<Integer, MovieReview> {
    public abstract List<MovieReview> findByMovieId(Integer id) throws DaoException;
    public abstract List<MovieReview> findByUserId(Integer id) throws DaoException;
    public abstract boolean isUnique(int movieId, int userId) throws DaoException;
    public abstract boolean exists(int reviewId, int movieId, int userId) throws DaoException;
}

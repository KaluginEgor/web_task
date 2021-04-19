package com.example.demo_web.model.dao;

import com.example.demo_web.model.entity.MovieReview;
import com.example.demo_web.exception.DaoException;

import java.util.List;

public abstract class AbstractMovieReviewDao extends AbstractBaseDao<Integer, MovieReview> {
    public abstract List<MovieReview> findByMovieId(Integer id) throws DaoException;
    public abstract List<MovieReview> findByUserId(Integer id) throws DaoException;
}

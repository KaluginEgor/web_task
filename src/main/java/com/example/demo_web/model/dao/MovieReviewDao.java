package com.example.demo_web.model.dao;

import com.example.demo_web.model.entity.MovieReview;
import com.example.demo_web.exception.DaoException;

import java.util.List;

public interface MovieReviewDao extends BaseDao<Integer, MovieReview> {
    List<MovieReview> findByMovieId(Integer id) throws DaoException;
    List<MovieReview> findByUserId(Integer id) throws DaoException;
}

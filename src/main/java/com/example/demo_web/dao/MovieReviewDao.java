package com.example.demo_web.dao;

import com.example.demo_web.entity.MovieReview;
import com.example.demo_web.exception.DaoException;

import java.util.List;

public interface MovieReviewDao extends BaseDao<Integer, MovieReview> {
    List<MovieReview> findByMovieId(Integer id) throws DaoException;
}
